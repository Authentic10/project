package bdma.bigdata.project.rest.dao;

import bdma.bigdata.project.rest.core.Grades;
import bdma.bigdata.project.rest.core.Student;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GradesDAO {

    static private Map<String, Student> grades = null;

    static public GradesDAO create() {
        GradesDAO dao = new GradesDAO();
        if (grades == null) {
            grades = new TreeMap<>();
        }

        return dao;
    }

    static public HashMap<String, String> getPercents(String s) {
        HashMap<String, String> grades;
        grades = Grades.readGrades(s);

        return grades;
    }
}
