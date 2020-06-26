#include "Questions.hh"

#include "LiQuizCompiler.hh"
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
  replace = "";
  int count = 1;
  buildString(temp, "<input type='radio' name='", qID, "' value='");
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
  buildString(temp, "<input type='radio' name='", qID, "' value='");
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
  buildString(replace, "<input class='' name='", qID, "' type='text' id='", qID,
              "' size='", len, "'/>");
  size = "";
  answer = "";
  len = 6;
  typeID = "";
  return replace;
}

unordered_map<char, string> FillIn::fillTypes{
    {'Q', "case insensitive"},
    {'s', "space insensitive"},
    {'n', "numeric"},
    {'S', "space and case insensitive"}};

string TextQuestion::print(const LiQuizCompiler *compiler, ostream &answersFile,
                           int &partNum, int &questionNum, double &points) {
  addAnswer(typeID, qID, answer, points, answersFile, partNum, questionNum);
  text.erase(0, 4);
  buildString(replace, "<textarea rows='10' cols='60' name='",
              qID, "' id='", qID, "'>", text, "</textarea>");
  return replace;
}

void DropDown::getAnswer() {
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

void DropDown::getOptions() {
  replace = R"(
    <select class='' name=')";
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

string DropDown::print(const LiQuizCompiler *compiler, ostream &answersFile,
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
  replace = R"(<select class='dro' name=')";
  replace += qID + "'" + "id='" + qID + "'>";
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

void RandomQuestion::getVar() {
  for (int i = 1; text[i] != '}'; i++) {
    var += text[i];
  }
}

void RandomQuestion::getRange() {
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
}

string RandomQuestion::print(const LiQuizCompiler *compiler,
                             ostream &answersFile, int &partNum,
                             int &questionNum, double &points) {
  text.erase(0, 3);
  getVar();
  text.erase(0, var.length() + 3);
  if (text[0] == '(') {
    getRange();
  }

  cout << "minVal: "
       << "\t" << minVal << endl;
  cout << "min: "
       << "\t" << min << endl;
  cout << "maxVal: "
       << "\t" << maxVal << endl;
  cout << "max: "
       << "\t" << max << endl;
  cout << "increm: "
       << "\t" << increm << endl;
  cout << "inc: "
       << "\t" << inc << endl;
  cout << endl << endl;

  return "";
}