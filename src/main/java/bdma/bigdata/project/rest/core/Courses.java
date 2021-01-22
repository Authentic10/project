package bdma.bigdata.project.rest.core;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Courses {

    private String Code;
    private String Name;

    protected Courses(){

    }

    protected Courses(String c, String n){
        this.Code = c;
        this.Name = n;
    }

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

}
