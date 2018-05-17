package com.loros.loros;

import java.util.ArrayList;
import java.util.List;

public class User {

    public String name;
    public String role;
    public String email;
    public ArrayList<Classroom> classroomList;

    public User() {

    }

    public User (String _name, String _role, String _email) {
        name = _name;
        role = _role;
        email = _email;
        classroomList = new ArrayList<Classroom>();
    }



}
