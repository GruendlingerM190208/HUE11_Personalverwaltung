package org.example;

public record Address(String street, int houseNumber, int zipCode, String country, String land) {
    @Override
    public String toString() {
        return String.format("| %-10s | %-10d | %-10d | %-10s | %-10s |", street, houseNumber, zipCode, country, land);
    }
}
