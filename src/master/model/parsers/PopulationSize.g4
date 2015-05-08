grammar PopulationSize;

import Expression;

// Parser rules:

assignment : popname loc? ':=' expression ;
popname : IDENT ;
loc : '[' locel (',' locel)* ']' ;
locel : NNINT | IDENT ;

// Lexer rules

ASSIGN : ':=' ;
