clear
t=-1:0.01:1;
x=cos(2*pi*t);
y=sin(2*pi*t);
figure(1)
plot(t, x, t, y)
xlabel('t');ylabel('value');
legend('x', 'y')
grid on