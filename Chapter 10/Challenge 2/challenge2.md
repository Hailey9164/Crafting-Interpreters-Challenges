# Lox’s function declaration syntax performs two independent operations. It creates a function and also binds it to a name. This improves usability for the common case where you do want to associate a name with the function. But in functional-styled code, you often want to create a function to immediately pass it to some other function or return it. In that case, it doesn’t need a name.
# Languages that encourage a functional style usually support anonymous functions or lambdas—an expression syntax that creates a function without binding it to a name. Add anonymous function syntax to Lox so that this works:
# fun thrice(fn) {
#   for (var i = 1; i <= 3; i = i + 1) {
#     fn(i);
#   }
# }
# thrice(fun (a) {
#   print a;
# });
# // "1".
# // "2".
# // "3".
# How do you handle the tricky case of an anonymous function expression occurring in an expression statement:
# fun () {};

The main parsing challenge is distinguishing between a named function declaration and an anonymous function expression. A case like fun () {}; is problematic because the parser assumes any fun keyword begins a named function declaration, which requires a name. To fix this, the parser must look ahead one token. If fun is followed by an identifier, it is parsed as a declaration. Otherwise, it is parsed as an anonymous function expression. This prevents errors and keeps the grammar consistent.