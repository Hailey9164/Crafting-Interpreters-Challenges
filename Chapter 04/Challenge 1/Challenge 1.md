# The lexical grammars of Python and Haskell are not regular. What does that mean, and why aren’t they?


The indentation rules of Python and Haskell make them not regular as they need to store the level of indentation to know what state they are in and regular grammars need only to store a single finite number for identifying the current state.  
