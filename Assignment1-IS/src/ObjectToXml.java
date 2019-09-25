import java.io.FileOutputStream;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class ObjectToXml {
    private void ToXml(ArrayList<Owner> owners) throws Exception {
        /*
        <User id=..>
            <Car1 ..> </Car>
            <Car2 ..> </Car>
        </User>
        <User id=..>
            <Car1 ..> </Car>
        </User>
        */

        JAXBContext contextObj = JAXBContext.newInstance(Owner.class);

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        FileOutputStream obj = new FileOutputStream("cars.xml");
        marshallerObj.marshal(owners, obj);
    }
}
