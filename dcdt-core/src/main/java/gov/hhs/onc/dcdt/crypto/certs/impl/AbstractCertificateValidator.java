package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidator;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;

public abstract class AbstractCertificateValidator extends AbstractToolBean implements CertificateValidator {
    protected ToolTestcaseCertificateResultType errorCode;
    protected boolean optional;

    @Override
    public ToolTestcaseCertificateResultType getErrorCode() {
        return this.errorCode;
    }

    @Override
    public void setErrorCode(ToolTestcaseCertificateResultType errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public boolean isOptional() {
        return this.optional;
    }

    @Override
    public void setOptional(boolean optional) {
        this.optional = optional;
    }
}
