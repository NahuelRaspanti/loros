package com.loros.loros;

import java.util.ArrayList;

public class Classroom {
    public User owner;
    public ArrayList<User> students = new ArrayList<User>();

    public Classroom(User _owner, ArrayList<User> _sL) {
        owner = _owner;
        students = _sL;
    }


}
