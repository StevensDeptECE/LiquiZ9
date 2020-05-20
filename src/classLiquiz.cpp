#include "json.hpp"
#include <iostream>
#include <sstream>
#include <fstream>
#include <regex>
#include <exception>
#include <unordered_map>
using namespace std;


class LiQuiz {
    private:
        int questionNum;
        int partNum;
        ofstream html;
        ofstream ans;
        ifstream liquizFile;
        regex def("^\\{def\\s+(\\w+)\\s*=\\s*\\[(.*\\])\\}");
	    regex questionStart("^\\{");
        static unordered_map<string, QuestionType*> questionTypes;
        static unordered_map<string, string> definitions;

        static QuestionType* findQuestionType(const string& type, const string& questionText) {
            QuestionType* question = (questionType.find(type) != questionType.end())
                ? questionType[type] : defaultQuestionType;
            if (question != nullptr) {
                // TODO: part of Question class
                question->setText(questionText);
                question->print(html);
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
        LiQuiz(const char liquizFileName[]) : questionNum(1) {}

        void openFile(const char liquizFileName[]) {
            liquizFile.open(liquizFileName);
            string baseFileName = removeExtension(liquizFileName);
            html.open(baseFileName + "html");
            ans.open(baseFileName + "ans");
        }

        string getJSONHeader() {
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
                    <link rel="stylesheet" href="quiz.css" type="text/css"/>
                    <script src='quiz.js'></script>
                </head>
                <body onload='startTime()'>
                <form method="get" action="gradquiz.jsp">
                <div id='header' class='header'>
                <div style='background-color: #ccc;text-align: center;border-radius: 10px; width: 240px; float: left'>
                    <img src='StevensLogo380x326.png' width='190' height='163'/>
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
                <audio id="alert25"><source src="25min.ogg" type="audio/ogg"/></audio>
                <audio id="alert20"><source src="20min.ogg" type="audio/ogg"></audio>
                <audio id="alert15"><source src="15min.ogg" type="audio/ogg"></audio>
                <audio id="alert10"><source src="10min.ogg" type="audio/ogg"></audio>
                <audio id="alert5"><source src="5min.ogg" type="audio/ogg"></audio>
                <audio id="alertover"><source src="over.ogg" type="audio/ogg"></audio>
                <audio id="classical">
                <source src="JohnLewisGrant_BachPrelude_01.mp3" type="audio/mp3"/>
                    <source src="JohnLewisGrant_BachPrelude_02.mp3" type="audio/mp3"/>
                    <source src="JohnLewisGrant_BachPrelude_03.mp3" type="audio/mp3"/>
                </audio>
                </div>
                </div>

                )";
            cout << "</div>";   // ?
        }

        inline const std::string& to_string(const std::string& s) {return s;}
        template<typename... Args>
        void buildString(std::string& dest, const Args&... args) {
            dest.clear();
            int unpack[]{0, (dest += to_string(args), 0)...};
        }

        void addAnswer(string& qid, const string& ans) {
            partNum++;
            buildString(qid, "q", questionNum, "_", partNum);
            ans << qid << '\t' << ans << '\t' << '\n';
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

        /*
        Build a select from a string current delimited with ','
        TODO: This needs to be more sophisticated. Right now no element may
        contain a comma.

        For efficiency, this function takes two strings to build
        The first is for the entire string, the second is a temporary used to build it
        */
        void buildSelect(string& select, string& temp, const string& qid, const string& input) {
            buildString(temp, "<select id='", qid, "'>\n" "<option></option>");
            buildStringSplitDelimiter(select, input, ',', temp, "</select>", "<option>", "</option>");
        }

        void grabQuestions() {
            string line, qID, temp, defSelect, questionText;
            questionText.reserve(1024);
            const string blank = "";
            smatch m;
            while (getline(liquizFile, line), !liquizFile.eof()) {
                if(regex_search(line, m, def)) {
                    buildInput(qID, m[2]);
                    buildSelect(defSelect, temp, qID, m[2]);
                    definitions[m[1]] = defSelect;
                } else if (regex_search(line, m, questionStart)) {
                    istringstream s(line);
                    nlohmann::json question;
                    s >> question;
                    cout << "question: " << question << '\n';   // need couts?
                    cout << question.at("name") << '\n';
                    getline(liquizFile, line);
                    questionText = line + '\n';
                    while (getline(liquizFile, line), !liquizFile.eof() && line != blank) {
                        questionText += line;
                        questionText += '\n';
                    }
                }
            }

        }

        void generateQuestion(nlohmann::json& question, const string& questionText) {
            double points = 10;
            string questionName = question.at("name");
            html << "<div class='q' id='q" << questionNum <<
                "'>" << questionNum << ". " << questionName << "<span class='pts'> (" << points << " points)</span></p>";
            string qType = question.at("qt");
            findQuestionType(qType, questionText);
            html << "</div>\n";
            cout << "questionText: " << questionText << '\n';
            questionNum++;
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
            liquizeFile.close();
            html.close();
            ans.close();
        }

        void generateQuiz(const char fileName[]) {
            openFile(fileName);
            generateHeader();
            grabQuestions();
            generateQuestion();
            generateFooter(); 
            closeFile();
        }
};




int main() {

}