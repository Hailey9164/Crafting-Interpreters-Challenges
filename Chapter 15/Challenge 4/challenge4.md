# To interpret OP_NEGATE, we pop the operand, negate the value, and then push the result. That’s a simple implementation, but it increments and decrements stackTop unnecessarily, since the stack ends up the same height in the end. It might be faster to simply negate the value in place on the stack and leave stackTop alone. Try that and see if you can measure a performance difference.
# Are there other instructions where you can do a similar optimization?

The same idea applies to most binary arithmetic instructions. For example, OP_ADD normally pops two values and pushes the result. Instead, you can read the top two values directly from the stack, store the result into the lower slot, and then decrement the stack pointer once. That reduces the number of stack operations and keeps the stack shallower. Subtraction, multiplication, division, and the comparison operators can all be optimized in the same way.

