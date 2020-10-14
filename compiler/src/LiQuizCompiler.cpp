#include "LiQuizCompiler.hh"

#include <sstream>

#include "Questions.hh"
#include <string_view>
#include <unistd.h>

using namespace std;
using namespace nlohmann;

string nameof(string) {
  return "string";
}

string nameof(int) {
  return "int";
}

uint32_t LiQuizCompiler::uuid = 1;

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

const string emptystr = "";
const string escapedDollar = "\\$";
const string defaultQuiz = "quiz.css";
const uint32_t defaultFillInBlankSize = 6;

void LiQuizCompiler::findQuestionType(const string &type, double &points,
                                      string &delim, int pos, int len) {
  if (questionTypes.find(type) == questionTypes.end()) {
    cerr << "Undefined question type " << type << " at line: " << lineNum << '\n';
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
    cerr << "missing definition " << name << " on line " << lineNum << endl;
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

LiQuizCompiler::LiQuizCompiler(const char outputDir[]) : outputDir(outputDir) {
  questionText.reserve(1024);
  setLogLevel(3);    // set log level to show everything
  questionNum = 1; // start quiz on first question
  questionCount = 0; // number of question inputs in the current question
  points = 0;
  uuid++;
}

static json merge( const json &a, const json &b ) {
  json result = a.flatten();
  json tmp = b.flatten();
  for ( auto it = tmp.begin(); it != tmp.end(); ++it )
    result[it.key()] = it.value();
  return result.unflatten();
}

void LiQuizCompiler::includeQSpec(json* parentQuizSpec, const string& filename) {
  ifstream specFile(("spec/" + filename).c_str());
  char dirName[256];
  cerr << "Current directory: " << getcwd(dirName, sizeof(dirName)) << '\n';
  cerr << "filename: " << "spec/" + filename << '\n';
  if (!specFile.good()) {
    cerr << "Cannot open file " << filename << '\n';
  }
  json specInfo = json::parse(specFile);

  if (logLevel >= 3) {
    cerr << "dumping qspec json before merge\n";
    for (auto i = specInfo.begin(); i != specInfo.end(); ++i)
      cerr << i.key() << "==>" << i.value() << '\n';
  }

  if (specInfo.find("parent") != specInfo.end()) {
    nlohmann::json parentQuizSpec;
    includeQSpec(&parentQuizSpec, specInfo.at("parent")); //TODO: merge specInfo on top of parentQuizSpec
    //TODO: merge isn't working, so comment out?
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

  lineNum = 1;
// TODO: check error on all these. If defaults does not exist, do nothing?
  if (specInfo.find("defaults") != specInfo.end()) {

    //TODO: How to find line number within the JSON?
    nlohmann::json defaults = specInfo.at("defaults");
    imgFile = lookup(defaults, "img", emptystr, 1);
    styleSheet = lookup(defaults, "stylesheet", defaultQuiz, lineNum);
    fillSize = lookup(defaults, "fillInTheBlankSize", defaultFillInBlankSize, lineNum);
    timeLimit = lookup(defaults, "timeLimit", 0, lineNum); // default is untimed
  }
  email = lookup(specInfo, "email", emptystr, lineNum);
  author = lookup(specInfo, "author", emptystr, lineNum);

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
      if (logLevel >= 3) {
        cerr << "DEF " << name << "==>" << defs << '\n';
      }
      answers << "defs" << "\t" << name << "\t" << defs << "\n";
    }
  }
}

void LiQuizCompiler::getJSONHeader() {
  string line;
  if (!getline(line)) {
    cerr << "Unexpected end of file line while getting JSON header\n";
    return;
  }
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
R"(
<!DOCTYPE html>
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

#if 0
xml <<
R"(<?xml version="1.0" encoding="UTF-8"?>
<questestinterop xmlns="http://www.imsglobal.org/xsd/ims_qtiasiv1p2" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.imsglobal.org/xsd/ims_qtiasiv1p2 http://www.imsglobal.org/xsd/ims_qtiasiv1p2p1.xsd">
  <assessment ident="liquiz)" << uuid << "\" title=\"" << quizName << 
R("">
<qtimetadata>
  <qtimetadatafield>
    <fieldlabel>cc_maxattempts</fieldlabel>
    <fieldentry>1</fieldentry>
  </qtimetadatafield>
</qtimetadata>
    <section ident="root_section">
      <section ident="liquiz") << uuid << R(" title="Fill in the missing ARM">
        <selection_ordering>
          <selection>
            <selection_number>1</selection_number>
            <selection_extension>
              <points_per_item>1.0</points_per_item>
            </selection_extension>
          </selection>
        </selection_ordering>
");
#endif
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
  require(question, "style", &style, lineNum);
  string preStart, preEnd;

  if (style == "pcode" || style == "code") {
    preStart = "<pre class='" + style + "'>";
    preEnd = "</pre>";
  } else {
    preStart = "<pre class='text'>";
    preEnd = "</pre>";
  }

  if (style != "def") {
    //    string temp = question.at("points");
    double totalPoints = lookup(question, "points", 0, lineNum);
    string questionName = lookup(question, "name", emptystr, lineNum);
    smatch m;
    const string end = "</p>";
		partNum = 0;

    while (regex_search(questionText, m, specials)) {
      string delim = m[2];

      int pos = questionText.find("<p hidden>", m.position());
      int endPos = questionText.find(end, m.position());
      uint32_t questionLineNum = stoi(questionText.substr(pos+10, endPos-pos-10+1)); //TODO: get rid of this!

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
    double points = totalPoints != 0 ? totalPoints / questionCount : partNum;
    questionText.pop_back();
    setAnswer();
//TODO: What is this?    regex_replace("$", questionText.begin(), questionText.end(), escapedDollar);
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
    saveXML();
    questionNum++;
  } else {
    string defs = lookup(question,"values", emptystr, lineNum);
    string name = lookup(question,"name", emptystr, lineNum);
    definitions[name] = defs;
    answers << "defs"
            << "\t" << name << "\t" << defs << '\n';
  }
}

void LiQuizCompiler::saveXML() {
  #if 0
  xml <<
R("<item ident="liquiz") << uuid << 
R(" title="Question">
  <itemmetadata>
    <qtimetadata>
      <qtimetadatafield>
        <fieldlabel>question_type</fieldlabel>
        <fieldentry>fill_in_multiple_blanks_question</fieldentry>
      </qtimetadatafield>
      <qtimetadatafield>
        <fieldlabel>points_possible</fieldlabel>
        <fieldentry>1.0</fieldentry>
      </qtimetadatafield>
      <qtimetadatafield>
        <fieldlabel>original_answer_ids</fieldlabel>
        <fieldentry>4671,4417,1033,1118,25,7804,9862</fieldentry>
      </qtimetadatafield>
      <qtimetadatafield>
        <fieldlabel>assessment_question_identifierref</fieldlabel>
        <fieldentry>gf47b767d37e06559ff801f2d253307ba</fieldentry>
      </qtimetadatafield>
    </qtimetadata>
  </itemmetadata>
<presentation>
  <material>
    <mattext texttype="text/html"> ");
#endif
#if 0
xml << "<p>" << "Fill in the missing assembler instructions and parameters. Please enter everything in lowercase" << "</p>";
xml << R("<table style="width: 80%">
<tr><td style="width: 40%"><pre style="font-size: 14pt;">_Z3sumPii:</pre></td></tr>
<tr><td></td><td style="width: 20%"><pre style="font-size: 14pt">mov</pre></td><td style="width: 20%">
<pre style="font-size: 14pt">r2,</pre></td><td style="width: 20%"><pre style="font-size: 14pt">#0</pre></td></tr> ");
#endif

#if 0
</mattext>
</material>
  <response_lid ident="response_i0">
    <material>
      <mattext>i0</mattext>
    </material>
             <render_choice>
                <response_label ident="4671">
                  <material>
                    <mattext texttype="text/plain">ldr</mattext>
                  </material>
                </response_label>
              </render_choice>
            </response_lid>
            <response_lid ident="response_p0">
              <material>
                <mattext>p0</mattext>
              </material>
              <render_choice>
                <response_label ident="4417">
                  <material>
                    <mattext texttype="text/plain">[r0]</mattext>
                  </material>
                </response_label>
              </render_choice>
            </response_lid>
            <response_lid ident="response_i2">
              <material>
                <mattext>i2</mattext>
              </material>
              <render_choice>
                <response_label ident="1033">
                  <material>
                    <mattext texttype="text/plain">add</mattext>
                  </material>
                </response_label>
                <response_label ident="1118">
                  <material>
                    <mattext texttype="text/plain">subs</mattext>
                  </material>
                </response_label>
              </render_choice>
            </response_lid>
            <response_lid ident="response_i3">
              <material>
                <mattext>i3</mattext>
              </material>
              <render_choice>
                <response_label ident="25">
                  <material>
                    <mattext texttype="text/plain">r1</mattext>
                  </material>
                </response_label>
                <response_label ident="7804">
                  <material>
                    <mattext texttype="text/plain">bne</mattext>
                  </material>
                </response_label>
                <response_label ident="9862">
                  <material>
                    <mattext texttype="text/plain">bgt</mattext>
                  </material>
                </response_label>
              </render_choice>
            </response_lid>
          </presentation>
          <resprocessing>
            <outcomes>
              <decvar maxvalue="100" minvalue="0" varname="SCORE" vartype="Decimal"/>
            </outcomes>
            <respcondition>
              <conditionvar>
                <varequal respident="response_i0">4671</varequal>
              </conditionvar>
              <setvar varname="SCORE" action="Add">25.00</setvar>
            </respcondition>
            <respcondition>
              <conditionvar>
                <varequal respident="response_p0">4417</varequal>
              </conditionvar>
              <setvar varname="SCORE" action="Add">25.00</setvar>
            </respcondition>
            <respcondition>
              <conditionvar>
                <varequal respident="response_i2">1033</varequal>
              </conditionvar>
              <setvar varname="SCORE" action="Add">25.00</setvar>
            </respcondition>
            <respcondition>
              <conditionvar>
                <varequal respident="response_i3">25</varequal>
              </conditionvar>
              <setvar varname="SCORE" action="Add">25.00</setvar>
            </respcondition>
          </resprocessing>
        </item>
#endif
}

void LiQuizCompiler::grabQuestions() {
  string line;
  string qID, temp;
  smatch m;

  while (getline(line)) {
    if (regex_search(line, m, questionStart)) {  // looking for the beginning of a question
      istringstream s(line);
      nlohmann::json question;  // gets the question header
      s >> question;
      questionText = "";
      while (getline(line) && line != DELIM) {  // gets line within question section
        line.pop_back(); // get rid of \n
        questionText += line + "<p hidden>" + to_string(lineNum) + "</p>" + '\n'; // TODO: get rid of this
      }
      for (int i = 0; i < questionText.length(); i++) {
        if (questionText[i] == '$') { //TODO: Come up with regex-based approach. This does not allow $ in text!!!
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
  delete [] bytes;
  html.close();
  answers.close();
}

void LiQuizCompiler::readFile(const char fileName[], char*& bytes, uint32_t& fileSize) {
  ifstream f(fileName, ios::in | ios::ate);
  if (!f.good()) {
    throw fileName; //TODO: add Ex object
  }
  fileSize = f.tellg();
  f.seekg(0, ios::beg);
  bytes = new char[fileSize];
  f.read(bytes, fileSize);
  f.close();
}

/*
  get a single line from the quiz input.
  This master routine returns a string_view for efficiency,
  pointing to the underlying byte buffer bytes
  It also skips comments which begin with # and tracks line numbers
*/
bool LiQuizCompiler::getline(string& line) {
  while (cursor < fileSize && bytes[cursor] == '#') { // skip comment
    cursor++;
    while (cursor < fileSize && bytes[cursor] != '\n')
      cursor++;
    lineNum++;
  }
  if (cursor >= fileSize)
    return false;
  uint32_t startLine = cursor;
  while (cursor < fileSize && bytes[cursor] != '\n')
    cursor++;
  line = string(&bytes[startLine], cursor - startLine + 1);
  lineNum++;
  cursor++;
  return true;
}
void LiQuizCompiler::generateQuiz(const char liquizFileName[]) {

  string baseFileName = removeExtension(liquizFileName);
  readFile((baseFileName + "lq").c_str(), bytes, fileSize);
  cursor = 0;
  html.open(outputDir + baseFileName + "html");
  answers.open(outputDir + baseFileName + "ans");
  xml.open(outputDir + baseFileName + "xml");
  generateHeader();
  grabQuestions();
  generateFooter();
  closeFile();
}

const regex LiQuizCompiler::questionStart("^\\{");
const regex LiQuizCompiler::specials("\\$([a-z]*\\(|\\d+[cs]?\\{)?([^\\$]+)\\$");
const regex LiQuizCompiler::qID("name='[q||T||Q||m||s||n||S]_[0-9]*_[0-9]*'");

unordered_map<string, QuestionType *> LiQuizCompiler::questionTypes{
    {"mch", new MultipleChoiceHorizontal()},
    {"mcv", new MultipleChoiceVertical()},
    {"mah", new MultipleAnswerHorizontal()},
    {"mav", new MultipleAnswerVertical()},
    {"f", new FillIn()},
    {"tar", new TextQuestion()},
    {"def", new Definition()},
    {"dro", new DropDownQuestion()},
    {"img", new Image()},
    {"vid", new Video()},
    {"rnd", new RandomVar()},
    {"var", new Variable()},
    {"for", new FormulaQuestion()},
    {"mat", new MatrixQuestion()}
  };

void LiQuizCompiler::dumpVariables() {
  for (const auto& def : variables) {
    cout << def.first << "\t==>\t" << def.second << '\n';
  }
}
