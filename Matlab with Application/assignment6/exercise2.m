clear
x=linspace(-1.5,1.5,100);
z=inline('x+3-tan(x)');
point=fzero(z,1);
y1=x+3;
y2=tan(x);
figure(1)
clf;
plot(x,y1,'r',x,y2,'b')
grid on
hold on
plot([0 0],[-15 15],'k')
integral=quad(z,0,point)
