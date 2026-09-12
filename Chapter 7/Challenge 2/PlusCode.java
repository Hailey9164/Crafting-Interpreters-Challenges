case PLUS:
    // number + number
    if (left instanceof Double && right instanceof Double) {
        return (double)left + (double)right;
    }

    // string + string
    if (left instanceof String && right instanceof String) {
        return (String)left + (String)right;
    }

    // string + anything
    if (left instanceof String) {
        return (String)left + stringify(right);
    }

    // anything + string
    if (right instanceof String) {
        return stringify(left) + (String)right;
    }

    throw new RuntimeError(expr.operator,
        "Operands must be numbers or strings.");
