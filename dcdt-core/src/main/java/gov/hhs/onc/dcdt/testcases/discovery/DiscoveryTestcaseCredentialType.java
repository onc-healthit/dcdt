package gov.hhs.onc.dcdt.testcases.discovery;

public enum DiscoveryTestcaseCredentialType {
    CA("ca"), BACKGROUND("background"), TARGET("target");

    private final String type;

    private DiscoveryTestcaseCredentialType(String type) {
        this.type = type;
    }

    public boolean isCa() {
        return this == CA;
    }

    public boolean isBackground() {
        return this == BACKGROUND;
    }

    public boolean isTarget() {
        return this == TARGET;
    }

    public String getType() {
        return this.type;
    }
}
