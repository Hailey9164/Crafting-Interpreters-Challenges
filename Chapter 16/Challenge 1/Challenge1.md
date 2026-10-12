# Many newer languages support string interpolation. Inside a string literal, you have some sort of special delimiters—most commonly ${ at the beginning and } at the end. Between those delimiters, any expression can appear. When the string literal is executed, the inner expression is evaluated, converted to a string, and then merged with the surrounding string literal.
# For example, if Lox supported string interpolation, then this . . . 
# var drink = "Tea";
# var steep = 4;
# var cool = 2;
# print "${drink} will be ready in ${steep + cool} minutes.";
#  . . . would print:
# Tea will be ready in 6 minutes.
# What token types would you define to implement a scanner for string interpolation? What sequence of tokens would you emit for the above string literal?
# What tokens would you emit for:
# "Nested ${"interpolation?! Are you ${"mad?!"}"}"
# Consider looking at other language implementations that support interpolation to see how they handle it.

Interpolation splits a string into segments. Each segment becomes either TOKEN_INTERPOLATION or TOKEN_STRING. Expressions inside ${} are scanned normally. The scanner switches modes whenever it sees ${ or }. Nested interpolation works because the scanner re-enters string mode after each expression.

"Tea will be ready in ${steep + cool} minutes."
TOKEN_INTERPOLATION "Tea will be ready in"
TOKEN_IDENTIFIER    "steep"
TOKEN_PLUS          "+"
TOKEN_IDENTIFIER    "cool"
TOKEN_STRING        "minutes."

"Nested ${"interpolation?! Are you ${"mad?!"}"}"
TOKEN_INTERPOLATION "Nested "
TOKEN_INTERPOLATION "interpolation?! Are you "
TOKEN_STRING        "mad?!"
TOKEN_STRING        ""
TOKEN_STRING        ""
Interpolation delimiters ${ and } are not tokens — they just tell the scanner when to switch modes.
