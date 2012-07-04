clear
x=1:5000;
y=randn(1,5000);
mean_value=mean(y);
std_value=std(y);
figure()
plot(x,y,'.')
hold on
plot([0,5000],[mean_value,mean_value], ...
    [0,5000],[mean_value+std_value,mean_value+std_value],...
    [0,5000], [mean_value-std_value,mean_value-std_value],'r')
hold off
x=1:1000;
y=3*randn(1,1000)+12;
mean_value=mean(y)
std_value=std(y)
figure()
plot(x,y,'.')
hold on
plot([0,1000],[mean_value,mean_value], ...
    [0,1000],[mean_value+std_value,mean_value+std_value],...
    [0,1000], [mean_value-std_value,mean_value-std_value],'r')
hold off

