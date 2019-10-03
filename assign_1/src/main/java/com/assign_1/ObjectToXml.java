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
    public static void main(String[] args) {
        ArrayList<Owner> owners = new ArrayList<>();
        Owner o = new Owner(1, "Pedro", 123, "Rua Dos Buracos");
        Car c = new Car(o, 8, "bmw", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        c = new Car(o, 7, "bmw", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        owners.add(o);

        o = new Owner(2, "Hobbit", 153, "Shire");
        c = new Car(o, 6, "mini", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        owners.add(o);

        o = new Owner(3, "Gonzaga", 197, "Rua Sésamo");
        c = new Car(o, 5, "ola", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        c = new Car(o, 4, "ola", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        c = new Car(o, 3, "nocar", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        owners.add(o);

        o = new Owner(4, "Duarte", 169, "FlagTown");
        c = new Car(o, 2, "lalala", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        owners.add(o);

        o = new Owner(5, "Lucas", 188, "Madeira");
        c = new Car(o, 1, "Micra", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        owners.add(o);

        String xml = "";
        try {
            xml = ObjectToXml.transform(owners);
            ObjectToXml.reverse(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println(xml);
    }

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