// Use this Donor class implementation:
package main;

import java.time.LocalDate;

import javafx.beans.property.*;

public class Donor {
    // Properties
    private final IntegerProperty id;
    private final StringProperty name;
    private final IntegerProperty age;
    private final StringProperty bloodGroup;
    private final StringProperty city;
    private final StringProperty contact;
    private final ObjectProperty<LocalDate> lastDonationDate;

    public Donor(int id, String name, int age, String bloodGroup, String city,
                 String contact, LocalDate lastDonationDate) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
        this.bloodGroup = new SimpleStringProperty(bloodGroup);
        this.city = new SimpleStringProperty(city);
        this.contact = new SimpleStringProperty(contact);
        this.lastDonationDate = new SimpleObjectProperty<>(lastDonationDate);
    }

    // ID property methods
    public IntegerProperty idProperty() {
        return id;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    // Name property methods
    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    // Age property methods
    public IntegerProperty ageProperty() {
        return age;
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    // Blood group property methods
    public StringProperty bloodGroupProperty() {
        return bloodGroup;
    }

    public String getBloodGroup() {
        return bloodGroup.get();
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup.set(bloodGroup);
    }

    // City property methods
    public StringProperty cityProperty() {
        return city;
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    // Contact property methods
    public StringProperty contactProperty() {
        return contact;
    }

    public String getContact() {
        return contact.get();
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    // Last donation date property methods
    public ObjectProperty<LocalDate> lastDonationDateProperty() {
        return lastDonationDate;
    }

    public LocalDate getLastDonationDate() {
        return lastDonationDate.get();
    }

    public void setLastDonationDate(LocalDate lastDonationDate) {
        this.lastDonationDate.set(lastDonationDate);
    }
}