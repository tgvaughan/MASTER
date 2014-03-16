grammar PFExpression;

start: expression;

// Parser rules:

expression :
        expression '+' term    # Add
    |   expression '-' term    # Sub
    |   term                   # MulOrDiv
    ;

term :
        term '*' factor        # Mul
    |   term '/' factor        # Div
    |   factor                 # Unit
    ;

factor :
        '(' expression ')'     # Bracketed
    |   population             # pop
    |   NUM                    # number
    ;

population : POPTYPE LOC? ;

// Lexer rules:

POPTYPE : [a-zA-Z]+ ;
LOC : '[' NNINT (',' NNINT)* ']' ;
NUM : NNINT ('.' D+)? ([eE] '-'? D+)? ;
fragment NNINT : '0' | NZD D* ;
fragment D : [0-9] ;
fragment NZD : [1-9] ;

WHITESPACE : [ \t\r\n]+ -> skip ;
