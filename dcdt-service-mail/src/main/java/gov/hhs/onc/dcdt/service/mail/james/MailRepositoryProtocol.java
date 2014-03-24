package gov.hhs.onc.dcdt.service.mail.james;

public enum MailRepositoryProtocol {
    MEMORY("memory");

    private final String protocol;

    private MailRepositoryProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return this.protocol;
    }
}
