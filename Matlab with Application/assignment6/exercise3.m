clear
x=linspace(0,100,1000);
y=inline('0.1*x.^3-5*x.^2-x+exp(-x)+4');
figure(1)
plot(x,y(x))
grid on
root_1=fzero(y, 0);
root_2=fzero(y, 50);
fprintf('The first root is %d.\n', root_1)
fprintf('The second root is %d.\n', root_2)
[x, minimal]=fminbnd(y,root_1,root_2);
fprintf('The minimal is %d.\n', minimal)
hold on
plot([root_1, root_2], [0, 0],'o', x, minimal, 'v')