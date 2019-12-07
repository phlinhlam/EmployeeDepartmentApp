package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... strings) throws Exception{
        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));
        Role adminRole = roleRepository.findByRole("ADMIN");
        Role userRole = roleRepository.findByRole("USER");

        User user1 = new User("linh@email.com","password","Admin","Lam",true,"admin");
        user1.setRoles(Arrays.asList(adminRole));
        user1.setUrl("https://res.cloudinary.com/phlinhlam/image/upload/v1575757235/iuyzonvkbyqhbmq1ctjp.png");

        userRepository.save(user1);

        User user2 = new User("user@gmail.com","password","User","Last Name",true,"user");
        user2.setRoles(Arrays.asList(userRole));
        user2.setUrl("https://res.cloudinary.com/phlinhlam/image/upload/v1575757235/iuyzonvkbyqhbmq1ctjp.png");
        userRepository.save(user2);


        //add a department to database
        Department department1 = new Department();
        department1.setDeptName("Human Resource");
        departmentRepository.save(department1);
        Department department = new Department();
        department.setDeptName("Technology");
        departmentRepository.save(department);

        user1.setDepartment(department1);
        user2.setDepartment(department);

        userRepository.save(user1);
        userRepository.save(user2);
    }

}