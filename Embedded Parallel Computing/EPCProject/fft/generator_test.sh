#!/bin/bash

#cat /dev/null > test.in
#for i in `seq 1 8`
#do
#    echo "000000001" >> test.in
#done
count=23
for number in `seq 0 3`
do
	echo "channel c_$count={finalFFT_$number.out, assembler_0.in_1};"
	count=$[$count+1]
done
