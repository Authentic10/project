package bdma.bigdata.project.rest.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Grades {

    private String Code;
    private String Name;
    private String Grade;


    protected Grades(String c, String n, String g){
        this.Code = c;
        this.Name = n;
        this.Grade = g;
    }

    //Read the grades based on semester
    static public HashMap<String, String> readGrades(String semester){
        HashMap<String, ArrayList<Float>> values = new HashMap<>();
        try {
            File gradeFIle = new File("/home/hadoop/IdeaProjects/project/src/main/java/grades/part-r-00000");
            Scanner scanner = new Scanner(gradeFIle);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] elements = line.split("\\s+");
                String[] gradeId = elements[0].split("/");
                if(gradeId[1].equals(semester)){
                    String year = gradeId[0];
                    float grade = Float.parseFloat(elements[1]);
                    if (!values.containsKey(year)) {
                        values.put(year, new ArrayList<>());
                    }
                    values.get(year).add(grade);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        return Courses.getPercentHashMap(values);
    }

    public String getCode() {
        return Code;
    }

    public String getName() {
        return Name;
    }

    public String getGrade() {
        return Grade;
    }

    public void setCode(String code) {
        Code = code;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }
}
