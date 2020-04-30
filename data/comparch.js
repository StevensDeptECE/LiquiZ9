class Register {
    constructor(name, pos, size) {
        this.name = name;
        this.pos = pos;
        this.size = size;
//        this.type = type;
        this.val = 0;
    }
    setCPU(cpu) {
        this.CPU = cpu;
    }
    display() {
        this.cell.innerHTML = this.name + ' ' + this.val.toString(16);
    }
}

class CPU {
    constructor(registerList, rows, cols) {
        var totalSize = 0;
        for (var r in registerList) {
            if (r.pos + r.size/4 > totalSize) {
                totalSize = r.pos + r.size/4;
            }
        }
        this.rows = rows;
        this.cols = cols;
        this.totalSize = totalSize;
        this.registerVals = Int32Array[totalSize];
        this.registers = registerList;
        this.buildRegisterView();
        this.display();
    }
    buildRegisterView() {
        var reg = 0;
        for (var r = 0; r < this.rows; r++) {
            var row = table.insertRow();
            for (var c = 0; c < this.cols; c++, reg++) {
                var cell = row.insertCell();
                this.registers[reg].cell = cell;
            }
        }
    }
    display(table) {
        for (var i = 0; i < this.registers.length; i++)
            this.registers.display();
    }

    mov(dest, src) {
        this.registers[dest] = this.registers[src];
        this.registers[dest].display();
    }

    add(dest, src1, src2) {
        this.registers[dest].val =
            this.registers[src1].val + this.registers[src2].val;
    }

    sub(dest, src1, src2) {
        this.registers[dest].val =
            this.registers[src1].val - this.registers[src2].val;
    }
}

var cpu;
function armv6() {
    var registers = [
        new Register("r0", 0, 4),
        new Register("r1", 1, 4),
        new Register("r2", 2, 4),
        new Register("r3", 3, 4),
        new Register("r5", 5, 4),
        new Register("r6", 6, 4),
        new Register("r7", 7, 4),
        new Register("r8", 8, 4),
        new Register("r9", 9, 4),
        new Register("r10", 10, 4),
        new Register("r11", 11, 4),
        new Register("r12", 12, 4),
        new Register("r13", 13, 4),
        new Register("r14", 14, 4),
        new Register("r15", 15, 4),
        new Register("pcsr", 16, 4),
        new Register("d0", 18, 8),
        new Register("d1", 20, 8),
        new Register("d2", 22, 8),
        new Register("d3", 24, 8),
        new Register("d4", 26, 8),
        new Register("d5", 28, 8),
        new Register("d6", 30, 8),
        new Register("d7", 32, 8),
        new Register("d8", 34, 8),
        new Register("d9", 36, 8),
        new Register("d10", 38, 8),
        new Register("d11", 40, 8),
        new Register("d12", 42, 8),
        new Register("d13", 44, 8),
        new Register("d14", 46, 8),
        new Register("d15", 48, 8)
    ];
    cpu = new CPU(registers);
}
    
function comparch() {
    armv6();
}
