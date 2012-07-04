function [varargout]=square(varargin)
	for i=1:length(varargin)
		x=varargin{1};
		y=x.^2;
		varargout{i}=y;
	end
end
