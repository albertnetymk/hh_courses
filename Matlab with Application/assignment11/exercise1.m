[x1, x2, x3] = dsolve('Dx1 = x1 + x2 - 2*x3', 'Dx2 = 2*x1 - 2*x3', 'Dx3 = -2*x1 + 2*x2 + x3', 'x1(0)=2', 'x2(0)=1', 'x3(0)=1');
t=linspace(0, 2, 50);
x1_value = subs(x1, t);
x2_value = subs(x2, t);
x3_value = subs(x3, t);
plot(t, x1_value, '-r*', t, x2_value, '-bo', t, x3_value, '-k+');
legend(char(x1), char(x2), char(x3))