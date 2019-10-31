package data;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    private String name;
    private String Category;
    private String Country;
    // private String photo;

    @ManyToOne
    private User user;

    public Item() {
        super();
    }

    public Item(int id, String name, String Category, String Country, User user) {
        this.id = id;
        this.name = name;
        this.Category = Category;
        this.Country = Country;
        this.user = user;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return this.Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public String getCountry() {
        return this.Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item id(int id) {
        this.id = id;
        return this;
    }

    public Item name(String name) {
        this.name = name;
        return this;
    }

    public Item Category(String Category) {
        this.Category = Category;
        return this;
    }

    public Item Country(String Country) {
        this.Country = Country;
        return this;
    }

    public Item user(User user) {
        this.user = user;
        return this;
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", name='" + getName() + "'" + ", Category='" + getCategory() + "'"
                + ", Country='" + getCountry() + "'" + ", user='" + getUser() + "'" + "}";
    }

}