// Declaration
grammar Arithmetic;

// Root Handling
main: evaluation*;
evaluation: assignment | expression;
assignment: ID '=' (NUMBER | expression);
expression: (ID | NUMBER) OPEARTOR (ID | NUMBER | expression);

ID: [a-zA-Z]+;
NUMBER: '-'? [0-9]+;
OPEARTOR: '+' | '-' | '*' | '/';
WS: [ \t\r\n] -> skip;
