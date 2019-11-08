package data;

import java.io.Serializable;
import java.util.Date;
import java.sql.Blob;

import javax.persistence.*;

@Entity
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "category", nullable = false)
    private String category;
    @Column(name = "country", nullable = false)
    private String country;
    @Column(name = "price", nullable = false)
    private int price;
    @Column(name = "date", nullable = false)
    private Date date;
    @Column(name = "photo", nullable = true)
    private Blob photo;
    @Column(name = "filename", nullable = true)
    private String filename;

    @ManyToOne
    private User user;

    public Item() {
        super();
    }

    public Item(String name, String category, String country, int price, Date date, Blob photo, String filename, User user) {
        this.name = name;
        this.category = category;
        this.country = country;
        this.price = price;
        this.date = date;
        this.photo = photo;
        this.filename = filename;
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

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Blob getPhoto() {
        return this.photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toString() {
        return this.name + " " + this.category + " " + this.country + " " + this.price + " " + this.date;
    }
}