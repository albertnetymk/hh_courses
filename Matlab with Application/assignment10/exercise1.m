clear
h = figure(1);
set(h, 'position', [800, 100, 400, 400]);
t=linspace(0.02, 2000, 100);
y=2*sin(5*t);
plot(t,y);
set(gca, 'xscale', 'log');
set(gca, 'xtick', [5 50 500]);
set(gca, 'yminorgrid', 'on');
