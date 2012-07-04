clear
load mytalk
sound(speech, FS)
for j=1:5
x(j)=input('Please input one complex numbers :');
end
figure(1)
clf
compass(x)
x(2,:)=angle(x(1,:));
x(3,:)=abs(x(1,:));
fprintf('complex number,\t\tphase angle,\t\tlength\n')
disp(transpose(x))
