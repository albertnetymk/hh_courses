clear
% the timing range
source range.m
% load range.mat so that we have BEGINTIME and ENDTIME
load range.mat

% the cell name
fid = fopen('cell/A01.txt', 'rt');
fscanf(fid, '#%s :')

cell = fscanf(fid, '%4i-%2i-%2i %2i:%2i:%2i %i ', [7, inf])

fclose(fid);

x=datenum(cell(1,:),cell(2,:),cell(3,:),cell(4,:),cell(5,:),cell(6,:));
y=cell(7,:);

beginIndex=find(x>BEGINTIME,1,'first');
endIndex=find(x<ENDTIME,1,'last');

realX=x(beginIndex:endIndex);
realY=y(beginIndex:endIndex);

clf()
figure(1)
plot(realX,realY,'r')

set(gca(), 'xlim', [BEGINTIME, ENDTIME])
%keyboard()
datetick('x', 'yyyy-mm-dd HH:MM:SS', 'keeplimits', 'keepticks')



