grammar ReactionString;

// Parser rules:

reaction : reactants '->' products ;

reactants : popsum ;
products : popsum ;

popsum : popel ('+' popel)* | '0' ;

popel : factor? popname loc? (':' id)?;

factor : NZINT ;

popname : NAME ;

loc : '[' locel (',' locel)* ']' ;

locel : '0' | NZINT | NAME ;

id : '0' | NZINT ;

// Lexer rules:

NZINT : NZD D* ;
fragment Z : '0' ;
fragment D : [0-9] ;
fragment NZD : [1-9] ;

NAME : [a-zA-Z_][0-9a-zA-Z_]* ;

WHITESPACE : [ \t\r\n]+ -> skip ;