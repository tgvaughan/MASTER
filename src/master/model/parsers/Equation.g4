grammar Equation;
import Expression;

equation : expression op=('=' | '>' | '<' | '>=' | '<=' | '!=') expression ;

EQ: '=' | '==';
GT: '>';
LT: '<';
GE: '>=';
LE: '<=';
NE: '!=';