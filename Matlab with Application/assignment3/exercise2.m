clear
for i=1:12
	value(i)=input('input one number between 0 and 20:  ');
	if(value(i)<0 || value(i)>20)
		error('out of range');
	end
end
figure(1)
clf()
plot(1:12, value,':b', 1:12, value, '*r');
[x, y]=ginput();
if(length(x)>12)
    x=x(1:12);
    y=y(1:12);
end
x=round(x);
y=round(y);
format short g,disp(x);
format short g,disp(y);
hold on
plot(x, y, '-b', x, y, '*r')