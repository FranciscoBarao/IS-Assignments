package com.assign_1;

import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlTransient;

    public class Student {  
        private int id;  
        private String name;  
        private int age;  

    public Student() {}  
    public Student(int id, String name, int age) {  
        super();  
        this.id = id;  
        this.name = name;  
        this.age = age;  
    }

    //@XmlTransient
    @XmlAttribute
    public int getId() {  
        return id;  
    }  
    public void setId(int id) {  
        this.id = id;  
    }  

    @XmlElement  
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    @XmlElement  
    public int getAge() {  
        return age;  
    }  
    public void setAge(int age) {  
        this.age = age;  
    }  
}  