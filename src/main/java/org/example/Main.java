package org.example;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
            List<Person> persons = personalManagement.getPersonsFromFile();
            switch (userInput) {
                case 1:
                    Person personFromTerminal = readPersonFromTerminal();
                    if (personFromTerminal != null) {
                        personalManagement.addPerson(personFromTerminal);
                        System.out.println("Neue Person wurde erstellt!");
                    }
                    break;
                case 2:
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
                            System.out.println("Person wurde nicht gefunden.");
                        }
                    }
                    break;
                case 3:
                    personId = selectPerson(persons);
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
                    List<Person> personsWithAttribut = personalManagement.searchPersonWithAttribut(searchingAttribut);
                    System.out.println("--------------------Diese Personen haben das Attribut '" + searchingAttribut + "'---------------------");
                    printPersons(personsWithAttribut);
                    break;
                case 5:
                    System.out.println("\n --------------------Personen---------------------");
                    printPersons(personalManagement.getPersonsFromFile());
                    break;
                case 6:
                    analyseGenders(new ArrayList<>(findGenderAmount(persons).entrySet()));
                    analyseAvarageAge(persons);
                    analyseSalarys(persons);
                    break;
                case 7:
                    System.out.println("Das Programm wird beendet.");
                    break;
            }
        }
    }

    private static void analyseSalarys(List<Person> persons) {
        persons = persons.stream().sorted(Comparator.comparingDouble(p -> p.job().salary())).toList();
        try {
            System.out.println("Höchster Gehalt: " + persons.get(persons.size() - 1).job().salary() + "€");
            System.out.println("Niedrigster Gehalt: " + persons.get(0).job().salary() + "€");
            double avarageSalary = persons.stream().map(m -> m.job().salary()).reduce(0d, Double::sum)
                    / persons.size();
            System.out.println("Durchschnittlicher Gehalt: " + avarageSalary);
        } catch (Exception e) {
            System.out.println("Keine Person im System");
        }
    }

    @SuppressWarnings("deprecation")
    private static double BirthdateToAge(Date birthday) {
        return new Date().getYear() - birthday.getYear();
    }

    private static void analyseAvarageAge(List<Person> persons) {
        double avarageAge = persons.stream()
                .map(m -> BirthdateToAge(m.birthday()))
                .reduce(0d, Double::sum) / persons.size();
        System.out.println("\nDurchschnittsalter: " + avarageAge + " Jahre \n");
    }

    private static void analyseGenders(List<Map.Entry<String, Integer>> entries) {
        entries = entries.stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).toList();
        System.out.printf("| %-10s | %-6s | %-6s |%n", "Geschlecht", "Anzahl", "%");
        System.out.println("+------------+--------+--------+");
        double start = entries.stream().map(Map.Entry::getValue).reduce(0, Integer::sum);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.printf("| %-10s | %-6s | %-6s |%n", entry.getKey(), entry.getValue(), nf.format((entry.getValue() / start) * 100) + "%");
        }
    }

    private static Map<String, Integer> findGenderAmount(List<Person> persons) {
        Map<String, Integer> genders = new HashMap<>();
        for (Person person : persons) {
            genders.put(person.gender().toLowerCase(), genders.getOrDefault(person.gender().toLowerCase(), 0) + 1);
        }
        return genders;
    }

    private static void printPersonAttributes(Person person) {
        Address address = person.address();
        Job job = person.job();
        System.out.println("1 -> " + person.firstName());
        System.out.println("2 -> " + person.lastName());
        System.out.println("3 -> " + new SimpleDateFormat("yyyy.MM.dd").format(person.birthday()));
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
        System.out.printf("| %-3s | %-10s | %-10s | %-10s | %-10s | %-10s | %-10s | %-10s | %-10s | %-10s | %-15s | %-20s | %-15s | %-15s | %-6s | %-5s | %-5s | %-20s |%n", "ID", "Vorname", "Nachname", "Geburtstag", "Geschlecht", "Straße", "Hausnummer", "PLZ", "Stadt", "Land", "Telefonnummer", "Email", "Jobtitel", "Abteilung", "Gehalt", "Von", "Bis", "Notizen");
        System.out.println("+-----+------------+------------+------------+------------+------------+------------+------------+------------+------------+-----------------+----------------------+-----------------+-----------------+--------+-------+-------+");
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
            System.out.print("Geburtstag der Person (yyyy.MM.dd): ");
            Date birthday = new PersonalManagement().StringToDate(scanner.next());
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