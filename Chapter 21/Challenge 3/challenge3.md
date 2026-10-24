# When running in the REPL, a user might write a function that references an unknown global variable. Then, in the next line, they declare the variable. Lox should handle this gracefully by not reporting an “unknown variable” compile error when the function is first defined.
# But when a user runs a Lox script, the compiler has access to the full text of the entire program before any code is run. Consider this program:
# fun useVar() {
#   print oops;
# }
# var ooops = "too many o's!";
# Here, we can tell statically that oops will not be defined because there is no declaration of that global anywhere in the program. Note that useVar() is never called either, so even though the variable isn’t defined, no runtime error will occur because it’s never used either.
# We could report mistakes like this as compile errors, at least when running from a script. Do you think we should? Justify your answer. What do other scripting languages you know do?

I think the compiler should report an error when running a script if it can determine that a global variable is never declared anywhere in the program. In most cases, an undeclared variable is a typo or programming mistake that would be better to catch before the program runs. Earlier error detection makes debugging easier and helps programmers find problems faster. However, I can also see situations during refactoring where code is temporarily incomplete and references variables that have not been created yet. Allowing the program to run in those cases can be useful. While a warning system could be a compromise, warnings are often ignored or treated inconsistently. I would prefer a compile-time error because I value catching mistakes as early as possible. Languages like Python generally detect undefined variables at runtime, while languages such as Java and C++ catch these errors during compilation. I think Lox would benefit from stronger compile-time checking when the full source code is available.
