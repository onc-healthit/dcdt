#!/bin/bash

# Determining system utility executables
FIND_EXEC="find"
READLINK_EXEC="readlink"

if (echo "$OSTYPE" | egrep -qs 'darwin'); then
	! type -a "gfind" &>"/dev/null" || FIND_EXEC="gfind"
	! type -a "greadlink" &>"/dev/null" || READLINK_EXEC="greadlink"
fi

scriptBinFile="$("$READLINK_EXEC" -f "$BASH_SOURCE")"
scriptBinDir="$(dirname "$scriptBinFile")"

tomcatConfDir="/etc/tomcat7"
tomcatServerConfFile="${1:-$tomcatConfDir/server.xml}"

tomcatServerConf="$(cat "$tomcatServerConfFile" | \
	tr '\n' '\0' | \
	sed -r '/<Connector[^>]+\/>/ s/([ \t\0\r]+port="808)0("[ \t\0\r]+)/\11\2/' | \
	tr '\0' '\n')"

printf "--" '%s' "$tomcatServerConf" 1>"$tomcatServerConfFile"
