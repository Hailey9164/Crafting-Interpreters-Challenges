# Several languages use angle brackets for generics and also have a >> right shift operator. This led to a classic problem in early versions of C++:
# vector<vector<string>> nestedVectors;
# This would produce a compile error because the >> was lexed to a single right shift token, not two > tokens. Users were forced to avoid this by putting a space between the closing angle brackets.
# Later versions of C++ are smarter and can handle the above code. Java and C# never had the problem. How do those languages specify and implement this?

Modern C++:
The lexer is context‑sensitive: If you are inside a template argument list, >> becomes two > tokens. Otherwise, it’s a shift operator. This is called the lexer hack.

Java:
Java’s spec technically doesn’t define this correctly, but javac does the right thing: The lexer always emits >> as a shift operator. The parser splits it into two > tokens when parsing generics.

C#: 
The lexer always emits two > tokens. The parser merges them into a shift operator only in expression context.
