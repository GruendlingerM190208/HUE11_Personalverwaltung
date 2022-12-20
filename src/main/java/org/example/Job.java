package org.example;

import java.text.NumberFormat;

public record Job(String jobtitle, String department, double salary, String workStartTime, String workEndTime) {
    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        return  String.format("| %-15s | %-15s | %-5s€ | %-5s | %-5s |", jobtitle, department, nf.format(salary), workStartTime, workEndTime);
    }
}
