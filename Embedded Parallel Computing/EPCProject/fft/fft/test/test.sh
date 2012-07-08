#!/bin/bash
points=16
j=0
for((i=0;i<points/2-1-2;i++))
do
	echo "channel \"c_\"+$j={distribute[$i].out_1,distribute[$[$i+1]].in};"
	j=$[$j+1]
	echo "channel \"c_\"+$j={distribute[$i].out_2,distribute[$[$i+2]].in};"
	j=$[$j+1]
done
