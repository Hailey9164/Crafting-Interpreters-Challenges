# Earlier, I said that the |, *, and + forms we added to our grammar metasyntax were just syntactic sugar. Take this grammar:
# expr → expr ( "(" ( expr ( "," expr )* )? ")" | "." IDENTIFIER )+
#     | IDENTIFIER
#     | NUMBER
# Produce a grammar that matches the same language but does not use any of that notational sugar.
# Bonus: What kind of expression does this bit of grammar encode?

expr -> expr calls 
| IDENTIFIER 
| NUMBER
calls -> calls call 
| call
call -> “(“ ”)” 
| “(“ arguments“)” 
| “.” IDENTIFIER
arguments -> expr
	| arguments “,” expr

It looks like a function call. 
