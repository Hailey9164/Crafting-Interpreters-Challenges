# Is this program valid?
# fun scope(a) {
#   var a = "local";
# }
# In other words, are a function’s parameters in the same scope as its local variables, or in an outer scope? What does Lox do? What about other languages you are familiar with? What do you think a language should do?

This program is not valid. Lox uses the same scope for the function parameters and local variables inside the body of the function, so it is not valid. In Java, it is an error because it does not allow shadowing variables. In C, it is an error because the function parameters and the body of the function share the same scope. In Dart, they have separate scopes, so it is allowed. 
