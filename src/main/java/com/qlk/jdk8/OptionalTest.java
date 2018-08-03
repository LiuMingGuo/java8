package com.qlk.jdk8;

import java.util.Optional;

public class OptionalTest {
    //before
    public String getCarInsuranceName(Person person) {
        if (person == null) {
            return "Unknown";
        }
        Car car = person.getCar();
        if (car == null) {
            return "Unknown";
        }
        Insurance insurance = car.getInsurance();
        if (insurance == null) {
            return "Unknown";
        }
        return insurance.getName();
    }
    //after
    public static String getCarInsuranceName(Optional<Person> person) {
        return person.map(Person::getCar)
                .map(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("Unknown");
    }

    public static void main(String[] args) {
        System.out.println(getCarInsuranceName(Optional.ofNullable(new Person())));
    }
}
 class Person {
    private Car car;
    public Car getCar() { return car; }
}
 class Car {
    private Insurance insurance;
     public Insurance getInsurance() { return insurance; }
}
 class Insurance {
    private String name;
    public String getName() { return name; }
}