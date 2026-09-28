# How do other languages you know handle local variables that # refer to the same name in their initializer, like:
# var a = "outer";
# {
#   var a = a;
# }
# Is it a runtime error? Compile error? Allowed? Do they treat global variables differently? Do you agree with their choices? Justify your answer.

C puts the inner variable in scope before its initializer runs, so var a = a refers to the new variable. Since C doesn’t clear memory, this means you’re reading garbage. It’s allowed, but it’s definitely unsafe. Java doesn’t let one local variable shadow another, so if both a variables are locals, it’s a compile‑time error. JavaScript allows shadowing, but with let and const, the inner variable is in a temporal dead zone until it’s initialized. So let a = a; throws a runtime error because the inner a isn’t ready yet. 

C’s behavior is unsafe because it lets you accidentally read uninitialized memory. Java’s approach is safer and more predictable, and its definite‑assignment rules help catch mistakes early. JavaScript’s behavior makes sense too, because it prevents you from accidentally using a variable before it’s initialized. Overall, languages that prevent this kind of shadowing avoid confusing bugs. 
