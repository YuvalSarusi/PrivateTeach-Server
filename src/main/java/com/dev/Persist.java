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
        return this.getTeacherByUsername(username) != null;
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
        if (!this.isTeacherUsernameExist(username) && !this.isStudentUsernameExist(username)){
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
    public boolean isStudentUsernameExist(String username){
        return getStudentByUsername(username) != null;
    }
    public String createStudent(
            String username,
            String token,
            String fullName,
            String phoneNumber,
            String email
            ){
        Session session = sessionFactory.openSession();
        String success = "usernameExist";
        if (!this.isTeacherUsernameExist(username) && !this.isStudentUsernameExist(username)){
            Transaction transaction = session.beginTransaction();
            Student student = new Student(username,token,fullName,phoneNumber,email);
            session.saveOrUpdate(student);
            success = student.getToken();
            transaction.commit();
            session.close();
        }
        return success;
    }
    public String checkStudentExist(String username, String token){
        String answerToken;
        Student student = this.getStudentByUsername(username);
        if (student == null){
            answerToken = "usernameDoesn'tExist";
        }
        else{
            if (!student.getToken().equals(token)){
                answerToken = "passwordWrong";
            }
            else{
                answerToken = student.getToken();
            }
        }
        return answerToken;
    }

    //lessons methods:
    public String addLesson(Date startDate, Date endDate, String teacherToken, String studentToken){
        Session session = sessionFactory.openSession();
        String success = null;
        Teacher teacher = this.getTeacherByToken(teacherToken);
        Student student = this.getStudentByToken(studentToken);
        if (teacher != null && startDate != null && endDate != null){ //student != null - not a problem. can be
            Lesson lesson = new Lesson(startDate, endDate, teacher,student);
            success = this.isLegalLessen(lesson);
            if (success.equals("success")){
                Transaction transaction = session.beginTransaction();
                session.saveOrUpdate(lesson);
                transaction.commit();
                session.close();
            }
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
    private String isLegalLessen(Lesson lesson){
        String response = "success";
        Date currentDate = new Date();
        //if the start date is not before end date needed to be chosen earlier start date
        if (!lesson.getStartDate().before(lesson.getEndDate())){
            response = "EarlierDate";
        }
        //if the start date is not after current date - needed to be chosen later date
        else if (!lesson.getStartDate().after(currentDate)){
            response = "LaterDate";
        }
        else{
            List<Lesson> futureLessons = getTeacherFutureLessons(lesson.getTeacher().getToken());
            for (int i=0; i<futureLessons.size() && response.equals("success"); i++ ){
                Lesson temp = futureLessons.get(i);
                if (temp.isInMiddle(lesson) != null){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    response = format.format(temp.isInMiddle(lesson));
                }
                else if (lesson.isInMiddle(lesson) != null){
                    response = "MiddleExist";
                }
            }
        }
        return response;
    }
}
