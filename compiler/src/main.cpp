#include "LiQuizCompiler.hh"
#include <unistd.h>
using namespace std;

int main(int argc, char *argv[]) {
  try {
    if (argc < 2) {
      LiQuizCompiler L("output/");
      L.generateQuiz("demo.lq");
    } else {
      chdir(argv[1]);
      for (int i = 2; i < argc; i++) {
        LiQuizCompiler L("output/");
        L.generateQuiz(argv[i]);
      }
    }
  } catch (std::exception &e) {
    cerr << e.what() << '\n';
  }
}