# In C, a block is a statement form that allows you to pack a series of statements where a single one is expected. The comma operator is an analogous syntax for expressions. A comma-separated series of expressions can be given where a single expression is expected (except inside a function call’s argument list). At runtime, the comma operator evaluates the left operand and discards the result. Then it evaluates and returns the right operand.
# Add support for comma expressions. Give them the same precedence and associativity as in C. Write the grammar, and then implement the necessary parsing code.


The comma operator has the lowest precedence as is left associative. 
Grammar: 
expression → comma ;
comma      → equality ( "," equality )* ;
equality   → comparison ( ( "!=" | "==" ) comparison )* ;
comparison → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
term       → factor ( ( "-" | "+" ) factor )* ;
factor     → unary ( ( "/" | "*" ) unary )* ;
unary      → ( "!" | "-" | "--" | "++" ) unary
           | postfix ;
postfix    → primary ( "--" | ++" )* ;
primary    → NUMBER | STRING | "true" | "false" | "nil"
           | "(" expression ")" ;
