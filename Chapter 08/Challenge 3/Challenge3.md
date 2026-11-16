# What does the following program do?
```lox
var a = 1;
{
  var a = a + 2;
  print a;
}
```
# What did you expect it to do? Is it what you think it should do? What does analogous code in other languages you are familiar with do? What do you think users will expect this to do?


The program prints 3. The inner var a does not come into scope until after its initializer expression is evaluated, so a + 2 refers to the outer a, whose value is 1. The result (3) is then stored in the new inner variable. This is what I expected. The current behavior is okay but ideal. Ideally, the new variable would enter scope immediately in a special unusable state, and referencing it in its own initializer would be a static error. This avoids confusing shadowing patterns. Java does not allow shadowing local variables. C# allows shadowing but not in a way where the same name resolves to different variables inside the same block. JavaScript’s var behaves like Lox, while let and const use a temporal dead zone and make this code an error. Python allows shadowing and resolves the initializer using the outer variable. Most users would be surprised that this code is valid at all, and would likely consider it confusing or bad style even if it prints 3.
