package gov.hhs.onc.dcdt.cert.lookup;

public interface CertLookUpFactory {
	
	public abstract CertificateInfo execute(CertificateInfo certInfo) throws CertLookUpException;

}
