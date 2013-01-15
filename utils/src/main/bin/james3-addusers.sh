#!/bin/bash

# Determining system utility executables
FIND_EXEC="find"
READLINK_EXEC="readlink"

if (echo "$OSTYPE" | egrep -qs 'darwin'); then
	! type -a "gfind" &>"/dev/null" || FIND_EXEC="gfind"
	! type -a "greadlink" &>"/dev/null" || READLINK_EXEC="greadlink"
fi

JAMES_CLI_HOST="localhost"

function _james_cli()
{
	"$jamesCliBinFile" -h "$JAMES_CLI_HOST" "$@"
}

scriptBinFile="$("$READLINK_EXEC" -f "$BASH_SOURCE")"
scriptBinDir="$(dirname "$scriptBinFile")"

jamesBinDir="$(find "/usr/share/direct" -maxdepth 1 -type "d" -name 'apache-james-*')/bin"
jamesCliBinFile="$jamesBinDir/james-cli.sh"

domain="$(echo "$1" | tr -d '[:blank:]')"

[ -n "$domain" ] || { echo "A domain name must be specified." && exit 1; }

existingUsers="$(_james_cli "listusers" | egrep '@')"

declare -a addUsers=(\
	"dts500@direct1.$domain" \
	"dts501@direct1.$domain" \
	"dts502@direct1.$domain" \
	"dts505@direct2.$domain" \
	"dts506@direct2.$domain" \
	"dts507@direct3.$domain" \
	"dts511@direct4.$domain" \
	"dts512@direct6.$domain" \
	"dts515@direct2.$domain" \
	"dts517@direct3.$domain" \
	"dts520@direct5.$domain" \
)

for addUser in ${addUsers[@]}; do
	addUserPass="${addUser%%@*}pass"
	
	{ echo "$existingUsers" | egrep -qsx "$addUser"; } || \
		{
			_james_cli "adduser" "$addUser" "$addUserPass" && \
			\
			echo "Added user: name=$addUser, pass=$addUserPass"
		}
done
