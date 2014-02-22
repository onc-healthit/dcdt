package gov.hhs.onc.dcdt.testcases.results;

public enum ToolTestcaseCertificateResultType {
    VALID_CERT("dcdt.testcase.result.cert.ValidCert.msg"), INCORRECT_CERT_TYPE("dcdt.testcase.result.cert.IncorrectCertType.msg"), MISSING_CERT_ATTR(
        "dcdt.testcase.result.cert.MissingCertAttr.msg"), EXPIRED_CERT("dcdt.testcase.result.cert.ExpiredCert.msg"), UNEXPECTED_CERT(
        "dcdt.testcase.result.cert.UnexpectedCert.msg"), NO_CERT("dcdt.testcase.result.cert.NoCert.msg");

    private final String message;

    private ToolTestcaseCertificateResultType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
