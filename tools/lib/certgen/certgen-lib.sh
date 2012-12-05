# Basic constants
declare -i FALSE=0
declare -i TRUE=1
declare -i UNKNOWN=-1
NEWLINE=$'\n'

# Algorithm constants
ALG_PBE_MD5_DES="PBE-MD5-DES"
ALG_RSA="rsa"

# File encoding constants
FORM_DER="der"
FORM_PEM="pem"

# File/name prefix constants
PREFIX_TMP="~"

# File/name suffix constants
SUFFIX_CA="_ca"
SUFFIX_CERT="_cert"
SUFFIX_KEY="_key"

# File extension constants
EXT_DER=".der"
EXT_PEM=".pem"
EXT_PKCS12=".p12"
EXT_REQ=".csr"

# OpenSSL configuration section constants
# Distinguished names
SECTION_DN_CA="dn_ca"
SECTION_DN_CERT="dn_cert"
# X509v3 extensions
SECTION_EXT_CA="ext_ca"
SECTION_EXT_CERT="ext_cert"
SECTION_EXT_CERT_DOMAIN="ext_cert_domain"
SECTION_EXT_REQ="ext_req"

##
# Calls the OpenSSL Command Line Interface (CLI) with the given options.
# 
# * options for OpenSSL
##
function _openssl()
{
	OPENSSL_CONF="$certgenOpensslConfFile" \
	SSLEAY_CONF= \
	"${OPENSSL:-openssl}" \
	"$@"
}
alias _openssl_req='_openssl "req"'
alias _openssl_pkcs8='_openssl "pkcs8"'
alias _openssl_pkcs12='_openssl "pkcs12"'
alias _openssl_rand='_openssl "rand"'
alias _openssl_x509='_openssl "x509"'

##
# Generates Certificate Authority (CA) key + certificate.
# 
# -c|--cert <path> certificate file path
# -k|--key <path> key file path
# -n|--name <name> name of the CA
##
function _ssl_gen_ca()
{
	local certFile
	local keyFile
	local name
	
	eval set "--" "$(getopt -a -o "-" -l "c:,cert:,k:,key:,n:,name:" "--" "$@")"
	
	while true; do
		case "$1" in
			"--c"|"--cert")
				certFile="$2"
				shift 2
				;;
			
			"--k"|"--key")
				keyFile="$2"
				shift 2
				;;
			
			"--n"|"--name")
				name="$2"
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
	
	dnSection="$SECTION_DN_CA" \
	name="$name" \
	\
	_openssl_req \
		-extensions "$SECTION_EXT_CA" \
		-keyform "$FORM_PEM" \
		-keyout "$keyFile" \
		-newkey "$ALG_RSA" \
		-nodes \
		-out "$certFile" \
		-outform "$FORM_PEM" \
		-x509
}

##
# Creates a DER encoded certificate from a PEM encoded one.
# 
# --cd|--certder <path> DER encoded certificate file path
# --cp|--certpem <path> PEM encoded certificate file path
##
function _ssl_cert_pem_to_der()
{
	local certFileDer
	local certFilePem
	
	eval set "--" "$(getopt -a -o "-" -l "cd:,certder:,cp:,certpem:" "--" "$@")"
	
	while true; do
		case "$1" in
			"--cd"|"--certder")
				certFileDer="$2"
				shift 2
				;;
			
			"--cp"|"--certpem")
				certFilePem="$2"
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
	
	_openssl_x509 \
		-in "$certFilePem" \
		-inform "$FORM_PEM" \
		-out "$certFileDer" \
		-outform "$FORM_DER"
}

