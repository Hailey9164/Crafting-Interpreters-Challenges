//> Evaluating Expressions interpreter-class
package com.craftinginterpreters.lox;
//> Statements and State import-list

//> Functions import-array-list
import java.util.ArrayList;
//< Functions import-array-list
//> Resolving and Binding import-hash-map
import java.util.HashMap;
//< Resolving and Binding import-hash-map
import java.util.List;
//< Statements and State import-list
//> Resolving and Binding import-map
import java.util.Map;
//< Resolving and Binding import-map

//> Statements and State interpreter
class Interpreter implements Expr.Visitor<Object>,
                             Stmt.Visitor<Void> {
//< Statements and State interpreter

//> Functions global-environment
  final Environment globals = new Environment();
  private Environment environment = globals;
//< Functions global-environment

//> Resolving and Binding locals-field
  // Challenge 4: store depth + index instead of just depth
  private static class VariableLocation {
    final int depth;
    final int index;

    VariableLocation(int depth, int index) {
      this.depth = depth;
      this.index = index;
    }
  }

  private final Map<Expr, VariableLocation> locals = new HashMap<>();
//< Resolving and Binding locals-field

private static Object uninitialized = new Object();

//> Functions interpreter-constructor
  Interpreter() {
    globals.define("clock", new LoxCallable() {
      @Override
      public int arity() { return 0; }

      @Override
      public Object call(Interpreter interpreter,
                         List<Object> arguments) {
        return (double)System.currentTimeMillis() / 1000.0;
      }

      @Override
      public String toString() { return "<native fn>"; }
      
    });

    globals.define("Array", new LoxCallable() {
      @Override
      public int arity() {
        return 1;
      }

      @Override
      public Object call(Interpreter interpreter, List<Object> arguments) {
        int size = (int)(double)arguments.get(0);
        return new LoxArray(size);
      }
    });

  }
//< Functions interpreter-constructor

//> Statements and State interpret
  void interpret(List<Stmt> statements) {
    try {
      for (Stmt statement : statements) {
        execute(statement);
      }
    } catch (RuntimeError error) {
      Lox.runtimeError(error);
    }
  }
//< Statements and State interpret

//> evaluate
  private Object evaluate(Expr expr) {
    return expr.accept(this);
  }
//< evaluate

//> Statements and State execute
  private void execute(Stmt stmt) {
    stmt.accept(this);
  }
//< Statements and State execute

//> Resolving and Binding resolve
  // Challenge 4: now receives depth + index
  void resolve(Expr expr, int depth, int index) {
    locals.put(expr, new VariableLocation(depth, index));
  }
//< Resolving and Binding resolve
//> apply-traits
private Map<String, LoxFunction> applyTraits(List<Expr> traits) {
  Map<String, LoxFunction> methods = new HashMap<>();

  for (Expr traitExpr : traits) {
    Object traitObject = evaluate(traitExpr);

    if (!(traitObject instanceof LoxTrait)) {
      Token name = ((Expr.Variable) traitExpr).name;
      throw new RuntimeError(name,
          "'" + name.lexeme + "' is not a trait.");
    }

    LoxTrait trait = (LoxTrait) traitObject;

    for (String name : trait.methods.keySet()) {
      if (methods.containsKey(name)) {
        throw new RuntimeError(trait.name,
            "A previous trait declares a method named '" +
                name + "'.");
      }

      methods.put(name, trait.methods.get(name));
    }
  }

  return methods;
}
//< apply-traits

//> Statements and State execute-block
  void executeBlock(List<Stmt> statements,
                    Environment environment) {
    Environment previous = this.environment;
    try {
      this.environment = environment;

      for (Stmt statement : statements) {
        execute(statement);
      }
    } finally {
      this.environment = previous;
    }
  }
//< Statements and State execute-block

//> Statements and State visit-block
  @Override
  public Void visitBlockStmt(Stmt.Block stmt) {
    executeBlock(stmt.statements, new Environment(environment));
    return null;
  }
//< Statements and State visit-block

//> Statements and State visit-break
  @Override
  public Void visitBreakStmt(Stmt.Break stmt) {
    throw new BreakException();
  }
//< Statements and State visit-break

//> Classes interpreter-visit-class
  @Override
  public Void visitClassStmt(Stmt.Class stmt) {
    environment.define(stmt.name.lexeme, null);

    // Build metaclass with static methods
    Map<String, LoxFunction> methods = applyTraits(stmt.traits);
    for (Stmt.Function method : stmt.classMethods) {
      LoxFunction function = new LoxFunction(method, environment, false);
      classMethods.put(method.name.lexeme, function);
    }

    LoxClass metaclass = new LoxClass(
        null,
        stmt.name.lexeme + " metaclass",
        classMethods
    );

    // Build instance methods
    Map<String, LoxFunction> methods = new HashMap<>();
    for (Stmt.Function method : stmt.methods) {
      LoxFunction function = new LoxFunction(
          method,
          environment,
          method.name.lexeme.equals("init")
      );
      methods.put(method.name.lexeme, function);
    }

    // Build the actual class
    LoxClass klass = new LoxClass(metaclass, stmt.name.lexeme, methods);

    environment.assign(stmt.name, klass);
    return null;
  }

//< Classes interpreter-visit-class

//> Statements and State visit-expression-stmt
  @Override
  public Void visitExpressionStmt(Stmt.Expression stmt) {
    evaluate(stmt.expression);
    return null;
  }
//< Statements and State visit-expression-stmt

//> Functions visit-function
  @Override
  public Void visitFunctionStmt(Stmt.Function stmt) {
    LoxFunction function = new LoxFunction(stmt, environment,
                                           false);
    environment.define(stmt.name.lexeme, function);
    return null;
  }
//< Functions visit-function

//> Control Flow visit-if
  @Override
  public Void visitIfStmt(Stmt.If stmt) {
    if (isTruthy(evaluate(stmt.condition))) {
      execute(stmt.thenBranch);
    } else if (stmt.elseBranch != null) {
      execute(stmt.elseBranch);
    }
    return null;
  }
//< Control Flow visit-if

//> Statements and State visit-print
  @Override
  public Void visitPrintStmt(Stmt.Print stmt) {
    Object value = evaluate(stmt.expression);
    System.out.println(stringify(value));
    return null;
  }
//< Statements and State visit-print

//> Functions visit-return
  @Override
  public Void visitReturnStmt(Stmt.Return stmt) {
    Object value = null;
    if (stmt.value != null) value = evaluate(stmt.value);

    throw new Return(value);
  }
//< Functions visit-return

//> Statements and State visit-var
  @Override
  public Void visitVarStmt(Stmt.Var stmt) {
    Object value = uninitialized;
    if (stmt.initializer != null) {
      value = evaluate(stmt.initializer);
    }

    environment.define(stmt.name.lexeme, value);
    return null;
  }
//< Statements and State visit-var

//> Control Flow visit-while
  @Override
  public Void visitWhileStmt(Stmt.While stmt) {
    try {
      while (isTruthy(evaluate(stmt.condition))) {
        execute(stmt.body);
      }
    } catch (BreakException ex) {
      // break exits the loop
    }
    return null;
  }
//< Control Flow visit-while

//> Statements and State visit-assign
  @Override
  public Object visitAssignExpr(Expr.Assign expr) {
    Object value = evaluate(expr.value);

    VariableLocation loc = locals.get(expr);
    if (loc != null) {
      environment.assignAt(loc.depth, loc.index, value);
    } else {
      globals.assign(expr.name, value);
    }

    return value;
  }
//< Statements and State visit-assign

//< visit-function-expr
  @Override
  public Object visitFunctionExpr(Expr.Function expr) {
      return new LoxFunction(
          null,
          expr,
          environment,
          false
      );
  }
//< visit-function-expr

//> visit-binary
  @Override
  public Object visitBinaryExpr(Expr.Binary expr) {
    Object left = evaluate(expr.left);
    Object right = evaluate(expr.right);

    switch (expr.operator.type) {
      case BANG_EQUAL: return !isEqual(left, right);
      case EQUAL_EQUAL: return isEqual(left, right);

      case GREATER:
        checkNumberOperands(expr.operator, left, right);
        return (double)left > (double)right;
      case GREATER_EQUAL:
        checkNumberOperands(expr.operator, left, right);
        return (double)left >= (double)right;
      case LESS:
        checkNumberOperands(expr.operator, left, right);
        return (double)left < (double)right;
      case LESS_EQUAL:
        checkNumberOperands(expr.operator, left, right);
        return (double)left <= (double)right;

      case MINUS:
        checkNumberOperands(expr.operator, left, right);
        return (double)left - (double)right;

      case PLUS:
        if (left instanceof Double && right instanceof Double) {
            return (double)left + (double)right;
        }
        if (left instanceof String && right instanceof String) {
            return (String)left + (String)right;
        }
        if (left instanceof String) {
            return (String)left + stringify(right);
        }
        if (right instanceof String) {
            return stringify(left) + (String)right;

        throw new RuntimeError(expr.operator,
            "Operands must be two numbers or two strings.");

      case SLASH:
        checkNumberOperands(expr.operator, left, right);
        if ((double)right == 0) {
            throw new RuntimeError(expr.operator, "Division by zero.");
        }
        return (double)left / (double)right;

      case STAR:
        checkNumberOperands(expr.operator, left, right);
        return (double)left * (double)right;
    }

    return null;
  }
//< visit-binary

//> visit-comma
  @Override
  public Object visitCommaExpr(Expr.Comma expr) {
    evaluate(expr.left);
    return evaluate(expr.right);
  }
//< visit-comma

//> visit-conditional
  @Override
  public Object visitConditionalExpr(Expr.Conditional expr) {
    Object cond = evaluate(expr.condition);

    if (isTruthy(cond)) {
        return evaluate(expr.thenBranch);
    } else {
        return evaluate(expr.elseBranch);
    }
  }
//< visit-conditional

//> Functions visit-call
  @Override
  public Object visitCallExpr(Expr.Call expr) {
    Object callee = evaluate(expr.callee);

    List<Object> arguments = new ArrayList<>();
    for (Expr argument : expr.arguments) {
      arguments.add(evaluate(argument));
    }

    if (!(callee instanceof LoxCallable)) {
      throw new RuntimeError(expr.paren,
          "Can only call functions and classes.");
    }

    LoxCallable function = (LoxCallable)callee;

    if (arguments.size() != function.arity()) {
      throw new RuntimeError(expr.paren, "Expected " +
          function.arity() + " arguments but got " +
          arguments.size() + ".");
    }

    return function.call(this, arguments);
  }
//< Functions visit-call

//> Classes interpreter-visit-get
  @Override
  public Object visitGetExpr(Expr.Get expr) {
    Object object = evaluate(expr.object);
    if (object instanceof LoxInstance) {
      Object result = ((LoxInstance) object).get(expr.name);

      if (result instanceof LoxFunction &&
          ((LoxFunction) result).isGetter()) {
        result = ((LoxFunction) result).call(this, null);
      }

      return result;
    }

    throw new RuntimeError(expr.name,
        "Only instances have properties.");
  }

//< Classes interpreter-visit-get

//> visit-grouping
  @Override
  public Object visitGroupingExpr(Expr.Grouping expr) {
    return evaluate(expr.expression);
  }
//< visit-grouping

//> visit-literal
  @Override
  public Object visitLiteralExpr(Expr.Literal expr) {
    return expr.value;
  }
//< visit-literal

//> Control Flow visit-logical
  @Override
  public Object visitLogicalExpr(Expr.Logical expr) {
    Object left = evaluate(expr.left);

    if (expr.operator.type == TokenType.OR) {
      if (isTruthy(left)) return left;
    } else {
      if (!isTruthy(left)) return left;
    }

    return evaluate(expr.right);
  }
//< Control Flow visit-logical

//> Classes interpreter-visit-set
  @Override
  public Object visitSetExpr(Expr.Set expr) {
    Object object = evaluate(expr.object);

    if (!(object instanceof LoxInstance)) {
      throw new RuntimeError(expr.name,
                             "Only instances have fields.");
    }

    Object value = evaluate(expr.value);
    ((LoxInstance)object).set(expr.name, value);
    return value;
  }
//< Classes interpreter-visit-set

//> Inheritance interpreter-visit-super
  @Override
  public Object visitSuperExpr(Expr.Super expr) {
    VariableLocation loc = locals.get(expr);
    LoxClass superclass = (LoxClass)environment.getAt(
        loc.depth, loc.index);

    LoxInstance object = (LoxInstance)environment.getAt(
        loc.depth - 1, loc.index);

    LoxFunction method = superclass.findMethod(expr.method.lexeme);

    if (method == null) {
      throw new RuntimeError(expr.method,
          "Undefined property '" + expr.method.lexeme + "'.");
    }

    return method.bind(object);
  }
//< Inheritance interpreter-visit-super

//> Classes interpreter-visit-this
  @Override
  public Object visitThisExpr(Expr.This expr) {
    return lookUpVariable(expr.keyword, expr);
  }
//< Classes interpreter-visit-this
//> visit-trait-stmt
  @Override
  public Void visitTraitStmt(Stmt.Trait stmt) {
    environment.define(stmt.name.lexeme, null);

    Map<String, LoxFunction> methods = applyTraits(stmt.traits);

    for (Stmt.Function method : stmt.methods) {
      if (methods.containsKey(method.name.lexeme)) {
        throw new RuntimeError(method.name,
            "A previous trait declares a method named '" +
                method.name.lexeme + "'.");
      }

      LoxFunction function = new LoxFunction(
          method, environment, false);
      methods.put(method.name.lexeme, function);
    }

    LoxTrait trait = new LoxTrait(stmt.name, methods);

    environment.assign(stmt.name, trait);
    return null;
  }
//< visit-trait-stmt

//> visit-unary
  @Override
  public Object visitUnaryExpr(Expr.Unary expr) {
    Object right = evaluate(expr.right);

    switch (expr.operator.type) {
      case BANG:
        return !isTruthy(right);
      case MINUS:
        checkNumberOperand(expr.operator, right);
        return -(double)right;
    }

    return null;
  }
//< visit-unary

//> Statements and State visit-variable
  @Override
  public Object visitVariableExpr(Expr.Variable expr) {
    return lookUpVariable(expr.name, expr);
  }
//> Resolving and Binding look-up-variable
  private Object lookUpVariable(Token name, Expr expr) {
    VariableLocation loc = locals.get(expr);
    if (loc != null) {
      return environment.getAt(loc.depth, loc.index);
    } else {
      return globals.get(name);
    }
  }
//< Resolving and Binding look-up-variable
//< Statements and State visit-variable

//> check-operand
  private void checkNumberOperand(Token operator, Object operand) {
    if (operand instanceof Double) return;
    throw new RuntimeError(operator, "Operand must be a number.");
  }
//< check-operand

//> check-operands
  private void checkNumberOperands(Token operator,
                                   Object left, Object right) {
    if (left instanceof Double && right instanceof Double) return;
    throw new RuntimeError(operator, "Operands must be numbers.");
  }
//< check-operands

//> is-truthy
  private boolean isTruthy(Object object) {
    if (object == null) return false;
    if (object instanceof Boolean) return (boolean)object;
    return true;
  }
//< is-truthy

//> is-equal
  private boolean isEqual(Object a, Object b) {
    if (a == null && b == null) return true;
    if (a == null) return false;

    return a.equals(b);
  }
//< is-equal

//> stringify
  private String stringify(Object object) {
    if (object == null) return "nil";

    if (object instanceof Double) {
      String text = object.toString();
      if (text.endsWith(".0")) {
        text = text.substring(0, text.length() - 2);
      }
      return text;
    }

    return object.toString();
  }
//< stringify

//> Statements and State break-exception
  private static class BreakException extends RuntimeException {}
//< Statements and State break-exception
}
