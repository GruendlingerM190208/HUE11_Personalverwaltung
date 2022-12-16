package org.example;

public record Person(int id, String firstName, String lastName, String birthday, String gender, Address address,
                     String phonenumber, String email, Job job, String notes) {
    public Person(String firstName, String lastName, String birthday, String gender, Address address, String phonenumber, String email, Job job, String notes) {
        this(0, firstName, lastName, birthday, gender, address, phonenumber, email, job, notes);
    }

    @Override
    public String toString() {
        return id + ": " + firstName + " " + lastName + " (" + gender + ") ist am " + birthday + " geboren und wohnt in " + address.toString() + ". Email: " + email + " Telefon: " + phonenumber +
                ". " + firstName + " arbeitet als " + job.jobtitle() + " in der Abteilung " + job.department() + " von " + job.workStartTime() + " - " + job.workEndTime() + " Uhr für " + job.salary() + "€/Monat Zusätzliche Infos: " + notes;
    }
}
