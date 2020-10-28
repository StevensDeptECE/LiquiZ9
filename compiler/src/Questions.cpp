#include "Questions.hh"

#include "LiQuizCompiler.hh"
#include <random>
using namespace std;

inline const std::string &to_string(const std::string &s) { return s; }
inline std::string to_string(char c) {
  char s[2] = {c, '\0'};
  return std::string(s);
}

template <typename... Args>
void buildString(std::string &dest, const Args &... args) {
  dest.clear();
  int unpack[]{0, (dest += to_string(args), 0)...};
  static_cast<void>(unpack);
}

template <typename... Args>
void appendString(std::string &dest, const Args &... args) {
  int unpack[]{0, (dest += to_string(args), 0)...};
  static_cast<void>(unpack);
}

QuestionType::~QuestionType() {}

void QuestionType::addAnswer(string &typeID, string &qID, const string &ans,
                             double points, ostream &answersFile, int &partNum,
                             int &questionNum) {
  partNum++;
  buildString(qID, typeID, "_", questionNum, "_", partNum);
  answersFile << qID << "\t" << points << "\t" << ans << '\n';
}

std::string QuestionType::setAnswer(const string &origText){
  string searchText = origText;
  size_t pos;
  std::string id = "name='";
  const char dash = '_';

  pos = qID.find(dash);
  pos = qID.find(dash, pos+1);

  if (pos != string::npos) {
    for (int i = pos+1; i <= qID.length(); i++) {
      id += qID[i];
    }
  }
  
  id += "'";
  id.erase(id.length()-2,1);
  std::string search = "name='" + qID + "'";
  
  pos = searchText.find(search);

  while (pos != string::npos) {
    searchText.replace(searchText.find(search), search.length(), id);
    pos = searchText.find(search);
  }


  return searchText;
}

static regex dSet("(:.*:)");

void MultipleChoiceHorizontal::getAnswer() {
  input = "";
  for (int i = 0; i < answer.length(); i++) {
    if (answer[i] == '*') {
      for (int j = i + 1; answer[j] != ',' && j < answer.length(); j++) {
        input += answer[j];
      }
      answer.erase(i, 1);
    }
  }
}

void MultipleChoiceHorizontal::getOptions() {
  int count = 1;
  buildString(temp, "<input type='radio' name='", qID, "' value='");
  replace = "        <div class='horizontal'>";
  for (int i = 0; i <= answer.length(); i++) {
    if (answer[i] == ',' || i == answer.length()) {
      appendString(replace, "<label>", temp, option,  "'>", option, "</label>");
      option = "";
      count++;
    } else {
      option += answer[i];
    }
  }
  count = 1;
  replace += "\n</div>";
}

string MultipleChoiceHorizontal::print(const LiQuizCompiler *compiler,
                                       ostream &answersFile, int &partNum,
                                       int &questionNum, double &points) {
  input = "";
  answer = text;
  smatch m;

  if (regex_search(answer, m, dSet)) {
    answer = text.erase(0, 4);
    defName = "";
    for (int i = 0; answer[i] != ':'; i++) {
      defName += answer[i];
    }
    answer.erase(0, defName.length() + 1);
  } else {
    answer = text.erase(0, 4);
  }

  getAnswer();
  addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
  getOptions();
  return replace;
}

void MultipleChoiceVertical::getAnswer() {
  input = "";
  for (int i = 0; i < answer.length(); i++) {
    if (answer[i] == '*') {
      for (int j = i + 1; answer[j] != ',' && j < answer.length(); j++) {
        input += answer[j];
      }
      answer.erase(i, 1);
    }
  }
}

void MultipleChoiceVertical::getOptions() {
  replace = "";
  int count = 1;
  buildString(temp, "<input type='radio' name='", qID, "' value='", "        <div class='vertical'>");
  for (int i = 0; i <= answer.length(); i++) {
    if (answer[i] == ',' || i == answer.length()) {
      replace += R"(<label>
          )";
      replace += temp + option + "'>" + option;
      replace += R"(  
          </label>
          )";
      option = "";
      count++;
    } else {
      option += answer[i];
    }
  }
  count = 1;
  replace += "\n        </div>";
}

string MultipleChoiceVertical::print(const LiQuizCompiler *compiler,
                                     ostream &answersFile, int &partNum,
                                     int &questionNum, double &points) {
  input = "";
  answer = text.erase(0, 4);
  getAnswer();
  addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
  getOptions();
  return replace;
}

