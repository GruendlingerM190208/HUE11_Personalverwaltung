package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class PersonalManagement {
    final String splitSymbol = ";";

    public void printMainMenu() {
        System.out.println("+--------------------------------------------------+");
        System.out.printf("| %-49s|%n", "");
        System.out.println("|              PERSONALVERWALTUNG                  |");
        System.out.printf("| %-49s|%n", "");
        System.out.printf("| %-49s|%n", "1. Neue Person hinzufügen");
        System.out.printf("| %-49s|%n", "2. Person bearbeiten ");
        System.out.printf("| %-49s|%n", "3. Person löschen");
        System.out.printf("| %-49s|%n", "4. Person suchen");
        System.out.printf("| %-49s|%n", "5. Liste aller Personen anzeigen  ");
        System.out.printf("| %-49s|%n", "6. Analysefunktionen");
        System.out.printf("| %-49s|%n", "7. beenden");
        System.out.printf("| %-49s|%n", "");
        System.out.println("+--------------------------------------------------+");
    }

    public boolean deletePerson(int id) {
        List<Person> persons = getPersonsFromFile();
        try {
            persons.remove(id - 1);
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
        printPersonsToFile(persons);
        return true;
    }

    public void addPerson(Person person) {
        List<Person> persons = getPersonsFromFile();
        persons.add(person);
        printPersonsToFile(persons);
    }

    public String serialize(Person person) {
        Address address = person.address();
        Job job = person.job();
        return splitSymbol + person.firstName() +
                splitSymbol + person.lastName() +
                splitSymbol + new SimpleDateFormat("yyyy.MM.dd").format(person.birthday()) +
                splitSymbol + person.gender() +
                splitSymbol + address.street() +
                splitSymbol + address.houseNumber() +
                splitSymbol + address.zipCode() +
                splitSymbol + address.land() +
                splitSymbol + address.country() +
                splitSymbol + person.phonenumber() +
                splitSymbol + person.email() +
                splitSymbol + job.jobtitle() +
                splitSymbol + job.department() +
                splitSymbol + job.salary() +
                splitSymbol + job.workStartTime() +
                splitSymbol + job.workEndTime() +
                splitSymbol + person.notes();
    }

    public boolean editPerson(int personId, int attributeId, String newEntry) {
        List<Person> persons = getPersonsFromFile();
        Person editingPerson;
        try {
            editingPerson = persons.get(personId - 1);
        } catch (Exception e) {
            return false;
        }
        String[] splitted = serialize(editingPerson).split(splitSymbol);
        splitted[attributeId] = newEntry;
        StringBuilder line = new StringBuilder(editingPerson.id() + splitSymbol);
        for (String s : splitted) {
            if (!s.equals("")) {
                line.append(s).append(splitSymbol);
            }
        }
        persons.set(personId - 1, deserialize(line.toString()));
        printPersonsToFile(persons);
        return true;
    }

    public List<Person> searchPersonWithAttribut(String searchingAttribut) {
        List<Person> persons = getPersonsFromFile();
        List<Person> personsWithAttribut = new ArrayList<>();
        for (Person person : persons) {
            if (serialize(person).toUpperCase().contains(searchingAttribut.toUpperCase())) {
                personsWithAttribut.add(person);
            }
        }
        return personsWithAttribut;
    }

    public void printPersonsToFile(List<Person> persons) {
        try (PrintWriter printWriter = new PrintWriter("C:/Users/micha/OneDrive/HTL/3. Klasse/POS/HUE11_Personalverwaltung/HUE11_Personalverwaltung/src/main/java/org/example/persons.txt")) {
            for (int i = 0; i < persons.size(); i++) {
                Person currentPerson = persons.get(i);
                printWriter.println((i + 1) + serialize(currentPerson));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> getPersonsFromFile() {
        List<String> serializedPersons;
        try (Stream<String> fileStream = Files.lines(Path.of("C:/Users/micha/OneDrive/HTL/3. Klasse/POS/HUE11_Personalverwaltung/HUE11_Personalverwaltung/src/main/java/org/example/persons.txt"))) {
            serializedPersons = fileStream.toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Person> persons = new ArrayList<>();
        if (!serializedPersons.isEmpty()) {
            for (String serializedPerson : serializedPersons) {
                Person person = deserialize(serializedPerson);
                if (person != null) {
                    persons.add(person);
                }
            }
        }
        return persons;
    }

    @SuppressWarnings("all")
    public Date StringToDate(String date) {
        String[] birthdaySplitted = date.split("\\.");
        return new Date(Integer.parseInt(birthdaySplitted[0]) - 1900, Integer.parseInt(birthdaySplitted[1]) - 1, Integer.parseInt(birthdaySplitted[2]));
    }

    public Person deserialize(String line) {
        String[] splitted = line.split(splitSymbol);
        try {
            return new Person(Integer.parseInt(splitted[0]),
                    splitted[1],
                    splitted[2],
                    StringToDate(splitted[3]),
                    splitted[4],
                    new Address(splitted[5],
                            Integer.parseInt(splitted[6]),
                            Integer.parseInt(splitted[7]),
                            splitted[8],
                            splitted[9]),
                    splitted[10],
                    splitted[11],
                    new Job(splitted[12],
                            splitted[13],
                            Double.parseDouble(splitted[14]),
                            splitted[15],
                            splitted[16]),
                    splitted[17]);
        } catch (Exception e) {
            System.out.println("Fehlerhafter Eintrag im Speicher gefunden!");
            return null;
        }
    }
}
