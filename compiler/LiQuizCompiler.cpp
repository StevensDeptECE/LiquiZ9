#include "LiQuizCompiler.hh"

#include <sstream>

#include "Questions.hh"

using namespace std;
using namespace nlohmann;

string nameof(string) {
  return "string";
}

string nameof(int) {
  return "int";
}

template<typename T>
static T lookup(nlohmann::json& json, const std::string& key, const T& defaultVal,
                int lineNum) {
  try {
    auto it = json.find(key);
    if (it != json.end())
      return T(it.value());
  } catch (std::exception& e) {
    cerr << "Error parsing json value " << key << " of type " << nameof(defaultVal) << e.what() << '\n';
  }
  return defaultVal;
}

template<typename T>
static void require(nlohmann::json& json, const std::string& key, T* target,
                int lineNum) {
  try {
    auto it = json.find(key);
    if (it != json.end())
      *target = it.value();
  } catch (std::exception& e) {
    cerr << "Expected " << key << " at line " << lineNum << '\n';
  }
}

const string empty = "";
const string defaultQuiz = "quiz.css";
const uint32_t defaultFillInBlankSize = 6;

void LiQuizCompiler::findQuestionType(const string &type, double &points,
                                      string &delim, int pos, int len) {
  if (questionTypes.find(type) == questionTypes.end()) {
    cerr << "Undefined question type " << type << " at line: " << questionLineNumber << '\n';
    return;
  }
  QuestionType *question = questionTypes[type];
  if (question != nullptr) {
    question->setText(delim);
    inputText = question->print(this, answers, partNum, questionNum, points);
    questionText.replace(pos, len, inputText);
    // answerText = question->setAnswer(questionText);
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

#if 0
/*
TODO: replace existing lookup of specInfo, 
  styleSheet = specInfo.at("defaults").at("stylesheet");
  with code that checks if the symbol is actually there, prints an error message ie 
  undefined symbol on line xxx
  
*/
static void expect(string& var, LiquiZCompiler* compiler, const json& specInfo, const char name[]) {

}

template<typename T>
static void lookup(T& var, LiquiZCompiler* compiler, const json& specInfo, const char name[], const char name2[]) {

}
#endif

LiQuizCompiler::LiQuizCompiler(const char liquizFileName[]) {
  questionText.reserve(1024);
  string baseFileName = removeExtension(liquizFileName);
  liquizFile.open("quizzes/" + baseFileName + "lq");
  html.open(baseFileName + "html");
  answers.open("quizzes/" + baseFileName + "ans");
  setLogLevel(3);    // set log level to show everything
  questionNum = 1; // start quiz on first question
  questionCount = 0; // number of question inputs in the current question
  points = 0;
}

static json merge( const json &a, const json &b ) {
  json result = a.flatten();
  json tmp = b.flatten();
  for ( auto it = tmp.begin(); it != tmp.end(); ++it )
    result[it.key()] = it.value();
  return result.unflatten();
}

void LiQuizCompiler::includeQSpec(json* parentQuizSpec, const string& filename) {
  string specText;
  {
    ifstream specFile("spec/" + filename);
    string line;
    while (getline(specFile, line)) {
      specText += line;
      specText += '\n';
    }
  }

  json specInfo = json::parse(specText);

  if (logLevel >= 3) {
    cerr << "dumping qspec json before merge\n";
    for (auto i = specInfo.begin(); i != specInfo.end(); ++i)
      cerr << i.key() << "==>" << i.value() << '\n';
  }

  if (specInfo.find("parent") != specInfo.end()) {
    nlohmann::json parentQuizSpec;
    includeQSpec(&parentQuizSpec, specInfo.at("parent")); //TODO: merge specInfo on top of parentQuizSpec
    merge(parentQuizSpec, specInfo);
    specInfo = parentQuizSpec;
  }
  if (parentQuizSpec != nullptr) {// only the first level sets all the variables below
    *parentQuizSpec = specInfo;
    return;
  }
// specInfo should now contain the merged specification of all recursive files

  if (logLevel >= 3) {
    for (auto i = specInfo.begin(); i != specInfo.end(); ++i)
      cerr << i.key() << "==>" << i.value() << '\n';
  }

  uint32_t lineNum = 1;
// TODO: check error on all these. If defaults does not exist, do nothing?
  if (specInfo.find("defaults") != specInfo.end()) {

    //TODO: How to find line number within the JSON?
    nlohmann::json defaults = specInfo.at("defaults");
    imgFile = lookup(defaults, "img", empty, 1);
    styleSheet = lookup(defaults, "stylesheet", defaultQuiz, lineNum);
    fillSize = lookup(defaults, "fillInTheBlankSize", defaultFillInBlankSize, lineNum);
    timeLimit = lookup(defaults, "timeLimit", 0, lineNumber); // default is untimed
  }
  email = lookup(specInfo, "email", empty, lineNum);
  author = lookup(specInfo, "author", empty, lineNum);

  if (specInfo.find("def") != specInfo.end()) {
    for (nlohmann::json::iterator it = specInfo.at("def").begin(); it != specInfo.at("def").end(); ++it) {
      string name = it.key();
      string defs;
      for (int i = 0; i < it.value().size(); i++) {
        string defVal = it.value()[i];
        defs += defVal;
        defs += ",";
      }
      defs.erase(defs.size()-1, 1);
      definitions[name] = defs;
      answers << "defs" << "\t" << name << "\t" << defs << "\n";
    }
  }
}

void LiQuizCompiler::getJSONHeader() {
  string line;
  getline(liquizFile, line);
  nlohmann::json header = nlohmann::json::parse(line);
#if 0
  string specName;
  if (expect(specName, this, header, "quizspec")) {
    includeQSpec(specName)
  }
#endif
  if (header.find("quizspec") != header.end()) {
    string specName = header.at("quizspec");//TODO: pull error checking into separate function above
    includeQSpec(nullptr, specName);
  }  
  quizName = header.at("name"); //TODO: error checking
}

void LiQuizCompiler::generateHeader() {
  getJSONHeader();
  html <<
R"(<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <link rel="stylesheet" type="text/css" href='css/)" <<
    styleSheet <<
R"('>
  <title>
    LiQuiz [)" <<
    quizName <<
R"(]
  </title>
  <script src='js/quiz.js'></script>
</head>
<body onload='startTime()" <<
  timeLimit << ")'>" <<
R"(
  <form method="get" action="gradquiz.jsp"></form>

  <!-- Header -->
  <div id='header' class='header'>
    <img class='logo' src='media/)" <<
  imgFile << "'/>" <<
R"(
    <div class='headerText'>
      <div class='quizTitle'>
        )" <<
  quizName <<
R"(
      </div>

      <div class='headerDetails'>
        <div class='headerRow'>
          )" <<
  author <<
R"(
        </div>
        <div class='headerRow'>
          Email  )" <<
           email << "  if you have any questions!" <<
