# The ParseRule row for TOKEN_MINUS has both prefix and infix function pointers. That’s because - is both a prefix operator (unary negation) and an infix one (subtraction).
# In the full Lox language, what other tokens can be used in both prefix and infix positions? What about in C or in another language of your choice?

Lox:
Left parenthesis (
prefix: grouping (expr)
infix: function call f(expr)
C: 
*
prefix: pointer dereference *ptr
infix: multiplication a * b

&
prefix: address-of &x
infix: bitwise and a & b

+
prefix: unary plus +x
infix: addition a + b

Languages with array indexing:
[
prefix: list literal [1, 2, 3]
infix: indexing arr[i]
