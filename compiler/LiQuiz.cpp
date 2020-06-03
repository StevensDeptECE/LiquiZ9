#include "json.hpp"
#include <iostream>
#include <sstream>
#include <fstream>
#include <regex>
#include <exception>
#include <unordered_map>
using namespace std;


string definitions;


inline const std::string& to_string(const std::string& s) {return s;}
inline std::string to_string(char c) {
    char s[2] = {c, '\0'};
    return std::string(s);
}

class QuestionType {
    friend class LiQuizCompiler;
    protected:
        const static regex specials;
        const static string DELIM;

        const static string REGEX;
        const static string SELECT; // selection (dropdown menu)
        const static string IMAGE; // a picture
        const static string IMAGEMAP; // an image map (interactive)
        const static string AUDIO; // an audio file
        const static string VIDEO; // a video
        const static string TEXTAREA;
        const static string SURVEY; // survey style, one line table including question
        const static string MAT; //Matrix question
        const static string EQ; //Matrix question
        //TODO: support select, MCH, and MCV for lookup. Right now it's just select
        //TODO: remove "" from stinking lookup!
        const static string SUFFIXES[];
        const static string PNG;
        const static string JPG;
        const static string MP4;
        const static string MP3;
        const static string OGG;

        const static regex CASEINSENS;
        const static regex SPACEINSENS;
        const static regex NUMERIC;
        const static regex SPACECASE;
        const static regex LOOKUP;
    private:
        double points;
    // is this the correct way?
    protected:
        string qID, replace, text;

    public:
        void setText(const string& t) { text = t; }
        virtual void print(ostream& h, ostream& a, int& pN, int& qN) = 0;
        virtual ~QuestionType() {}

        void setPoints(double p, double questionCount) {
            points = p / questionCount;
        }

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

        void buildStringSplitDelimiter(string& dest, const string& in, const char delimit, const string& start, const string& end, 
                                            const string& pre, const string& post) {
            dest = start;
            int startIndex = 0, endIndex;
            if (in[startIndex]=='"')
                startIndex++; // skip surrounding quotes
            for (int i = 0; i < in.length(); i++) {
                if (in[i] == delimit) {
                    endIndex = i-1;
                    if (in[endIndex] == '"')
                        endIndex--;
                    dest += pre;
                    for (int j = startIndex; j <= endIndex; j++)
                        dest += in[j];
                    dest += post;
                    startIndex = i+1;
                    if (in[startIndex] == '"')
                        startIndex++;
                }
            }
            dest += pre;
            endIndex = in.length()-1;
            if (in[endIndex] == '"')
                endIndex--;
            for (int j = startIndex; j < endIndex; j++)
                dest += in[j];
            dest += post;
            dest += end;
        }

        void contentPrint(string& typeID, string& type, string& value, ostream& answersFile, int& partNum, int& questionNum) {
            replace.reserve(4096);

            if(type == IMAGE) {
                buildString(replace, "<img src='", value, "'></img>");
            } else if(type == AUDIO) {
                string url = type;
                string audioType = url.substr(url.length()-3);
                buildString(replace, "<audio controls><source src='", type, "' type='audio/", audioType, "'></audio>");
            } else if(type == VIDEO) {
                string videoType = ""; // TODO: use this
			    buildString(replace, "<video controls width='320' height='240'><source src='", value, "' type='video/mp4'></video>");
            } else if(type == DELIM) {
                addAnswer(typeID, qID, value, points, answersFile, partNum, questionNum);
                buildString(replace, "<input class='' name='", qID, "'type='text' id='", qID, "' size='6'/>");
            }
        }
};

const regex QuestionType::specials("\\$([a-z]*\\(|\\d+[cs]?\\{)?([^\\$]+)\\$");
const string QuestionType::DELIM = "---"; // fill in the blank

