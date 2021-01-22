package bdma.bigdata.project.rest.dao;

import bdma.bigdata.project.rest.core.Courses;
import bdma.bigdata.project.rest.core.Student;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CoursesDAO {

    static private Map<String, Student> courses = null;

    static public CoursesDAO create() {
        CoursesDAO dao = new CoursesDAO();
        if (courses == null) {
            courses = new TreeMap<>();
        }

        return dao;
    }

    static public HashMap<String, String> getPercents(String m) {
        HashMap<String, String> courses;
        courses = Courses.readGrades(m);

        return courses;
    }
}
