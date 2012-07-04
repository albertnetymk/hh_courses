%%

%class      Scanner
%implements BooleanTokens
%int
%line

%{
int     token;
Object  semanticValue;
int     line(){return yyline;}
%}

%%
true  	  {semanticValue = true; return token = TRUE;}
false 	  {semanticValue = false; return token = FALSE;}
[a-zA-Z]+ {semanticValue = yytext(); return token = ID;}
&     	  {return token = '&';}
\|    	  {return token = '|';}
\-    	  {return token = '-';}
\(    	  {return token = '(';}
\)    	  {return token = ')';}
[ ]   	  {}
<<EOF>>   {return token = ENDINPUT;}
\n	  {}
.  	  {throw new Error("Unexpected character: " + yytext());}
