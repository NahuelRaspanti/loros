package com.loros.loros;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Classroom {
    public String owner;
    public String class_name;
    public Map<String, Boolean> students = new HashMap<>();
    public Map<String, Trabalengua> trabalenguas = new HashMap<>();

    public Classroom(){}

    public Classroom(String _owner,String _className, Map<String, Boolean>  _sL, Map<String, Trabalengua> _tl) {
        owner = _owner;
        class_name = _className;
        students = _sL;
        trabalenguas = _tl;
    }

    public String getOwner() {
        return owner;
    }

    public String getClass_name() {
        return class_name;
    }

    public Map<String, Boolean>  getStudents() {
        return students;
    }

    public Map<String, Trabalengua> getTrabalenguas() {
        return trabalenguas;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public void setStudents(Map<String, Boolean>  students) {
        this.students = students;
    }

    public void setTrabalenguas(Map<String, Trabalengua> trabalenguas) {
        this.trabalenguas = trabalenguas;
    }
}
