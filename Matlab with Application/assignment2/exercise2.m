load stockholm-snow.mat
for i=1:10
	time(i)=A(3*i-2);
	temperature(i)=A(3*i-1);
	snowdepth(i)=A(3*i);
end

%ax=plotyy(time,temperature,time,snowdepth,@plot);

%figure
subplot(2,1,1)
plot(time,temperature)
xlabel('time')
ylabel('Temperature')
title('temperature')
subplot(2,1,2)
plot(time,snowdepth)
xlabel('time')
ylabel('Snowdepth')
title('Snowdepth')

