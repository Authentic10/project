package bdma.bigdata.project.rest.dao;

import bdma.bigdata.project.rest.core.Courses;
import bdma.bigdata.project.rest.core.Instructors;

import java.util.HashMap;


public class CoursesDAO {

    static public HashMap<String, String> getPercents(String m) {
        HashMap<String, String> courses;
        courses = Courses.readGrades(m);
        return courses;
    }

    static public HashMap<String, String> getCoursePercent(String id, String year) {
        HashMap<String, String> courses;
        courses = Courses.readGradesYear(id, year);

        return courses;
    }

    static public HashMap<String, Courses> getCourseProgram(String p, String year) {
        HashMap<String, Courses> courses;
        courses = Courses.readGradesProgram(p, year);

        return courses;
    }

    static public HashMap<String, Instructors> getPercentsIns(String n) {
        HashMap<String, Instructors> instructors;
        instructors = Instructors.readGrades(n);

        return instructors;
    }
}
