package com.projectis.assign_2;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id", nullable = false)
    int id;
    private String name;
    private String address;
    private String presidentName;

    @OneToMany(targetEntity = footballPlayer.class)
    private List<footballPlayer> formerPlayers;

    public Team() {
        super();
    }

    public Team(String name, String address, String presidentName) {
        this.name = name;
        this.address = address;
        this.presidentName = presidentName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPresidentName() {
        return this.presidentName;
    }

    public void setPresidentName(String presidentName) {
        this.presidentName = presidentName;
    }

    public void setFormerPlayers(List<footballPlayer> formerPlayers) {
        this.formerPlayers = formerPlayers;
    }

    public List<footballPlayer> getFormerPlayers() {
        return formerPlayers;
    }

    @Override
    public String toString() {
        return "{" + " name='" + getName() + "'" + ", address='" + getAddress() + "'" + ", presidentName='"
                + getPresidentName() + "'" + "}";
    }

}