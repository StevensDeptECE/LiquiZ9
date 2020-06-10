#include "json.hpp"
#include <iostream>
#include <sstream>
#include <fstream>
#include <regex>
#include <exception>
#include <unordered_map>
using namespace std;


inline const std::string& to_string(const std::string& s) {return s;}
inline std::string to_string(char c) {
    char s[2] = {c, '\0'};
    return std::string(s);
}

class QuestionType {
    protected:
        string qID, replace, text;
        int fillSize;

    public:
        void setText(const string& t) { text = t; }
        virtual string print(ostream& a, int& pN, int& qN, double& p) = 0;
        virtual ~QuestionType() {}

        void addAnswer(string& typeID, string& qID, const string& ans, double points, ostream &answersFile, int& partNum, int& questionNum) {
            partNum++;
            buildString(qID, typeID, "_", "q", questionNum, "_", partNum);
            answersFile << qID << "\t" << points << "\t" << ans << '\n';

        }

        template<typename... Args>
        void buildString(std::string& dest, const Args&... args) {
            dest.clear();
            int unpack[]{0, (dest += to_string(args), 0)...};
            static_cast<void>(unpack);
        }

        void setSize(int size) {
            fillSize = size;
        }
};

class MultipleChoiceHorizontal : public QuestionType {
    private:
        string temp, input, answer, option;
        string typeID = "q";
    public:
        void getAnswer() {
            input = "";
            for (int i = 0; i < answer.length(); i++) {
                    if (answer[i] == '*') {
                        for (int j = i+1; answer[j] != ',' && j < answer.length(); j++) {
                            input += answer[j];
                        }
                        answer.erase(i, 1);
                    }
                }
        }

        void getOptions() {
            replace = "";
            buildString(temp, "<input class='mc' name='", qID, "' type='radio' value='");
            for (int i = 0; i <= answer.length(); i++) {
                if (answer[i] == ',' || i == answer.length()) {
                    replace += temp + option + "'>" + option + "\t";
                    option = "";
                } else {
                    option += answer[i];
                }
            }
        }

        string print(ostream& answersFile, int& partNum, int& questionNum, double& points) {
            input = "";
            answer = text.erase(0,4);
            getAnswer();
            addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
            getOptions();
            return replace;
        }
};

class MultipleChoiceVertical : public QuestionType {
    private:
        string temp, input, answer, option;
        string typeID = "q";
    public:
        void getAnswer() {
            input = "";
            for (int i = 0; i < answer.length(); i++) {
                    if (answer[i] == '*') {
                        for (int j = i+1; answer[j] != ',' && j < answer.length(); j++) {
                            input += answer[j];
                        }
                        answer.erase(i, 1);
                    }
                }
        }

        void getOptions() {
            replace = "";
            buildString(temp, "<input class='mc' name='", qID, "' type='radio' value='");
            for (int i = 0; i <= answer.length(); i++) {
                if (answer[i] == ',' || i == answer.length()) {
                    replace += temp + option + "'>" + option + "\n\n";
                    option = "";
                } else {
                    option += answer[i];
                }
            }
            replace.erase(replace.length()-1, 1);
            replace.erase(replace.length()-1, 1);
        }

        string print(ostream& answersFile, int& partNum, int& questionNum, double& points) {
            input = "";
            answer = text.erase(0,4);
            getAnswer();
            addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
            getOptions();
            return replace;
        }
};

class MultipleAnswerHorizontal : public QuestionType {
    private:
        string temp, input, answer, option;
        string typeID = "m";
    public:
        void getAnswer() {
            input = "";
            for (int i = 0; i < answer.length(); i++) {
                    if (answer[i] == '*') {
                        for (int j = i+1; answer[j] != ',' && j < answer.length(); j++) {
                            input += answer[j];
                            input += ",";
                        }
                        answer.erase(i, 1);
                    }
            }
            input.erase(input.length()-1, 1);
        }

        void getOptions() {
            replace = "";
            buildString(temp, "<input class='ma' name='", qID, "' type='checkbox' value='");
            for (int i = 0; i <= answer.length(); i++) {
                if (answer[i] == ',' || i == answer.length()) {
                    replace += temp + option + "'>" + option + "\t";
                    option = "";
                } else {
                    option += answer[i];
                }
            }
        }

