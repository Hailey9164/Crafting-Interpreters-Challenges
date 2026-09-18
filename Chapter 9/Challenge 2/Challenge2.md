# Likewise, looping can be implemented using those same tools, provided our interpreter supports an important optimization. What is it, and why is it necessary? Name a language that uses this technique for iteration.
Scheme is a language that proves you don’t actually need built‑in looping constructs. In Scheme, all iteration can be expressed using just recursion and conditional execution. If you want a piece of code to run multiple times, you wrap that code in a function and have the function call itself at the end. That recursive call represents the next iteration. For example, this Lox loop:
```lox
for (var i = 0; i < 100; i = i + 1) {
  print i;
}
```
can be rewritten using recursion:
```
fun forStep(i) {
  print i;
  if (i < 99) forStep(i + 1);
}
```
Here, forStep() prints the current value, and if we haven’t reached the end yet, it calls itself again with the next value. The concern with this style is stack usage. A loop like this might recurse almost a hundred times, and in many languages that would risk a stack overflow. But in cases like this, the recursive call happens in tail position. In other words, it’s the very last thing the function does. When a recursive call is in tail position, the interpreter doesn’t need to keep the current stack frame around. It can safely reuse the same stack space for the next call. This optimization is called tail call optimization (TCO). With TCO, you can write loops using recursion without growing the call stack at all. Each iteration uses constant stack space. Scheme requires implementations to perform tail call optimization so programmers can rely on recursion as their primary way to express iteration.
