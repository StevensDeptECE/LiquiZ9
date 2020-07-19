OBJ	:=	main.o Questions.o LiQuizCompiler.o
CXX	:=	g++
DEBUG	:=	-g
LiQuiz: $(OBJ)
	$(CXX) $(DEBUG) $(OBJ) -o LiQuiz
	ln -f LiQuiz ../bin

main.o : main.cpp LiQuizCompiler.hh
	$(CXX) $(DEBUG) -c $<

Questions.o : Questions.cpp Questions.hh LiQuizCompiler.hh
	$(CXX) $(DEBUG) -c $<

LiQuizCompiler.o : LiQuizCompiler.cpp Questions.hh LiQuizCompiler.hh
	$(CXX) $(DEBUG) -c $<
