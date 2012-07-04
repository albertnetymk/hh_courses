clear
for i=1:5
	while(1)
		fprintf('%d%s\n',i,'th input')
		tmp=input('Please input one [1x2] matrix  :');
		if(size(tmp,1)==1 && size(tmp,2)==2 && tmp(1)>0 ...
			&& tmp(1)<15 && tmp(2)>0 && tmp(2)<15)
			a(i,1)=tmp(1);
			a(i,2)=tmp(2);
			break;
		else
			disp('the accepted rang is from 0 to 15, please input again')
			continue;
		end
	end
end
x=a(:,1);
y=a(:,2);
p=polyfit(x,y,1);
estimate_x=linspace(0,15,100);
estimate_y=polyval(p,estimate_x);
figure()
plot(x,y,'*b',estimate_x,estimate_y,'r')
%second part of this exercise
A(5,1)=1;
A(4,5)=1;
A(3,4)=1;
A(2,3)=1;
A(1,2)=1;
figure()
gplot(A,a)
