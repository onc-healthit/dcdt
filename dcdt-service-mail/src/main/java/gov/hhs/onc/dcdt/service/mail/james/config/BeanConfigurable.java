package gov.hhs.onc.dcdt.service.mail.james.config;

import org.apache.james.lifecycle.api.Configurable;

public interface BeanConfigurable<T extends JamesConfigBean> extends Configurable {
    public T getConfigBean();

    public void setConfigBean(T configBean);
}
