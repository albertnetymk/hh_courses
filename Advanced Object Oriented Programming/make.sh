#!/bin/bash -e
ant -f ../build.xml -Dbasedir="$PWD" $@
