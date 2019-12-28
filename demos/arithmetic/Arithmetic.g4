// Declaration
grammar Arithmetic;

// Root Handling
main: evaluation+;
evaluation: assignment | expression;
assignment: (ID '=' VALUE);
expression: (VALUE OPEARTOR VALUE);

ID: [a-zA-Z]+;
VALUE: [0-9]+;
OPEARTOR: '+' | '-' | '*' | '/';
WS: [ \t\r\n] -> skip;
