// Declaration
grammar Arithmetic;

@members {
  int total = 0;
}

// Root Handling
main: evaluation*;
evaluation: assignment | expression;
assignment: ID '=' (NUMBER | expression);
expression:
  (ID | a=NUMBER) OPEARTOR (ID | b=NUMBER | expression)
  {
    total += $a.int;
    total += $b.int;
    System.out.println(total);
  }
;

ID: [a-zA-Z]+;
NUMBER: '-'? [0-9]+;
OPEARTOR: '+' | '-' | '*' | '/';
WS: [ \t\r\n] -> skip;