R"(
        </div>
        <div class='headerRow'>
          <input id='pledge' type='checkbox' name='pledged' value='pledged'/>
          <label for='pledge'>I pledge my honor that I have abided by the Stevens Honor System</label>
        </div>
        <span class='headerRow'>Time Remaining:</span><span id='topTime'></span>
        <input class='controls' type='button' value='Submit Quiz' onClick='showResult()'/>
      </div>
    </div>
    <button id='audioControl' class='audioControl' onClick='scheduleAudio()'>Turn audio ON</button>
  </div>
)";
}

void LiQuizCompiler::setAnswer() {
  smatch m;
  answerText = questionText;
  int pos, end;
  while (regex_search(answerText, m, qID)) {
    pos = m.position();
    for (int i = m.position(); answerText[i] != '='; i++) {
      pos++;
    }
    answerText.insert(pos+2, "a");
  }
}
static const char* name(double) {
  return "double";
}

static string name(int) {
  return "int";
}


void LiQuizCompiler::makeQuestion(nlohmann::json &question) {
  string style;
  require(question, "style", &style, questionLineNumber);
  string preStart, preEnd;

  if (style == "pcode" || style == "code") {
    preStart = "<pre class='pcode'>";
    preEnd = "</pre>";
  } else {
    preStart = "<pre class='text'>";
    preEnd = "</pre>";
  }

  if (style != "def") {
    //    string temp = question.at("points");
    double totalPoints = lookup(question, "points", 1, questionLineNumber);
    string questionName = lookup(question, "name", empty, questionLineNumber);
    html << R"(
  <div class='section'>
    <div class='question' id='q)";
    html << questionNum << "'>";
    html << R"(
      <div>
        )";
    html << questionNum << "." << "\t" << questionName;
    html << R"(
        <span class='pts'>  )";
    html << "(" << totalPoints<< " points)</span><input type='button' class='protestButton' onClick='protestRequest()' value='Click to report a problem'>";
    html << R"(
      </div>
      )";
    html << preStart << endl;
    smatch m;
    double points = totalPoints / questionCount;
    const string end = "</p>";

    while (regex_search(questionText, m, specials)) {
      string delim = m[2];

      int pos = questionText.find("<p hidden>", m.position());
      int endPos = questionText.find(end, m.position());
#if 0      
      string qLine;
      for (int i = pos + 10; i < endPos; i++) {
        qLine += questionText[i];
      }
#endif
      questionLineNumber = stoi(questionText.substr(pos+10, endPos-pos-10+1));

      string type;
      if (delim[0] != 'f') {
        for (int i = 0; delim[i] != ':' && delim[i] != '{'; i++) {
          type += delim[i];
        }
      } else {
        type = delim[0];
      }
      findQuestionType(type, points, delim, m.position(), m.length());
    }
    questionText.erase(questionText.length()-1,1);
    setAnswer();
    html << questionText << preEnd;

    html << R"(
    </div>

    <div class='answer'>
      )";
    html << preStart;
    html << R"(
)";
    html << answerText << preEnd;
    html << R"(
    </div>
  </div>
  
  )";
    questionNum++;
  } else {
    string defs = lookup(question,"values", empty, questionLineNumber);
    string name = lookup(question,"name", empty, questionLineNumber);
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
    <div class='footer'>
      <span class='footer'>Time Remaining:</span><span id='bottomTime'></span>
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
const regex LiQuizCompiler::qID("name='[q||T||Q||m||s||n||S]_[0-9]*_[0-9]*'");

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
    {"vid", new Video()},
    {"rnd", new RandomVar()},
    {"var", new Variable()},
    {"for", new Formula()}
  };

void LiQuizCompiler::dumpVariables() {
  for (const auto& def : variables) {
    cout << def.first << "\t==>\t" << def.second << '\n';
  }
}