const string QuestionType::REGEX = "re(";
// $re(  $i( ignore case
const string QuestionType::SELECT = "("; // selection (dropdown menu)
const string QuestionType::IMAGE = "img("; // a picture
const string QuestionType::IMAGEMAP = "map("; // an image map (interactive)
const string QuestionType::AUDIO = "aud("; // an audio file
const string QuestionType::VIDEO = "vid("; // a video
const string QuestionType::TEXTAREA = "ta(";
const string QuestionType::SURVEY = "sur("; // survey style, one line table including question
const string QuestionType::MAT = "mat("; //Matrix question
const string QuestionType::EQ = "eq("; //Matrix question
//TODO: support select, MCH, and MCV for lookup. Right now it's just select
//TODO: remove "" from stinking lookup!
const string QuestionType::SUFFIXES[] = {"png", "jpg", "mp3", "mp4"};
const string QuestionType::PNG = "png";
const string QuestionType::JPG = "jpg";
const string QuestionType::MP4 = "mp4";
const string QuestionType::MP3 = "mp3";
const string QuestionType::OGG = "ogg";

const regex QuestionType::CASEINSENS("Q:");
const regex QuestionType::SPACEINSENS("s:");
const regex QuestionType::NUMERIC("n:");
const regex QuestionType::SPACECASE("S:");
const regex QuestionType::LOOKUP("%");

class MultipleChoiceHorizontal : public QuestionType {
    private:
        string temp, input, answer, option;
        string typeID = "q";
    public:
        void getAnswer() {
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
            buildString(temp, "<input class='mc' name='", qID, "'type='radio' value='");
            for (int i = 0; i <= answer.length(); i++) {
                if (answer[i] == ',' || i == answer.length()) {
                    replace += temp + option + "'>" + option + "\t";
                    option = "";
                } else {
                    option += answer[i];
                }
            }
        }

        void print(ostream& htmlFile, ostream& answersFile, int& partNum, int& questionNum) {
            string outText = text;
            smatch m;
            // TODO: formatting
            htmlFile << "<pre class=''>\n";
            while (regex_search(outText, m, specials)) {
                string delim = m[1];
                answer = m[2];
                getAnswer();
                contentPrint(typeID, delim, input, answersFile, partNum, questionNum);
                getOptions();
                outText.replace(m.position(), m.length(), replace);
            }
            htmlFile << outText << "</pre>\n";
        }
};

class MultipleChoiceVertical : public QuestionType {
    private:
        string temp, input, answer, option;
        string typeID = "q";
    public:
        void getAnswer() {
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
            buildString(temp, "<input class='mc' name='", qID, "'type='radio' value='");
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

        void print(ostream& htmlFile, ostream& answersFile, int& partNum, int& questionNum) {
            string outText = text;
            smatch m;
            // TODO: formatting
            htmlFile << "<pre class=''>\n";
            while (regex_search(outText, m, specials)) {
                string delim = m[1];
                answer = m[2];
                getAnswer();
                contentPrint(typeID, delim, input, answersFile, partNum, questionNum);
                getOptions();
                outText.replace(m.position(), m.length(), replace);
            }
            htmlFile << outText << "</pre>\n";
        }
};

class MultipleAnswerHorizontal : public QuestionType {
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
            buildString(temp, "<input class='ma' name='", qID, "'type='checkbox' value='");
            for (int i = 0; i <= answer.length(); i++) {
                if (answer[i] == ',' || i == answer.length()) {
                    replace += temp + option + "'>" + option + "\t";
                    option = "";
                } else {
                    option += answer[i];
                }
            }
        }

