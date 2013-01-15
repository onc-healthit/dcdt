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

jamesBinDir="$(find "/usr/share/direct" -maxdepth 1 -type "d" -name 'apache-james-*')/bin"
setdomainBinFile="$jamesBinDir/setdomain.sh"
jamesBaseDir="$(dirname "$jamesBinDir")"
jamesConfDir="$jamesBaseDir/conf"
jamesDomainListConfFile="$jamesConfDir/domainlist.conf"
jamesMailetContainerConfFile="$jamesConfDir/mailetcontainer.conf"

domain="$(echo "$1" | tr -d '[:blank:]')"

[ -n "$domain" ] || { echo "A domain name must be specified." && exit 1; }

"$setdomainBinFile" "$domain" || exit

domainNameReplacement="\\1$domain\\2"
mailetReplacement=

for ((a = 1; a <= 7; a++)); do
	[ -z "$mailetReplacement" ] || mailetReplacement="$mailetReplacement,"
	
	domainNameReplacement="$domainNameReplacement\\n\\1direct$a.$domain\\2"
	mailetReplacement="${mailetReplacement}direct$a.$domain"
done

sed \
	-i \
	-r \
	"s/^([[:space:]]*<domainname>)$domain(<\\/domainname>)\$/$domainNameReplacement/" \
	"$jamesDomainListConfFile" || exit

sed \
	-i \
	-r \
	"s/(\"org\\.nhindirect\\.gateway\\.smtp\\.james\\.matcher\\.RecipAndSenderIsNotLocal=[^\"]*,?)$domain(,[^\"]+\"|\")/\\1$mailetReplacement\\2/" \
	"$jamesMailetContainerConfFile"