// RpnPrinter: A Visitor that prints expressions in Reverse Polish Notation (RPN).
// This class implements the Expr.Visitor<String> interface, meaning each visit
// method returns a String representation of the expression it handles.
class RpnPrinter implements Expr.Visitor<String> {

  // Public method to print any expression using this visitor.
  // It simply calls accept(), which dispatches to the correct visit method.
  String print(Expr expr) {
    return expr.accept(this);
  }

  // Handles binary expressions like (a + b), (x * y), etc.
  // RPN format prints: left right operator
  @Override
  public String visitBinaryExpr(Expr.Binary expr) {
    return expr.left.accept(this) + " " +
           expr.right.accept(this) + " " +
           expr.operator.lexeme;
  }

  // Handles grouping expressions like (expr).
  // In RPN, grouping doesn't change the printed form, so we just print the inner expression.
  @Override
  public String visitGroupingExpr(Expr.Grouping expr) {
    return expr.expression.accept(this);
  }

  // Handles literal values: numbers, strings, booleans, etc.
  @Override
  public String visitLiteralExpr(Expr.Literal expr) {
    return expr.value.toString();
  }

  // Handles unary expressions like -x or !x.
  // RPN prints: operand operator
  @Override
  public String visitUnaryExpr(Expr.Unary expr) {
    String operator = expr.operator.lexeme;

    // Special case: unary minus uses "~" instead of "-" to avoid confusion
    // with binary minus in RPN output.
    if (expr.operator.type == TokenType.MINUS) {
      operator = "~";
    }

    return expr.right.accept(this) + " " + operator;
  }

  // Demo main method: builds an expression tree and prints it in RPN.
  public static void main(String[] args) {

    // Expression being built:
    // (-123) * ( "str" )
    // Using Expr.Binary, Expr.Unary, Expr.Literal, Expr.Grouping
    Expr expression = new Expr.Binary(
        new Expr.Unary(
            new Token(TokenType.MINUS, "-", null, 1),
            new Expr.Literal(123)),
        new Token(TokenType.STAR, "*", null, 1),
        new Expr.Grouping(
            new Expr.Literal("str")));

    // Print the RPN form of the expression
    System.out.println(new RpnPrinter().print(expression));
  }
}