        string print(ostream& answersFile, int& partNum, int& questionNum, double& points) {
            input = "";
            answer = text.erase(0,4);
            getAnswer();
            addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
            getOptions();
            return replace;
        }
};

class MultipleAnswerVertical : public QuestionType {
    private:
        string temp, input, answer, option;
        string typeID = "m";
    public:
        void getAnswer() {
            for (int i = 0; i < answer.length(); i++) {
                    if (answer[i] == '*') {
                        for (int j = i+1; answer[j] != ',' && j < answer.length(); j++) {
                            input += answer[j];
                            input += ",";
                        }
                        answer.erase(i, 1);
                    }
            }
            input.erase(input.length()-1, 1);
        }

        void getOptions() {
            replace = "";
            buildString(temp, "<input class='ma' name='", qID, "' type='checkbox' value='");
            for (int i = 0; i <= answer.length(); i++) {
                if (answer[i] == ',' || i == answer.length()) {
                    replace += temp + option + "'>" + option + "\n\n";
                    option = "";
                } else {
                    option += answer[i];
                }
            }
            replace.erase(replace.length()-1, 1);
            replace.erase(replace.length()-1, 1);
        }

        string print(ostream& answersFile, int& partNum, int& questionNum, double& points) {
            input = "";
            answer = text.erase(0,4);
            getAnswer();
            addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
            getOptions();
            return replace;
        }
};

class FillIn : public QuestionType {
    private:
        static unordered_map<char, string> fillTypes;
        string answer, typeID, size, orig;
        int len = 6;

    public:
        void getFillInType(const char& type) {
            (fillTypes.find(type) != fillTypes.end()) ? typeID = type : typeID = "q";
        }

        string print(ostream& answersFile, int& partNum, int& questionNum, double& points) {
            getFillInType(text[1]);

            len = fillSize;

            if (typeID != "q") {
                answer = text.erase(0,2);
            } else {
                answer = text.erase(0,1);
            }

            if (answer[0] == '{') {
                for (int i = 1; answer[i] != '}'; i++) {
                    size += answer[i];
                }

                len = stoi(size);
                answer.erase(0, size.length()+3);
            } else {
                answer.erase(0, 1);
            }

            addAnswer(typeID, qID, answer, points, answersFile, partNum, questionNum);
            buildString(replace, "<input class='' name='", qID, "' type='text' id='", qID, "' size='", len, "'/>");
            size = "";
            answer = "";
            len = 6;
            typeID = "";
            return replace;
        }
};

unordered_map<char, string> FillIn::fillTypes {
    {'Q', "case insensitive"},
    {'s', "space insensitive"},
    {'n', "numeric"},
    {'S', "space and case insensitive"}
};

class TextQuestion : public QuestionType {
    private:
        string answer = "N/A";
        string typeID = "T";
    public:
        string print(ostream& answersFile, int& partNum, int& questionNum, double& points) {
            addAnswer(typeID, qID, answer, points, answersFile, partNum, questionNum);
            text.erase(0,4);
            buildString(replace, "<textarea rows='20' cols='80' id='", qID, "' name='", qID, "'>", text, "</textarea>");
            return replace;
        }
};

class DropDown : public QuestionType {
    private:
        string answer, option, input;
        string typeID = "q";
        int count = 0;
    public:
        void getAnswer() {
            input = "";
            for (int i = 0; i < answer.length(); i++) {
                    if (answer[i] == '*') {
                        for (int j = i+1; answer[j] != ',' && j < answer.length(); j++) {
                            input += answer[j];
                        }
                        answer.erase(i, 1);
                    }
                }
        }

        void getOptions() {
            buildString(replace, "<select class='' name='", qID, "'>");
            for (int i = 0; i <= answer.length(); i++) {
                if (answer[i] == ',' || i == answer.length()) {
                    replace += "<option value='" + option + "'>" + option + "</option>\n";
                    option = "";
                } else {
                    option += answer[i];
                }
            }
            replace += "</select>";
        }

        string print(ostream& answersFile, int& partNum, int& questionNum, double& points) {
            input = "";
            answer = text.erase(0,4);
            getAnswer();
            addAnswer(typeID, qID, input, points, answersFile, partNum, questionNum);
            getOptions();
            answer = "";
            return replace;
        }
};

class Image : public QuestionType {
    public:
        string print(ostream& answersFile, int& partNum, int& questionNum, double& points) {
            text.erase(0,4);
            string temp = "media/" + text;
            buildString(replace, "<img src='", temp, "'></img>");
            return replace;
        }
};

