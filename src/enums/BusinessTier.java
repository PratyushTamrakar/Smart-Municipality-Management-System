package enums;

/**
 * Flat annual business-tax rate per tier.
 * PLACEHOLDER RATES — replace with your municipality's actual schedule.
 */
public enum BusinessTier {
    TIER_1("Tier 1 (Large)", 15000.0),
    TIER_2("Tier 2 (Medium)", 10000.0),
    TIER_3("Tier 3 (Small)", 5000.0);

    private final String label;
    private final double annualTax;

    BusinessTier(String label, double annualTax) {
        this.label = label;
        this.annualTax = annualTax;
    }

    public String getLabel() {
        return label;
    }

    public double getAnnualTax() {
        return annualTax;
    }

    @Override
    public String toString() {
        return label;
    }
}