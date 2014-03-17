#!/bin/bash
rm -f *.class

if [ "$1" = "tui" ]; then
	export LD_LIBRARY_PATH="${2}/c/lib"
	javac \
		-cp ".:${2}:${2}/java/classes:${2}/test/classes:${2}/java/lib/commons-logging.jar:${2}/java/lib/log4j-1.2.8.jar" \
		TUIStarter.java
	java -Dcharva.color=1 \
		-cp ".:${2}:${2}/java/classes:${2}/test/classes:${2}/java/lib/commons-logging.jar:${2}/java/lib/log4j-1.2.8.jar" \
		TUIStarter
else
	javac Starter.java
	java Starter ${1}
fi