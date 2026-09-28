//> Statements and State environment-class
package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList; //> challenge-4-import-arraylist

class Environment {
//> enclosing-field
  final Environment enclosing;
//< enclosing-field

  private final Map<String, Object> values = new HashMap<>();
  private final ArrayList<Object> slots = new ArrayList<>(); //> challenge-4-local-slots

//> environment-constructors
  Environment() {
    enclosing = null;
  }

  Environment(Environment enclosing) {
    this.enclosing = enclosing;
  }
//< environment-constructors

//> environment-get
  Object get(Token name) {
    if (values.containsKey(name.lexeme)) {
      return values.get(name.lexeme);
    }

    if (enclosing != null) return enclosing.get(name);

    throw new RuntimeError(name,
        "Undefined variable '" + name.lexeme + "'.");
  }
//< environment-get

//> environment-assign
  void assign(Token name, Object value) {
    if (values.containsKey(name.lexeme)) {
      values.put(name.lexeme, value);
      return;
    }

    if (enclosing != null) {
      enclosing.assign(name, value);
      return;
    }

    throw new RuntimeError(name,
        "Undefined variable '" + name.lexeme + "'.");
  }
//< environment-assign

//> environment-define
  void define(String name, Object value) {
    // Challenge 4: local variables go into slots, globals stay in map
    if (enclosing == null) {
      // global variable
      values.put(name, value);
    } else {
      // local variable → append to slots
      slots.add(value); // index = slots.size() - 1
    }
  }
//< environment-define

//> Resolving and Binding ancestor
  Environment ancestor(int distance) {
    Environment environment = this;
    for (int i = 0; i < distance; i++) {
      environment = environment.enclosing; // [coupled]
    }
    return environment;
  }
//< Resolving and Binding ancestor

//> Resolving and Binding get-at
  // Challenge 4: index-based local lookup
  Object getAt(int distance, int index) {
    return ancestor(distance).slots.get(index);
  }
//< Resolving and Binding get-at

//> Resolving and Binding assign-at
  // Challenge 4: index-based local assignment
  void assignAt(int distance, int index, Object value) {
    ancestor(distance).slots.set(index, value);
  }
//< Resolving and Binding assign-at

//> omit
  @Override
  public String toString() {
    String result = values.toString();
    if (enclosing != null) {
      result += " -> " + enclosing.toString();
    }
    return result;
  }
//< omit
}
