# How do other languages handle code like this:
# var a = a;
# What would you do if it was your language? Why?
Different languages handle this differently. Some treat it as an error because the variable is being referenced before it has been initialized. Others allow it and use a default value such as null or undefined. I think Lox's current behavior of reporting an error is the best choice because it catches what is most likely a programming mistake. In the expression var a = a;, the new variable a does not yet have a meaningful value, so allowing the reference could lead to confusing behavior and difficult-to-find bugs. Reporting an error makes the language safer and easier to reason about.
