package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolConnectionPasswordCredentialBean;
import javax.annotation.Nonnegative;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractToolConnectionPasswordCredentialBean<T> extends AbstractToolConnectionCredentialBean<T, String> implements
    ToolConnectionPasswordCredentialBean<T> {
    protected int genPassLen;

    @Override
    protected String generateSecret() {
        return RandomStringUtils.randomAscii(this.genPassLen);
    }

    @Nonnegative
    @Override
    public int getGeneratedPasswordLength() {
        return this.genPassLen;
    }

    @Override
    public void setGeneratedPasswordLength(@Nonnegative int genPassLen) {
        this.genPassLen = genPassLen;
    }

    @Override
    public boolean hasSecret() {
        return !StringUtils.isBlank(this.secret);
    }
}
