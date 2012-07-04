clear
sim('exercise2');

subplot(3,1,1); plot(logsout.x1);title('x1');
subplot(3,1,2); plot(logsout.x2);title('x2');
subplot(3,1,3); plot(logsout.x3);title('x3');
% subplot(3,1,1); plot(tout,xout(:,1));title('x1');
% subplot(3,1,2); plot(tout,xout(:,2));title('x2');
% subplot(3,1,3); plot(tout,xout(:,3));title('x3');