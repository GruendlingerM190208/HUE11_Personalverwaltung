package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;

public record Person(int id, String firstName, String lastName, Date birthday, String gender, Address address,
                     String phonenumber, String email, Job job, String notes) {
    public Person(String firstName, String lastName, Date birthday, String gender, Address address, String phonenumber, String email, Job job, String notes) {
        this(0, firstName, lastName, birthday, gender, address, phonenumber, email, job, notes);
    }

    @Override
    public String toString() {
        return String.format("| %-3d | %-10s | %-15s | %-10s | %-10s %s %-15s | %-20s %s %-20s |", id, firstName, lastName, new SimpleDateFormat("yyyy.MM.dd").format(birthday), gender, address.toString(), phonenumber, email, job, notes);
    }
}
