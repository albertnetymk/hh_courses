%Known bug:
%x1,x2 can not be the same
function [varargout] = coordinate(x1,y1,x2,y2,x)
	y=(y2-y1)/(x2-x1)*(x-x1)+y1;
    if(nargout == 0)
	figure()
	plot([x1,x2, x],[y1,y2, y])
    hold on
    plot(x, y, '*')
    else
        varargout{1}=y;
    end
end
