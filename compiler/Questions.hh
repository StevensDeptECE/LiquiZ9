#pragma once
#include <iostream>
#include <regex>
#include <string>
#include <unordered_map>

class LiQuizCompiler;
class QuestionType {
 protected:
  LiQuizCompiler *compiler;
  std::string qID, replace, text, defName;
  int fillSize;

 public:
  void setText(const std::string &t) { text = t; }
  virtual std::string print(const LiQuizCompiler *compiler, std::ostream &a,
                            int &pN, int &qN, double &p) = 0;
  virtual ~QuestionType();

  void addAnswer(std::string &typeID, std::string &qID, const std::string &ans,
                 double points, std::ostream &answersFile, int &partNum,
                 int &questionNum);

  std::string setAnswer(const std::string &ogText);
};

class MultipleChoiceHorizontal : public QuestionType {
 private:
  std::string temp, input, answer;
  std::string typeID = "q";
  std::string option = " ";

 public:
  void getAnswer();
  void getOptions();

  std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                    int &partNum, int &questionNum, double &points) override;
  
  // std::string setAnswer() override;
};

class MultipleChoiceVertical : public QuestionType {
 private:
  std::string temp, input, answer;
  std::string typeID = "q";
  std::string option = "";

 public:
  void getAnswer();
  void getOptions();

  std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                    int &partNum, int &questionNum, double &points) override;
};

class MultipleAnswerHorizontal : public QuestionType {
 private:
  std::string temp, input, answer;
  std::string typeID = "m";
  std::string option = "";

 public:
  void getAnswer();
  void getOptions();

  std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                    int &partNum, int &questionNum, double &points) override;
};

class MultipleAnswerVertical : public QuestionType {
 private:
  std::string temp, input, answer;
  std::string typeID = "m";
  std::string option = "";

 public:
  void getAnswer();
  void getOptions();

  std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                    int &partNum, int &questionNum, double &points) override;
};

class FillIn : public QuestionType {
 private:
  static std::unordered_map<char, std::string> fillTypes;
  std::string answer, typeID, size, orig;
  int len = 6;

 public:
  void getFillInType(const char &type);

  std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                    int &partNum, int &questionNum, double &points) override;
};

class TextQuestion : public QuestionType {
 private:
  std::string answer = "N/A";
  std::string typeID = "T";

 public:
  std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                    int &partNum, int &questionNum, double &points) override;
};

class DropDown : public QuestionType {
 private:
  std::string answer, option, input;
  std::string typeID = "q";
  int count = 0;

 public:
  void getAnswer();

  void getOptions();

  std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                    int &partNum, int &questionNum, double &points) override;
};

class Image : public QuestionType {
 public:
  std::string print(const LiQuizCompiler *, std::ostream &answersFile,
                    int &partNum, int &questionNum, double &points) override;
};

class Video : public QuestionType {
 public:
  std::string print(const LiQuizCompiler *, std::ostream &answersFile, int &partNum,
               int &questionNum, double &points) override;
};

class Definition : public QuestionType {
 private:
  std::string defs, answer, name, option;
  std::string typeID = "q";
  int count = 0;

 public:
  void getOptions();

  std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                    int &partNum, int &questionNum, double &points) override;
};

class RandomQuestion : public QuestionType {
  private:
    std::string var, minVal, maxVal, increm;
    double min, max, inc;
    std::string typeID = "r";
  public:
    void getVar();
    void getRange();

    std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                    int &partNum, int &questionNum, double &points) override;
};