#include "LiQuizCompiler.hh"

#include <sstream>

#include "Questions.hh"
using namespace std;

void LiQuizCompiler::findQuestionType(const string &type, double &points,
                                      string &delim) {
  QuestionType *question = (questionTypes.find(type) != questionTypes.end())
                               ? questionTypes[type]
                               : defaultQuestionType;
  if (question != nullptr) {
    question->setText(delim);
    inputText = question->print(this, answers, partNum, questionNum, points);
  }
}

string LiQuizCompiler::removeExtension(const char fileName[]) {
  int i;
  for (i = 0; fileName[i] != '\0'; i++)
    ;
  for (; i >= 0 && fileName[i] != '.'; i--)
    ;
  return string(fileName, i + 1);
}

void LiQuizCompiler::findDefinitions(const string &name, string &defs) const {
  if (definitions.find(name) == definitions.end()) {
    cerr << "missing definition " << name << " on line " << questionLineNumber
         << endl;
  } else {
    defs = definitions.at(name);
  }
}

LiQuizCompiler::LiQuizCompiler(const char liquizFileName[]) {
  questionText.reserve(1024);
  string baseFileName = removeExtension(liquizFileName);
  liquizFile.open("quizzes/" + baseFileName + "lq");
  html.open(baseFileName + "html");
  answers.open("quizzes/" + baseFileName + "ans");
}

nlohmann::json LiQuizCompiler::getJSONHeader() {
  string line;
  getline(liquizFile, line);
  nlohmann::json header = nlohmann::json::parse(line);
  return header;
}

void LiQuizCompiler::generateHeader() {
  nlohmann::json header = getJSONHeader();
  html <<
      R"(
                <!DOCTYPE html>
                <html>
                <head>
                <meta charset="UTF-8"/>
                <title>
                )";

  if (header.find("quizspec") != header.end()) {
    string specName = header.at("quizspec");
    quizName = header.at("name");
    string line;
    nlohmann::json specInfo;

    specFile.open("spec/" + specName);
    while (!specFile.eof()) {
      getline(specFile, line);
      specText += line;
      specText += '\n';
    }

    specInfo = nlohmann::json::parse(specText);
    imgFile = specInfo.at("defaults").at("img");
    styleSheet = specInfo.at("defaults").at("stylesheet");
    fillSize = specInfo.at("defaults").at("fillInTheBlankSize");
    timeLimit = specInfo.at("defaults").at("timeLimit");
    email = specInfo.at("email");
    author = specInfo.at("author");

    specFile.close();
  }

  html <<
      R"(
                </title>
                    <link rel="stylesheet" type="text/css" href='css/)";
  html << styleSheet << "'>"
       << "\n";
  html <<
      R"( 
                    <script src='js/quiz.js'></script>
                </head>
                <body onload='startTime()";
  html << timeLimit << ")'>" << '\n';
  html <<
      R"(
                <form method="get" action="gradquiz.jsp">
                <div id='header' class='header'>
                <div style='background-color: #ccc;text-align: center;border-radius: 10px; width: 240px; float: left'>
                    <img class='header' src='media/)";
  html << imgFile << "'/>"
       << "\n";
  html <<
      R"(
                </div>
                <div style='margin-left: 250px'>
                    <table>
                    <tr><td class='headtext'>)";
  html << quizName << "</div></td><td></td></tr>"
       << "\n";
  html << "<tr><td class='headtext'>" << author << "</td></tr>"
       << "\n";
  html << "<tr><td class='headtext'>Email  " << email
       << "  if you have any questions!</td></tr>"
       << "\n";
  html <<
      R"(
                    <tr><td><input class='ctrl' id='pledge' type='checkbox' name='pledged' value='pledged'/><label for='pledge'> I pledge my honor that I have abided by the Stevens Honor System</label></td>
                    <tr><td class='headtext'>Time Remaining:</td><td id='topTime' class='time'></td><td><input id='audioControl' class='controls' type='button' value='turn audio ON' onClick='scheduleAudio()'/>
                </td></tr>
                    </table>
                <audio id="alert25"><source src="media/25min.ogg" type="audio/ogg"/></audio>
                <audio id="alert20"><source src="media/20min.ogg" type="audio/ogg"></audio>
                <audio id="alert15"><source src="media/15min.ogg" type="audio/ogg"></audio>
                <audio id="alert10"><source src="media/10min.ogg" type="audio/ogg"></audio>
                <audio id="alert5"><source src="media/5min.ogg" type="audio/ogg"></audio>
                <audio id="alertover"><source src="media/over.ogg" type="audio/ogg"></audio>
                <audio id="classical">
                <source src="media/JohnLewisGrant_BachPrelude_01.mp3" type="audio/mp3"/>
                    <source src="media/JohnLewisGrant_BachPrelude_02.mp3" type="audio/mp3"/>
                    <source src="media/JohnLewisGrant_BachPrelude_03.mp3" type="audio/mp3"/>
                </audio>
                </div>
                </div>

                )";
}

