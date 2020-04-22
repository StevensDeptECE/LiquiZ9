BIN	:= ${LIQUIZ}/bin
DATA	:= ${LIQUIZ}/data

all: $(BIN)/liquiz

$(BIN)/liquiz: liquiz.cc
	$(CXX) liquiz.cc -o $(BIN)/liquiz -o $(BIN)/liquiz

quiz:
	cd web; liquiz ../data/*.lq

