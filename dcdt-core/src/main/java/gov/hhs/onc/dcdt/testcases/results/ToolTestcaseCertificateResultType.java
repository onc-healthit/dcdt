package gov.hhs.onc.dcdt.testcases.results;

public enum ToolTestcaseCertificateResultType {
    VALID_CERT("dcdt.testcase.result.cert.ValidCert.msg"), INCORRECT_CERT_TYPE("dcdt.testcase.result.cert.IncorrectCertType.msg"), MISSING_CERT_ATTR(
        "dcdt.testcase.result.cert.MissingCertAttr.msg"), EXPIRED_CERT("dcdt.testcase.result.cert.ExpiredCert.msg"), UNEXPECTED_CERT(
        "dcdt.testcase.result.cert.UnexpectedCert.msg"), UNREADABLE_CERT_DATA("dcdt.testcase.result.cert.UnreadableCertData.msg"), INVALID_SUBJ_ALT_NAME(
        "dcdt.testcase.result.cert.InvalidSubjAltName.msg"), INVALID_SUBJ_DN("dcdt.testcase.result.cert.InvalidSubjDn.msg"), NO_CERT(
        "dcdt.testcase.result.cert.NoCert.msg"), INCORRECT_CERT("dcdt.testcase.result.cert.IncorrectCert.msg"), MISSING_CERT(
        "dcdt.testcase.result.cert.MissingCert.msg"), INVALID_PATH("dcdt.testcase.result.cert.InvalidPath.msg");

    private final String message;

    private ToolTestcaseCertificateResultType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
