package com.jhay.todo.controller;

import com.jhay.todo.dto.TasksDTO;
import com.jhay.todo.serviceImpl.TasksServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class TaskController {
    private final TasksServiceImpl tasksServiceImpl;

    @PostMapping("/addTask")
    public String createNewTask(@Validated TasksDTO tasksDTO, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        Long user_id = (Long) session.getAttribute("user_id");
        boolean isAdded = tasksServiceImpl.createTask(tasksDTO,user_id);
        if(isAdded) {
            model.addAttribute("taskStatus", "Task Added Successfully");
        }else{
            model.addAttribute("taskStatus", "Task already Exist");
        }
        return "redirect:/landing";
    }

    @GetMapping("/viewAllTask")
    public ModelAndView viewAllTask(ModelAndView modelAndView, @ModelAttribute("taskStatus") String taskStatus, @RequestParam(defaultValue = "0") int page, HttpServletRequest request){
        HttpSession session = request.getSession();
        Long user_id = (Long) session.getAttribute("user_id");
        if(session.getAttribute("user_name") == null){
            modelAndView.setViewName("index");
        }else {
            modelAndView.setViewName("tasks");
            modelAndView.addObject("user_name", session.getAttribute("user_name"));
            modelAndView.addObject("taskList",tasksServiceImpl.viewAllTask(user_id, page, 3));
            modelAndView.addObject("totalTask",tasksServiceImpl.viewAllTask(user_id,page,3).getTotalElements());
            modelAndView.addObject("pendingTask", tasksServiceImpl.getTotalTaskByStatus(user_id,"pending",page,3));
            modelAndView.addObject("in_progressTask", tasksServiceImpl.getTotalTaskByStatus(user_id,"in_progress",page,3));
            modelAndView.addObject("completedTask", tasksServiceImpl.getTotalTaskByStatus(user_id,"completed",page,3));
            modelAndView.addObject("taskStatus",taskStatus);
        }
        return modelAndView;
    }
    @GetMapping("/viewStatusTask/{status}")
    public String viewStatusTask(@PathVariable String status, Model model, @ModelAttribute("taskStatus") String taskStatus, HttpServletRequest request, @RequestParam(defaultValue = "0") int page){
        HttpSession session = request.getSession();
        Long user_id = (Long) session.getAttribute("user_id");
        if(session.getAttribute("user_name") == null){
            return "index";
        }else {
            model.addAttribute("user_name", session.getAttribute("user_name"));
            model.addAttribute("totalTask",tasksServiceImpl.viewAllTask(user_id, page, 3).getTotalElements());
            model.addAttribute("pendingTask", tasksServiceImpl.getTotalTaskByStatus(user_id,"pending", page, 3));
            model.addAttribute("in_progressTask", tasksServiceImpl.getTotalTaskByStatus(user_id,"in_progress", page, 3));
            model.addAttribute("completedTask", tasksServiceImpl.getTotalTaskByStatus(user_id,"completed", page, 3));
            model.addAttribute("status",status.toUpperCase());
            model.addAttribute("taskList",tasksServiceImpl.viewAllTaskByStatus(user_id,status, page, 3));
            model.addAttribute("taskStatus", taskStatus);
            return "statusTask";
        }
    }

    @GetMapping("/viewSingleTask/{taskId}")
    public String viewSingleTask(@PathVariable Long taskId, Model model, @ModelAttribute("taskStatus") String taskStatus, HttpServletRequest request){
        model.addAttribute("task", tasksServiceImpl.viewSpecificTask(taskId));
        model.addAttribute("taskStatus", taskStatus);
        HttpSession session = request.getSession();
        model.addAttribute("user_name", session.getAttribute("user_name"));
        if(session.getAttribute("user_name") == null){
            return "index";
        }else {
            return "singleTask";
        }
    }
    @GetMapping("/viewEditTask/{taskId}")
    public String editTask(@PathVariable Long taskId, Model model, @ModelAttribute("taskStatus") String taskStatus, HttpServletRequest request){
        HttpSession session = request.getSession();
        model.addAttribute("user_name", session.getAttribute("user_name"));
        model.addAttribute("task", tasksServiceImpl.viewSpecificTask(taskId));
        model.addAttribute("taskStatus", taskStatus);
        if(session.getAttribute("user_name") == null){
            return "index";
        }else {
            return "edit";
        }
    }


    @PostMapping("/editTask/{taskId}")
    public String editTaskDetails(@PathVariable Long taskId, @Validated TasksDTO tasksDTO, RedirectAttributes redirectAttributes, HttpServletRequest request){
        String title = tasksDTO.getTitle();
        String description = tasksDTO.getDescription();
        boolean isEdited = tasksServiceImpl.editTaskTitleAndDescription(taskId,title,description);
        if(isEdited){
            redirectAttributes.addFlashAttribute("taskStatus", "Task Edited Successfully");
        }else{
            redirectAttributes.addFlashAttribute("taskStatus", "Editing Task Failed, Please Try Again Later");
        }
        String url = request.getHeader("referer");
        HttpSession session = request.getSession();
        if(session.getAttribute("user_name") == null){
            return "index";
        }else {
            return "redirect:" + url;
        }
    }


    @PostMapping("/updateStatus/{taskId}")
    public String updateTaskStatus(@PathVariable Long taskId, @RequestParam("status") String status, HttpServletRequest request){
        String url = request.getHeader("referer");
        boolean isUpdated = tasksServiceImpl.updateTaskStatus(taskId,status);
        return "redirect:"+url;
    }
    @GetMapping("/deleteTask/{taskId}")
    public String deleteTask(@PathVariable Long taskId,RedirectAttributes redirectAttributes, HttpServletRequest request){
        String url = request.getHeader("referer");
        tasksServiceImpl.deleteTask(taskId);
        redirectAttributes.addFlashAttribute("taskStatus","Task with id: "+taskId+" deleted Successfully");
        return "redirect:"+url;
    }

    @GetMapping("/searchTask")
    public String searchProduct(@ModelAttribute("keyword") String keyword,HttpServletRequest request,@ModelAttribute("taskStatus") String taskStatus, Model model, @RequestParam(defaultValue = "0") int page){
        HttpSession session = request.getSession();
        Long user_id = (Long) session.getAttribute("user_id");
        model.addAttribute("user_name", session.getAttribute("user_name"));
        model.addAttribute("totalTask",tasksServiceImpl.viewAllTask(user_id, page, 3).getTotalElements());
        model.addAttribute("pendingTask", tasksServiceImpl.getTotalTaskByStatus(user_id,"pending", page, 3));
        model.addAttribute("in_progressTask", tasksServiceImpl.getTotalTaskByStatus(user_id,"in_progress", page, 3));
        model.addAttribute("completedTask", tasksServiceImpl.getTotalTaskByStatus(user_id,"completed", page, 3));
        model.addAttribute("status", keyword);
        model.addAttribute("taskList",tasksServiceImpl.searchForTask(user_id,keyword, page, 3));
        model.addAttribute("taskStatus", taskStatus);
        if(session.getAttribute("user_name") == null){
            return "index";
        }else {
            return "searchTasks";
        }
    }
}
