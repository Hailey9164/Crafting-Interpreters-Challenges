# Many languages, especially later in their evolution, define “contextual keywords”. These are identifiers that act like reserved words in some contexts but can be normal user-defined identifiers in others.
# For example, await is a keyword inside an async method in C#, but in other methods, you can use await as your own identifier.
# Name a few contextual keywords from other languages, and the context where they are meaningful. What are the pros and cons of having contextual keywords? How would you implement them in your language’s front end if you needed to?

C#:
await — keyword only inside async methods
yield — keyword only inside iterator blocks
var — keyword only in variable declarations

Python:
match and case — keywords only inside match statements

Rust:
async, await, try — contextual until stabilized

Pros:
Backward compatible — you don’t break old code that used the word as a variable.
Allows language evolution — you can add new features without reserving new keywords globally.
Cleaner syntax — avoids ugly punctuation or symbols.

Cons:
Confusing for users — people often don’t realize they’re contextual.
Effectively reserved anyway — using await as a variable name feels wrong once the language uses it heavily.
Harder for tooling — IDEs and linters must understand context.

Contextual keywords are implemented by keeping the scanner completely unaware of them and letting the parser decide when they should behave like real keywords. The scanner always treats words like “await” or “yield” as ordinary identifiers because it doesn’t know what part of the language they appear in. Only once the parser begins analyzing structure does the language gain enough context to reinterpret certain identifiers as keywords. For example, inside an async function, the parser recognizes that an identifier spelled “await” should be treated as the special await keyword, while in any other context it remains a normal user‑defined name. This approach keeps the lexer simple and avoids breaking older programs that may already use these words as identifiers. It also allows a language to evolve by introducing new keyword‑like features without globally reserving new words. The trade‑off is that contextual keywords can confuse users and make tooling more complex, but they are often the only practical way to extend a language without disrupting existing code. 
