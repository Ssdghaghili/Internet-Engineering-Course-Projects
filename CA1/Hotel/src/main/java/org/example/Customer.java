package org.example;

public class Customer {
    private int ssn;
    private String name;
    private String phone;
    private int age;

    public Customer() {}

    public Customer(int ssn, String name, String phone, int age) {
        this.ssn = ssn;
        this.name = name;
        this.phone = phone;
        this.age = age;
    }

    public int getSsn() { return ssn; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public int getAge() { return age; }
}