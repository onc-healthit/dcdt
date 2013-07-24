#!/bin/bash

set -e

function _exec_java()
{
    (
        cd "$baseDir"
        
        java \
            -cp "$(_build_java_class_path)" \
            -Ddcdt.base.dir="$baseDir" \
            -Ddcdt.bin.file="$binFile" \
            -Ddcdt.bin.dir="$binDir" \
            -Ddcdt.conf.dir="$confDir" \
            -Ddcdt.lib.dir="$libDir" \
            -Ddcdt.log.dir="@{project.log.dir}" \
            -Ddcdt.log.file.basename="@{project.log.file.basename}" \
            -Ddcdt.standalone.term.width="$(_get_term_width)" \
            "$@"
    )
}

function _build_java_class_path()
{
    local javaClassPath="$CONF_DIR_NAME"
    
    for jarFileName in $("$findExec" "$libDir" -mindepth 1 -maxdepth 1 -type "f" -name '*.jar' -printf '%f\n'); do
        javaClassPath="$javaClassPath:$LIB_DIR_NAME/$jarFileName"
    done
    
    printf "--" '%s\n' "$javaClassPath"
}

function _get_term_width()
{
    stty -a | egrep -o 'columns [[:digit:]]+' | cut -d " " -f 2
}

readonly NULL="/dev/null"

readonly CONF_DIR_NAME="conf"
readonly LIB_DIR_NAME="lib"

readonly FIND_EXEC_DEFAULT="find"
readonly FIND_EXEC_DARWIN="gfind"
readonly READLINK_EXEC_DEFAULT="readlink"
readonly READLINK_EXEC_DARWIN="greadlink"

# Determining system utility executables
findExec="$FIND_EXEC_DEFAULT"
readlinkExec="$READLINK_EXEC_DEFAULT"

case "${OSTYPE,,}" in
    *darwin*)
        ! type -a "$FIND_EXEC_DARWIN" &>"$NULL" || findExec="$FIND_EXEC_DARWIN"
        ! type -a "$READLINK_EXEC_DARWIN" &>"$NULL" || readlinkExec="$READLINK_EXEC_DARWIN"
        ;;
esac

binFile="$("$readlinkExec" -f "$BASH_SOURCE")"
binDir="$(dirname "$binFile")"
baseDir="$(dirname "$binDir")"
confDir="$baseDir/$CONF_DIR_NAME"
libDir="$baseDir/$LIB_DIR_NAME"

# Executing standalone
%{STANDALONE_EXEC}

exit 0
