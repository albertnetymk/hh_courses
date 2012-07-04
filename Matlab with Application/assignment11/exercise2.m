number = input('Please input number of equations: ');
if(isnan(number) || number <= 0 || number > 3)
    error('incorrect number');
end
for i=1:number
    fprintf('%d%s\n',i,'th input')
    equation = input('Please input one differential equation:\n', 's');
    eq{i} = equation;
end

for i=1:number
    fprintf('%d%s\n',i,'th input')
    initial = input('Please input one differential equation initial condition:\n', 's');
    init{i} = char(initial);
end
% we assume that the variable name is x1, x2, x3
t=linspace(0, 10, 100);
switch number
    case 1
        x1 = dsolve(eq{1}, init{1});
        figure(1)
        clf();
        plot(t, subs(x1, t))
        title(char(eq{1}))
        legend('x1')
    case 2
        [x1, x2] = dsolve(eq{1}, eq{2}, init{1}, init{2});
        figure(1)
        clf();
        plot(t, subs(x1), t, subs(x2, t))
        title(strcat(char(eq{1}), ',', char(eq{2})))
        legend('x1')
    case 3
        [x1, x2, x3] = dsolve(eq{1}, eq{2}, eq{3}, init{1}, init{2}, init{3});
        figure(1)
        clf();
        plot(t, subs(x1), t, subs(x2, t), t, subs(x3, t))
        title(strcat(char(eq{1}), ',', char(eq{2}), ',', char(eq{3})))
        legend('x1')
end