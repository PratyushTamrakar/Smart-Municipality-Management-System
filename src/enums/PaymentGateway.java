package enums;

public enum PaymentGateway {
    ESEWA("eSewa"),
    KHALTI("Khalti"),
    CONNECT_IPS("Connect IPS");

    private final String label;

    PaymentGateway(String label) {
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