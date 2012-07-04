#!/bin/bash -e

base=~/workspace/project/Minijava;

#1=${base}/src/lab4/TrivialTest.java
if [ $# -eq 1 ]; then
	file=${base}/src/lab4/$1
else
	file=${base}/src/lab4/TrivialTest.java
fi

echo $file

javac -cp ${base}/bin CodeGenerator.java -d ${base}/bin
java -cp  ${base}/bin:${base}/lib/lexerParser.jar Main "$file" > "$file".s
cat "$file".s
