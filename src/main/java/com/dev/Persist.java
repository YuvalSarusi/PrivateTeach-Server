package com.dev;

import com.dev.objects.Lesson;
import com.dev.objects.Student;
import com.dev.objects.Teacher;
import com.dev.utils.Utils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@Component
public class Persist {
    private Connection connection;

    private final SessionFactory sessionFactory;


    @Autowired
    public Persist (SessionFactory sf) {
        this.sessionFactory = sf;
    }


    @PostConstruct
    public void createConnectionToDatabase () {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/private_teach", "root", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //teachers methods:
    public Teacher getTeacherByUsername(String username){
        Session session = sessionFactory.openSession();
        Teacher teacher = (Teacher) session
                .createQuery("FROM Teacher t WHERE t.username = :username")
                .setParameter("username",username)
                .uniqueResult();
        return teacher;
    }
    public Teacher getTeacherByToken(String token){
        Session session = sessionFactory.openSession();
        Teacher teacher = (Teacher) session
                .createQuery("FROM Teacher t WHERE t.token = :token")
                .setParameter("token",token)
                .uniqueResult();
        return teacher;
    }
    public List<Teacher> getALlTeachers(){
        Session session = sessionFactory.openSession();
        List<Teacher> teachers = session.createQuery("FROM Teacher t")
                .list();
        return teachers;
    }
    public boolean isTeacherUsernameExist(String username){
        boolean isExist = false;
        if (this.getTeacherByUsername(username) != null){
            isExist = true;
        }
        return isExist;
    }
    public String checkTeacherExist(String username, String token){
        String answerToken;
        Teacher teacher = this.getTeacherByUsername(username);
        if (teacher == null){
            answerToken = "usernameDoesn'tExist";
        }
        else{
            if (!teacher.getToken().equals(token)){
                answerToken = "passwordWrong";
            }
            else{
                answerToken = teacher.getToken();
            }
        }
        return answerToken;
    }
    public String createTeacher(
            String username,
            String token,
            String fullName,
            String phoneNumber,
            String email,
            int price,
            String subject
             ){
        Session session = sessionFactory.openSession();
        String success = "usernameExist";
        if (!this.isTeacherUsernameExist(username)){
            Transaction transaction = session.beginTransaction();
            Teacher teacher = new Teacher(username,token,fullName,phoneNumber,email,price,subject);
            session.saveOrUpdate(teacher);
            success = teacher.getToken();
            transaction.commit();
            session.close();
        }
        return success;
    }


    //students methods:
    public Student getStudentByUsername(String username){
        Session session = sessionFactory.openSession();
        Student student = (Student) session
                .createQuery("FROM Student s WHERE s.username = :username")
                .setParameter("username",username)
                .uniqueResult();
        return student;
    }
    public Student getStudentByToken(String token){
        Session session = sessionFactory.openSession();
        Student student = (Student) session
                .createQuery("FROM Student s WHERE s.token = :token")
                .setParameter("token",token)
                .uniqueResult();
        return student;
    }

    //lessons methods:
    public Boolean addLesson(Date startDate, Date endDate, String teacherToken, String studentToken){
        Session session = sessionFactory.openSession();
        Boolean success = false;
        Teacher teacher = this.getTeacherByToken(teacherToken);
        Student student = this.getStudentByToken(studentToken);
        if (teacher != null){
            Transaction transaction = session.beginTransaction();
            Lesson lesson = new Lesson(startDate, endDate, teacher,student);
            session.saveOrUpdate(lesson);
            success = true;
            transaction.commit();
            session.close();
        }
        return success;
    }
    public List<Lesson> getAllLessons(){
        Session session = sessionFactory.openSession();
        List<Lesson> lessons = session.createQuery("FROM Lesson l")
                .list();
        for (Lesson lesson:lessons){
            lesson.setStartDateString(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(lesson.getStartDate()));
            lesson.setEndDateString(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(lesson.getEndDate()));
        }
        return lessons;
    }
    public List<Lesson> getTeacherFutureLessons(String teacherToken){
        Session session = sessionFactory.openSession();
        List<Lesson> tempLessons = session.createQuery("FROM Lesson l WHERE l.teacher.token =: token")
                .setParameter("token", teacherToken)
                .list();
        List<Lesson> lessons = new ArrayList<>();
        for (Lesson lesson : tempLessons){
            Date date = new Date();
            if (date.before(lesson.getStartDate())){
                lesson.setStartDateString(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(lesson.getStartDate()));
                lesson.setEndDateString(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(lesson.getEndDate()));
                lessons.add(lesson);
            }
        }
        return lessons;
    }
    public List<Lesson> getTeacherPastLessons(String teacherToken){
        Session session = sessionFactory.openSession();
        List<Lesson> tempLessons = session.createQuery("FROM Lesson l WHERE l.teacher.token =: token")
                .setParameter("token", teacherToken)
                .list();
        List<Lesson> lessons = new ArrayList<>();
        for (Lesson lesson : tempLessons){
            Date date = new Date();
            if (date.after(lesson.getStartDate())){
                lesson.setStartDateString(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(lesson.getStartDate()));
                lesson.setEndDateString(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(lesson.getEndDate()));
                lessons.add(lesson);
            }
        }
        return lessons;
    }
}
