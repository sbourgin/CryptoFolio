package model;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by sylvain on 2/24/18.
 */
@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 7610814673212268704L;

    private int id;
    private String firstName;
    private String lastName;
    private Integer salary;

    public Employee() {}

    public Employee(String fname, String lname, int salary) {
        this.firstName = fname;
        this.lastName = lname;
        this.salary = salary;
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "first_name", nullable = true, length = 20)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name", nullable = true, length = 20)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "salary", nullable = true)
    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id &&
                Objects.equal(firstName, employee.firstName) &&
                Objects.equal(lastName, employee.lastName) &&
                Objects.equal(salary, employee.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, firstName, lastName, salary);
    }
}