##
# Creates both DER and PEM encoded, PKCS#8 format keys from a PEM encoded, regular format one.
# 
# --kd|--keyder <path> DER encoded key file path
# --kp|--keypem <path> PEM encoded key file path
##
function _ssl_keys_to_pkcs8()
{
	local keyFileDer
	local keyFilePem
	
	eval set "--" "$(getopt -a -o "-" -l "kd:,keyder:,kp:,keypem:" "--" "$@")"
	
	while true; do
		case "$1" in
			"--kd"|"--keyder")
				keyFileDer="$2"
				shift 2
				;;
			
			"--kp"|"--keypem")
				keyFilePem="$2"
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
	
	keyFileTmp="$(_tmp_file_name "$keyFilePem")"
	mv "$keyFilePem" "$keyFileTmp" || return

	_openssl_pkcs8 \
		-in "$keyFileTmp" \
		-inform "$FORM_PEM" \
		-nocrypt \
		-out "$keyFileDer" \
		-outform "$FORM_DER" \
		-topk8 \
		-v1 "$ALG_PBE_MD5_DES" || return

	_openssl_pkcs8 \
		-in "$keyFileDer" \
		-inform "$FORM_DER" \
		-nocrypt \
		-out "$keyFilePem" \
		-outform "$FORM_PEM" \
		-v1 "$ALG_PBE_MD5_DES" || return

	rm "$keyFileTmp" || return
}

##
# Generates an entry key + certificate request.
# 
# -a|--address <address> email address
# --kb|--keybits <num> number of key bits
# -k|--key <path> key file path
# -n|--name <name> name of the entry
# -r|--req <path> certificate request file path
# -k|--key <path> key file path
# --vd|--validdays <num> number of days the certificate will be valid for
##
function _ssl_gen_entry()
{
	local address
	declare -i keyBits
	local keyFile
	local name
	local reqFile
	declare -i validityDays
	
	eval set "--" "$(getopt -a -o "-" -l "a:,address:,k:,key:,kb:,keybits:,n:,name:,r:,req:,vd:,validdays:" "--" "$@")"
	
	while true; do
		case "$1" in
			"--a"|"--address")
				address="$2"
				shift 2
				;;
			
			"--k"|"--key")
				keyFile="$2"
				shift 2
				;;
			
			"--kb"|"--keybits")
				keyBits=$2
				shift 2
				;;
			
			"--n"|"--name")
				name="$2"
				shift 2
				;;
			
			"--r"|"--req")
				reqFile="$2"
				shift 2
				;;
			
			"--vd"|"--validdays")
				validityDays=$2
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
	
	address="$address" \
	dnSection="$SECTION_DN_CERT" \
	keyBits="$keyBits" \
	name="$name" \
	validityDays="$validityDays" \
	\
	_openssl_req \
		-keyform "$FORM_PEM" \
		-keyout "$keyFile" \
		-new \
		-newkey "$ALG_RSA" \
		-nodes \
		-out "$reqFile" \
		-reqexts "$SECTION_EXT_REQ" || return
}

##
# Signs an entry certificate request.
# 
# -a|--address <address> email address
# -c|--cert <path> certificate file path
# --cac|--cacert <path> CA certificate file path
# --cak|--cakey <path> CA key file path
# -n|--name <name> name of the entry
# -r|--req <path> certificate request file path
# --vd|--validdays <num> number of days the certificate will be valid for
##
function _ssl_sign_entry()
{
	local address
	local caCertFile
	local caKeyFile
	local certFile
	local name
	local reqFile
	declare -i validityDays
	
	eval set "--" "$(getopt -a -o "-" -l "a:,address:,c:,cert:,cac:,cacert:,cak:,cakey:,n:,name:,r:,req:,vd:,validdays:" "--" "$@")"
	
	while true; do
		case "$1" in
			"--a"|"--address")
				address="$2"
				shift 2
				;;
			
			"--c"|"--cert")
				certFile="$2"
				shift 2
				;;
			
			"--cac"|"--cacert")
				caCertFile="$2"
				shift 2
				;;
			
			"--cak"|"--cakey")
				caKeyFile="$2"
				shift 2
				;;
			
			"--n"|"--name")
				name="$2"
				shift 2
				;;
			
			"--r"|"--req")
				reqFile="$2"
				shift 2
				;;
			
			"--vd"|"--validdays")
				validityDays=$2
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
	
	local extSection="$SECTION_EXT_CERT"
	[[ "$certAddress" =~ ^[^@]+@[^@]+$ ]] || extSection="$SECTION_EXT_CERT_DOMAIN"
	
	address="$address" \
	name="$name" \
	validityDays="$validityDays" \
	\
	_openssl_x509 \
		-CA "$caCertFile" \
		-CAform "$FORM_PEM" \
		-CAkey "$caKeyFile" \
		-CAkeyform "$FORM_PEM" \
		-extensions "$extSection" \
		-extfile "$certgenOpensslConfFile" \
		-in "$reqFile" \
		-out "$certFile" \
		-outform "$FORM_PEM" \
		-req \
		-set_serial "$(_ssl_gen_serial)" || exit
}

