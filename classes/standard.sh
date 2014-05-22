#!/bin/bash
rm -f *.class

if [ "$1" = "tui" ]; then
	if [ $(uname -s) = "Darwin" ]; then
		export DYLD_LIBRARY_PATH="${DYLD_LIBRARY_PATH}:${2}/c/lib"
	else
		export LD_LIBRARY_PATH="${LD_LIBRARY_PATH}:${2}/c/lib"
	fi
	nxjpcc \
		Starter.java && \
	javac \
		-cp ".:${2}:${2}/java/classes:${2}/java/lib/commons-logging.jar:${2}/java/lib/log4j-1.2.8.jar" \
		TUIStarter.java && \
	java -Dcharva.color=1 \
		-cp ".:${2}:${2}/java/classes:${2}/java/lib/commons-logging.jar:${2}/java/lib/log4j-1.2.8.jar" \
		TUIStarter 2> charva.log
elif [ "$1" = "gui" ]; then
	nxjpcc Starter.java && \
	nxjpcc MidgameStarter.java && \
	nxjpcc GUIStarter.java && \
	nxjpc GUIStarter
else
	nxjpcc Starter.java && \
	nxjpc Starter ${1} ${2} ${3}
fi