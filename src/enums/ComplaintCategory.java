package enums;

public enum ComplaintCategory {
    ELECTRICITY("Electricity"),
    WATER("Water"),
    WASTE("Waste"),
    DISPUTE_WITH_NEIGHBOURS("Dispute with Neighbours");

    private final String label;

    ComplaintCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}