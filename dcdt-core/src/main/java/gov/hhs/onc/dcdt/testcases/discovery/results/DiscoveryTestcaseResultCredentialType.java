package gov.hhs.onc.dcdt.testcases.discovery.results;

public enum DiscoveryTestcaseResultCredentialType {
    EXPECTED("_expected"), DISCOVERED("_discovered");

    private final String attachmentFileNameSuffix;

    private DiscoveryTestcaseResultCredentialType(String attachmentFileNameSuffix) {
        this.attachmentFileNameSuffix = attachmentFileNameSuffix;
    }

    public String getAttachmentFileNameSuffix() {
        return this.attachmentFileNameSuffix;
    }
}
