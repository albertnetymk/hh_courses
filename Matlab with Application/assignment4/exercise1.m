clear
load matrisdata
fprintf('%s%d%s%d\n','The length of vector x is :', size(x,1), ' x ', size(x,2))
Y=[mean(x) median(x) std(x)];
disp('mean	 	median          standard_deviation')
fprintf('%6.6f \t %6.6f \t %6.6f\n', Y)
fprintf('%s%d%s%d\n','The size of A is :', size(A,1), ' x ', size(A,2))
%Y=[transpose(1:20)	transpose(mean(A)) transpose(median(A)) transpose(std(A))];
%disp('row	  mean		 median 	standard_deviation')
%fprintf('%d \t %6.6f \t %6.6f \t %6.6f\n', Y')
Y=[		mean(mean(A));	 median(median(A));	 std(std(A))];
disp(' mean		 median 	standard_deviation')
fprintf('%6.6f \t %6.6f \t %6.6f\n', Y)
