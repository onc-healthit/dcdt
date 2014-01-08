package gov.hhs.onc.dcdt.testcases.hosting;

public enum HostingTestcaseBinding {
    ADDRESS("address"), DOMAIN("domain"), NEITHER("neither"), EITHER("either");

    private final String binding;

    private HostingTestcaseBinding(String binding) {
        this.binding = binding;
    }

    public String getBinding() {
        return this.binding;
    }
}