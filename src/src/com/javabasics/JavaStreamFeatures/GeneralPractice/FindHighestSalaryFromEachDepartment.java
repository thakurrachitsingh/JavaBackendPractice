package com.javabasics.JavaStreamFeatures.GeneralPractice;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class FindHighestSalaryFromEachDepartment {

    public static void main(String[] args) {
        List<Employee> lst = new ArrayList<>();
        lst.add(new Employee(1, "Rachit", "front", 1000));
        lst.add(new Employee(1, "Naman", "front", 0));
        lst.add(new Employee(1, "shashank", "back", 2000));
        lst.add(new Employee(1, "Ajay", "back", 2000));
        lst.add(new Employee(1, "Vishal", "devops", 2000));
        lst.add(new Employee(1, "Harshit", "devops", 3000));

        Comparator<Employee> comparator = Comparator.comparing(Employee::getSalary);

        Map<String, Optional<Employee>> collect = lst.stream().collect(Collectors.groupingBy(Employee::getDepartment, HashMap::new,
                Collectors.reducing(BinaryOperator.maxBy(comparator))));

//        Optional<Map.Entry<String, Optional<Employee>>> max = collect.entrySet().stream().max((a, b) -> (int) (a.getValue().get().getSalary() - b.getValue().get().getSalary()));
        Optional<Long> maxxSalalryOnly = collect.entrySet().stream().map(x -> x.getValue().get().getSalary()).max((a, b) -> (int) (a - b));
        System.out.println(maxxSalalryOnly);
    }

}


class Employee{

    int id;
    String name;
    String department;
    long salary;

    public Employee(int id, String name, String department, long salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                '}';
    }
}