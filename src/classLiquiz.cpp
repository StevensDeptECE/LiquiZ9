#include "json.hpp"
#include <iostream>
#include <sstream>
#include <fstream>
#include <regex>
#include <exception>
#include <unordered_map>
using namespace std;

int questionNum = 1;
int partNum; // the subnumber within each question
ofstream answers;


const regex specials("\\$([a-z]*\\(|\\d+[cs]?\\{)?([^\\$]+)\\$");
const string BLANK = ""; // fill in the blank
const string REGEX = "re(";
// $re(  $i( ignore case
const string SELECT = "("; // selection (dropdown menu)
const string IMAGE = "img("; // a picture
const string IMAGEMAP = "map("; // an image map (interactive)
const string AUDIO = "aud("; // an audio file
const string VIDEO = "vid("; // a video
const string TEXTAREA = "ta(";
const string SURVEY = "sur("; // survey style, one line table including question
//const string MCH = "mch("; //Multiple Choice Horizontal Layout
const regex MCH("mch:");
const string MCV = "mcv("; //Multiple Choice Vertical Layout
const string MAH = "mah("; //Multiple Answer Horizontal Layout
const string MAV = "mav("; //Multiple Answer Vertical Layout
const string LOOKUP = "$%"; // lookup a previously defined select
const string MAT = "mat("; //Matrix question
const string EQ = "eq("; //Matrix question
//TODO: support select, MCH, and MCV for lookup. Right now it's just select
//TODO: remove "" from stinking lookup!
const string SUFFIXES[] = {"png", "jpg", "mp3", "mp4"};
const string PNG = "png";
const string JPG = "jpg";
const string MP4 = "mp4";
const string MP3 = "mp3";
const string OGG = "ogg";



inline const std::string& to_string(const std::string& s) {return s;}
inline std::string to_string(char c) {
	char s[2] = {c, '\0'};
	return std::string(s);
}

template<typename... Args>
void buildString(std::string& dest, const Args&... args) {
    dest.clear();
    int unpack[]{0, (dest += to_string(args), 0)...};
    static_cast<void>(unpack);
}

void buildInput(string& qid, const string& ans) {
	partNum++;
	buildString(qid, "q", questionNum, "_", partNum);
    answers << qid << '\t' << ans << '\t' << '\n';
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



class QuestionType {
    // is this the correct way?
    protected:
        string qID, replace, text;

    private:
        //string text;

    public:
        void setText(const string& t) { text = t; }
        //void printCore(ostream& s) const;
        virtual void print(ostream& s) = 0;
        virtual ~QuestionType() {}

        void contentPrint(string& type, string& value) {
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
            } else if(type == BLANK) {
                buildInput(qID, value);
                buildString(replace, "<input class='' type='text' id='", qID, "' size='6'/>");
            }
        }
};

QuestionType* defaultQuestionType;

class MulitipleChoiceHorizontal : public QuestionType {
    private:
        string temp, line;
    public:
        void print(ostream& htmlFile) {
            string outText = text;
            smatch m;
            // TODO: formatting
            htmlFile << "<pre class='pcode'>\n";

            while (regex_search(outText, m, MCH)) {
                string delim = m[1], answer = m[2];
                outText.replace(m.position(), m.length(), "<input type='radio' name='mch' value='test'>");
            }
            htmlFile << outText << "</pre>\n";
        }
};

class CodeQuestion : public QuestionType {
    private:
        string temp;
    public:
        void print(ostream& htmlFile) {
            string outText = text;
            smatch m;
            htmlFile << "<pre>\n";
            while (regex_search(outText, m, specials)) {
                string delim = m[1], value = m[2];
                contentPrint(delim, value);

                // if (isdigit(outText[m.position(1)])) {
                //     string sizedFillin = m[1];
                //     int size = stoi(sizedFillin);
                //     buildInput(qID, m[2]);
                //     buildString(replace, "<input class='' type='text' id='", qID, "' size='", size, "'/>");
                // }
                outText.replace(m.position(), m.length(), replace);
            }
            htmlFile << outText << "</pre>\n";
        }
};

