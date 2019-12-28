// Declaration
grammar Arithmetic;
// Root Handling
r: EXPR;
EXPR: VAR '=' NUMBER;
VAR: [a-z];
NUMBER: [0-9];
WS: [ \t\r\n] -> skip;
