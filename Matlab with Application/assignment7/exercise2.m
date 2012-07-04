clear
number=input('number of throws	:')
point1=ceil(6*rand(1,number));
point2=ceil(6*rand(1,number));
result=point1+point2;
result=(result==6);
s=0;
for i=1:number
	s=s+result(i);
	propobality(i)=s/i;
end
r=propobality(number);
fprintf('The propobality is %1.5f\n',r)
figure(1)
clf
plot(propobality,'*b')
hold on
grid on
plot([0,number],[r,r],'r')
