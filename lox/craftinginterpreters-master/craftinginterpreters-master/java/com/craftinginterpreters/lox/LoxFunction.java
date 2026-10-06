//> Functions lox-function
package com.craftinginterpreters.lox;

import java.util.List;

class LoxFunction implements LoxCallable {
//> NEW: name field for anonymous functions
  private final String name;
//< NEW

//> UPDATED: Expr.Function instead of Stmt.Function
  private final Expr.Function declaration;
//< UPDATED

//> closure-field
  private final Environment closure;
//< closure-field

//> Classes is-initializer-field
  private final boolean isInitializer;

  LoxFunction(String name, Expr.Function declaration,
              Environment closure, boolean isInitializer) {
    this.name = name;                // NEW
    this.declaration = declaration;  // UPDATED
    this.closure = closure;
    this.isInitializer = isInitializer;
  }
//< Classes is-initializer-field
//> is-getter
  public boolean isGetter() {
    return declaration.params == null;
  }
//< is-getter

//> Classes bind-instance
  LoxFunction bind(LoxInstance instance) {
    Environment environment = new Environment(closure);
    environment.define("this", instance);
//> lox-function-bind-with-initializer
    return new LoxFunction(name, declaration, environment,
                           isInitializer);
//< lox-function-bind-with-initializer
  }
//< Classes bind-instance

//> function-to-string
  @Override
  public String toString() {
    if (name == null) return "<fn>";        // NEW: anonymous function
    return "<fn " + name + ">";             // named function
  }
//< function-to-string

//> function-arity
  @Override
  public int arity() {
    return declaration.parameters.size();   // UPDATED
  }
//< function-arity

//> function-call
  @Override
  public Object call(Interpreter interpreter,
                     List<Object> arguments) {
//> call-closure
    Environment environment = new Environment(closure);
//< call-closure

    for (int i = 0; i < declaration.parameters.size(); i++) {
      environment.define(declaration.parameters.get(i).lexeme,
          arguments.get(i));
    }

//> catch-return
    try {
      interpreter.executeBlock(declaration.body, environment);
    } catch (Return returnValue) {
//> Classes early-return-this
      if (isInitializer) return closure.getAt(0, "this");
//< Classes early-return-this
      return returnValue.value;
    }
//< catch-return

//> Classes return-this
    if (isInitializer) return closure.getAt(0, "this");
//< Classes return-this

    return null;
  }
//< function-call
}
//< Functions lox-function
