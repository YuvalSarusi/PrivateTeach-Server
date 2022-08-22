package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.Lesson;
import com.dev.objects.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private Persist persist;

    @PostConstruct
    private void init () {

    }

    //teacher methods
    @RequestMapping(value = "check-teacher-exist", method = RequestMethod.GET)
    public String checkTeacherExist (@RequestParam String username, String token) {
        //LOGGER.info("success");
        return persist.checkTeacherExist(username,token);
    }

    @RequestMapping(value = "get-all-teachers", method = RequestMethod.GET)
    public List<Teacher> getAllTeachers () {
        //LOGGER.info("success");
        return persist.getALlTeachers();
    }

    @RequestMapping(value = "create-new-teacher", method = RequestMethod.POST)
    public String createTeacher( @RequestParam
            String username,
            String token,
            String fullName,
            String phoneNumber,
            String email,
            String price,
            String subject
    ){
        String returnedToken;
        int intPrice;
        try{
            intPrice = Integer.parseInt(price);
            returnedToken = persist.createTeacher(username,token,fullName,phoneNumber,email,intPrice,subject);
        }
        catch (NumberFormatException exception){
            returnedToken = "formatProblem";
        }
        return returnedToken;
    }

    @RequestMapping(value = "get-teacher-by-token", method = RequestMethod.GET)
    public Teacher getTeacherByToken (@RequestParam String token) {
        return persist.getTeacherByToken(token);
    }


    @RequestMapping(value = "get-teacher-past-lessons", method = RequestMethod.GET)
    public List<Lesson> getTeacherPastLessons (@RequestParam String teacherToken) {
        return persist.getTeacherPastLessons(teacherToken);
    }

    @RequestMapping(value = "get-teacher-future-lessons", method = RequestMethod.GET)
    public List<Lesson> getTeacherFutureLessons (@RequestParam String teacherToken) {
        return persist.getTeacherFutureLessons(teacherToken);
    }


    //lessons methods
    @RequestMapping(value = "add-new-lesson", method = RequestMethod.POST)
    public String addLesson(@RequestParam String startDate, String endDate, String teacherToken, String studentToken ){
        String returnedToken = "failed"; //return String so the app can get it with StringResponseListener
        Date newStartDate;
        Date newEndDate;
        try {
             newStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startDate);
             newEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endDate);
             returnedToken = persist.addLesson(newStartDate, newEndDate, teacherToken,studentToken);
        } catch (ParseException e) {
            returnedToken = "formatProblem"; //return error of format
            e.printStackTrace();
        }
        return returnedToken;
    }


    //student methods

    @RequestMapping(value = "create-new-student", method = RequestMethod.POST)
    public String createNewStudent (@RequestParam   String username,
                                          String token,
                                          String fullName,
                                          String phoneNumber,
                                          String email
                                          ) {
        return persist.createStudent(username, token, fullName, phoneNumber, email);
    }
    @RequestMapping(value = "check-student-exist", method = RequestMethod.GET)
    public String checkStudentExist (@RequestParam String username, String token) {
        //LOGGER.info("success");
        return persist.checkStudentExist(username,token);
    }

}
