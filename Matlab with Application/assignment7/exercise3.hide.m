clear
p=[10 12 13 12.2 11.5];
t=0:6:24;
coefficient=polyfit(t,p,4);
estimate_t=linspace(0,24,100);
estimate_p=polyval(coefficient,estimate_t);
figure()
plot(t,p,'*b',estimate_t,estimate_p,'r')
grid on
coefficient=polyint(coefficient);
result=polyval(coefficient,24)-polyval(coefficient,0)
