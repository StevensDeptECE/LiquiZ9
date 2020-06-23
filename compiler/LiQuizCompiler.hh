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
  std::ifstream specFile;

  QuestionType *defaultQuestionType;

  int questionNum = 1;
  int partNum;  // the subnumber within each question
  int lineNumber;
  int questionLineNumber;
  double questionCount = 0;
  double points = 0;
  int fillSize, timeLimit;
  std::string specText, imgFile, styleSheet, quizName, license, copyright,
      author, email;

  void findQuestionType(const std::string &type, double &points,
                        std::string &delim, int pos, int len);

  std::string removeExtension(const char fileName[]);

  void findDefinitions(const std::string &name, std::string &defs) const;

  nlohmann::json getJSONHeader();

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