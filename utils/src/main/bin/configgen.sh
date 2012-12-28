#!/bin/bash

utilBinFile="$(readlink -f "$BASH_SOURCE")"
utilBinDir="$(dirname "$utilBinFile")"
utilBaseDir="$(dirname "$utilBinDir")"
utilConfDirName="conf"
utilConfDir="$utilBaseDir/$utilConfDirName"
utilLibDirName="lib"
utilLibDir="$utilBaseDir/$utilLibDirName"
utilMainClass="@{configgen.class.main}"

utilClassPath="$utilConfDirName"

for utilJarFileName in $(find "$utilLibDir" -mindepth 1 -maxdepth 1 -type "f" -name '*.jar' -printf '%f\n'); do
	utilClassPath="$utilClassPath:$utilLibDirName/$utilJarFileName"
done

(
	cd "$utilBaseDir" && \
	\
	java \
		-cp "$utilClassPath" \
		"$utilMainClass" \
		"$@"
)
