package com.dev.objects;


import javax.persistence.*;

@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "token")
    private String token;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "email")
    private String email;
    @Column (name = "price")
    private int price;
    @Column(name = "subject")
    private String subject;


    public Teacher() {
    }

    public Teacher(String username,String token, String fullName, String phoneNumber, String email, int price, String subject) {
        this.username = username;
        this.token = token;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.price = price;
        this.subject = subject;
    }

    public Teacher(int id, String username, String token, String fullName, String phoneNumber, String email, int price, String subject) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.price = price;
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