void MultipleAnswerHorizontal::getAnswer() {
  input = "";
  for (int i = 0; i < answer.length(); i++) {
    if (answer[i] == '*') {
      for (int j = i + 1; answer[j] != ',' && j < answer.length(); j++) {
        input += answer[j];
      }
      input += ",";
      answer.erase(i, 1);
    }
  }
  input.erase(input.length() - 1, 1);
}

void MultipleAnswerHorizontal::getOptions() {
  replace = "";
  int count = 1;
  buildString(temp, "<input type='checkbox' name='", qID, "' value='");
  replace += 
R"(        <div class='horizontal'>
          )";
  for (int i = 0; i <= answer.length(); i++) {
    if (answer[i] == ',' || i == answer.length()) {
      replace += R"(<label>
          )";
      replace += temp + option + "'>" + option;
      replace += R"(  
          </label>
          )";
      option = "";
      count++;
    } else {
      option += answer[i];
    }
  }
  count = 1;
  replace += R"(
        </div>)";
}

string MultipleAnswerHorizontal::print(const LiQuizCompiler *compiler,
                                       ostream &answersFile, int &partNum,
                                       int &questionNum, double &points) {
  input = "";
  answer = text.erase(0, 4);
  getAnswer();
  addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
  getOptions();
  return replace;
}

void MultipleAnswerVertical::getAnswer() {
  for (int i = 0; i < answer.length(); i++) {
    if (answer[i] == '*') {
      for (int j = i + 1; answer[j] != ',' && j < answer.length(); j++) {
        input += answer[j];
      }
      input += ",";
      answer.erase(i, 1);
    }
  }
  input.erase(input.length() - 1, 1);
}

void MultipleAnswerVertical::getOptions() {
  replace = "";
  int count = 1;
  buildString(temp, "<input type='checkbox' name='", qID, "' value='");
  replace += 
R"(        <div class='vertical'>
          )";
  for (int i = 0; i <= answer.length(); i++) {
    if (answer[i] == ',' || i == answer.length()) {
      replace += R"(<label>
          )";
      replace += temp + option + "'>" + option;
      replace += R"(  
          </label>
          )";
      option = "";
      count++;
    } else {
      option += answer[i];
    }
  }
  count = 1;
  replace += R"(
        </div>)";
}

string MultipleAnswerVertical::print(const LiQuizCompiler *compiler,
                                     ostream &answersFile, int &partNum,
                                     int &questionNum, double &points) {
  input = "";
  answer = text.erase(0, 4);
  getAnswer();
  addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
  getOptions();
  return replace;
}

void FillIn::getFillInType(const char &type) {
  (fillTypes.find(type) != fillTypes.end()) ? typeID = type : typeID = "q";
}

string FillIn::fillinStyle = "fillin"; // default fillin style

string FillIn::print(const LiQuizCompiler *compiler, ostream &answersFile,
                     int &partNum, int &questionNum, double &points) {
  replace = "";
  getFillInType(text[1]);

  if (typeID != "q") {
    answer = text.erase(0, 2);
  } else {
    answer = text.erase(0, 1);
  }

  if (answer[0] == '{') {
    for (int i = 1; answer[i] != '}'; i++) {
      size += answer[i];
    }

    len = stoi(size);
    answer.erase(0, size.length() + 3);
  } else {
    answer.erase(0, 1);
  }

  addAnswer(typeID, qID, answer, points, answersFile, partNum, questionNum);
  buildString(replace, "<input class='", getStyle(), "' name='", qID, "' type='text' id='", qID,
              "' size='", len, "'/>");
  size = "";
  answer = "";
  len = 6;
  typeID = "";
  return replace;
}

/*
 These types are used only for grading. 
 In terms of styles and behavior on the client side there are many more
 
 A hex fillin will only accept hex digits so there should be a difference style (fixed font, perhaps different color)
 and JavaScript to reject any letter not in 0-9,A-F, and why not uniform all caps while we are at it.
 convention will be css name "hex" and JavaScript function checkHex()

 
 */
unordered_map<char, string> FillIn::fillTypes{
    {'Q', "case insensitive"},
    {'s', "space insensitive"},
    {'n', "numeric"},
    {'S', "space and case insensitive"}};

string Hex::hexStyle = "hex"; // Style for hex questions
uint32_t Hex::defaultLen = 8;

string OpCode::opcodeStyle = "opcode"; // Style for opcode questions
uint32_t OpCode::defaultLen = 5;

string Command::commandStyle = "command"; // Style for command line question, longer by default
uint32_t Command::defaultLen = 20;


