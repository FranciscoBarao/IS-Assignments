package com.projectis.assign_2;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "footballPlayer")
public class footballPlayer implements Serializable {
    private static final long serialVersionUID = 7526472295622776147L;

    @Id
    @GeneratedValue
    @Column(name = "studentid", nullable = false)
    int id;
    private String name;
    private String dateBirth;
    private String position;
    private float height;

    public footballPlayer(String name, String dateBString, String position, float height) {
        this.name = name;
        this.dateBirth = dateBString;
        this.position = position;
        this.height = height;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateBirth() {
        return this.dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
