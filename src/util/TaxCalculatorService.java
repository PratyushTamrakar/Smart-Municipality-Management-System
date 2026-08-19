package util;

public class TaxCalculatorService {

    // Calculate Property Tax based on square feet and location zone multiplier
    public static double calculatePropertyTax(double areaSqFt, int wardNumber) {
        double baseRatePerSqFt = 2.5; // Base rate
        double wardMultiplier = (wardNumber <= 3) ? 1.5 : 1.2; // Urban vs suburban multiplier
        return areaSqFt * baseRatePerSqFt * wardMultiplier;
    }

    // Calculate Business Tax based on business tier
    public static double calculateBusinessTax(int categoryChoice) {
        return switch (categoryChoice) {
            case 1 -> 5000.0;  // Small Retail / Sole Proprietorship
            case 2 -> 15000.0; // Medium Enterprise
            case 3 -> 40000.0; // Large Corporation
            default -> 5000.0;
        };
    }

    // Calculate Vehicle Tax based on engine displacement (CC)
    public static double calculateVehicleTax(int engineCC) {
        if (engineCC <= 125) return 2500.0;
        if (engineCC <= 250) return 4500.0;
        if (engineCC <= 400) return 8000.0;
        return 15000.0;
    }
}