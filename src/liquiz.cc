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

class Question {
protected:
	string text;
public:
	void setText(const string& t) { text = t; }
	void printCore(ostream& s) const;
	virtual void print(ostream& s) const = 0;
	virtual ~Question() {}
};

#if 0
unordered_map<string, string> mediaTypes;
void replaceSuffixWithMedia(const string& s ) {
	for (int i = s.length() - 1; i > 0; i--)
		if (s[i] == '.') {
			string suffix = s.substr(i+1);
			unordered_map<string, string>::iterator i = mediaTypes.find(suffix);
			if (i == mediaTypes.end()) {
				cerr << "Unknown media type " << suffix << '\n';
				const string& mediaTypes = i->second;
			}
		}
}
#endif
void buildStringSplitDelimiter(string& dest, const string& in, const char delimit,
															 const string& start, const string& end,
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

inline const std::string& to_string(const std::string& s) { return s; }
inline std::string to_string(char c) {
	char s[2] = {c, '\0'};
	return std::string(s);
}

template<typename... Args>
void buildString(std::string& dest, const Args&... args)
{
	dest.clear();
	using ::to_string;
	using std::to_string;
	int unpack[]{0, (dest += to_string(args), 0)...};
	static_cast<void>(unpack);
}

void buildInput(string& qid, const string& ans) {
	partNum++;
	buildString(qid, "q", questionNum, "_", partNum);
  answers << qid << '\t' << ans << '\t' << '\n';
}

void buildSurvey(string& survey, string& temp, const string& qid,
								 const string& input) {
	string question = "test";
	// TODO: quick and dirty, hardcode for now
	buildString(survey,
							"<table class='survey'>\n",
							"<tr><td>", question, "</td>\n",
							"<td>Strongly disagree <input type='radio' name='", qid, "'/></td>\n"
							"<td>Disagree <input type='radio' name='", qid, "'/></td>\n"
							"<td>unsure <input type='radio' name='", qid, "'/></td>\n"
							"<td>Agree <input type='radio' name='", qid, "'/></td>\n"
							"<td>Strongly Agree <input type='radio' name='", qid, "'/></td>",
              "</tr>");

	survey += "</table>";
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


void buildMCH(string& multipleChoice, string& temp,
							const string& qid, const string& input) {
	buildString(temp, "<td><input class='mc' type='radio' name='", qid, "'>");
	buildStringSplitDelimiter(multipleChoice, input, ',',
														"<table class='mch'><tr>", "</tr></table>",
														temp, "</input></td>");
}

void buildMCV(string& multipleChoice, string& temp,
							const string& qid, const string& input) {
	buildString(temp, "<tr><td><input class='mc' type='radio' name='", qid, "'>");
	buildStringSplitDelimiter(multipleChoice, input, ',',
														"<table class='mcv'>", "</table>",
														temp, "</input></td></tr>");
}

void buildMAH(string& multipleChoice, string& temp,
							const string& qid, const string& input) {
	buildString(temp, "<td><input class='ma' type='checkbox' name='", qid, "'>");
	buildStringSplitDelimiter(multipleChoice, input, ',',
														"<table class='mch'><tr>", "</tr></table>",
														temp, "</input></td>");
}

void buildMAV(string& multipleChoice, string& temp,
							const string& qid, const string& input) {
	buildString(temp, "<tr><td><input class='ma' type='checkbox' name='", qid, "'>");
	buildStringSplitDelimiter(multipleChoice, input, ',',
														"<table class='mcv'>", "</table>",
														temp, "</input></td></tr>");
}

class CodeQuestion : public Question {
public:
	void print(ostream& s) const override;
};

class PCodeQuestion : public Question {
public:
	void print(ostream& s) const override;
};


class TextQuestion : public Question {
public:
	void print(ostream& s) const override;
};

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
const string MCH = "mch("; //Multiple Choice Horizontal Layout
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

Question* defaultQuestionType;
unordered_map<string, Question*> questionType;
unordered_map<string, string> definitions;

/*
	TODO: case insensitive comparison
 */
regex fillInDelimiters("\\$[^\\$]+\\$");
//regex selectDelimiters("%([^%]+)%");

void CodeQuestion::print(ostream& s) const {
	s << "<pre>\n";
  printCore(s);
  s << "</pre>\n";
}

void PCodeQuestion::print(ostream& s) const {
	s << "<pre class='pcode'>\n";
  printCore(s);
  s << "</pre>\n";
}


void TextQuestion::print(ostream& s) const {
	s << "<p class='text'>\n";
  printCore(s);
	s << "</p>\n";
}

void Question::printCore(ostream& s) const {
	smatch m;
	string replace;
	replace.reserve(4096);
	string outText = text;
	string temp;
  string qid;
	temp.reserve(1024);
	while (regex_search(outText, m, specials)) { // if special pattern found
    string delim = m[1], value= m[2];
		cerr << delim << "==>" << value << '\n';
		if (m[1] == IMAGE) {
				buildString(replace, "<img src='", m[2], "'></img>");
		} else if (m[1] == AUDIO) {
			string url = m[1];
			string audioType = url.substr(url.length()-3);
			buildString(replace, "<audio controls><source src='", m[1], "' type='audio/", audioType, "'></audio>");
		} else if (m[1] == VIDEO) {
			string videoType = ""; // TODO: use this
			buildString(replace, "<video controls width='320' height='240'><source src='", m[2], "' type='video/mp4'></video>");
		} else if (m[1] == BLANK) {
			cout << "BLANK\n";
      buildInput(qid, m[2]);
      buildString(replace, "<input class='' type='text' id='", qid, "' size='6'/>");
	  } else if (m[1] == REGEX) {
      buildInput(qid, m[2]);
      buildString(replace, "<input type='text' id='", qid, "' size='6'/>");
		} else if (m[1] == SELECT) {
      buildInput(qid, m[2]);
			buildSelect(replace, temp, qid, m[2]); //TODO: this is simplified. It does not allow commas in the individual options													
		} else if (m[1] == MCH) {
      buildInput(qid, m[2]);
			buildMCH(replace, temp, qid, m[2]); //TODO: this is simplified. It does not 
		} else if (m[1] == MCV) {
      buildInput(qid, m[2]);
			buildMCV(replace, temp, qid, m[2]); //TODO: this is simplified. It does not 
		} else if (m[1] == MAH) {
      buildInput(qid, m[2]);
			buildMAH(replace, temp, qid, m[2]); //TODO: this is simplified. It does not 
		} else if (m[1] == MAV) {
      buildInput(qid, m[2]);
			buildMAV(replace, temp, qid, m[2]); //TODO: this is simplified. It does not 
		} else if (m[1] == SURVEY) {
      buildInput(qid, m[2]);
   		buildSurvey(replace, temp, qid, m[2]);
    } else if (m[1] == MAT) {
      const string& s = m[2];
      int rows = atoi(&s[0]), cols;
      int i;
      for (i = 0; s[i] != '('; i++)
        if (s[i] == ',') {
          cols = atoi(&s[i]+1);
        }
      replace.reserve(rows * cols * 50 + 100); // preallocate approx. right capacity
      replace = "<table class='mat'>";
      for (int r = 0; r < rows; r++) {
        replace += "<tr>";
        for (int c = 0; c < cols; c++) {
          replace += "<td><input class='mat' type='text' size='6'></td>";
        }
        replace += "</tr>";
       }
       replace += "</table>";
    }else if (m[1] == TEXTAREA) {
      buildInput(qid, m[2]);
			buildString(replace, "<textarea rows='20' cols='80' id='", qid, "'>", m[2], "</textarea>");
		} else if (m[1] == LOOKUP) {
			cout << "LOOKUP\n";
			unordered_map<string, string>::iterator i = definitions.find(m[2]);
			if (i == definitions.end()) {
				cerr << "undefined definition " << m[1] << '\n';
				replace="";
			} else {
				partNum++;
				replace = definitions[m[2]];
			}
		} else if (isdigit(outText[m.position(1)])) {
      string sizedFillin = m[1];
      int size = stoi(sizedFillin);
      buildInput(qid, m[2]);
      buildString(replace, "<input class='' type='text' id='", qid, "' size='", size, "'/>");

    }
		cout << "m[1]=" << m[1] << '\n';
		cout << "m[2]=" << m[2] << '\n';
		outText.replace(m.position(), m.length(), replace);
	//		string outText = regex_replace(text, specials, replace);
		//	string outText = regex_replace(text, fillInDelimiters, replacement);
		//	while (regex_search(outText, selectDelimiters
	//TODO: 
	//	while (regex_search(text, m, selectDelimiters)) {
	//	}
	}
	
	s << outText;
}

void generateHeader(ostream& out, nlohmann::json& header) {
	out <<
R"(
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"/>
<title>
)";
	if (header.find("name") != header.end()) {
		out << header["name"];
	}
	out <<
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
	cout << "</div>";
}

void generateQuestion(ostream& out,
											nlohmann::json& q,
											const string& questionText) {
	double points = 10;
	string questionName = q.at("name");
	out << "<div class='q' id='q" << questionNum <<
		"'>" << questionNum << ". " << questionName << "<span class='pts'> (" << points << " points)</span></p>";
	string qtype = q.at("qt");// ? "code" : q["type"];
	Question* question = (questionType.find(qtype) != questionType.end())
		? questionType[qtype] : defaultQuestionType;
	if (question != nullptr) {
		question->setText(questionText);
		question->print(out);
	}	
	out << "</div>\n";
	cout << "questionText: " << questionText << '\n';
	questionNum++;
}

void generateFooter(ostream& out) {
  out <<
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
string removeFileExtension(const char filename[]) {
	int i;
	for (i = 0; filename[i] != '\0'; i++)
		;
	for (; i >= 0 && filename[i] != '.'; i--)
		;
	return string(filename, i+1);
}
	
void generateLiquizHTML(const char liquizFile[]) {
	string baseFilename = removeFileExtension(liquizFile);
  string outFile = baseFilename + "html";
	string ansFile = baseFilename + "ans";
	ifstream f(liquizFile);
	ofstream out(outFile);
  answers.open(ansFile);

	string line;
  getline(f, line);
	nlohmann::json header = nlohmann::json::parse(line);
	cout << header << "\n\n";
	regex def("^\\{def\\s+(\\w+)\\s*=\\s*\\[(.*\\])\\}");
	regex jsonObj("^\\{");
	smatch m;
	string questionText;
	const string blank = "";
	questionText.reserve(1024);
	generateHeader(out, header);
  string defSelect, temp, qid;
	while (getline(f, line), !f.eof()) {
		if (regex_search(line, m, def)) {
			cout << m[1] << '\n';
			buildInput(qid, m[2]);
      buildSelect(defSelect, temp, qid, m[2]);
      definitions[m[1]] = defSelect;
		} else if (regex_search(line, m, jsonObj)) {
			istringstream s(line);
			nlohmann::json question;
			s >> question;
			cout << "question: " << question << '\n';
			cout << question.at("name") << '\n';
			getline(f, line);
			questionText = line + '\n';
			while (getline(f, line), !f.eof() && line != blank) {
				questionText += line;
        questionText += '\n';
			}
			generateQuestion(out, question, questionText);
		}
	}
	generateFooter(out);
}

int main(int argc, char* argv[]) {
	questionType["text"] = defaultQuestionType = new TextQuestion();
	questionType["pcode"] = new PCodeQuestion();
  questionType["code"] = new CodeQuestion();
	//  questionType["pcode"] = new PseudocodeQuestion();
//questionType["mc"]
	try {
    if (argc < 2)
		  generateLiquizHTML("datastruct_numbertheoretic.lq");
    else
     for (int i = 1; i < argc; i++) {
		   generateLiquizHTML(argv[i]);
     }

	} catch (std::exception& e) {
		cerr << e.what() << '\n';
	}
}
