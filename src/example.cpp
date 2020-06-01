#include <iostream>
#include <unordered_map>
using namespace std;


class A {
    public:
        virtual void print() = 0;
};

class B : public A {
    public:
        void print() {
            cout << "Class B\n";
        }
};

class C : public A {
    public:
        void print() {
            cout << "Class C\n";
        }
};

class D {
    private:
        static unordered_map<string, A*> questionTypes;
    private:
        A* defaultQuestionType;

        void findQuestionType(const string& type) {
            A* question = (questionTypes.find(type) != questionTypes.end())
                ? questionTypes[type] : defaultQuestionType;
            if (question != nullptr) {
                question->print();
            }
        }
    public:
        void test() {
            string test = "B";
            cout << "test\n";
            findQuestionType(test);
        }
};

unordered_map<string, A*> questionTypes {
    {"B", new B()},
    {"C", new C()}
};


int main(int argc, char* argv[]) {
    D d;
    d.test();
}