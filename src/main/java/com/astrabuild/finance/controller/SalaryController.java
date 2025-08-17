package com.astrabuild.finance.controller;

import com.astrabuild.finance.entity.Salary;
import com.astrabuild.finance.entity.User;
import com.astrabuild.finance.repository.SalaryRepository;
import com.astrabuild.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SalaryController {

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private UserRepository userRepository;

    // Show dashboard with salary list
    @GetMapping("/accountant-dashboard")
    public String showDashboard(Model model) {
        List<Salary> salaries = salaryRepository.findAll();
        model.addAttribute("salaries", salaries);
        return "accountant-dashboard";
    }

    // Show create salary form
    @GetMapping("/salary/create")
    public String showCreateForm(Model model) {
        Salary salary = new Salary();
        List<User> users = userRepository.findAll();  // fetch all users directly from DB table
        model.addAttribute("salary", salary);
        model.addAttribute("users", users);
        return "salary-create";
    }

    // Handle create salary form submission
    @PostMapping("/salary/create")
    public String createSalary(@ModelAttribute Salary salary) {
        salaryRepository.save(salary);
        return "redirect:/accountant-dashboard";
    }

    @PostMapping("/salary/edit/{id}")
    public String updateSalary(@PathVariable("id") Long id, @ModelAttribute Salary salary) {
        Salary existingSalary = salaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid salary Id:" + id));

        // Only update amount, paymentDate, and note as requested
        existingSalary.setAmount(salary.getAmount());
        existingSalary.setPaymentDate(salary.getPaymentDate());
        existingSalary.setNote(salary.getNote());

        salaryRepository.save(existingSalary);
        return "redirect:/accountant-dashboard";
    }

    @GetMapping("/salary/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid salary Id:" + id));
        model.addAttribute("salary", salary);
        return "salary-edit";
    }

    @GetMapping("/salary/delete/{id}")
    public String deleteSalary(@PathVariable("id") Long id) {
        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid salary Id:" + id));
        salaryRepository.delete(salary);
        return "redirect:/accountant-dashboard";
    }

}
