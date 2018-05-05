package com.loros.loros;

import java.util.ArrayList;
import java.util.List;

public class User {

    public String name;
    public String role;
    public ArrayList<Classroom> classroomList;

    public User() {

    }

    public User (String _name, String _role) {
        name = _name;
        role = _role;
        classroomList = new ArrayList<Classroom>();
    }



}
