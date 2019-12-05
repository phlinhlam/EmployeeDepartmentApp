package com.example.demo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name="Department_Data")
public class Department {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column(name="dept_id")
    private long dept_id;

    @NotNull
    @Size(min = 3)
    private String deptName;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<User> users;

    public Department() {
    }

    public Department(@NotNull @Size(min = 3) String deptName) {
        this.deptName = deptName;
    }

    public long getDept_id() {
        return dept_id;
    }

    public void setDept_id(long dept_id) {
        this.dept_id = dept_id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }


    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
