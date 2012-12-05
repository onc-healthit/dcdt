#!/bin/bash

# Checking Bash version - at least version 3.2 is required for regex expansion, etc.
(( (${BASH_VERSINFO[0]} > 3) || (${BASH_VERSINFO[1]} >= 2) )) &>"/dev/null" || \
	{ echo "Bash version >= 3.2 is required. Current version: $BASH_VERSION" && exit 1; }

shopt -s "expand_aliases"
shopt -s "extglob"

# readlink on Mac platforms is not a builtin - aliasing 'greadlink' instead
! { [[ "$OSTYPE" =~ *darwin* ]] && type -a "greadlink" &>"/dev/null"; } || \
	alias readlink='greadlink'

# Filesystem constants
declare -a CERTGEN_LIB_SCRIPT_FILE_NAMES=(\
	"certgen-lib.sh"
)

# Determining filesystem variables
certgenBinFile="$(readlink -f "$BASH_SOURCE")"
certgenBinFileName="$(basename "$certgenBinFile")"
toolsBinDir="$(dirname "$certgenBinFile")"
toolsDir="$(dirname "$toolsBinDir")"
toolsLibDir="$toolsDir/lib"
certgenLibDir="$toolsLibDir/certgen"
certgenOpensslConfFile="$certgenLibDir/openssl-certgen.cnf"
certgenPropsFile="$certgenLibDir/certgen.properties"
projectDir="$(dirname "$toolsDir")"
targetDir="$projectDir/target"
certgenOutDir="$targetDir/certgen"

# Sanity checks and sourcing library scripts
[ -e "$certgenLibDir" ] || { echo "Certificate generator library directory does not exist: $certgenLibDir" && exit 1; }
[ -d "$certgenLibDir" ] || { echo "Certificate generator library directory path is not a directory: $certgenLibDir" && exit 1; }
[ -r "$certgenLibDir" ] || { echo "Certificate generator library directory is not readable: $certgenLibDir" && exit 1; }

for certgenLibScriptFileName in ${CERTGEN_LIB_SCRIPT_FILE_NAMES[@]}; do
	certgenLibScriptFile="$certgenLibDir/$certgenLibScriptFileName"
	
	[ -e "$certgenLibScriptFile" ] || { echo "Certificate generator library script file does not exist: $certgenLibScriptFile" && exit 1; }
	[ -f "$certgenLibScriptFile" ] || { echo "Certificate generator library script file path is not a file: $certgenLibScriptFile" && exit 1; }
	[ -r "$certgenLibScriptFile" ] || { echo "Certificate generator library script is not readable: $certgenLibScriptFile" && exit 1; }
	
	source "$certgenLibScriptFile" || { echo "Unable to source certificate generator library script: $certgenLibScriptFile" && exit 1; }
done

[ -e "$certgenOpensslConfFile" ] || { echo "Certificate generator OpenSSL configuration file does not exist: $certgenOpensslConfFile" && exit 1; }
[ -f "$certgenOpensslConfFile" ] || { echo "Certificate generator OpenSSL configuration file path is not a file: $certgenOpensslConfFile" && exit 1; }
[ -r "$certgenOpensslConfFile" ] || { echo "Certificate generator OpenSSL configuration is not readable: $certgenOpensslConfFile" && exit 1; }

[ -e "$certgenPropsFile" ] || { echo "Certificate generator properties file does not exist: $certgenPropsFile" && exit 1; }
[ -f "$certgenPropsFile" ] || { echo "Certificate generator properties file path is not a file: $certgenPropsFile" && exit 1; }
[ -r "$certgenPropsFile" ] || { echo "Certificate generator properties is not readable: $certgenPropsFile" && exit 1; }

# Parsing cmdline arguments
domain=
declare -i clean=$UNKNOWN
declare -i help=$FALSE
outDir=

# Storing # of cmdline arguments before they are reset
declare -i numArgs=$#

eval set "--" "$(getopt -a -o "-" -l "c,clean,d:,domain:,help,o:,outdir:" "--" "$@")"

while true; do
	case "$1" in
		"--c"|"--clean")
			clean=$TRUE
			shift
			;;
		
		"+c")
			clean=$FALSE
			shift
			;;
		
		"--d"|"--domain")
			domain="$2"
			shift 2
			;;
		
		"--help")
			help=$TRUE
			shift
			;;
		
		"--o"|"--outdir")
			outDir="$2"
			shift 2
			;;
		
		"--")
			shift
			break
			;;
			
		*)
			break
			;;
	esac
done

# Printing help and exiting if explicitely invoked or no cmdline arguments were provided
if (( ($numArgs == 0) || $help)); then
	cat <<-EOF
		Usage: $certgenBinFileName [-c|+c|--clean] -d|--domain <name> [-o|--outdir <dir>]
		       $certgenBinFileName --help
		
		-c|+c|--clean          optional, remove existing output directory without prompting
		-d|--domain <name>     domain name to use
		--help                 show this help information
		-o|--outdir <dir>      optional, directory to output to, default: <project basedir>/target/certgen/<domain name>
EOF
	
	exit
fi

