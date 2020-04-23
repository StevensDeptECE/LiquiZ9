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

class Question {
protected:
	string text;
public:
	void setText(const string& t) { text = t; }
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
	using ::to_string;
	using std::to_string;
	int unpack[]{0, (dest += to_string(args), 0)...};
	static_cast<void>(unpack);
}

/*
	Build a select from a string current delimited with ','
  TODO: This needs to be more sophisticated. Right now no element may
	contain a comma.

	For efficiency, this function takes two strings to build
  The first is for the entire string, the second is a temporary used to build it
 */
void buildSelect(string& select, string& temp, const string& input) {
	buildString(temp, "<select id='q", questionNum, "_", partNum, "'>");
	buildStringSplitDelimiter(select, input, ',', temp, "</select>", "<option>", "</option>");
}

class CodeQuestion : public Question {
public:
	void print(ostream& s) const override;
};


const regex specials("(\\$[\\[\\{%\\(\\$]?)(.*)[\\]\\}\\)\\$]?\\$");
const string BLANK = "$";
const string MEDIA = "$[";
const string REGEX = "${";
const string SELECT = "$(";
const string MCH = "$mch("; //Multiple Choice Horizontal Layout
const string MCV = "$mcv("; //Multiple Choice Vertical Layout
const string LOOKUP = "$%"; // lookup a previously defined select
//TODO: support select, MCH, and MCV for lookup. Right now it's just select
//TODO: remove "" from stinking lookup!
const string TEXTAREA = "$(";
const string SUFFIXES[] = {"png", "jpg", "mp3", "mp4"};
const string PNG = "png";
const string JPG = "jpg";
const string MP4 = "mp4";
const string MP3 = "mp3";
const string OGG = "ogg";
unordered_map<string, Question*> questionType;
unordered_map<string, string> definitions;

/*
	TODO: case insensitive comparison
 */
regex fillInDelimiters("\\$[^\\$]+\\$");
//regex selectDelimiters("%([^%]+)%");
void CodeQuestion::print(ostream& s) const {
	s << "<pre>";
	smatch m;
	string replace;
	replace.reserve(4096);
	string outText = text;
	string temp;
	temp.reserve(1024);
	while (regex_search(outText, m, specials)) { // if special pattern found
		if (m[1] == MEDIA) {
		  const string& url = m[2];
			const string suffix = url.substr(url.length()-3);
			if (suffix == PNG || suffix == JPG) {
				buildString(replace, "<img src='", url, "'></img>");
			} else if (suffix == MP4) {
				buildString(replace, "<video controls width='320' height='240'><source src='", url, "' type='video/mp4'></video>");
			} else if (suffix == MP3) {
				buildString(replace, "<audio controls><source src='", url, "' type='audio/mp3'></audio>");
			} else if (suffix == OGG) {
				buildString(replace, "<audio controls><source src='", url, "' type='audio/ogg'></audio>");
			} 
		} else if (m[1] == BLANK) {
  		partNum++;
      buildString(replace, "<input type='text' id='q", questionNum, '_', partNum, "' size='6'/>");
	  } else if (m[1] == REGEX) {
  		partNum++;
      buildString(replace, "<input type='text' id='q", questionNum, '_', partNum, "' size='6'/>");
		} else if (m[1] == SELECT) {
			partNum++;
			buildSelect(replace, temp, m[2]); //TODO: this is simplified. It does not allow commas in the individual options													
		} else if (m[1] == TEXTAREA) {
			partNum++;
			buildString(replace, "<textarea id='q", questionNum, '_', partNum, "'>", m[2], "</textarea>");
		} else if (m[1] == LOOKUP) {
			unordered_map<string, string>::iterator i = definitions.find(m[2]);
			if (i == definitions.end()) {
				cerr << "undefined definition " << m[1] << '\n';
				continue;
			}
			partNum++;
			replace = definitions[m[2]];
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
	s << "</pre>";
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
	out << "<div class='q' id='q" << questionNum <<
		" <span class='pts'>(" << points << ")</span></p>";
	string qtype = (q.find("type") == q.end()) ? "code" : q["type"];
	Question* question = questionType[qtype];
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
    <div class="controls">
      <div style="position: flow">Time Remaining</div>
      <div id="bottomTime" class="time"></div>
      <input class="controls" type="button" value="Submit Quiz" onClick="checkAndSubmit()\"/>
    </div>
    </form>
  </body>
</html>
)";
}

void generateLiquizHTML(const char liquizFile[], const char htmlFile[]) {
	ifstream f(liquizFile);
	ofstream out(htmlFile);
	string line;
  getline(f, line);
	istringstream s(line);
	nlohmann::json header;
	s >> header;
	cout << header << "\n\n";
	regex def("^\\{def\\s+(\\w+)\\s*=\\s*\\[(.*\\])\\}");
	regex jsonObj("^\\{");
	smatch m;
	string questionText;
	const string blank = "";
	questionText.reserve(1024);
	generateHeader(out, header);
  string defSelect, temp;
	while (getline(f, line), !f.eof()) {
		if (regex_search(line, m, def)) {
			cout << m[1] << '\n';
      buildSelect(defSelect, temp, m[2]);
      definitions[m[1]] = defSelect;
		} else if (regex_search(line, m, jsonObj)) {
			istringstream s(line);
			nlohmann::json question;
			s >> question;
			cout << "question: " << question << '\n';
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
questionType["code"] = new CodeQuestion();
//questionType["pcode"] = new PseudocodeQuestion();
//questionType["mc"]
	try {
    if (argc < 2)
		  generateLiquizHTML("datastruct_numbertheoretic.lq", "datastruct_numbertheoretic.html");
    else
     for (int i = 1; i < argc; i++) {
       string outFile = argv[i];
       outFile = outFile.substr(0,outFile.length()-2);
       outFile += "html";
		   generateLiquizHTML(argv[i], outFile.c_str());
     }

	} catch (std::exception& e) {
		cerr << e.what() << '\n';
	}
}
