// Declaration
grammar Arithmetic;
// Root Handling
main: assignment;
assignment: (ID '=' VALUE);

ID: [a-zA-Z]+;
VALUE: [0-9]+;
WS: [ \t\r\n] -> skip;