string TextQuestion::print(const LiQuizCompiler *compiler, ostream &answersFile,
                           int &partNum, int &questionNum, double &points) {
  addAnswer(typeID, qID, answer, points, answersFile, partNum, questionNum);
  text.erase(0, 4);
  buildString(replace, "<textarea rows='10' cols='60' name='",
              qID, "' id='", qID, "'>", text, "</textarea>");
  return replace;
}

void DropDownQuestion::getAnswer() {
  input = "";
  for (int i = 0; i < answer.length(); i++) {
    if (answer[i] == '*') {
      for (int j = i + 1; answer[j] != ',' && j < answer.length(); j++) {
        input += answer[j];
      }
      answer.erase(i, 1);
    }
  }
}

void DropDownQuestion::getOptions() {
  replace = R"(<select class='' name=')";
  replace += qID + "'" + "id='" + qID + "'>";
  for (int i = 0; i <= answer.length(); i++) {
    if (answer[i] == ',' || i == answer.length()) {
      replace += R"(
        <option value=')" + option + "'>" + option + "</option>";
      option = "";
    } else {
      option += answer[i];
    }
  }
  replace += R"(
    </select>)";
}

string DropDownQuestion::print(const LiQuizCompiler *compiler, ostream &answersFile,
                       int &partNum, int &questionNum, double &points) {
  input = "";
  answer = text.erase(0, 4);
  getAnswer();
  addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
  getOptions();
  answer = "";
  return replace;
}

string Image::print(const LiQuizCompiler *, ostream &answersFile, int &partNum,
                    int &questionNum, double &points) {
  text.erase(0, 4);
  string temp = "media/" + text;
  buildString(replace, "<img src='", temp, "'></img>");
  return replace;
}

string Video::print(const LiQuizCompiler *, ostream &answersFile, int &partNum,
                    int &questionNum, double &points) {
  text.erase(0, 4);
  string temp = "media/" + text;
  buildString(replace, "<video controls width='320' height='240'><source src='",
              temp, "' type='video/mp4'></video>");
  return replace;
}

void Definition::getOptions() {
  //TODO: support any form of multiple choice with predefined definitions, not just dropdowns
  
  // build a string with the select and the first option which is empty. By default, question is unanswered unless
  // the user selects something.
  buildString(replace, "<select class='dro' name='",  qID, "' id='", qID, "'><option value=''></option");
  for (int i = 0; i <= defs.length(); i++) {
    if (defs[i] == ',' || i == defs.length()) {
      replace += R"(
            <option value=')" + option + "'>" + option + "</option>";
      option = "";
    } else {
      option += defs[i];
    }
  }
  replace += R"(
        </select>)";
}

string Definition::print(const LiQuizCompiler *compiler, ostream &answersFile,
                         int &partNum, int &questionNum, double &points) {
  answer = text.erase(0, 4);

  for (int i = 0; answer[i] != ':'; i++) {
    name += answer[i];
    count++;
  }

  compiler->findDefinitions(name, defs);
  answer = answer.erase(0, count + 1);

  addAnswer(typeID, qID, answer, points, answersFile, partNum, questionNum);
  getOptions();
  answer, name = "";
  count = 0;
  return replace;
}

void RandomVar::getVar() {
  for (int i = 1; text[i] != '}'; i++) {
    var += text[i];
  }
}

void RandomVar::getRange() {
#if 0
  for (int i = 1; text[i] != ','; i++) {
    minVal += text[i];
  }
  min = stod(minVal);
  text.erase(0, minVal.length() + 2);

  for (int i = 1; text[i] != ','; i++) {
    increm += text[i];
  }
  inc = stod(increm);
  text.erase(0, increm.length() + 1);

  for (int i = 1; text[i] != ')'; i++) {
    maxVal += text[i];
  }
  max = stod(maxVal);
  text.erase(0, maxVal.length() + 1);
#endif
}

void QuestionType::setText(const string &t) {
  text = t;
}

static regex parseRandomVar("rnd:([a-zA-Z][a-zA-Z0-9_]*)\\{(-?\\d+(?:\\.?\\d*)),(-?\\d+(?:\\.?\\d*)),(-?\\d+(?:\\.?\\d*))\\}");
unordered_map<string, string> vars;
default_random_engine generator;

