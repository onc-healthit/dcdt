package gov.hhs.onc.dcdt.beans;

import javax.annotation.Nonnegative;

public interface ToolConnectionPasswordCredentialBean<T> extends ToolConnectionCredentialBean<T, String> {
    @Nonnegative
    public int getGeneratedPasswordLength();

    public void setGeneratedPasswordLength(@Nonnegative int genPassLen);
}
