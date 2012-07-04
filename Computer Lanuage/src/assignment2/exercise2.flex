%%

%class Scanner
%implements Tokens
%int
%unicode

%{
	int token;
%}

%%

[ ]		{}
a		{return token='a';}
b		{return token='b';}
<<EOF>>		{return token=ENDINPUT;}
\r|\n|\r\n	{}
.		{throw new Error("unexpected"+yytext());}
