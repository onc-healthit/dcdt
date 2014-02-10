package gov.hhs.onc.dcdt.testcases.results;

public enum ToolTestcaseCertificateResultType {
    VALID_CERT("A valid X.509 certificate was found."),
    INCORRECT_CERT_TYPE("A certificate with the incorrect certificate type was found."),
    MISSING_CERT_ATTR("The X.509 certificate that was found has a missing attribute."),
    EXPIRED_CERT("An expired X.509 certificate was found."),
    UNEXPECTED_CERT("A certificate should not have been found for the Direct address provided."),
    NO_CERT("A X.509 certificate was not found.");

    private final String message;

    private ToolTestcaseCertificateResultType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
