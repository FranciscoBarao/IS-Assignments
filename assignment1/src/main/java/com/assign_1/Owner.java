package com.assign_1;

import java.io.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;

@XmlRootElement(name = "owner")
public class Owner implements Serializable {
    private static final long serialversionUID = 1L;
    private int id;
    private String name;
    private int telephone;
    private String address;

    @XmlElement(name = "car")
    private ArrayList<Car> cars;

    Owner(){
        this.cars = new ArrayList<>();
        this.id = 0;
        this.name = "Default";
        this.telephone = 0;
        this.address = "Default";
    }

    Owner(int id, String name, int telephone, String address) {
        this.cars = new ArrayList<>();
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.address = address;
    }

    public String toString() {
        return "Person: " + this.id + " " + this.name + " " + this.telephone + " " + this.address + "\n"
                + carsToString();
    }

    public String carsToString() {
        String ans = "Cars: ";
        for (Car c : cars) {
            ans += c.toString() + "\n";
        }
        return ans;
    }

    public void addCars(Car car) {
        cars.add(car);
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlTransient
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    @XmlTransient
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
