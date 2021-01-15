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
        for (int i = 0; i < 100; ++i) {
            Student stu = Student.create();
            students.put(stu.getID(), stu);
        }
        Student stu = Student.create("2008080888", "Iam 2008080888", "5");
        students.put(stu.getID(), stu);
        return dao;
    }

    public Student getStudent(String ID) {
        if (students.containsKey(ID)) {
            return students.get(ID);
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