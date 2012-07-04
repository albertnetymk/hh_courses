%%

%class Scanner
%implements minijavaTokens
%unicode
%int
%debug

%{
int token;
Object semanticValue;
%}

LineTerminator=\r|\n|\r\n
Space=[ \t\f]

Comment={TraditionalComment}|{EndOfLineComment}

TraditionalComment="/*" ~"*/"
EndOfLineComment="//" ~{LineTerminator}

Identifier=[:jletter:] [:jletterdigit:]*
IntegerLiteral=0|[1-9][0-9]*

//BinaryOperators=[(&&)<+-/*]

//Visibility="public" | "private" | "protected"


%%

//"abstract"		{return token=ABSTRACT;}
//"break"		{return token=BREAK;}
//"switch"		{return token=SWITCH;}
//"case"		{return token=CASE;}
//"implements"		{return token=IMPLEMENTS;}
//"default"		{return token=DEFAULT;}

//keywords of minijava
"public"		{return token=PUBLIC;}
"new"			{return token=NEW;}
"static"		{return token=STATIC;}
"void"			{return token=VOID;}
"main"			{return token=MAIN;}
"class"			{return token=CLASS;}
//{BinaryOperators}	{semanticValue=yytext();return token=BINARYOPERATOR;}
"extends"		{return token=EXTENDS;}
"this"			{return token=THIS;}
"if"			{return token=IF;}
"else"			{return token=ELSE;}
"while"			{return token=WHILE;}
"return"		{return token=RETURN;}

//{Visibility}		{semanticValue=yytext();return token=VISIBILITY;}
//primitive and reference type
"boolean"		{return token=BOOLEAN;}
"true"			{return token=TRUE;}
"false"			{return token=FALSE;}

"int"			{return token=INT;}
"length"		{return token=LENGTH;}
{IntegerLiteral}	{semanticValue=Integer.parseInt(yytext());return token=INTEGER_LITERAL;}

"String"		{return token=STRING;}
{Identifier}		{semanticValue=yytext();return token=IDENTIFIER;}
"System.out.println"	{return token=SYSTEMOUTPRINTLN;}

//{BinaryOperators}	{semanticValue=yytext();return token=BINARYOPERATOR;}
//operators
\+         	        {return token='+';}
-          	        {return token='-';}
\*           	  	{return token='*';}
\/          	   	{return token='/';}
\(      	        {return token='(';}
\)       	        {return token=')';}
\{			{return token='{';}
\}			{return token='}';}
\[			{return token='[';}
\]			{return token=']';}
\!			{return token='!';}
"&&"			{return token=AND;}
=			{return token='=';}
"!="			{return token=UNEG;}
\<			{return token='<';}
\>			{return token='>';}

\.			{return token='.';}
;               	{return token=';';}
,			{return token=',';}



{Space}			{}
{Comment}		{}
{LineTerminator}	{}
<<EOF>>			{return token=ENDINPUT;}
.			{throw new Error("unexpected"+yytext());}


