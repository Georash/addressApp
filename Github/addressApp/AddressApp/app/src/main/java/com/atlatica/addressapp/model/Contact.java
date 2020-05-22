package com.atlatica.addressapp.model;

import java.util.Comparator;

public class Contact {

    private int id;
    //private String image;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public Contact() {


    }

    public Contact(int id, String firstName, String lastName, String email, String phone) {
        this.id = id;
       // this.image = image;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public static final Comparator<Contact> BY_FIRSTNAME_ASCENDING = new Comparator<Contact>() {
        @Override
        public int compare(Contact o1, Contact o2) {
            //ascending
            return o1.getFirstName().compareTo(o2.getFirstName());
        }
    };


    public static final Comparator<Contact> BY_FIRSTNAME_DESCENDING = new Comparator<Contact>() {
        @Override
        public int compare(Contact o1, Contact o2) {
            //ascending
            return o2.getFirstName().compareTo(o1.getFirstName());
        }
    };

}
