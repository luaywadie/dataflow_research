// Declaration
grammar Arithmetic;

// Root Handling
main: evaluation*;
evaluation: assignment | expression;
assignment: ID '=' (NUMBER | expression);
expression: (ID | a=NUMBER) OPEARTOR (ID | b=NUMBER | expression) {System.out.println($b.text);};

ID: [a-zA-Z]+;
NUMBER: '-'? [0-9]+;
OPEARTOR: '+' | '-' | '*' | '/';
WS: [ \t\r\n] -> skip;
