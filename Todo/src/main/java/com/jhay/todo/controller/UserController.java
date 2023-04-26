package com.jhay.todo.controller;


import com.jhay.todo.dto.TasksDTO;
import com.jhay.todo.dto.UsersDTO;
import com.jhay.todo.serviceImpl.TasksServiceImpl;
import com.jhay.todo.serviceImpl.UsersServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UsersServiceImpl usersServiceImpl;
    private final TasksServiceImpl tasksServiceImpl;

    @GetMapping("/")
    public ModelAndView viewHomePage(ModelAndView modelAndView){
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/landing")
    public ModelAndView viewLandingPage(ModelAndView modelAndView, @ModelAttribute("taskStatus") String taskStatus, @RequestParam(defaultValue = "0") int page, HttpServletRequest request){

        HttpSession session = request.getSession();
        Long user_id = (Long) session.getAttribute("user_id");
        if(session.getAttribute("user_name") == null){
            modelAndView.setViewName("index");
        }else{
            modelAndView.setViewName("landing");
            modelAndView.addObject("user_name", session.getAttribute("user_name"));
            modelAndView.addObject("taskList",tasksServiceImpl.viewAllTask(user_id, page, 3));
            modelAndView.addObject("totalTask",tasksServiceImpl.viewAllTask(user_id, page, 3).getTotalElements());
            modelAndView.addObject("pendingTask", tasksServiceImpl.getTotalTaskByStatus(user_id,"pending", page, 3));
            modelAndView.addObject("in_progressTask", tasksServiceImpl.getTotalTaskByStatus(user_id,"in_progress", page, 3));
            modelAndView.addObject("completedTask", tasksServiceImpl.getTotalTaskByStatus(user_id,"completed", page, 3));
            modelAndView.addObject("taskForm", new TasksDTO());
            modelAndView.addObject("taskStatus",taskStatus);
        }
        return modelAndView;
    }
    @GetMapping("/signup")
    public ModelAndView viewSignUpPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("userForm", new UsersDTO());
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView viewLoginPage(ModelAndView modelAndView){
        modelAndView.setViewName("login");
        modelAndView.addObject("userForm",new UsersDTO());
        return modelAndView;
    }

    @GetMapping("/logout")
    public String logoutUser(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/registerUser")
    public String registerNewUser(@Validated UsersDTO usersDTO, Model model){
        if(usersDTO.getPassword().equals(usersDTO.getConfirmPassword())){
            boolean isRegistered = usersServiceImpl.getUserRegistered(usersDTO);
            if(isRegistered){
                model.addAttribute("userStatus", "Registration Successful, Proceed to Login");
            }else{
                model.addAttribute("userStatus", "Email Already Exist");
            }
        }else{
            model.addAttribute("userStatus", "Passwords do not match");
        }
        model.addAttribute("userForm", new UsersDTO());
        return "register";
    }

    @PostMapping("/loginUser")
    public String loginUser(@Validated UsersDTO usersDTO, Model model, HttpServletRequest request){
        UsersDTO userLoggedIn = usersServiceImpl.getUserLoggedIn(usersDTO);
        if(userLoggedIn == null){
            model.addAttribute("userStatus", "Wrong Password");
            model.addAttribute("userForm", new UsersDTO());
            return "login";
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("user_id",userLoggedIn.getId());
        session.setAttribute("user_name", userLoggedIn.getFirstName());
        return "redirect:/landing";
    }

}