class Video : public QuestionType {
    public:
        string print(ostream& answersFile, int& partNum, int& questionNum, double& points) {
            text.erase(0,4);
            string temp = "media/" + text;
            buildString(replace, "<video controls width='320' height='240'><source src='", temp, "' type='video/mp4'></video>");
            return replace;
        }
};

class LiQuizCompiler {
    private:
        const static regex def;
	    const static regex questionStart;
        const static regex specials;

        static unordered_map<string, QuestionType*> questionTypes;
        static unordered_map<string, string> definitions;

        friend class Definition;

    private:
        const string DELIM = "---";
        string questionText;
        string inputText;

        ofstream html;
        ofstream answers;
        ifstream liquizFile;
        ifstream specFile;

        QuestionType* defaultQuestionType;

        int questionNum = 1;
        int partNum; // the subnumber within each question
        double questionCount = 0;
        double points = 0;
        int fillSize;
        string specText, imgFile, styleSheet;
        

        void findQuestionType(const string& type, double& points, string& delim) {
            QuestionType* question = (questionTypes.find(type) != questionTypes.end())
                ? questionTypes[type] : defaultQuestionType;
            if (question != nullptr) {
                question->setText(delim);
                if(type == "f") {
                    question->setSize(fillSize);
                }
                inputText = question->print(answers, partNum, questionNum, points);
            }
        }

        string removeExtension(const char fileName[]) { 
            int i;
	        for (i = 0; fileName[i] != '\0'; i++)
		        ;
	        for (; i >= 0 && fileName[i] != '.'; i--)
		        ;
            return string(fileName, i+1);
        }

        static void findDefinitions(string& name, string& defs) {
            if (definitions.find(name) == definitions.end()) {
                name = "missing definitions name";
                defs = "unavailable";
            } else {
                defs = definitions.at(name);
            }
        }

    public:
        LiQuizCompiler(const char liquizFileName[]) {
            questionText.reserve(1024);
            string baseFileName = removeExtension(liquizFileName);
            //liquizFile.open("quizzes/" + baseFileName + "lq");
            liquizFile.open(liquizFileName);
            html.open(baseFileName + "html");
            answers.open(baseFileName + "ans");
        }

        nlohmann::json getJSONHeader() {
            string line;
            getline(liquizFile, line);
            nlohmann::json header = nlohmann::json::parse(line);
            return header;
        }

        void generateHeader() {
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
                string quizName = header.at("name");
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

                specFile.close();
            }

            html <<
                R"(
                </title>
                    <link rel="stylesheet" type="text/css" href='css/)"; 
            html << styleSheet << "'>" << "\n";
            html <<
                R"( 
                    <script src='js/quiz.js'></script>
                </head>
                <body onload='startTime()'>
                <form method="get" action="gradquiz.jsp">
                <div id='header' class='header'>
                <div style='background-color: #ccc;text-align: center;border-radius: 10px; width: 240px; float: left'>
                    <img src='media/)";
            html << imgFile << "' width='190' height='163'/>" << "\n";
            html <<
                R"(
                </div>
                <div style='margin-left: 250px'>
                    <table>
                    <tr><td class='headtext'>Userid</div></td><td><input class='ctrl' id='userid' type='text' name='userid'/></td><td></td></tr>
                    <tr><td class='headtext'>Passwd</td><td><input class='ctrl' id='passwd' type='password' name='passwd'/></td></tr>
                    <tr><td class='headtext'>Name</td><td><input class='ctrl' id='name' type='text' name='name'/></td></tr>
                    <tr><td><input class='ctrl' id='pledge' type='checkbox' name='pledge'/></td>
                    <td class='headtext' colspan='2'>I pledge my honor that I have abided by the Stevens Honor System</td></tr>
                    <tr><td class='headtext'>Time Remaining</td><td id='topTime' class='time'></td><td><input id='audioControl' class='controls' type='button' value='turn audio ON' onClick='scheduleAudio()'/>
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

