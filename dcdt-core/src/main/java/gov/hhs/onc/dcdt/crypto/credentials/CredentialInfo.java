package gov.hhs.onc.dcdt.crypto.credentials;

import gov.hhs.onc.dcdt.crypto.CryptographyInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;

public interface CredentialInfo extends CredentialDescriptor<KeyInfo, CertificateInfo>, CryptographyInfo {
}
