grammar PFExpression;

start: expression;

// Parser rules:

expression :
        expression '+' term
    |   expression '-' term
    |   term
    ;

term :
        term '*' factor
    |   term '/' factor
    |   factor
    ;

factor :
        '(' expression ')'
    |   population
    |   NUM
    ;

population : POPTYPE LOC? ;

// Lexer rules:

POPTYPE : [a-zA-Z]+ ;
LOC : '[' NNINT (',' NNINT)* ']' ;
NUM : '-'? NNINT ('.' D+)? ([eE] '-'? D+)? ;
fragment NNINT : '0' | NZD D* ;
fragment D : [0-9] ;
fragment NZD : [1-9] ;

WHITESPACE : [ \t\r\n]+ -> skip ;
