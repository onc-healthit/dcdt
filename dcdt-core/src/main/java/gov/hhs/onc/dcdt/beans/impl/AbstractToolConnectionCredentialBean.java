package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolConnectionCredentialBean;
import javax.annotation.Nullable;

public abstract class AbstractToolConnectionCredentialBean<T, U> extends AbstractToolNamedBean implements ToolConnectionCredentialBean<T, U> {
    protected T id;
    protected U secret;

    @Override
    public boolean hasId() {
        return (this.id != null);
    }

    @Nullable
    @Override
    public T getId() {
        return this.id;
    }

    @Override
    public void setId(@Nullable T id) {
        this.id = id;
    }

    @Override
    public boolean hasSecret() {
        return (this.secret != null);
    }

    @Nullable
    @Override
    public U getSecret() {
        return this.secret;
    }

    @Override
    public void setSecret(@Nullable U secret) {
        this.secret = secret;
    }
}
