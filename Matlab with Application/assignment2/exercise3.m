load snow.mat
subplot(2,1,1)
plot(S(1:7,1),S(1:7,2),'rs-',S(8:14,1),S(8:14,2),'gd-',S(15:21,1),S(15:21,2),'bo-',S(22:28,1),S(22:28,2),'mx-',S(29:35,1),S(29:35,2),'k<-',S(36:42,1),S(36:42,2),'cp-')

legend('Stockholm','Norrkoping','Goteborg','Lund','Falun','Ostersund','location','northwestoutside');
title('temperature');
subplot(2,1,2)
plot(S(1:7,1),S(1:7,3),'rs-',S(8:14,1),S(8:14,3),'gd-',S(15:21,1),S(15:21,3),'bo-',S(22:28,1),S(22:28,3),'mx-',S(29:35,1),S(29:35,3),'k<-',S(36:42,1),S(36:42,3),'cp-')

legend('Stockholm','Norrkoping','Goteborg','Lund','Falun','Ostersund','location','northwestoutside');
title('snow-depth');
