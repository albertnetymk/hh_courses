clear
x=[20.5 33 51 72 96];
y=[760 820 871 940 1038];
p=polyfit(x,y,4);
estimate_x=linspace(0,100,1000);
estimate_y=polyval(p,estimate_x);
figure(1)
clf;
plot(x,y,'ob',estimate_x,estimate_y,'r')
x1=[100 110 130 150];
y1=polyval(p,x1);
Y=[x1;y1];
disp('T	R')
fprintf('%d\t%5d\n',Y)
