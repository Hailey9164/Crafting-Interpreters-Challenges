# What bytecode instruction sequences would you generate for the following expressions:
# 1 * 2 + 3
# 1 + 2 * 3
# 3 - 2 - 1
# 1 + 2 * 3 - 4 / -5
# (Remember that Lox does not have a syntax for negative number literals, so the -5 is negating the number 5.)

CONST 1, CONST 2, MULTIPLY, CONST 3, ADD

CONST 1, CONST 2, CONST 3, MULTIPLY, ADD

CONST 3, CONST 2, SUBTRACT, CONST 1, SUBTRACT

CONST 1, CONST 2, CONST 3, MULTIPLY, ADD, CONST 4, CONST 5, NEGATE, DIVIDE, SUBTRACT
