package com.dev.objects;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Transient
    private String startDateString;
    @Transient
    private String endDateString;


    @ManyToOne
    @JoinColumn(name = "teacher")
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "student")
    private Student student;

    /*
    create 2 String that will contain the start&end dates formats by yyyy-MM-dd HH:mm
    that will give easy access to formidable string date at the client
    every time we will change startDate or endDate value in an object, we will change the string as well
    */
    public Lesson(int id, Date startDate, Date endDate, Teacher teacher, Student student) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teacher = teacher;
        this.student = student;
        this.startDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(startDate);
        this.endDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(endDate);
    }

    public Lesson(Date startDate, Date endDate, Teacher teacher, Student student) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.teacher = teacher;
        this.student = student;
        this.startDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(startDate);
        this.endDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(endDate);
    }

    public Lesson() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
        this.startDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(startDate);
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        this.endDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(endDate);
    }

    public String getStartDateString() {
        return startDateString;
    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    public String getEndDateString() {
        return endDateString;
    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }

    //check if lesson start or end date is in the middle of this lesson. true - is in the middle
    public Date isInMiddle(Lesson lesson){
        Date date;
        //check if start date not in the middle of exist lesson
        boolean startDateWrong = (lesson.getStartDate().after(this.getStartDate())
                &&
                lesson.getStartDate().before(this.getEndDate()));
        //check if end date not in the middle of exist lesson
        boolean endDateWrong = (lesson.getEndDate().after(this.getStartDate())
                &&
                lesson.getEndDate().before(this.getEndDate()));
        if (startDateWrong)
            date = lesson.getStartDate();
        else if (endDateWrong)
            date = lesson.getEndDate();
        else
            date = null;
        return date;
    }
}