        void makeQuestion(nlohmann::json& question) {
            string style = question.at("style");
            string preStart, preEnd;

            if (style == "pcode" || style == "code") {
                preStart = "<pre class='pcode'>\n";
                preEnd = "</pre>";
            } else {
                preStart = "<pre class='text'>";
                preEnd = "<pre>";
            }

            if (style != "def") {
                string temp = question.at("points");
                double points = std::stod(temp);
                string questionName = question.at("name");
                html << "<div class='q' id='q" << questionNum <<
                    "'>" << questionNum << ". " << questionName << "<span class='pts'> (" << points << " points)</span></p>";
                html << "\n" << preStart << "\n";
                smatch m;
                points = points / questionCount;
                while (regex_search(questionText, m, specials)) {
                    string delim = m[2];
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
                }
                html << questionText << preEnd << "</div>\n";
                questionNum++;
            } else {
                string defs = question.at("values");
                string name = question.at("name");
                definitions[name] = defs;
                answers << "defs" << "\t" << name << "\t" << defs << '\n';
            }
        }

        void grabQuestions() {
            string line, qID, temp;
            smatch m;
            while (getline(liquizFile, line), !liquizFile.eof()) {
                if (regex_search(line, m, questionStart)) {      // looking for the beginning of a question
                    istringstream s(line);
                    nlohmann::json question;
                    s >> question;
                    getline(liquizFile, line);
                    questionText = line + '\n';
                    if (line != DELIM) {
                        while (getline(liquizFile, line), !liquizFile.eof() && line != DELIM) {
                            questionText += line;
                            questionText += '\n';
                        }
                        for (int i = 0; i < questionText.length(); i++) {
                            if (questionText[i] == '$') {
                                questionCount++;
                            }
                        }
                        questionCount /= 2;
                    }
                    makeQuestion(question);
                    questionCount = 0;
                }
            }
        }

        void generateFooter() {
            html <<
                R"(
                    <div class='controls'>
                    <div style='position: flow'>Time Remaining</div>
                    <div id='bottomTime' class='time'></div>
                    <input class='controls' type='button' value='Submit Quiz' onClick='checkAndSubmit()'/>
                    </div>
                    </form>
                </body>
                </html>
                )";
        }

        void closeFile() {
            liquizFile.close();
            html.close();
            answers.close();
        }

        void generateQuiz() {
            generateHeader();
            grabQuestions();
            generateFooter(); 
            closeFile();
        }
};

const regex LiQuizCompiler::def("^\\{def\\s+(\\w+)\\s*=\\s*\\[(.*\\])\\}");
const regex LiQuizCompiler::questionStart("^\\{");
const regex LiQuizCompiler::specials("\\$([a-z]*\\(|\\d+[cs]?\\{)?([^\\$]+)\\$");

unordered_map<string, string> LiQuizCompiler::definitions {};

class Definition : public QuestionType {
    private:
        string defs, answer, name, option;
        string typeID = "q";
        int count = 0;
    public:
        void getOptions() {
            buildString(replace, "<select class='' name='", qID, "'>");
            for (int i = 0; i <= defs.length(); i++) {
                if (defs[i] == ',' || i == defs.length()) {
                    replace += "<option value='" + option + "'>" + option + "</option>\n";
                    option = "";
                } else {
                    option += defs[i];
                }
            }
            replace += "</select>";
        }

        string print(ostream& answersFile, int& partNum, int& questionNum, double& points) {
            answer = text.erase(0,4);

            for (int i = 0; answer[i] != ':'; i++) {
                name += answer[i];
                count++;
            }

            LiQuizCompiler::findDefinitions(name, defs);
            answer = answer.erase(0, count+1);

            addAnswer(typeID, qID, answer, points, answersFile, partNum, questionNum);
            getOptions();
            answer, name = "";
            count = 0;
            return replace;
        }
};

unordered_map<string, QuestionType*> LiQuizCompiler::questionTypes {
    {"mch", new MultipleChoiceHorizontal()},
    {"mcv", new MultipleChoiceVertical()},
    {"mah", new MultipleAnswerHorizontal()},
    {"mav", new MultipleAnswerVertical()},
    {"f", new FillIn()},
    {"tar", new TextQuestion()},
    {"def", new Definition()},
    {"dro", new DropDown()},
    {"img", new Image()},
    {"vid", new Video()}
};


int main(int argc, char* argv[]) {
	try {
        if (argc < 2) {
            LiQuizCompiler L("qTesting.lq");
            L.generateQuiz();
        } else {
            for (int i = 1; i < argc; i++) {
                LiQuizCompiler L(argv[i]);
                L.generateQuiz();
            }
        }
	} catch (std::exception& e) {
		cerr << e.what() << '\n';
	}
}