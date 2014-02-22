package gov.hhs.onc.dcdt.service.ldap.config.impl;

/**
 * @see org.apache.directory.server.annotations.SaslMechanism
 */
public class SaslMechanismConfig {
    private String name;
    private Class<?> implClass;

    public Class<?> getImplClass() {
        return this.implClass;
    }

    public void setImplClass(Class<?> implClass) {
        this.implClass = implClass;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
