OBJ	:=	main.o Questions.o LiQuizCompiler.o
CXX	:=	g++
DEBUG	:=	-g
../../bin/LiQuiz: $(OBJ)
	$(CXX) $(DEBUG) $(OBJ) -o $@

main.o : main.cpp LiQuizCompiler.hh
	$(CXX) $(DEBUG) -c $<

Questions.o : Questions.cpp Questions.hh LiQuizCompiler.hh
	$(CXX) $(DEBUG) -c $<

LiQuizCompiler.o : LiQuizCompiler.cpp Questions.hh LiQuizCompiler.hh
	$(CXX) $(DEBUG) -c $<
