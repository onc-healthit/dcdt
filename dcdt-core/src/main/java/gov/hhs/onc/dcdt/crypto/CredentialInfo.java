package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.keys.KeyPairInfo;

public interface CredentialInfo extends CredentialDescriptor<KeyPairInfo, CertificateInfo> {
}
