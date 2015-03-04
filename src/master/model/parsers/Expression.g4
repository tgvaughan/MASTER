grammar Expression;

// Parser rules:

expression :
        expression op=('+'|'-') factor  # AddSub
    |   factor                          # ELSEWHERE1
    ;

factor :
        factor op=('*'|'/') molecule   # MulDiv
    |   molecule                       # ELSEWHERE2
    ;

molecule :
        '-' molecule       			             # Negation
    |   atom '^' molecule                        # Exponentiation
    |   atom                                     # ELSEWHERE3
    ;

atom :
        '(' expression ')'                                  # Bracketed
    |   '[' expression (',' expression)* ']'                # Array
    |   op=(EXP|LOG|SQRT|SUM|THETA|ABS) '(' expression ')'  # UnaryOp
    |   VARNAME  ('[' i=NNINT ']')?                         # Variable
    |   val=(NNFLOAT | NNINT)                               # Number
    ;

// Lexer rules:

ADD : '+' ;
SUB : '-' ;
MUL : '*' ;
DIV : '/' ;
POW : '^' ;

EXP : 'exp' ;
LOG : 'log' ;
SQRT : 'sqrt' ;
SUM : 'sum' ;
THETA : 'theta' ;
ABS : 'abs' ;

NNINT : '0' | NZD D* ;
NNFLOAT : NNINT ('.' D*) ([eE] '-'? D+)? ;
fragment D : [0-9] ;
fragment NZD : [1-9] ;

VARNAME : [a-zA-Z_][a-zA-Z_0-9]* ;

WHITESPACE : [ \t\r\n]+ -> skip ;
