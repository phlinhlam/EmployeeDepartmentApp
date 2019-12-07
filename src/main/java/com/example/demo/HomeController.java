package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Set;

@Controller
public class HomeController {
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CloudinaryConfig cloudc;
    //register
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/register")
//    public String showRegistrationPage(Model model)
//    {
//        model.addAttribute("user", new User());
//        return "registration";
//    }
//
//    @PostMapping("/register")
//    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model)
//    {
//        model.addAttribute("user", user);
//        if(result.hasErrors()){
//            return "registration";
//        }
//        else
//        {
//            userService.saveUser(user);
//            model.addAttribute("message","User Account Created");
//        }
//        return "index";
//    }
    @RequestMapping("/")
    public String index() {
//        model.addAttribute("users", userRepository.findAll());
//        model.addAttribute("departments", departmentRepository.findAll());
        return "login";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping("/secure")
    public String secure(Principal principal, Model model) {
        String username = principal.getName();
        //show list of employees
        model.addAttribute("users", userRepository.findAll());
        //show list of departments
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("user", userRepository.findByUsername(username));
        return "secure";
    }

    @RequestMapping("/listDepartment")
    public String listDepartment(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        return "listDept";
    }

    @GetMapping("/addDepartment")
    public String deptForm(Model model) {
        model.addAttribute("department", new Department());
        return "departmentForm";
    }

    @RequestMapping("/listUser")
    public String listUser(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "listUser";
    }

    @GetMapping("/addUser")
    public String movieForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("departments", departmentRepository.findAll());
        return "userForm";
    }

    @PostMapping("/processUser")
    public String processUser(@Valid @ModelAttribute User user,
                              @RequestParam("deptid") long id,
                              @RequestParam("file") MultipartFile file, BindingResult result) {
        user.setDepartment(departmentRepository.findById(id).get());
        userRepository.save(user);
        Department department = departmentRepository.findById(id).get();
        Set<User> users = department.getUsers();
        if(!file.isEmpty())
        {
            try{
                Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
                user.setUrl(uploadResult.get("url").toString());
                userRepository.save(user);
            }catch (IOException ex){
                ex.printStackTrace();
                return "redirect:/userForm";
            }
        }
        else
        {
            userRepository.save(user);
        }
        users.add(user);
        department.setUsers(users);
        departmentRepository.save(department);
        return "redirect:/secure";
    }

    @PostMapping("/processDepartment")
    public String processDeptForm(@Valid Department department, BindingResult result) {
        if (result.hasErrors()) {
            return "departmentForm";
        }
        departmentRepository.save(department);
        return "redirect:/secure";
    }
    @PostMapping("/searchDepartment")
    public String search(Model model, @RequestParam("searchString") String search)
    {
        model.addAttribute("departments", departmentRepository.findByDeptNameContainingIgnoreCase(search));
        return "showSearch";
    }
    @RequestMapping("/detail/{dept_id}")
    public String showFlight(@PathVariable("dept_id") long deptNum, Model model)
    {
        model.addAttribute("department", departmentRepository.findById(deptNum));
        return "showSearch";
    }

    @RequestMapping("/update/{dept_id}")
    public String updateFlight(@PathVariable("dept_id") long deptNum, Model model){
        model.addAttribute("department", departmentRepository.findById(deptNum).get());
        return "departmentForm";
    }

}