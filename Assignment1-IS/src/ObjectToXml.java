import java.io.FileOutputStream;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class ObjectToXml {
    public static void main(String[] args) throws Exception{
        JAXBContext contextObj = JAXBContext.newInstance(Turma.class);

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        ArrayList<Student> list = new ArrayList<Student>();
        Student emp1=new Student(20112,"Alberto",21);
        Student emp2=new Student(20113,"Patricia",22);
        Student emp3=new Student(20114,"Luis",21);

        list.add(emp1);
        list.add(emp2);
        list.add(emp3);

        Turma t = new Turma(list);
        FileOutputStream obj = new FileOutputStream("student.xml");

        marshallerObj.marshal(t, obj);

    }
}