        void print(ostream& htmlFile, ostream& answersFile, int& partNum, int& questionNum) {
            string outText = text;
            smatch m;
            // TODO: formatting
            htmlFile << "<pre class=''>\n";
            while (regex_search(outText, m, specials)) {
                string delim = m[1];
                answer = m[2];
                getAnswer();
                contentPrint(typeID, delim, input, answersFile, partNum, questionNum);
                getOptions();
                outText.replace(m.position(), m.length(), replace);
            }
            htmlFile << outText << "</pre>\n";
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
            buildString(temp, "<input class='ma' name='", qID, "'type='checkbox' value='");
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

        void print(ostream& htmlFile, ostream& answersFile, int& partNum, int& questionNum) {
            string outText = text;
            smatch m;
            // TODO: formatting
            htmlFile << "<pre class=''>\n";
            while (regex_search(outText, m, specials)) {
                string delim = m[1];
                answer = m[2];
                getAnswer();
                contentPrint(typeID, delim, input, answersFile, partNum, questionNum);
                getOptions();
                outText.replace(m.position(), m.length(), replace);
            }
            htmlFile << outText << "</pre>\n";
        }
};

class CodeQuestion : public QuestionType {
private:
        string temp, typeID, option, outText;
    public:
        void getAnswer(string& answer) {
            for (int i = 0; i <= answer.length(); i++) {
                if (answer[i] == ',' || i == answer.length()) {
                    temp += "\t";
                } else {
                    temp += answer[i];
                }
            }
            answer = temp;
        }

        void fillType(string& line) {
            smatch m;
            if (regex_search(line, m, CASEINSENS)) {
                typeID = "Q";
                line.replace(m.position(), m.length(), "");
            } else if (regex_search(line, m, SPACEINSENS)) {
                typeID = "s";
                line.replace(m.position(), m.length(), "");
            } else if (regex_search(line, m, NUMERIC)) {
                typeID = "n";
                getAnswer(line);
                line.replace(m.position(), m.length(), "");
            } else if (regex_search(line, m, SPACECASE)) {
                typeID = "S";
                line.replace(m.position(), m.length(), "");
            } else if (regex_search(line, m, LOOKUP)) {
                typeID = "L";
                line.replace(m.position(), m.length(), "");
            } else {
                typeID = "q";
            }
        }

        void print(ostream& htmlFile, ostream& answersFile, int& partNum, int& questionNum) {
            outText = text;
            smatch m;
            htmlFile << "<pre class='pcode'>\n";
            while (regex_search(outText, m, specials)) {
                string delim = m[1], value = m[2];
                fillType(value);
                if(typeID != "L") {
                    outText.replace(m.position(), m.length(), replace);
                } else {
                    typeID = "q";
                    outText.replace(m.position(), m.length(), definitions);
                }
                contentPrint(typeID, delim, value, answersFile, partNum, questionNum);
            }
            htmlFile << outText << "</pre>\n";
        }
 };

class PCodeQuestion : public QuestionType {
    private:
        string temp, typeID, option, outText;
    public:
        void getAnswer(string& answer) {
            for (int i = 0; i <= answer.length(); i++) {
                if (answer[i] == ',' || i == answer.length()) {
                    temp += "\t";
                } else {
                    temp += answer[i];
                }
            }
            answer = temp;
        }

        void fillType(string& line) {
            smatch m;
            if (regex_search(line, m, CASEINSENS)) {
                typeID = "Q";
                line.replace(m.position(), m.length(), "");
            } else if (regex_search(line, m, SPACEINSENS)) {
                typeID = "s";
                line.replace(m.position(), m.length(), "");
            } else if (regex_search(line, m, NUMERIC)) {
                typeID = "n";
                getAnswer(line);
                line.replace(m.position(), m.length(), "");
            } else if (regex_search(line, m, SPACECASE)) {
                typeID = "S";
                line.replace(m.position(), m.length(), "");
            } else if (regex_search(line, m, LOOKUP)) {
                typeID = "L";
                line.replace(m.position(), m.length(), "");
            } else {
                typeID = "q";
            }
        }

