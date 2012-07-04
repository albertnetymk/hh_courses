clear
fun=input('input your function:  ','s');
f=inline(fun,'x','y','z');
x=0:0.1:20;
y=f(x,x,x);
figure(1)
clf()
plot(x,y)
hold on
title_value = subs(char(f), 'y', 'x')
title_value = subs(char(title_value), 'z', 'x')
title(char(title_value))
xlabel('x')
grid on
