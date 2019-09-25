import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;

public class Owner {
    private int id;
    private String name;
    private int telephone;
    private String address;
    private ArrayList<Car> cars;

    Owner(int id, String name, int telephone, String address){
        this.cars = new ArrayList<>();
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.address = address;
    }

    public String toString(){
        String l = "";
        l = l + id + name + telephone + address;
        for(Car c : cars){
            l = l + "\n" + c.toString();
        }
        l = l + "\n";
        return l;
    }
    public void addCars(Car car){
        cars.add(car);
    }
    public ArrayList<Car> getCars(){
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
