# Pick an open-source implementation of a language you like. Download the source code and poke around in it. Try to find the code that implements the scanner and parser. Are they handwritten, or generated using tools like Lex and Yacc? (.l or .y files usually imply the latter.)


CPython
The scanner is handwritten in C, and the parser is in CPython’s PEG grammar. 
