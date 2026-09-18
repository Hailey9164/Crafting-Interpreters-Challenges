# A few chapters from now, when Lox supports first-class functions and dynamic dispatch, we technically won’t need branching statements built into the language. Show how conditional execution can be implemented in terms of those. Name a language that uses this technique for its control flow.


You can express conditional behavior entirely using objects and callbacks. You create two classes: one representing “true” and one representing “false.” Each class has methods that take function arguments (callbacks). The “true” object calls the “then” callback. The “false” object calls the “else” callback.So instead of writing:
```
if (condition) {
  doSomething();
} else {
  doOtherThing();
}
```
You’d write something like:
``` 
condition.ifThen(thenCallback);
condition.ifThenElse(thenCallback, elseCallback);
```
The “true” object runs the first callback, and the “false” object runs the second. You make one instance of each class and use them as your boolean values. Some languages actually do this. Smalltalk is the classic example. Its booleans are objects, and control flow is implemented by sending messages to those objects instead of using special syntax. With a more expressive block‑passing syntax (like the Grace language uses), you could even make it look almost like normal control flow, even though it’s really just method calls.
