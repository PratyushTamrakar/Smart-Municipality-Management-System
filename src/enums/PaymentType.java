package enums;

public enum PaymentType {
    WASTE_MANAGEMENT_FEE("Waste Management Fee"),
    VEHICLE_TAX("Vehicle Tax"),
    BUSINESS_TAX("Business Tax"),
    PROPERTY_TAX("Property Tax");

    private final String label;

    PaymentType(String label) {
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