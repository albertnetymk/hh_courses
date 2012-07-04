% clear
% x=linspace(-3,3,100);
% y=linspace(-3,3,100);
% [X,Y]=meshgrid(x,y);
% Z=cos(X)+Y.*exp(-X.^2-Y.^2);
% for i=1:40
%     surf(sin(2*pi*i/40)*Z,Z)
%     M=getframe;
% end
% figure(1)
% clf
% movie(M,1,4)
clear
image=imread('school.jpeg');
image(60:190, 203:207, 1) = 255;
image(60:190, 203:207, 2:3) = 0;


image(58:62, 205:425, 1) = 255;
image(58:62, 205:425, 2:3) = 0;

image(188:192, 205:425, 1) = 255;
image(188:192, 205:425, 2:3) = 0;

image(60:190, 423:427, 1) = 255;
image(60:190, 423:427, 2:3) = 0;

imshow(image)
