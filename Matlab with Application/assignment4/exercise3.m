clear
load tabell3
j=1;
for i=1:6:25
    y(j)=tabell3(i);
    j=j+1;
end
x=1:6:25;
figure()
bar(x,y)
set(gca, 'XTick', x)
set(gca, 'XTickLabel',{'16-24';'25-34';'35-44';'45-54';'55-74'})
figure()
subplot(2,1,1)
x=0;y=0;z=0;
j=1;
totalFemale=0;
for i=5:6:29
    z(j)=tabell3(i);
    y=tabell3(i)/tabell3(i-3)*100;
    totalFemale=totalFemale+y;
    j=j+1;
end
pie([z totalFemale-sum(z)],{'16-24','25-34','35-44','45-54','55-74',...
    'Don''t use Internet'});
title('Female');
subplot(2,1,2)
x=0;y=0;z=0;
j=1;
totalMale=0;
for i=6:6:30
    z(j)=tabell3(i);
    y=tabell3(i)/tabell3(i-3)*100;
    totalMale=totalMale+y;
    j=j+1;
end
pie([z totalMale-sum(z)],{'16-24','25-34','35-44','45-54','55-74',...
    'Don''t use Internet'});
title('Male');
