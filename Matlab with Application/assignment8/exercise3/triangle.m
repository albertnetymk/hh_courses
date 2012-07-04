function triangle(varargin)
	switch(nargin)
		case 1
			varargin{2}=varargin{1}/2;
		case 2
            while(varargin{2} >= varargin{1})
                varargin{2}=input(strcat('Please input one number < ', num2str(varargin{1}), ':'));
            end
		otherwise
			fprintf('%s\n','the number of arguments can only be one or two');
	end
varargin{3}=sqrt(varargin{1}^2-varargin{2}^2);
fprintf('The three sides are %f, %f, %f respectively.\n',varargin{1},...
		varargin{2},varargin{3})
% angle(2)=acos((varargin{1}^2+varargin{3}^2-varargin{2}^2) ...
% 				/(2*varargin{1}*varargin{3}));
% angle(3)=acos((varargin{1}^2+varargin{2}^2-varargin{3}^2) ...
% 				/(2*varargin{1}*varargin{2}));
% angle(1)=pi/2;
% fprintf('The corresponding angles are %f, %f, %f\n', angle)
global col;
a=[0 0; 0 varargin{2}; varargin{3} 0];
A(3,1)=1;
A(3,2)=1;
A(2,1)=1;
A(3,3)=0;
if(length(col)==0)
    gplot(A, a)
else
gplot(A, a, col)
end
axis('equal')
%axis([-5,10,-5,10])
end