##
# Creates a PKCS#12 file containing an entry's key + certificate
# 
# -c|--cert <path> certificate file path
# -k|--key <path> key file path
# -n|--name <name> name of the entry
# -p|--pkcs12 <path> PKCS#12 file path
##
function _ssl_entry_to_pkcs12()
{
	local certFile
	local keyFile
	local name
	local pkcs12File
	
	eval set "--" "$(getopt -a -o "-" -l "c:,cert:,k:,key:,n:,name:,p:,pkcs12:" "--" "$@")"
	
	while true; do
		case "$1" in
			"--c"|"--cert")
				certFile="$2"
				shift 2
				;;
			
			"--k"|"--key")
				keyFile="$2"
				shift 2
				;;
			
			"--n"|"--name")
				name="$2"
				shift 2
				;;
			
			"--p"|"--pkcs12")
				pkcs12File="$2"
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
	
	_openssl_pkcs12 \
		-export \
		-in "$certFile" \
		-inkey "$keyFile" \
		-name "$name" \
		-nodes \
		-out "$pkcs12File" \
		-passout "pass:"
}

##
# Generates a certificate serial number.
# 
# -h|--hex whether to use hex encoding
# -n|--num <num> number of bytes to generate
##
function _ssl_gen_serial()
{
	declare -i hex=$TRUE
	declare -i numBytes=64
	
	eval set "--" "$(getopt -a -o "-" -l "h,hex,n:,num:" "--" "$@")"

	while true; do
		case "$1" in
			"--h"|"--hex")
				hex=$TRUE
				shift
				;;
			
			"+h")
				hex=$FALSE
				shift
				;;
			
			"--n"|"--num")
				numBytes="$2"
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
	
	local result="$(_openssl_rand $( ((!$hex)) || printf "--" '-hex\n') $numBytes)"
	
	[[ "$result" =~ ^[[:digit:]a-f]+$ ]] || return $(false)
	
	((!$hex)) || echo -n "0x"
	echo "$result"
}

##
# Gets a part of the value of a property.
# 
# 1 part index
# 2 property key
##
function _props_entry_value_part()
{
	_props_entry_value "$2" | sed -nr "s/^([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)\$/\\$1/p"
}
alias _props_entry_name='_props_entry_value_part 1'
alias _props_entry_address='_props_entry_value_part 2'
alias _props_entry_validity_days='_props_entry_value_part 3'
alias _props_entry_key_bits='_props_entry_value_part 4'

##
# Gets the value of a property.
# 
# 1 property key
##
function _props_entry_value()
{
	_props_entry_lines | sed -nr "/^$1=/ s/^$1=(.+)\$/\\1/p" | domain="$domain" awk '
		{
			while(match($0, "[$]{[^}]+}"))
			{
				varName=substr($0, RSTART + 2, RLENGTH - 3)
				gsub("[$]{"varName"}", ENVIRON[varName])
			}
			
			print $0
		}
	'
}

##
# Gets all available property keys.
##
function _props_entry_keys()
{
	_props_entry_lines | sed -nr 's/^(CERT_[0-9]+)=.+$/\1/p'
}

##
# Gets all available property lines.
##
function _props_entry_lines()
{
	egrep '^CERT_[[:digit:]]+=[^\|]+\|[^\|]+|[[:digit:]]+|[[:digit:]]+$' "$certgenPropsFile"
}

##
# Gets a temporary file path given an existing file path. 
# 
# 1 file path
##
function _tmp_file_name()
{
	echo "$(dirname "$1")/$PREFIX_TMP$(basename "$1")"
}
