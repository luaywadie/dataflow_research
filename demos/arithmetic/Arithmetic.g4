// Declaration
grammar Arithmetic;

@members {
  int total = 0;

  public void showValue(int n) {
    System.out.println(n);
  }
}

// Root Handling
main: evaluation*;
evaluation: assignment | expression;
assignment: ID '=' (NUMBER | expression);
expression:
  (ID | a=NUMBER) OPEARTOR (ID | b=NUMBER | expression)
  {
    showValue($a.int);
    showValue($b.int);
    total += $a.int;
    total += $b.int;
    /* System.out.println(total); */
  }
;

ID: [a-zA-Z]+;
NUMBER: '-'? [0-9]+;
OPEARTOR: '+' | '-' | '*' | '/';
WS: [ \t\r\n] -> skip;
