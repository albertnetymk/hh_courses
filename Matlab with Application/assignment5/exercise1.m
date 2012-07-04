clear
y=[0.51 0.82 1.1 1.22 1.34 1.4];
x=[0 0.3 0.8 1.1 1.6 2.25];
X=[x.^2;x;ones(1,6)]';
Y=y';
a=(X'*X)\X'*Y
estimate_x=linspace(0,3,100);
estimate_y=a(1)*estimate_x.^2+a(2)*estimate_x+a(3);
plot(x,y,'bo',estimate_x,estimate_y,'r');
%another way to do that
%p=polyfit(x,y,2);
%estimate_x=linspace(0,3,100);
%estimate_y=polyval(p,estimate_x);
%plot(x,y,'*b',estimate_x,estimate_y,'r');
