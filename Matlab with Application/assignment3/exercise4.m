% Feb 8th, 2010
% Mingkun Yang 900506-T008
clear 
frame={'first_name','last_name','age','favourite_sport','work'};
number=input('how many people do you want to input:   ');
x=cell(number,5);
for i=1:number
	for j=1:5
		x{i,j}=input([frame{1,j} '  '],'s');
	end
end
structure=cell2struct(x,frame,2);
for i=1:number
	disp([x{i,1} ' ' x{i,2} ' is ' x{i,3} ' years old and likes ' x{i,4} ' and works as a ' x{i,5}]);
end
