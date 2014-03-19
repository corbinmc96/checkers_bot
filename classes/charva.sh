#!/bin/bash
rm -f ${2}.class

export LD_LIBRARY_PATH="${1}/c/lib"
javac \
	-cp ".:${1}:${1}/java/classes:${1}/test/classes:${1}/java/lib/commons-logging.jar:${1}/java/lib/log4j-1.1.8.jar" \
	${2}.java
java -Dcharva.color=1 \
	-cp ".:${1}:${1}/java/classes:${1}/test/classes:${1}/java/lib/commons-logging.jar:${1}/java/lib/log4j-1.1.8.jar" \
	${2}
