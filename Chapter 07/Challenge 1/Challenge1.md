# Allowing comparisons on types other than numbers could be useful. The operators might have a reasonable interpretation for strings. Even comparisons among mixed types, like 3 < "pancake" could be handy to enable things like ordered collections of heterogeneous types. Or it could simply lead to bugs and confusion.
# Would you extend Lox to support comparing other types? If so, which pairs of types do you allow and how do you define their ordering? Justify your choices and compare them to other languages.


Other languages like Python, Java, and C++ allow for comparing string types, and therefore Lox should be able to do so too. They are compared using operators, and the comparison gives a boolean result: 1 is TRUE, and 0 is FALSE. They would be ordered lexicographically using the ASCII values of the characters in the string. For example, “a” < “b” would be true. 

I think strings should not be able to be compared to numbers. For example, “a” < 5. Java allows this by converting the number to a string. However, this can be messy, as it can lead to unforeseen errors. 
