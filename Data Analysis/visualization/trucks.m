clear

% the timing range
source range.m
% load range.mat so that we have BEGINTIME and ENDTIME
load range.mat

% we need to calculate the total distance of this truck within this period
load matrix.mat

total(1:7,1)=0;

for i = 1:7
	name = strcat('truck/HYLTRUCK0', num2str(i), '.txt');
	fid = fopen(name, 'rt');
	fscanf(fid, '#%s :')
	
	truck(:,:,i) = fscanf(fid, '%4i-%2i-%2i %2i:%2i:%2i : %*s %i ', [7, inf]);
	
	fclose(fid);

	x(i,:)=datenum(truck(1,:,i),truck(2,:,i),truck(3,:,i),truck(4,:,i),truck(5,:,i),truck(6,:,i));
	y(i,:)=truck(7,:,i);
	
	beginIndex=find(x(i,:)>BEGINTIME,1,'first');
	endIndex=find(x(i,:)<ENDTIME,1,'last');
	
	realX(i,:)=x(i,beginIndex:endIndex);
	realY(i,:)=y(i,beginIndex:endIndex);

	for j = 1:(size(realY,2)-1)
		current = realY(i,j);
		next = realY(i,j+1);
		total(i,1) = total(i,1) + matrix(current,next);
	end
end

clf
figure(1)
bar(total)
