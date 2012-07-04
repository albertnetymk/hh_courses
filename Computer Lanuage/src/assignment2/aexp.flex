%%

%class Scanner
%implements IntegerTokens
%int

%{
	int token;
	int val;
%}

%%

0|[1-9][0-9]*	{val=Integer.parseInt(yytext()); return token=INTEGER;}
\+		{return token='+';}
-		{return token='-';}
\*		{return token='*';}
\/		{return token='/';}
\(		{return token='(';}
\)		{return token=')';}
;		{return token=';';}
[ ]		{}
<<EOF>>		{return token=ENDINPUT;}
\r|\n|\r\n	{}
.		{throw new Error("unexpected"+yytext());}
