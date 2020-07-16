#include "LiQuizCompiler.hh"
using namespace std;

int main(int argc, char *argv[]) {
  try {
    if (argc < 2) {
      LiQuizCompiler L;
      L.generateQuiz("cpe390-linuxcommands.lq");
//      L.generateQuiz("cpe553-pointers.lq");
    } else {
      for (int i = 1; i < argc; i++) {
        LiQuizCompiler L;
        L.generateQuiz(argv[i]);
      }
    }
  } catch (std::exception &e) {
    cerr << e.what() << '\n';
  }
}