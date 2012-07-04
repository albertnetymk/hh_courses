clear
x=1:5;
fh=input('Which operation would you like to use(square,squareroot, cuberoot) :','S');
y=feval(fh,x);
fprintf('     x\ty\n')
%fprintf('%d\t%f\n",x',y');
disp([x' y'])
