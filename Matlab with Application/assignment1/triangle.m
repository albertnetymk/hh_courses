%input two short sides of one right angle triangle
%and this function outputs the area and the long side and the angles
function [angles, area, z] = triangle(one, two)
angles(1) = atan(one/two)*180/pi;
angles(2) = 90 - angles(1);
area = 0.5*one*two;
z = sqrt(one^2+two^2);
end