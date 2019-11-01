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
    private String category;
    private String country;
    // private String photo;

    @ManyToOne
    private User user;

    public Item() {
        super();
    }

    public Item(String name, String category, String country, User user) {
        super();
        this.name = name;
        this.category = category;
        this.country = country;
        this.user = user;
    }

    public Item(int id, String name, String category, String country, User user) {
        super();
        this.id = id;
        this.name = name;
        this.category = category;
        this.country = country;
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
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", name='" + getName() + "'" + ", Category='" + getCategory() + "'"
                + ", Country='" + getCountry() + "'" + ", user='" + getUser() + "'" + "}";
    }

}