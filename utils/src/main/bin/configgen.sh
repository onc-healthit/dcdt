#!/bin/bash

# Determining system utility executables
FIND_EXEC="find"
READLINK_EXEC="readlink"

if (echo "$OSTYPE" | egrep -qs 'darwin'); then
	! type -a "gfind" &>"/dev/null" || FIND_EXEC="gfind"
	! type -a "greadlink" &>"/dev/null" || READLINK_EXEC="greadlink"
fi

utilBinFile="$("$READLINK_EXEC" -f "$BASH_SOURCE")"
utilBinDir="$(dirname "$utilBinFile")"
utilBaseDir="$(dirname "$utilBinDir")"
utilConfDirName="conf"
utilConfDir="$utilBaseDir/$utilConfDirName"
utilLibDirName="lib"
utilLibDir="$utilBaseDir/$utilLibDirName"
utilMainClass="@{configgen.class.main}"

utilClassPath="$utilConfDirName"

for utilJarFileName in $("$FIND_EXEC" "$utilLibDir" -mindepth 1 -maxdepth 1 -type "f" -name '*.jar' -printf '%f\n'); do
	utilClassPath="$utilClassPath:$utilLibDirName/$utilJarFileName"
done

utilTermWidth="$(stty -a | egrep -o 'columns [[:digit:]]+' | cut -d " " -f 2)"

(
	cd "$utilBaseDir" && \
	\
	java \
		-cp "$utilClassPath" \
		-Ddcdt.utils.log.file.name.base="@{configgen.log.file.name.base}" \
		-Ddcdt.utils.term.width="$utilTermWidth" \
		"$utilMainClass" \
		"$@"
)
