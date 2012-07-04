#!/bin/bash -e
temp=$(mktemp);
for file in $@
do
	echo "package lab4;" > $temp;
	echo "" >> $temp;
	cat "$file" >> $temp;
	# overwrite the original
	cp "$temp" "$file";
done
