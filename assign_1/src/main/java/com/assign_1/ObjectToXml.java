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
import com.assign_1.*;

public class ObjectToXml {

    public static String transform(ArrayList<Owner> pre_list) throws Exception {
        OwnerList list = new OwnerList(pre_list);

        JAXBContext contextObj = JAXBContext.newInstance(list.getClass());

        java.io.StringWriter sw = new StringWriter();

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // FileOutputStream obj = new FileOutputStream("student.xml");

        marshallerObj.marshal(list, sw);

        return sw.toString();
    }

    public static void reverse(String xml) {
        try {
            OwnerList list = new OwnerList();
            JAXBContext jaxbContext = JAXBContext.newInstance(OwnerList.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            list = (OwnerList) jaxbUnmarshaller.unmarshal(new StringReader(xml));

            System.out.println(list.getOwnersStr());

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}