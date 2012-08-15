package gov.onc.certLookup;

public interface CertLookUpFactory {
	
	public abstract CertificateInfo execute(CertificateInfo certInfo) throws CertLookUpException;

}
