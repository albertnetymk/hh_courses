clear
load temp.mat
[minimum,index_row]=min(T);
[minimum,index_column]=min(minimum);
year=index_row(index_column)+1980;
date=datestr(datenum(year,1,0)+index_column);
disp('minimum of temperature and the date');
disp(date);
disp(minimum);
[maximum,index_row]=max(T);
[maximum,index_column]=max(maximum);
year=index_row(index_column)+1980;
date=datestr(datenum(year,1,0)+index_column);
disp('maximum of temperature and the date');
disp(date);
disp(maximum);
% mean temperature
mean_Jan=mean(T(:,1:31),2);
mean_Jun=mean(T(:,152:181),2);
mean_Jul=mean(T(:,182:212),2);
mean_Aug=mean(T(:,213:243),2);
mean_Temperature=[transpose(1981:1990) mean_Jan mean_Jun mean_Jul mean_Aug];
disp('mean temperature of Jan, Jun, Jul, Aug')
format short g, disp(mean_Temperature)