        void print(ostream& htmlFile, ostream& answersFile, int& partNum, int& questionNum) {
            outText = text;
            smatch m;
            htmlFile << "<pre class='pcode'>\n";
            while (regex_search(outText, m, specials)) {
                string delim = m[1], value = m[2];
                fillType(value);
                if(typeID != "L") {
                    outText.replace(m.position(), m.length(), replace);
                } else {
                    typeID = "q";
                    outText.replace(m.position(), m.length(), definitions);
                }
                contentPrint(typeID, delim, value, answersFile, partNum, questionNum);
            }
            htmlFile << outText << "</pre>\n";
        }
};

class Definitions : public QuestionType {
    private:
        string temp, defselect, option, defs;
        string typeID = "def";
    public:
        void getOptions() {
            buildString(definitions, "<select class='' name='", qID, "'>");
            for (int i = 0; i <= defs.length(); i++) {
                if (defs[i] == ',' || i == defs.length()) {
                    definitions += "<option value='" + option + "'>" + option + "</option>\n";
                    option = "";
                } else {
                    option += defs[i];
                }
            }
            definitions += "</select>";
        }

        void print(ostream& htmlFile, ostream& answersFile, int& partNum, int& questionNum) {
            string outText = text;
            smatch m;
            if (regex_search(outText, m, specials)) {
                defs = m[2];
                getOptions();
                answersFile << typeID << "\t\t\t" << defs << "\n";
            }
        }
};

class LiQuizCompiler {
    private:
        const static regex def;
	    const static regex questionStart;
        static unordered_map<string, QuestionType*> questionTypes;
    private:
        string questionText;
        ofstream html;
        ofstream answers;
        ifstream liquizFile;
        QuestionType* defaultQuestionType;
        int questionNum = 1;
        int partNum; // the subnumber within each question
        double questionCount = 0;
        

        void findQuestionType(const string& type, const string& questionText, const double& points) {
            QuestionType* question = (questionTypes.find(type) != questionTypes.end())
                ? questionTypes[type] : defaultQuestionType;
            if (question != nullptr) {
                question->setPoints(points, questionCount);
                question->setText(questionText);
                question->print(html, answers, partNum, questionNum);
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

    public:
        LiQuizCompiler(const char liquizFileName[]) {
            questionText.reserve(1024);
            string baseFileName = removeExtension(liquizFileName);
            liquizFile.open("quizzes/" + baseFileName + "lq");
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
            if (header.find("name") != header.end()) {
                html << header["name"];
            }
            html <<
                R"(
                </title>
                    <link rel="stylesheet" href="css/quiz.css" type="text/css">
                    <script src='js/quiz.js'></script>
                </head>
                <body onload='startTime()'>
                <form method="get" action="gradquiz.jsp">
                <div id='header' class='header'>
                <div style='background-color: #ccc;text-align: center;border-radius: 10px; width: 240px; float: left'>
                    <img src='media/StevensLogo380x326.png' width='190' height='163'/>
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
            string temp = (question.at("points"));
            string qType = question.at("qt");
            double points = std::stod(temp);
            if (qType != "def") {
                string questionName = question.at("name");
                html << "<div class='q' id='q" << questionNum <<
                    "'>" << questionNum << ". " << questionName << "<span class='pts'> (" << points << " points)</span></p>";
                findQuestionType(qType, questionText, points);
                html << "</div>\n";
                questionNum++;
            } else {
                findQuestionType(qType, questionText, points);
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
                    string questionType = question.at("qt");
                    getline(liquizFile, line);
                    questionText = line + '\n';
                    while (getline(liquizFile, line), !liquizFile.eof() && line != QuestionType::DELIM) {
                        questionText += line;
                        questionText += '\n';
                    }
                    for (int i = 0; i < questionText.length(); i++) {
                        if (questionText[i] == '$') {
                            questionCount++;
                        }
                    }
                    questionCount /= 2;
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

unordered_map<string, QuestionType*> LiQuizCompiler::questionTypes {
    {"pcode", new PCodeQuestion()},
    {"code", new CodeQuestion()},
    {"mch", new MultipleChoiceHorizontal()},
    {"mcv", new MultipleChoiceVertical()},
    {"mah", new MultipleAnswerHorizontal()},
    {"mav", new MultipleAnswerVertical()},
    {"def", new Definitions()}
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