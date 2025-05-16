package org.example.touragency.controller;

public class Session {
    private static int userId;
    private static String firstName;
    private static String lastName;

    public static void setUser(int id, String fname, String lname) {
        userId = id;
        firstName = fname;
        lastName = lname;
    }

    public static int getUserId() {
        return userId;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void clear() {
        userId = 0;
        firstName = null;
        lastName = null;
    }
}