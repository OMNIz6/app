package com.app.util.app.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.util.app.model.Student;
import com.app.util.app.repo.StudentRepo;

@Controller
public class MainController {
    // Logger for logging
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StudentRepo sRepo;

    @RequestMapping({"","/home"})
    public String index(){
		return "index";
    }
    @RequestMapping({"/image"})
    public String uploadImage(Model model, @RequestParam("image") MultipartFile file) throws IOException {
      String fileName = file.getOriginalFilename();
      log.info("FileName: " + fileName);
      if (fileName == null || fileName.contains("..")) {
				model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
				log.info("Sorry! Filename contains invalid path sequence \" + fileName");
			}
      byte[] imageData = file.getBytes();
      Student student = new Student(fileName, imageData);
      sRepo.save(student);
      model.addAttribute("id", student.getId());
      model.addAttribute("name", student.getName());
		  return "index";
    }
    @RequestMapping("/student/image/{id}")
    public void showProductImage(@PathVariable String id, HttpServletResponse response) throws IOException {
      log.info("id is: " + id);
      response.setContentType("image/jpeg"); // Or whatever format you wanna use
      Optional<Student> student = sRepo.findById(Long.parseLong(id));
      
      InputStream is = new ByteArrayInputStream(student.get().getImage());
      IOUtils.copy(is, response.getOutputStream());
    }
}
