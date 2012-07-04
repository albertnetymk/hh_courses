clear
t=-1:0.01:1;
x=cos(2*pi*t);
y=sin(2*pi*t);
figure(1)
subplot(2,1,1)
plot(t, x, 'r')
t1=-1:0.1:1;
x1=cos(2*pi*t1);
hold on
plot(t1, x1, 'bo')
xlabel('t');ylabel('x')
grid on
subplot(2,1,2)
plot(t, y, 'b')
t1=-1:0.1:1;
y1=sin(2*pi*t1);
hold on
plot(t1, y1, 'ro')
xlabel('t');ylabel('y')
grid on