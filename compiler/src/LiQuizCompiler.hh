#pragma once
#include <fstream>
#include <iostream>
#include <regex>
#include <map>
#include <unordered_map>
#include "json.hpp"

class QuestionType;

class LiQuizCompiler {
 private:
  const static std::regex questionStart;
  const static std::regex specials;
  const static std::regex qID;
  
  static uint32_t   uuid;

  static std::unordered_map<std::string, QuestionType *> questionTypes;
  std::map<std::string, std::string> variables;

  std::unordered_map<std::string, std::string> definitions;

  friend class Definition;

 private:
  const std::string DELIM = "---\n";
  std::string questionText;
  std::string inputText;
  std::string answerText;
  std::string answerInput;

  std::string outputDir; // directory where the public output goes (html)
  //TODO: need to generate randomized numbers for the images so that file names do not 
  //give away answers and place in the output directory
  std::ofstream html;
  std::ofstream answers;
  std::ofstream xml;

  uint32_t cursor; // byte offset into quiz file
  char* bytes; // underlying bytes in quiz
  uint32_t fileSize; // size of quiz text in bytes

  QuestionType *defaultQuestionType;

  int logLevel;           // verbose level, how much debugging to display
  int questionNum;        // the number of the current question
  int partNum;            // the subnumber within each question
  int lineNum;            // line number within the .lq file
  //int questionLineNumber; // TODO: old, get rid of this!
  double questionCount;   // number of question inputs in the current question
  double points;          // total number of points in the quiz
  int fillSize;           // default number of characters in a fill-in question
  int timeLimit;          // number of minutes to take the quiz, 0 means untimed

  std::string imgFile, styleSheet, quizName, license, copyright,
      author, email;

  void setLogLevel(int level) { logLevel = level; }
  void findQuestionType(const std::string &type, double &points,
                        std::string &delim, int pos, int len);

  std::string removeExtension(const char fileName[]);

  void findDefinitions(const std::string &name, std::string &defs) const;

  void includeQSpec(nlohmann::json* parentQuizSpec, const std::string& filename);
  void getJSONHeader();

  void setAnswer();

  void generateHeader();
  void saveXML();
  void makeQuestion(nlohmann::json &question);
  void grabQuestions();
  void generateFooter();
  void closeFile();
  void readFile(const char filename[], char*& bytes, uint32_t& fileSize);

  bool getline(std::string& line);

 public:
  LiQuizCompiler(const char outputDir[]);
  int getLineNum() const { return lineNum; }

  void generateQuiz(const char liquizFileName[]);
  void dumpVariables();
};
