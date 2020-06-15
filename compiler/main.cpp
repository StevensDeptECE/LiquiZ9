#include "LiQuizCompiler.hh"
//#include <exception>
using namespace std;

int main(int argc, char *argv[]) {
  try {
    if (argc < 2) {
      LiQuizCompiler L("qTesting.lq");
      L.generateQuiz();
    } else {
      for (int i = 1; i < argc; i++) {
        LiQuizCompiler L(argv[i]);
        L.generateQuiz();
      }
    }
  } catch (std::exception &e) {
    cerr << e.what() << '\n';
  }
}