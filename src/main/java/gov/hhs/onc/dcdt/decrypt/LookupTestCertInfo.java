/**
 * 
 */
package gov.hhs.onc.dcdt.decrypt;

/**
 * Contains the info for a single path, good or bad, through a cert lookup test case
 * @author joelamy
 *
 */
public class LookupTestCertInfo {
	
	// The base name is the name of the p12 cert, minus the ".p12"
	// Currently expects separate files for public cert and private key
	// so it looks for <certBaseName>_cert.pem and <certBaseName>_key.pem
	// Currently expects unprotected private key (no password needed to decrypt)
	private final String certBaseName;
	
	// Explanation of the result of the test case if this cert successfully decrypts the message
	private final String resultStringIfThisDecrypts;

	/**
	 * @param certBaseName
	 * @param privateKeyFile
	 * @param resultStringIfThisDecrypts
	 */
	public LookupTestCertInfo(String certBaseName, String resultStringIfThisDecrypts) {
		super();
		this.certBaseName = certBaseName;
		this.resultStringIfThisDecrypts = resultStringIfThisDecrypts;
	}

	public String getCertFilename() {
		return certBaseName + "_cert.pem";
	}

	public String getPrivateKeyFilename() {
		return certBaseName + "_key.pem";
	}

	public String getResultStringIfThisDecrypts() {
		return resultStringIfThisDecrypts;
	}
}
