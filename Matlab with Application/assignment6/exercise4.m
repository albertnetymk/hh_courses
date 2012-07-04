clear
p=input('Please input the first polynomial:')
%p=[1 0 0 4];
dp=polyder(p);
ddp=polyder(dp);
critical_x=roots(dp);
critical_x=critical_x(find(critical_x==real(critical_x)));
critical_y=polyval(p, critical_x);
x=linspace(min(critical_x)-10,max(critical_x)+10,100);
y=polyval(p, x);
figure(1)
clf;
plot(x,y, critical_x, critical_y, 'ro')
%     second_derivative=polyval(ddp, critical_x);
%     critical_x=critical_x(find(second_derivative~=0));
%     critical_y=polyval(p,critical_x);
%     x=linspace(min(critical_x)-10,max(critical_x)+10,100);
%     y=polyval(p, x);
%     figure(1)
%     clf;
%     plot(x,y, critical_x, critical_y, 'ro')
