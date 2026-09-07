package enums;

public enum CertificateType {
    BIRTH("Birth Certificate"),
    DEATH("Death Certificate");

    private final String label;

    CertificateType(String label) {
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