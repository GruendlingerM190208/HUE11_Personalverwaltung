package org.example;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PersonalManagement personalManagement = new PersonalManagement();
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;
        while (userInput != 7) {
            personalManagement.printMainMenu();
            System.out.print("Eingabe>");
            String userInputString = scanner.next();
            try {
                userInput = Integer.parseInt(userInputString);
            } catch (Exception e) {
                System.out.println("'" + userInputString + "' steht nicht zur Auswahl!");
            }
            switch (userInput) {
                case 1:
                    Person personFromTerminal = readPersonFromTerminal();
                    if (personFromTerminal != null) {
                        personalManagement.addPerson(personFromTerminal);
                        System.out.println("Neue Person wurde erstellt!");
                    }
                    break;
                case 2:
                    List<Person> persons = personalManagement.getPersonsFromFile();
                    int personId = selectPerson(persons);
                    if (personId != -1) {
                        printPersonAttributes(persons.get(personId - 1));
                        System.out.print("Welches Attribut soll bearbeitet werden? >");
                        int selectedAttribute = scanner.nextInt();
                        System.out.print("Was soll der neue Eintrag sein? >");
                        String newEntry = scanner.next();
                        if (personalManagement.editPerson(personId, selectedAttribute, newEntry)) {
                            System.out.println("Person aktualisiert.");
                        } else {
                            System.out.println("Person nicht gefunden.");
                        }
                    }
                    break;
                case 3:
                    personId = selectPerson(personalManagement.getPersonsFromFile());
                    if (personId != -1) {
                        if (personalManagement.deletePerson(personId)) {
                            System.out.println("Person wurde gelöscht");
                        } else {
                            System.out.println("Person wurde nicht gefunden");
                        }
                    }
                    break;
                case 4:
                    System.out.print("Geben sie das gesuchte Attribut ein: >");
                    String searchingAttribut = scanner.next();
                    Person personWithAttribut = personalManagement.searchPersonWithAttribut(searchingAttribut);
                    if(personWithAttribut != null){
                        System.out.println("Person mit Attribut '" + searchingAttribut + "' wurde gefunden:");
                        System.out.println(personWithAttribut);
                    } else {
                        System.out.println("Person mit Attribut '" + searchingAttribut + "' wurde nicht gefunden:");
                    }
                    break;
                case 5:
                    printPersons(personalManagement.getPersonsFromFile());
                    break;
                case 6:
                    break;
                case 7:
                    System.out.println("Das Programm wird beendet.");
                    break;
            }
        }
    }

    private static void printPersonAttributes(Person person) {
        Address address = person.address();
        Job job = person.job();
        System.out.println("1 -> " + person.firstName());
        System.out.println("2 -> " + person.lastName());
        System.out.println("3 -> " + person.birthday());
        System.out.println("4 -> " + person.gender());
        System.out.println("5 -> " + address.street());
        System.out.println("6 -> " + address.houseNumber());
        System.out.println("7 -> " + address.zipCode());
        System.out.println("8 -> " + address.land());
        System.out.println("9 -> " + address.country());
        System.out.println("10 -> " + person.phonenumber());
        System.out.println("11 -> " + person.email());
        System.out.println("12 -> " + job.jobtitle());
        System.out.println("13 -> " + job.department());
        System.out.println("14 -> " + job.salary());
        System.out.println("15 -> " + job.workStartTime());
        System.out.println("16 -> " + job.workEndTime());
        System.out.println("17 -> " + person.notes());
    }

    private static int selectPerson(List<Person> persons) {
        printPersons(persons);
        System.out.print("\nGeben sie die ID von der gewünschten Person ein >");
        try {
            return new Scanner(System.in).nextInt();
        } catch (Exception ex) {
            System.out.println("Das war keine ID!");
        }
        return -1;
    }

    private static void printPersons(List<Person> persons) {
        System.out.println("\n --------------------Personen---------------------");
        for (Person person : persons) {
            System.out.println(person.toString());
        }
    }

    private static Person readPersonFromTerminal() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Vorname der Person: ");
            String firstname = scanner.next();
            System.out.print("Nachname der Person: ");
            String lastname = scanner.next();
            System.out.print("Geburtstag der Person (dd.mm.yyyy): ");
            String birthday = scanner.next();
            System.out.print("Geschlecht der Person: ");
            String gender = scanner.next();

            System.out.print("Straße der Person: ");
            String street = scanner.next();
            System.out.print("Hausnummer der Person: ");
            int housenumber = scanner.nextInt();
            System.out.print("Postleitzahl der Person: ");
            int zipCode = scanner.nextInt();
            System.out.print("Stadt der Person: ");
            String country = scanner.next();
            System.out.print("Land der Person: ");
            String land = scanner.next();

            System.out.print("Telefonnummer der Person: ");
            String phonenumber = scanner.next();
            System.out.print("Email der Person: ");
            String email = scanner.next();

            System.out.print("Jobtitel der Person: ");
            String jobtitle = scanner.next();
            System.out.print("Abteilung der Person: ");
            String department = scanner.next();
            System.out.print("Gehalt der Person: ");
            double salary = scanner.nextDouble();
            System.out.print("Arbeitsbeginn der Person: ");
            String workStartTime = scanner.next();
            System.out.print("Arbeitsende der Person: ");
            String workEndTime = scanner.next();

            System.out.print("Notizen zur Person: ");
            String notes = scanner.next();
            return new Person(firstname, lastname, birthday, gender, new Address(street, housenumber, zipCode, country, land), phonenumber, email, new Job(jobtitle, department, salary, workStartTime, workEndTime), notes);
        } catch (Exception ex) {
            System.out.println("Überprüfen sie ihre eingabe!");
        }
        return null;
    }
}