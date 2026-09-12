# What happens right now if you divide a number by zero? What do you think should happen? Justify your choice. How do other languages you know handle division by zero, and why do they make the choices they do?
# Change the implementation in visitBinaryExpr() to detect and report a runtime error for this case.


Currently, if I divide a number by zero, it returns Infinity, -Infinity, or NaN based on the sign of the dividend. Lox should raise a runtime error to let the user know something went wrong. This is what Python, Java, and Ruby do. This is also a consistent choice and is safest, as it could lead to other errors. 
