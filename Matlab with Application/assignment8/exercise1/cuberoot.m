function [varargout]=thirdroot(varargin)
	for i=1:length(varargin)
		x=varargin{1};
		y=power(x,1/3);
		varargout{i}=y;
	end
end
