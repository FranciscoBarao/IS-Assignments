package data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "country", nullable = false)
    private String country;

    @OneToMany
    private List<Item> items;

    public User() {
    }

    public User(String email, String password, String name, String country) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.country = country;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Item> getItems() {
        return this.items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public User email(String email) {
        this.email = email;
        return this;
    }

    public User password(String password) {
        this.password = password;
        return this;
    }

    public User name(String name) {
        this.name = name;
        return this;
    }

    public User country(String country) {
        this.country = country;
        return this;
    }

    public User items(List<Item> items) {
        this.items = items;
        return this;
    }

    @Override
    public String toString() {
        return "{" + " email='" + getEmail() + "'" + ", password='" + getPassword() + "'" + ", name='" + getName() + "'"
                + ", country='" + getCountry() + "'" + ", items='" + getItems() + "'" + "}";
    }

}