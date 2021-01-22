package bdma.bigdata.project.rest.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Student {
    private String ID = "0";
    private String name = "";
    private String email = "";
    private String program = "0";
    private List<Grades> First = new ArrayList<>();
    private List<Grades> Second = new ArrayList<>();


    public Student() {
    }

    static public Student readStudentFile(String id, String p){
        Student student = new Student();
        try {
            File studentFIle = new File("/home/hadoop/IdeaProjects/project/src/main/java/students/part-r-00000");
            Scanner scanner = new Scanner(studentFIle);
            while (scanner.hasNextLine()){
                if(student.ID.equals("0")){
                    String line = scanner.nextLine();
                    String[] elements = line.split("\\s+");
                    if(elements[0].equals(id) && elements[4].equals(p)){
                        System.out.println("S1 : "+elements[4]);
                        student.ID = id;
                        student.name = elements[1]+" "+elements[2];
                        student.email = elements[3];
                        student.program = elements[4];
                    }
                } else{
                    scanner.close();
                    return student;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return student;
    }

    static public Student readGradesFile(String id, String p){
        Student student = Student.readStudentFile(id, p);
        if(student.ID.equals("0")){
            return student;
        }
        String[] semesters = semestersForProgram(p);
        try {

            File gradeFIle = new File("/home/hadoop/IdeaProjects/project/src/main/java/grades/part-r-00000");
            Scanner scanner = new Scanner(gradeFIle);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] elements = line.split("\\s+");
                if(elements[0].contains(student.ID)){
                    String[] gradeId = elements[0].split("/");
                        String c_name = Student.getCourseName(gradeId[3]);
                        if(semesters[0].equals(gradeId[1])){
                            student.First.add(new Grades(gradeId[3], c_name ,elements[1]));
                        } else if(semesters[1].equals(gradeId[1])){
                            student.Second.add(new Grades(gradeId[3], c_name,elements[1]));
                        }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return student;
    }

    static public String getCourseName(String id){
        List<Courses> courses = Student.readCourses();
        String course_name = "";
        for(Courses c : courses){
            if(c.getCode().equals(id))
                course_name =  c.getName();
        }
        return course_name;
    }
    
    static public List<Courses> readCourses(){
        List<Courses> courses = new ArrayList<>();
        try {
            File courseFile = new File("/home/hadoop/IdeaProjects/project/src/main/java/courses/part-r-00000");
            Scanner scanner = new Scanner(courseFile);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] elements = line.split("\\t");

                String[] courses_id = elements[0].split("/");
                
                Courses c = new Courses(courses_id[0],elements[1]);
                courses.add(c);
            }
            scanner.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return courses;
    }

    /*static public Student readCoursesFile(String id, String p){
        Student student = Student.readGradesFile(id, p);
        String c = student.get
        try {
            File courseFile = new File("/home/hadoop/IdeaProjects/project/src/main/java/courses/part-r-00000");
            Scanner scanner = new Scanner(courseFile);
            while (scanner.hasNextLine()){
                if(student.ID.equals("0")){
                    String line = scanner.nextLine();
                    String[] elements = line.split("\\s+");

                    if(elements[0].equals(id) && elements[4].equals(p)){
                        student.ID = id;
                        student.name = elements[1]+" "+elements[2];
                        student.email = elements[3];
                        student.program = elements[4];
                    }
                } else{
                    scanner.close();
                    return student;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return student;
    }*/

    static public String[] semestersForProgram(String p){
        String[] s = new String[2];
        switch (p){
            case "1":
                s[0]="01";
                s[1]="02";
                break;
            case "2":
                s[0]="03";
                s[1]="04";
                break;
            case "3":
                s[0]="05";
                s[1]="06";
                break;
            case "4":
                s[0]="07";
                s[1]="08";
                break;
            case "5":
                s[0]="09";
                s[1]="10";
                break;
            default:
                s[0]="0";
        }
        return s;
    }


    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProgram() {
        return program;
    }

    public List<Grades> getFirst() {
        return First;
    }

    public List<Grades> getSecond() {
        return Second;
    }
}
