clear
i=1;
while(i<=4)
	fprintf('%d%s\n',i,'th number')
	a=input('Please input one number :', 's');
    if(a=='N' || a=='n')
         return;
    end
	try
		a=str2double(a);
        
        if(isnan(a))
            disp('input should be number')
            continue;
        end
    catch exception
        disp('something is wrong')
        continue;

    end
x(i)=a;
i=i+1;
end
[first_max index]=max(x);
first_max
x(index) = NaN;
second_max=max(x)
