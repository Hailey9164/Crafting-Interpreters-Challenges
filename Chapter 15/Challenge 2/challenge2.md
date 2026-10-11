# If we really wanted a minimal instruction set, we could eliminate either OP_NEGATE or OP_SUBTRACT. Show the bytecode instruction sequence you would generate for:
# 4 - 3 * -2
# First, without using OP_NEGATE. Then, without using OP_SUBTRACT.
# Given the above, do you think it makes sense to have both instructions? Why or why not? Are there any other redundant instructions you would consider including?

Normal: 
4 - (3 * (- 2))
CONST 4, CONST 3, CONST 2, NEGATE, MULTIPLY, SUBTRACT

Without negation:
4 - (3 * (0 - 2))
CONST 4, CONST 3, CONST 0, CONST 2, SUBTRACT, MULTIPLY, SUBTRACT

Without subtraction:
4 + - (3 * (- 2))
CONST 4, CONST 3, CONST 2, NEGATE, MULTIPLY, NEGATE, ADD
It makes sense to have both instructions. It is simpler when both instructions are included. Given how common both negation and subtraction are, and given that we've got plenty of room in our opcode set, it makes perfect sense to have instructions for both. I would also consider specialized instructions to load common number constants like zero and one. It might be worth having instructions to increment and decrement a number too.
