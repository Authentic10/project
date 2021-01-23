package bdma.bigdata.project.rest.core;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Courses {

    private String Code;
    private String Name;
    private float Rate;

    protected Courses(){

    }

    protected Courses(String n, float r){
        this.Rate = r;
        this.Name = n;
    }

    protected Courses(String c, String n){
        this.Code = c;
        this.Name = n;
    }

    //Read grades based on courses
    static public HashMap<String, String> readGrades(String id){
        HashMap<String, ArrayList<String>> values;
        values = Courses.readCourses();
        HashMap<String, ArrayList<Float>> courses = new HashMap<>();
        try {
            File gradeFIle = new File("/home/hadoop/IdeaProjects/project/src/main/java/grades/part-r-00000");
            Scanner scanner = new Scanner(gradeFIle);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] elements = line.split("\\s+");
                String[] gradeId = elements[0].split("/");
                if(gradeId[3].equals(id)){
                    if (values.containsKey(id)) {
                        int size = values.get(id).size();
                        for(int i =0; i<size; i++){
                            String[] els;
                            els = values.get(id).get(i).split("/");
                            if (!courses.containsKey(els[0])) {
                                courses.put(els[0], new ArrayList<>());
                            }
                            if (els[1].equals(gradeId[0])) {
                                courses.get(els[0]).add(Float.parseFloat(elements[1]));
                            }

                        }
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        return getPercentHashMap(courses);
    }

    //Calculate the percents from HashMap variable
    static HashMap<String, String> getPercentHashMap(HashMap<String, ArrayList<Float>> courses) {
        HashMap<String, String> results = new HashMap<>();
        float sum;
        for(String key: courses.keySet()){
            sum = 0;
            int size = courses.get(key).size();
            for(int i =0; i<size; i++){
                sum+= courses.get(key).get(i);
            }
            float percent = (sum)/(2000*size);

            results.put(key, String.valueOf(percent).substring(0,4));
        }

        return results;
    }

    //Read grades based on the year and  the courses
    static public HashMap<String, String> readGradesYear(String id, String year){

        HashMap<String, String> course = new HashMap<>();
        float sum = 0;
        int count = 0;
        try {
            File gradeFIle = new File("/home/hadoop/IdeaProjects/project/src/main/java/grades/part-r-00000");
            Scanner scanner = new Scanner(gradeFIle);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] elements = line.split("\\s+");
                String[] gradeId = elements[0].split("/");
                if(gradeId[3].equals(id)){
                    if(gradeId[0].equals(year)){
                        sum+= Float.parseFloat(elements[1]);
                        ++count;
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        String course_name = Student.getCourseName(id);

        if(sum!=0){
            float percent = (sum)/(2000*count);
            course.put(course_name, String.valueOf(percent).substring(0,4));
            return course;
        } else {
            return null;
        }
    }

    //Read grades based on the year and the program
    static public  HashMap<String, Courses> readGradesProgram(String p, String year){

        HashMap<String, ArrayList<Float>> courses = new HashMap<>();
        String[] semesters = Student.semestersForProgram(p);
        if(semesters.length==2){
            try {
                File gradeFIle = new File("/home/hadoop/IdeaProjects/project/src/main/java/grades/part-r-00000");
                Scanner scanner = new Scanner(gradeFIle);
                while (scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    String[] elements = line.split("\\s+");
                    String[] gradeId = elements[0].split("/");

                    if(gradeId[0].equals(year)){
                        for(int i=0; i< semesters.length; i++){
                            if(semesters[i].equals(gradeId[1])){
                                if (!courses.containsKey(gradeId[3])) {
                                    courses.put(gradeId[3], new ArrayList<>());
                                }
                                courses.get(gradeId[3]).add(Float.parseFloat(elements[1]));
                            }
                        }
                    }
                }
                scanner.close();
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }


            HashMap<String, Courses> results = new HashMap<>();
            float sum;
            for(String key: courses.keySet()){
                sum = 0;
                int size = courses.get(key).size();
                for(int i =0; i<size; i++){
                    sum+= courses.get(key).get(i);
                }
                float mean = sum/size;

                String course_name = Student.getCourseName(key);

                results.put(key, new Courses(course_name, mean));
            }
            return results;
        }
        return null;
    }

    //Get all the courses
    static public HashMap<String, ArrayList<String>> readCourses(){
        HashMap<String, ArrayList<String>> courses = new HashMap<>();
        try {
            File courseFile = new File("/home/hadoop/IdeaProjects/project/src/main/java/courses/part-r-00000");
            Scanner scanner = new Scanner(courseFile);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] elements = line.split("\\t");

                String[] courses_id = elements[0].split("/");
                int year = 9999 - Integer.parseInt(courses_id[1]);
                String note = elements[1]+"/"+year;

                if (!courses.containsKey(courses_id[0])) {
                    courses.put(courses_id[0], new ArrayList<>());
                }
                courses.get(courses_id[0]).add(note);
            }
            scanner.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return courses;
    }

    public String getCode() {
        return Code;
    }

    public String getName() {
        return Name;
    }

    public void setCode(String code) {
        Code = code;
    }

    public void setName(String name) {
        Name = name;
    }

    public float getRate() {
        return Rate;
    }

}
