package enums;

public enum ComplaintStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved");

    private final String label;

    ComplaintStatus(String label) {
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