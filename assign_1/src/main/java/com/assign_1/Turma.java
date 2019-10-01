package com.assign_1;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

    @XmlRootElement(name = "class")
    public class Turma {  
        private List<Student> students;

    public Turma() {}  
    public Turma(List<Student> students) {  
        super();  
        this.students = students;  
    }  

    public List<Student> getStudents() {  
        return students;  
    }  
    public void setStudents(List<Student> students) {  
        this.students = students;  
    } 
}  