package model;

import com.google.common.base.Objects;

import javax.persistence.*;

/**
 * Created by sylvain on 2/22/18.
 */
@Entity
@Table(name = "EMPLOYEE", schema = "cryptofolio", catalog = "")
public class EmployeeEntity {
    private int id;
    private String firstName;
    private String lastName;
    private Integer salary;

    public EmployeeEntity() {}
    public EmployeeEntity(String fname, String lname, int salary) {
        this.firstName = fname;
        this.lastName = lname;
        this.salary = salary;
    }

    @Id
    @Column(name = "id", nullable = false)
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
        EmployeeEntity that = (EmployeeEntity) o;
        return id == that.id &&
                Objects.equal(firstName, that.firstName) &&
                Objects.equal(lastName, that.lastName) &&
                Objects.equal(salary, that.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, firstName, lastName, salary);
    }
}
