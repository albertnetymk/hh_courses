% input file is Matrix.txt, which contains one triangular matrix, whose size is 181 x 181
% matrix.m can read from this file and fill up missing data to construct the symmetric matrix
clear
fid = fopen('Matrix.txt', 'rt');

matrix = fscanf(fid, '%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,', [181, 181]);
fclose(fid);

for i=1:181
	for j=1:181
		% only the upper triangular is 0, so we assign the corresponding lower triangular values to them
		if (j>i)
			matrix(i,j)=matrix(j,i);
		end
	end
end
% uncomment the subsequent line if you want to save the matrix to Matrix.mat
% save('matrix.mat', 'matrix');
