package bdma.bigdata.project.rest.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    public List<Student> getStudents() {
        List<Student> list = new ArrayList<>();
        for (Map.Entry<String, Student> entry : students.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }
}