class PCodeQuestion : public QuestionType {
    private:
        string temp;
    public:
        void print(ostream& htmlFile) {
            string outText = text;
            smatch m;
            htmlFile << "<pre class='pcode'>\n";
            while (regex_search(outText, m, specials)) {
                string delim = m[1], answer = m[2];
                contentPrint(delim, answer);

                // if (isdigit(outText[m.position(1)])) {
                //     string sizedFillin = m[1];

                //     int size = stoi(sizedFillin);
                //     buildInput(qID, m[2]);
                //     buildString(replace, "<input class='' type='text' id='", qID, "' size='", size, "'/>");
                // }
                outText.replace(m.position(), m.length(), replace);
            }
            htmlFile << outText << "</pre>\n";
        }
};



// class MultipleChoiceVerticle : public QuestionType {
//     private: 
//         string temp;
//     public:
//         void print(string& delim, string& value) {
//             if(delim == MCV) {
//                 buildString(qID, value);
//                 buildString(temp, "<td><input class='mc' type='radio' name='", qID, "'>");
//                 buildStringSplitDelimiter(replace, value, ',', "<table class='mcv'><tr>", "</tr></table>", temp, "</input></td>");
//             }
//         }
// };


unordered_map<string, QuestionType*> questionTypes;
unordered_map<string, string> definitions;

class LiQuiz {
    private:
        //int questionNum;
        //int partNum;
        string questionText;
        ofstream html;
        //ofstream ans;
        ifstream liquizFile;
        regex def;
	    regex questionStart;

        void findQuestionType(const string& type, const string& questionText) {
            QuestionType* question = (questionTypes.find(type) != questionTypes.end())
                ? questionTypes[type] : defaultQuestionType;
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
        LiQuiz(const char liquizFileName[]) :  def("^\\{def\\s+(\\w+)\\s*=\\s*\\[(.*\\])\\}"), questionStart("^\\{") {
            questionText.reserve(1024);
            string baseFileName = removeExtension(liquizFileName);
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
        }

        void generateQuestion(nlohmann::json& question) {
            double points = 10;
            string questionName = question.at("name");
            html << "<div class='q' id='q" << questionNum <<
                "'>" << questionNum << ". " << questionName << "<span class='pts'> (" << points << " points)</span></p>";
            string qType = question.at("qt");
            findQuestionType(qType, questionText);
            html << "</div>\n";
            questionNum++;
        }

        friend void buildInput(string& qid, const string& ans);

        void grabQuestions() {
            string line, qID, temp, defSelect;
            smatch m;
            while (getline(liquizFile, line), !liquizFile.eof()) {
                if(regex_search(line, m, def)) {    // looking for definitions - TODO: this doesn't seem to work
                    buildInput(qID, m[2]);
                    buildSelect(defSelect, temp, qID, m[2]);
                    definitions[m[1]] = defSelect;
                } else if (regex_search(line, m, questionStart)) {      // looking for the beginning of a question
                    istringstream s(line);
                    nlohmann::json question;
                    s >> question;
                    string questionType = question.at("qt");
                    getline(liquizFile, line);
                    questionText = line + '\n';
                    while (getline(liquizFile, line), !liquizFile.eof() && line != BLANK) {
                        questionText += line;
                        questionText += '\n';
                    }
                    generateQuestion(question);
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


int main(int argc, char* argv[]) {
    questionTypes["pcode"] = new PCodeQuestion();
    questionTypes["code"] = new CodeQuestion();
    questionTypes["mch"] = new MulitipleChoiceHorizontal();

	try {
        if (argc < 2) {
            LiQuiz L("datastruct_numbertheoretic.lq");
            L.generateQuiz();
        } else {
            for (int i = 1; i < argc; i++) {
                LiQuiz L(argv[i]);
                L.generateQuiz();
            }
        }
	} catch (std::exception& e) {
		cerr << e.what() << '\n';
	}
}