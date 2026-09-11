# Likewise, add support for the C-style conditional or “ternary” operator ?:. What precedence level is allowed between the ? and :? Is the whole operator left-associative or right-associative?


In C, the ternary operator sits between logical-or and assignment, and it is right-associative. 
expression → comma
comma → assignment
assignment → conditional
conditional → equality ( "?" expression ":" conditional )? ;
equality   → comparison ( ( "!=" | "==" ) comparison )* ;
comparison → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
term       → factor ( ( "-" | "+" ) factor )* ;
factor     → unary ( ( "/" | "*" ) unary )* ;
unary      → ( "!" | "-" | "--" | "++" ) unary
           | postfix ;
postfix    → primary ( "--" | ++" )* ;
primary    → NUMBER | STRING | "true" | "false" | "nil"
           | "(" expression ")" ;
