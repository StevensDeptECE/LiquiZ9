#pragma once
#include <fstream>
#include <iostream>
#include <regex>
#include <unordered_map>
#include "json.hpp"

class QuestionType;

class LiQuizCompiler {
 private:
  const static std::regex questionStart;
  const static std::regex specials;
  const static std::regex qID;

  static std::unordered_map<std::string, QuestionType *> questionTypes;
  std::unordered_map<std::string, std::string> definitions;

  friend class Definition;

 private:
  const std::string DELIM = "---";
  std::string questionText;
  std::string inputText;
  std::string answerText;
  std::string answerInput;

  std::ofstream html;
  std::ofstream answers;
  std::ifstream liquizFile;
  
  QuestionType *defaultQuestionType;

  int logLevel;           // verbose level, how much debugging to display
  int questionNum;        // the number of the current question
  int partNum;            // the subnumber within each question
  int lineNumber;         // line number within the .lq file
  int questionLineNumber; //
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
  void makeQuestion(nlohmann::json &question);
  void grabQuestions();
  void generateFooter();
  void closeFile();

 public:
  LiQuizCompiler(const char liquizFileName[]);

  int getLineNumber() const { return lineNumber; }

  void generateQuiz();
};