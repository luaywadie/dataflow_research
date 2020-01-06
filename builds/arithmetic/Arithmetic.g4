// Declaration
grammar Arithmetic;

@members {
  int total = 0;

  // Core Function
  public int getOP(String op,int a, int b) {
      if (op.equals("+")) { return add(a,b); }

      return 0;
  }

  // Addition Function
  public int add(int a, int b) { return a + b; };
}

// Root Handling
main: evaluation*;
evaluation: assignment | expression;
assignment: ID '=' (NUMBER | expression);
expression:
  (ID | a=NUMBER) op=OPEARTOR (ID | b=NUMBER | expression)
  {
    int result = getOP($op.text, $a.int, $b.int);
    System.out.println(result);
  }
;

ID: [a-zA-Z]+;
NUMBER: '-'? [0-9]+;
OPEARTOR: '+' | '-' | '*' | '/';
WS: [ \t\r\n] -> skip;
