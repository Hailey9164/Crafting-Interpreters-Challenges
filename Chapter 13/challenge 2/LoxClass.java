//> Classes lox-class
package com.craftinginterpreters.lox;

import java.util.List;
import java.util.Map;

/* Classes lox-class < Classes lox-class-callable
class LoxClass {
*/
//> lox-class-callable
class LoxClass extends LoxInstance implements LoxCallable {
//< lox-class-callable
  final String name;
//> Inheritance lox-class-superclass-field
  final LoxClass superclass;
//< Inheritance lox-class-superclass-field
/* Classes lox-class < Classes lox-class-methods

  LoxClass(String name) {
    this.name = name;
  }
*/
//> lox-class-methods
  private final Map<String, LoxFunction> methods;

/* Classes lox-class-methods < Inheritance lox-class-constructor
  LoxClass(String name, Map<String, LoxFunction> methods) {
*/
//> Inheritance lox-class-constructor
//> Classes lox-class-constructor
  class LoxClass extends LoxInstance implements LoxCallable {
    final String name;
    private final Map<String, LoxFunction> methods;

    LoxClass(LoxClass metaclass, String name,
            Map<String, LoxFunction> methods) {
      super(metaclass); // class object is an instance of its metaclass
      this.name = name;
      this.methods = methods;
    }
  }
//< Classes lox-class-constructor

//< lox-class-methods
//> lox-class-find-method
  LoxFunction findMethod(LoxInstance instance, String name) {
    LoxFunction method = null;
    LoxFunction inner = null;
    LoxClass klass = this;

    while (klass != null) {
      if (klass.methods.containsKey(name)) {
        inner = method;
        method = klass.methods.get(name);
      }
      klass = klass.superclass;
    }

    if (method != null) {
      return method.bind(instance, inner);
    }

    return null;
  }

//< lox-class-find-method

  @Override
  public String toString() {
    return name;
  }
//> lox-class-call-arity
  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    LoxInstance instance = new LoxInstance(this);
    LoxFunction initializer = findMethod(instance, "init");

    if (initializer != null) {
      initializer.call(interpreter, arguments);
    }

    return instance;
  }


  @Override
  public int arity() {
/* Classes lox-class-call-arity < Classes lox-initializer-arity
    return 0;
*/
//> lox-initializer-arity
    LoxFunction initializer = findMethod("init");
    if (initializer == null) return 0;
    return initializer.arity();
//< lox-initializer-arity
  }
//< lox-class-call-arity
}
