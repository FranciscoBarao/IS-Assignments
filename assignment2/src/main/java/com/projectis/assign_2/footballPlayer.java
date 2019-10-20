package com.projectis.assign_2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "footballPlayer")
public class footballPlayer {

    @Id
    @GeneratedValue
    @Column(name = "player_id", nullable = false)
    int id;
    private String name;
    private String dateBirth;
    private String position;
    private float height;

    @ManyToOne
    private Team team;

    public footballPlayer() {
        super();
    }

    public footballPlayer(String name, String dateBString, String position, float height) {
        this.name = name;
        this.dateBirth = dateBString;
        this.position = position;
        this.height = height;
    }

    public String toString() {
        return this.name + " " + this.dateBirth + " " + this.position + " " + this.height;
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

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team t) {
        this.team = t;
    }

}
