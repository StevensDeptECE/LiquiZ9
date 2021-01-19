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
  static std::string fillinStyle;
  static uint32_t defaultLen;
  static std::unordered_map<char, std::string> fillTypes;
  std::string answer, typeID, size, orig;
  int len;

 public:
  void getFillInType(const char &type);
  virtual std::string getStyle() const { return fillinStyle; }
  uint32_t getDefaultLen() const { return defaultLen; }
  std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                    int &partNum, int &questionNum, double &points) override;
};

class Hex : public FillIn {
 private:
  static std::string hexStyle;
  static uint32_t defaultLen;
 public:
  std::string getStyle() const { return hexStyle; }
  uint32_t getDefaultLen() const { return defaultLen; }
};

class OpCode : public FillIn {
 private:
  static std::string opcodeStyle;
  static uint32_t defaultLen;
 public:
  std::string getStyle() const { return opcodeStyle; }
  uint32_t getDefaultLen() const { return defaultLen; }
};

class Command : public FillIn {
 private:
  static std::string commandStyle;
  static uint32_t defaultLen;
 public:
  std::string getStyle() const { return commandStyle; }
  uint32_t getDefaultLen() const { return defaultLen; }
};

class TextQuestion : public QuestionType {
 private:
  std::string answer = "N/A";
  std::string typeID = "T";

 public:
  std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                    int &partNum, int &questionNum, double &points) override;
};

class DropDownQuestion : public QuestionType {
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
    void setText(const std::string& body) override;
    void getVar();
    void getRange();

    std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                    int &partNum, int &questionNum, double &points) override;
  friend std::ostream& operator <<(std::ostream& s, const RandomVar& r) {
    return s << r.min << "," << r.inc << "," << r.max;
  }
};

class Variable : public QuestionType {
public:
  void setText(const std::string& body) override;
  std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                  int &partNum, int &questionNum, double &points) override;
private:
  std::string name;
};

/*
  A formula question evaluates a set of variables and can then ask the student to compute any variable

*/
class FormulaQuestion : public QuestionType {
public:
  void setText(const std::string& body) override;
  std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                  int &partNum, int &questionNum, double &points) override;
  virtual ~FormulaQuestion();
};

class MatrixQuestion : public QuestionType {
public:
  void setText(const std::string& body) override;
  std::string print(const LiQuizCompiler *compiler, std::ostream &answersFile,
                  int &partNum, int &questionNum, double &points) override;
  virtual ~MatrixQuestion();
private:
  uint32_t rows,cols; // the size of the matrix
  uint32_t inputLen;
  std::string matrixList; // the list of comma-separated values to be displayed
  // an underscore (_) means a numeric fillin question
  // an asterisk (*) means a text fillin question
};