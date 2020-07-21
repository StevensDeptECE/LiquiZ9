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
  virtual void setText(const std::string &t);
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
  std::string option = "";

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

class RandomVar : public QuestionType {
  private:
    std::string var;
    double min, max, inc;
    std::string typeID = "r";
  public:
    void setText(const std::string& delim) override;
    void getVar();
    void getRange();

    std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                    int &partNum, int &questionNum, double &points) override;
  friend std::ostream& operator <<(std::ostream& s, const RandomVar& r) {
    return s << r.min << "," << r.inc << "," << r.max;
  }
};

class Variable : public QuestionType {
private:
  std::string name;
public:
  void setText(const std::string& delim) override;
  std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                  int &partNum, int &questionNum, double &points) override;
};

/*
  A formula question evaluates a set of variables and can then ask the student to compute any variable
  
 */
class Formula : public QuestionType {

  std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                  int &partNum, int &questionNum, double &points) override;
  virtual ~Formula();
};