# Sanity checking domain
! [[ "$domain" =~ ^[[:space:]]*$ ]] || { echo "A domain must be specified." && exit 1; }
[[ "$domain" =~ ^[[:alnum:]._\-]+$ ]] || { echo "Domain is invalid: $domain" && exit 1; }

# Determing / sanity checking output directory
! [[ "$outDir" =~ ^[[:space:]]*$ ]] || outDir="$certgenOutDir/$domain"

# Making sure output directory path is absolute
[[ "$outDir" =~ ^/ ]] || outDir="$toolsBinDir/$outDir"

if [ -e "$outDir" ]; then
	[ -d "$outDir" ] || { echo "Output directory path is not a directory: $outDir" && exit 1; }
	
	{
		(($clean != $TRUE)) && \
		read -p "Output directory ($outDir) exists.${NEWLINE}Remove [Y(ES)/n(o)] it?: " "rmOutDir" && \
		[[ ( "${rmOutDir,,}" == "n" ) || ( "${rmOutDir,,}" == "no" ) ]]
	} || \
	{
		{ rm -r "$outDir" && echo "Removed output directory: $outDir"; } || exit;
	}
fi

# Sanity checking output directory
[ -e "$outDir" ] || { { mkdir -p "$outDir" && echo "(Re)created output directory: $outDir"; } || exit; }
[ -w "$outDir" ] || { echo "Output directory is not writeable: $outDir" && exit 1; }

caOutDir="$outDir/ca"

# Sanity checking Certificate Authority (CA) output directory
[ -e "$caOutDir" ] || { { mkdir "$caOutDir" && echo "(Re)created Certificate Authority (CA) output directory: $caOutDir"; } || exit; }
[ -w "$caOutDir" ] || { echo "Certificate Authority (CA) output directory is not writeable: $caOutDir" && exit 1; }

caName="$domain$SUFFIX_CA"
caCertFileBase="$caOutDir/$caName$SUFFIX_CERT"
caCertFileDer="$caCertFileBase$EXT_DER"
caCertFilePem="$caCertFileBase$EXT_PEM"
caKeyFileBase="$caOutDir/$caName$SUFFIX_KEY"
caKeyFileDer="$caKeyFileBase$EXT_DER"
caKeyFilePem="$caKeyFileBase$EXT_PEM"

# Generate Certificate Authority (CA) key + certificate
_ssl_gen_ca \
	-c "$caCertFilePem" \
	-k "$caKeyFilePem" \
	-n "$caName" || exit

# Create DER encoded Certificate Authority (CA) certificate
_ssl_cert_pem_to_der \
	--cd "$caCertFileDer" \
	--cp "$caCertFilePem" || exit

# Convert Certificate Authority (CA) key to PKCS#8 format in both PEM and DER encodings
_ssl_keys_to_pkcs8 \
	--kd "$caKeyFileDer" \
	--kp "$caKeyFilePem" || exit

for entryKey in $(_props_entry_keys); do
	entryName="$(_props_entry_name "$entryKey")"
	entryAddress="$(_props_entry_address "$entryKey")"
	declare -i entryValidityDays="$(_props_entry_validity_days "$entryKey")"
	declare -i entryKeyBits="$(_props_entry_key_bits "$entryKey")"
	
	entryCertFileBase="$outDir/$entryName$SUFFIX_CERT"
	entryCertFileDer="$entryCertFileBase$EXT_DER"
	entryCertFilePem="$entryCertFileBase$EXT_PEM"
	entryCertFileReq="$entryCertFileBase$EXT_REQ"
	entryKeyFileBase="$outDir/$entryName$SUFFIX_KEY"
	entryKeyFileDer="$entryKeyFileBase$EXT_DER"
	entryKeyFilePem="$entryKeyFileBase$EXT_PEM"
	entryKeyFilePkcs12="$entryKeyFileBase$EXT_PKCS12"
	
	# Generate entry key + certificate request
	_ssl_gen_entry \
		-a "$entryAddress" \
		-k "$entryKeyFilePem" \
		--kb entryKeyBits \
		-n "$entryName" \
		-r "$entryCertFileReq" \
		--vd $entryValidityDays || exit
	
	# Sign entry certificate request
	_ssl_sign_entry \
		-a "$entryAddress" \
		-c "$entryCertFilePem" \
		--cac "$caCertFilePem" \
		--cak "$caKeyFilePem" \
		-n "$entryName" \
		-r "$entryCertFileReq" \
		--vd $entryValidityDays || exit
	
	rm "$entryCertFileReq" || exit
	
	# Create DER encoded entry certificate
	_ssl_cert_pem_to_der \
		--cd "$entryCertFileDer" \
		--cp "$entryCertFilePem" || exit

	# Convert entry key to PKCS#8 in both PEM and DER encodings
	_ssl_keys_to_pkcs8 \
		--kd "$entryKeyFileDer" \
		--kp "$entryKeyFilePem" || exit
	
	# Create a PKCS#12 file containing the entry's key + certificate
	_ssl_entry_to_pkcs12 \
		-c "$entryCertFilePem" \
		-k "$entryKeyFilePem" \
		-n "$entryName" \
		-p "$entryKeyFilePkcs12" || exit
done