static double parse(const string v, double defaultVal) {
  try {
    return stod(v);
  } catch (const exception& e) {
    cerr << "Error parsing string " << v << " as double.\n";
    return defaultVal;
  }
}
static uint32_t parse(const string v, uint32_t defaultVal) {
  try {
    return stod(v);
  } catch (const exception& e) {
    cerr << "Error parsing string " << v << " as double.\n";
    return defaultVal;
  }
}

void RandomVar::setText(const string& body) {
  cerr << "Random var " << body << "\n";
  smatch m;
  regex_search(body, m, parseRandomVar);

  cout << "matches:" << m[1] << "," << m[2] << "," << m[3] << "," << m[4] << '\n';
  string varName = m[1];
  cout << "varname" << varName << '\n';
  min = parse(m[2], 0.0); // parse and give double precision defaults if not found
  inc = parse(m[3], 1.0);
  max = parse(m[4], 10.0);
  cerr << "var =" << varName << " min=" << min << " inc=" << inc << " max=" << max << '\n';
  uint32_t numRandom = ceil((max-min)/inc);
  uniform_int_distribution<int32_t> dist(0,numRandom);
  uint32_t r = dist(generator);  // generates random integer
  double rval = min + r * inc;
  vars[varName] = to_string(rval);
}

string RandomVar::print(const LiQuizCompiler *compiler,
                             ostream &answersFile, int &partNum,
                             int &questionNum, double &points) {
  text.erase(0, 3);
  getVar();
  text.erase(0, var.length() + 3);
  if (text[0] == '(') {
    getRange();
  }

  cout << *this << '\n';

  return "";
}
static const regex parseVar("var:([a-zA-Z][a-zA-Z0-9_]*)");

void Variable::setText(const std::string& body) {
  smatch m;
  regex_search(body, m, parseVar);
  name = m[1];
  cerr << "var " << name << "\n";
}

string Variable::print(const LiQuizCompiler *compiler,
                             ostream &answersFile, int &partNum,
                             int &questionNum, double &points) {
  if (vars.find(name) != vars.end()) {
    cerr << "variable " << name << "==>" <<  vars.at(name) << '\n'; //TODO: compiler->vars?

    return vars.at(name);
  }
  cerr << "Undefined variable " << name << '\n';
  return "";
}

void FormulaQuestion::setText(const string& body) {

}

string FormulaQuestion::print(const LiQuizCompiler *compiler,
                             ostream &answersFile, int &partNum,
                             int &questionNum, double &points) {
  cerr << "formula\n "; 
  return "formula";
}

FormulaQuestion::~FormulaQuestion() {
}

static regex parseMatrixQuestion("mat\\{(\\d+),(\\d+)\\}([^\\$]+)");
constexpr char underscore = '_';
void MatrixQuestion::setText(const string& body) {
  smatch m;
  regex_search(body, m, parseMatrixQuestion); // parse body of matrix question
  rows = parse(m[1], 1U), cols = parse(m[2], 1U); // get matrix size, default to 1x1
  matrixList = m[3];  
  inputLen = 6; // TODO: get this number from defaults and/or from extra parameter in matrix
	
}
string MatrixQuestion::print(const LiQuizCompiler *compiler,
                             ostream &answersFile, int &partNum,
                             int &questionNum, double &points) {
  string token, answer, typeID;
  token.reserve(64);
  answer.reserve(64);
  typeID.reserve(2);
  istringstream matS(matrixList);
	replace = "<table class='mat'>\n";
  for (uint32_t r = 0; r < rows; r++) {
		replace += "<tr>";
    for (uint32_t c = 0; c < cols; c++) {
			replace += "<td>";
      if (!getline(matS, token, ',')) {
        int lineNum = 0; // TODO: add compiler pointer to setText parameters
        cerr << "Error parsing MatrixQuestion line:" << lineNum << '\n';
				replace += "\n</tr>\n</table>\n";
        return replace;
      }
      if (token[0] == underscore) {
        if (token.length() >= 3 && token[2] == ':') {
          switch(token[1]) {
            case 'Q':
            case 's':
            case 'n':
              typeID = string(1, token[1]);
          }
        } else {
          typeID = "n"; // default type for matrix question is numeric
        }
        answer = token.substr(1);
        addAnswer(typeID, qID, answer, points, answersFile, partNum, questionNum);
        appendString(replace, "<input class='mat' name='", qID,
										"' type='text' id='", qID, "' size='", inputLen, "'/>");
      } else {
        replace += token;
      }
			replace += "</td>";
    }
		replace += "</tr>\n";
  }
	replace += "</table>\n";
  return replace;
}

MatrixQuestion::~MatrixQuestion() {
}
