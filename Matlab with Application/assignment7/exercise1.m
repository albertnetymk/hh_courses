clear
load temp.mat
s=sum(sum(T<0));
disp('Temperature is below 0')
daysPerYear=s/10
s=sum(sum((T>0).*(T<10)));
disp('Temperature is between 0 and 10')
daysPerYear=s/10
