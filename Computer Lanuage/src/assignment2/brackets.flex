%%

%class      BracketsScanner
%implements BracketsTokens
%int
%debug

%{
int     token;
%}

LineTerminator=\r|\n|\r\n
%%
"/*" ~"*/"	{} 
"//" ~{LineTerminator}	{}
'.*'		{System.out.println();}
\" ~\"		{}
"("  {return token = '('; }
")"  {return token = '('; }
"["  {return token = '['; }
"]"  {return token = ']'; }
"{"  {return token = '{'; }
"}"  {return token = '}'; }
<<EOF>>                 {return token = ENDINPUT;}
.|\n {}

