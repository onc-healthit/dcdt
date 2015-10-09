package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.beans.ToolMessage;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolResultBean;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidatorContext;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("certValidatorContextImpl")
@Lazy
@Scope("prototype")
public class CertificateValidatorContextImpl extends AbstractToolResultBean implements CertificateValidatorContext {
    private CertificateInfo certInfo;
    private MailAddress directAddr;
    private List<ToolMessage> msgs = new ArrayList<>();

    public CertificateValidatorContextImpl(MailAddress directAddr, CertificateInfo certInfo) {
        this.directAddr = directAddr;
        this.certInfo = certInfo;
    }

    @Override
    public CertificateInfo getCertificateInfo() {
        return this.certInfo;
    }

    @Override
    public MailAddress getDirectAddress() {
        return this.directAddr;
    }

    @Override
    public List<ToolMessage> getMessages() {
        return this.msgs;
    }
}
