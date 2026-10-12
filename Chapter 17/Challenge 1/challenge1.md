# To really understand the parser, you need to see how execution threads through the interesting parsing functions—parsePrecedence() and the parser functions stored in the table. Take this (strange) expression:
# (-1 + 2) * 3 - -4
# Write a trace of how those functions are called. Show the order they are called, which calls which, and the arguments passed to them.


expression
| parsePrecedence(PREC_ASSIGNMENT)
| | grouping
| | | expression
| | | | parsePrecedence(PREC_ASSIGNMENT)
| | | | | unary // for "-"
| | | | | | parsePrecedence(PREC_UNARY)
| | | | | | | number
| | | | | binary // for "+"
| | | | | | parsePrecedence(PREC_FACTOR) // PREC_TERM + 1
| | | | | | | number
| | binary // for "*"
| | | parsePrecedence(PREC_UNARY) // PREC_FACTOR + 1
| | | | number
| | binary // for "-"
| | | parsePrecedence(PREC_FACTOR) // PREC_TERM + 1
| | | | unary // for "-"
| | | | | parsePrecedence(PREC_UNARY)
| | | | | | number
