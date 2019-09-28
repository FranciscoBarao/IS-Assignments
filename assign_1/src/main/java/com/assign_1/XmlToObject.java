import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;

public class XmlToObject {
    private ArrayList<Owner> ToObject(){
        try{
            File file = new File("cars.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Owner.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            return (ArrayList<Owner>) jaxbUnmarshaller.unmarshal(file);

        } catch (
                JAXBException e) {e.printStackTrace();
        }
        return null;
    }
}
