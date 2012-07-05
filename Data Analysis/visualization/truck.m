clear

% the timing range
source range.m
% load range.mat so that we have BEGINTIME and ENDTIME
load range.mat

fid = fopen('truck/HYLTRUCK01.txt', 'rt');
fscanf(fid, '#%s :')

truck = fscanf(fid, '%4i-%2i-%2i %2i:%2i:%2i : %*s %i ', [7, inf]);

fclose(fid);

x=datenum(truck(1,:),truck(2,:),truck(3,:),truck(4,:),truck(5,:),truck(6,:));
y=truck(7,:);

beginIndex=find(x>BEGINTIME,1,'first');
endIndex=find(x<ENDTIME,1,'last');

realX=x(beginIndex:endIndex);
realY=y(beginIndex:endIndex);
keyboard
% we need to calculate the total distance of this truck within this period
load matrix.mat
total=0;
for i = 1:(size(realY,2)-1)
	current = realY(i);
	next = realY(i+1);
	total = total + matrix(current,next);
end
