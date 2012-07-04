function [varargout]=squareroot(varargin)
	for i=1:length(varargin)
		x=varargin{1};
		y=sqrt(x);
		varargout{i}=y;
	end
end
