package org.example;

public record Address(String street, int houseNumber, int zipCode, String country, String land) {
    @Override
    public String toString() {
        return zipCode + " " + country + " " + street + " " + houseNumber + " in " + land;
    }
}
