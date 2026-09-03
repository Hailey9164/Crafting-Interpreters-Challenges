## Aside from separating tokens—distinguishing print foo from printfoo—spaces aren’t used for much in most languages. However, in a couple of dark corners, a space does affect how code is parsed in CoffeeScript, Ruby, and the C preprocessor. Where and what effect does it have in each of those languages?


For CoffeeScript and Ruby it affects the parentheses in method and function calls. A space can change whether CoffeeScript thinks you are calling a function or accessing a property. In Ruby a space determines whether parentheses are part of the call or a unary operator. For C, it affects distinguishing function macros from simple macros. 
