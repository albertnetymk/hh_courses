clear
beta=input('Please input one [1x3] matrix :');
gamma=input('Please input one [1x3] matrix :');
t=linspace(-2*pi,2*pi,100);
f=sin(beta(1)*t)+cos(gamma(1)*t);
g=sin(beta(2)*t)+cos(gamma(2)*t);
h=sin(beta(3)*t)+cos(gamma(3)*t);
figure(1)
clf
plot(t,f,t,g,t,h)
legend(strcat('\beta=', num2str(beta(1)), ' \gamma=', num2str(gamma(1))), ... 
    strcat('\beta=', num2str(beta(2)), ' \gamma=', num2str(gamma(2))), ...
    strcat('\beta=', num2str(beta(3)), ' \gamma=', num2str(gamma(3))), 'Location', 'NorthWest');
