clear
%dx/dt + x = a + sin(bt) x(0)=2
% t=linspace(0,10,100)';
a=1;
b=2;
sim('exercise1')
figure(1)
clf;
plot(tout, xout)