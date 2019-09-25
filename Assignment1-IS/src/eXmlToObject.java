import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class eXmlToObject {
    public static void main(String[] args) {
        try {
            File file = new File("student.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Student.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Student e = (Student) jaxbUnmarshaller.unmarshal(file);
            System.out.println(e.getId()+" "+e.getName()+" "+e.getAge());

        } catch (JAXBException e) {e.printStackTrace(); }

    }
}