package com.assign_1;

// import java.io.File;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;
// import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;

public class ObjectToXml {  
    public static String transform(ArrayList<Owner> list) throws Exception{  
        JAXBContext contextObj = JAXBContext.newInstance(Owner.class, Car.class);  

        java.io.StringWriter sw = new StringWriter();

        Marshaller marshallerObj = contextObj.createMarshaller();  
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // FileOutputStream obj = new FileOutputStream("student.xml");
    
        marshallerObj.marshal(list, sw);

        return sw.toString();
    }  

    public static void reverse(String xml) {  
        try {      
                JAXBContext jaxbContext = JAXBContext.newInstance(Owner.class, Car.class);    

                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();    
                ArrayList<Owner> list = (ArrayList<Owner>)jaxbUnmarshaller.unmarshal(new StringReader(xml));    

                list.forEach((owner) -> System.out.println(owner.getCarsStr()));

            } catch (JAXBException e) {e.printStackTrace(); }    

    }  
}  