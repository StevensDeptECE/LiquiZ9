#include <iostream>
#include <sstream>
#include "json.hpp"
using namespace std;

int main() {
	istringstream s( R"({"a":"hello", "b":23})" );
	nlohmann::json j;
	s >> j;
	cout << j.at("a") << '\n';
	cout << j.at("b") << '\n';
}
