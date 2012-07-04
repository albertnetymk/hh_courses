load university-halmstad.mat
%year
%students
%points
%degrees
%profit
%cost
table(1,:)=Q(1,:);
table(2,:)=Q(6,:)./Q(4,:);
table(3,:)=Q(6,:)./Q(2,:);
table(4,:)=Q(3,:)./Q(2,:);

format short g, disp(table');
