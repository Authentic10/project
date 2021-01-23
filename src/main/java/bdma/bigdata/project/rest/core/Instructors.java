package bdma.bigdata.project.rest.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Instructors {

    private String name;
    private float rate;

    protected Instructors(String n, float r){
        this.name = n;
        this.rate = r;
    }

    //Read grades based on instructor's name
    static public  HashMap<String, Instructors> readGrades(String n){

        HashMap<String, String> courses;
        courses = Instructors.getInstructorsCoursesID(n);

        HashMap<String, ArrayList<Float>> values = new HashMap<>();

            try {
                File gradeFIle = new File("/home/hadoop/IdeaProjects/project/src/main/java/grades/part-r-00000");
                Scanner scanner = new Scanner(gradeFIle);
                while (scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    String[] elements = line.split("\\s+");
                    String[] gradeId = elements[0].split("/");

                    for(String i : courses.keySet()){
                        String[] coursesID;
                        coursesID = i.split("/");

                        if(coursesID[0].equals(gradeId[3])){
                            if (!values.containsKey(i)) {
                                values.put(i, new ArrayList<>());
                            }
                            values.get(i).add(Float.parseFloat(elements[1]));
                        }
                    }
                }
                scanner.close();
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }


            HashMap<String, Instructors> results = new HashMap<>();
            float sum;
            for(String key: values.keySet()){
                sum = 0;
                int size = values.get(key).size();
                for(int i =0; i<size; i++){
                    sum+= values.get(key).get(i);
                }
                float percent = (sum)/(2000*size);

                String[] c_id = key.split("/");
                String course_name = Instructors.getCourseName(c_id[0], c_id[1]);

                results.put(key, new Instructors(course_name, percent));
            }

            return results;

    }

    //Get the course name based on the year and the course ID
    static public String getCourseName(String id, String y){
        List<Courses> courses = Instructors.readCoursesId();
        String course_name = "";
        for(Courses c : courses){
            String[] c_id = c.getCode().split("/");
            if(c_id[0].equals(id) && c_id[1].equals(y))
                course_name =  c.getName();
        }
        return course_name;
    }

    //Get courses ID
    static public List<Courses> readCoursesId(){
        List<Courses> courses = new ArrayList<>();
        try {
            File courseFile = new File("/home/hadoop/IdeaProjects/project/src/main/java/courses/part-r-00000");
            Scanner scanner = new Scanner(courseFile);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] elements = line.split("\\t");

                String[] courses_id = elements[0].split("/");

                int year = 9999 - Integer.parseInt(courses_id[1]);
                String id = courses_id[0]+"/"+year;

                Courses c = new Courses(id,elements[1]);
                courses.add(c);
            }
            scanner.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return courses;
    }

    //Get courses' ID based on an instructor's name
    static public HashMap<String, String> getInstructorsCoursesID(String n){

        HashMap<String, String> results = new HashMap<>();
        HashMap<String, ArrayList<String>> ins = Instructors.readInstructors();
        HashMap<String, String> courses = Instructors.readCourses();

        for(String i : ins.keySet()){
            if(i.equals(n)){
                int size = ins.get(i).size();
                for(int j=0; j<size; j++){
                    for(String k : courses.keySet()){
                        if(courses.get(k).equals(ins.get(i).get(j))){
                            results.put(k,ins.get(i).get(j));
                        }
                    }

                }
            }
        }

      return results;
    }

    //Get all the instructors
    static public HashMap<String, ArrayList<String>> readInstructors(){
        HashMap<String, ArrayList<String>> instructors = new HashMap<>();
        try {
            File file = new File("/home/hadoop/IdeaProjects/project/src/main/java/instructors/part-r-00000");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] elements = line.split("\\t");

                String[] ins_id = elements[0].split("/");
                String course_name = elements[1];
                String id = ins_id[0];

                if (!instructors.containsKey(id)) {
                    instructors.put(id, new ArrayList<>());
                }
                instructors.get(id).add(course_name);
            }
            scanner.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return instructors;
    }


    static public HashMap<String, String> readCourses(){
        HashMap<String, String> courses = new HashMap<>();
        try {
            File courseFile = new File("/home/hadoop/IdeaProjects/project/src/main/java/courses/part-r-00000");
            Scanner scanner = new Scanner(courseFile);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] elements = line.split("\\t");

                String[] courses_id = elements[0].split("/");
                int year = 9999 - Integer.parseInt(courses_id[1]);
                String course_name = elements[1];
                String id = courses_id[0]+"/"+year;

                courses.put(id, course_name);

            }
            scanner.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return courses;
    }

    public String getName() {
        return name;
    }

    public float getRate() {
        return rate;
    }
}