void LiQuizCompiler::makeQuestion(nlohmann::json &question) {
  string style = question.at("style");
  string preStart, preEnd;

  if (style == "pcode" || style == "code") {
    preStart = "<pre class='pcode'>\n";
    preEnd = "</pre>";
  } else {
    preStart = "<pre class='text'>";
    preEnd = "</pre>";
  }

  if (style != "def") {
    string temp = question.at("points");
    double totalPoints = std::stod(temp);
    string questionName = question.at("name");
    html << "<div class='q' id='q" << questionNum << "'>" << questionNum << ". "
         << questionName << "<span class='pts'> (" << points
         << " points)</span></p>";
    html << "\n" << preStart << "\n";
    smatch m;
    double points = totalPoints / questionCount;

    while (regex_search(questionText, m, specials)) {
      string delim = m[2];

      int pos = questionText.find("<p hidden>", m.position());
      string end = "</p>";
      int endPos = questionText.find(end, m.position());
      string qLine;
      for (int i = pos + 10; i < endPos; i++) {
        qLine += questionText[i];
      }
      questionLineNumber = stoi(qLine);

      string type;
      if (delim[0] != 'f') {
        for (int i = 0; delim[i] != ':'; i++) {
          type += delim[i];
        }
      } else {
        type = delim[0];
      }
      findQuestionType(type, points, delim);
      questionText.replace(m.position(), m.length(), inputText);
      // answerText = "<input type='text' id='a_2_2' hidden/>";
    }

    html << questionText << preEnd;
    html << endl;
    html << "<input type='button' class='protestButton'"
            "onClick='protestRequest()' value='Click to report a problem'><br><br>";
    html << "<div id='" << questionNum << "'></div></div>\n";
    questionNum++;
  } else {
    string defs = question.at("values");
    string name = question.at("name");
    definitions[name] = defs;
    answers << "defs"
            << "\t" << name << "\t" << defs << '\n';
  }
}

void LiQuizCompiler::grabQuestions() {
  string line, qID, temp;
  smatch m;
  lineNumber = 1;
  while (getline(liquizFile, line), !liquizFile.eof()) {
    if (regex_search(
            line, m,
            questionStart)) {  // looking for the beginning of a question
      istringstream s(line);
      nlohmann::json question;  // gets the question header
      s >> question;
      lineNumber++;
      while (getline(liquizFile, line),
             !liquizFile.eof() &&
                 line != DELIM) {  // gets line within question section
        lineNumber++;
        questionText =
            questionText + line + "<p hidden>" + to_string(lineNumber) + "</p>";
        questionText += '\n';
      }
      lineNumber++;
      for (int i = 0; i < questionText.length(); i++) {
        if (questionText[i] == '$') {
          questionCount++;
        }
      }
      questionCount /= 2;
      makeQuestion(question);
      questionCount = 0;
      questionText = "";
    }
  }
}

void LiQuizCompiler::generateFooter() {
  html <<
      R"(
          <div class='controls'>
          <div style='position: flow'>Time Remaining</div>
          <div id='bottomTime' class='time'></div>
          <input class='controls' type='button' value='Submit Quiz' onClick='showResult()'/>
          </div>
          </form>
      </body>
      </html>
      )";
}

void LiQuizCompiler::closeFile() {
  liquizFile.close();
  html.close();
  answers.close();
}

void LiQuizCompiler::generateQuiz() {
  generateHeader();
  grabQuestions();
  generateFooter();
  closeFile();
}

const regex LiQuizCompiler::questionStart("^\\{");
const regex LiQuizCompiler::specials(
    "\\$([a-z]*\\(|\\d+[cs]?\\{)?([^\\$]+)\\$");

unordered_map<string, QuestionType *> LiQuizCompiler::questionTypes{
    {"mch", new MultipleChoiceHorizontal()},
    {"mcv", new MultipleChoiceVertical()},
    {"mah", new MultipleAnswerHorizontal()},
    {"mav", new MultipleAnswerVertical()},
    {"f", new FillIn()},
    {"tar", new TextQuestion()},
    {"def", new Definition()},
    {"dro", new DropDown()},
    {"img", new Image()},
    {"vid", new Video()}};