# Our scanner here, like most, discards comments and whitespace since those aren’t needed by the parser. Why might you want to write a scanner that does not discard those? What would it be useful for?


Some IDEs, such as VS code, use whitespace for reading the comments and use those to provide help text. The scanner needs to be able to read these comments in order for this feature to function. 
