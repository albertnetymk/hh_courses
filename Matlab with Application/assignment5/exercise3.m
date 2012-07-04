%Thu Apr 22 18:14:42 CEST 2010
%Mingkun Yang 900506-T008
clear
A=[1 1 -2;2 0 -2;-2 2 1];
[X,Lambda]=eig(A);
y=[2 1 1];
y=y';
a=X\y;
t=linspace(0,2,100);
x1=a(1)*exp(Lambda(1,1)*t)*X(1,1)+a(2)*exp(Lambda(2,2)*t)*X(1,2)+a(3)*exp(Lambda(3,3)*t)*X(1,3);
x2=a(1)*exp(Lambda(1,1)*t)*X(2,1)+a(2)*exp(Lambda(2,2)*t)*X(2,2)+a(3)*exp(Lambda(3,3)*t)*X(2,3);
x3=a(1)*exp(Lambda(1,1)*t)*X(3,1)+a(2)*exp(Lambda(2,2)*t)*X(3,2)+a(3)*exp(Lambda(3,3)*t)*X(3,3);
plot(t,x1,t,x2,t,x3,'-')
grid
legend('x1(t)','x2(t)','x3(t)')
title('Differential Equation')
