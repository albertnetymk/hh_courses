clear
[rank, names, number, previous] = textread('namn.txt', '%d%s%d%d');

matchedNameIndex=strmatch('J',names);
for i=1:size(matchedNameIndex,1)
	y(i)=number(matchedNameIndex(i));
end
xtick=[names(matchedNameIndex(1),:),names(matchedNameIndex(2),:),names(matchedNameIndex(3),:),names(matchedNameIndex(4),:),... 
			names(matchedNameIndex(5),:),names(matchedNameIndex(6),:),names(matchedNameIndex(7),:),names(matchedNameIndex(8),:), ...
			names(matchedNameIndex(9),:), names(matchedNameIndex(10),:) ];
figure(1)
bar(y)
set(gca, 'XLim', [0.5 10.5])
set(gca, 'XTick', [1:size(matchedNameIndex,1)])
set(gca,'XTickLabel',xtick)
