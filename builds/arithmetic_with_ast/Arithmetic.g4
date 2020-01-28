grammar Arithmetic;

/**
LEXER
TOKENS - name must begin with uppercase letter; by convention ALL letters are uppercase
*/
WHITESPACE : (' ' | '\t' | '\n' | '\r') -> skip; // must wrap in () to use -> skip command
EQ : 'equals' | '=' | '->';
OPERATOR : PLUS | MINUS | MULT | DIV | MOD | EXP ;
// fragments are rules that can only be used within the lexer; they are invisible to the parser
fragment PLUS : 'plus' | '+';
fragment MINUS : 'minus' | '-';

fragment MULT : 'times' | '*';
fragment DIV : 'divby' | '/';
fragment MOD : 'mod' | '%';
fragment EXP : 'pow' | '^';

INT : [0-9]+ ; // integers are 1 or more numeric digits
ID : [a-zA-Z] ([a-zA-Z] | [0-9])* ; // identifiers must begin with a character, and may be followed by
                                    // 0 or more additional numeric or letter characters

/**
PARSER
Production rules - name must begin with lowercase letter; by convention write in camel case
*/
root : '<BEGIN' statement+ 'END>' EOF; // EOF is an automatically defined token (notice it is not in the lexer)
statement : (assignment | arithmeticExpression) ';';
assignment : ID EQ arithmeticExpression;
arithmeticExpression : lOp = (INT | ID) OPERATOR rOp = (INT | ID);