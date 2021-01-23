package bdma.bigdata.project.rest.dao;

import java.util.*;

import bdma.bigdata.project.rest.core.Student;

public class StudentDAO {

    static private Map<String, Student> students = null;

    static public StudentDAO create() {
        StudentDAO dao = new StudentDAO();
        if (students == null) {
            students = new TreeMap<>();
        }

        return dao;
    }

    public Student getStudent(String ID, String p) {
        Student stu;
        stu = Student.readGradesFile(ID, p);
        students.put(stu.getID(), stu);
        if (students.containsKey(ID)) {
            Student s = students.get(ID);
            if(s.getProgram().equals(p)){
                return s;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    static public HashMap<String, Float> getStudentsRanks(String p, String year) {
        HashMap<String, Float> students;
        students = Student.getStudentRanks(p, year);

        return students;
    }
}