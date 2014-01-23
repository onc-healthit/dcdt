package gov.hhs.onc.dcdt.data.events;

public enum ToolBeanEntityEventType {
    DELETE("delete"), LOAD("load"), SAVE("save"), TX_BEGIN("tx_begin"), TX_COMPLETE("tx_complete");

    private String typeDisplay;

    private ToolBeanEntityEventType(String typeDisplay) {
        this.typeDisplay = typeDisplay;
    }

    @Override
    public String toString() {
        return this.typeDisplay;
    }

    public String getTypeDisplay() {
        return this.typeDisplay;
    }
}
