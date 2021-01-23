package bdma.bigdata.project.rest.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Student {
    private String ID = "0";
    private String Name = "";
    private String Email = "";
    private String Program = "0";
    private List<Grades> First = new ArrayList<>();
    private List<Grades> Second = new ArrayList<>();


    public Student() {
    }

    //Get a student based on his ID and the program in the grades' file
    static public Student readStudentFile(String id, String p){
        Student student = new Student();
        try {
            File studentFIle = new File("/home/hadoop/IdeaProjects/project/src/main/java/students/part-r-00000");
            Scanner scanner = new Scanner(studentFIle);
            while (scanner.hasNextLine()){
                if(student.ID.equals("0")){
                    String line = scanner.nextLine();
                    String[] elements = line.split("\\s+"); //Split ID and the Grade
                    if(elements[0].equals(id) && elements[4].equals(p)){
                        student.ID = id;
                        student.Name = elements[1]+" "+elements[2];
                        student.Email = elements[3];
                        student.Program = elements[4];
                    }
                } else{
                    scanner.close();
                    return student;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return student;
    }

    //Get the grades of students based on the program
    static public Student readGradesFile(String id, String p){
        Student student = Student.readStudentFile(id, p);
        if(student.ID.equals("0")){
            return student;
        }
        //Get the semesters corresponding to the program
        String[] semesters = semestersForProgram(p);
        try {

            File gradeFIle = new File("/home/hadoop/IdeaProjects/project/src/main/java/grades/part-r-00000");
            Scanner scanner = new Scanner(gradeFIle);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] elements = line.split("\\s+");
                if(elements[0].contains(student.ID)){
                    String[] gradeId = elements[0].split("/");
                        String c_name = Student.getCourseName(gradeId[3]);
                        if(semesters[0].equals(gradeId[1])){
                            student.First.add(new Grades(gradeId[3], c_name ,elements[1]));
                        } else if(semesters[1].equals(gradeId[1])){
                            student.Second.add(new Grades(gradeId[3], c_name,elements[1]));
                        }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return student;
    }

    //Get a course name based on on the course ID
    static public String getCourseName(String id){
        List<Courses> courses = Student.readCourses();
        String course_name = "";
        for(Courses c : courses){
            if(c.getCode().equals(id))
                course_name =  c.getName();
        }
        return course_name;
    }

    //Get all the courses
    static public List<Courses> readCourses(){
        List<Courses> courses = new ArrayList<>();
        try {
            File courseFile = new File("/home/hadoop/IdeaProjects/project/src/main/java/courses/part-r-00000");
            Scanner scanner = new Scanner(courseFile);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] elements = line.split("\\t");

                String[] courses_id = elements[0].split("/");
                
                Courses c = new Courses(courses_id[0],elements[1]);
                courses.add(c);
            }
            scanner.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return courses;
    }

    //Get students ranks based on the program and the year
    static public  HashMap<String, Float> getStudentRanks(String p, String year){

        //Get each student and all their grades
        HashMap<String, ArrayList<Float>> students = new HashMap<>();
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
                                if (!students.containsKey(gradeId[2])) {
                                    students.put(gradeId[2], new ArrayList<>());
                                }
                                students.get(gradeId[2]).add(Float.parseFloat(elements[1]));
                            }
                        }
                    }
                }
                scanner.close();
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }

            //Sum each student's grades and calculate the mean
            HashMap<String, Float> results = new HashMap<>();
            float sum;
            for(String key: students.keySet()){
                sum = 0;
                int size = students.get(key).size();
                for(int i =0; i<size; i++){
                    sum+= students.get(key).get(i);
                }
                float mean = sum/size;

                results.put(key, mean);

                //Sorting the map
                results = Student.sortByValue(results, false);

            }
            return results;
        }
        return null;
    }

    //Sort a map based on the value
    static public HashMap<String, Float> sortByValue(HashMap<String, Float> map, final boolean order) {
        List<Map.Entry<String, Float>> list = new LinkedList<>(map.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }


    //Get the semesters corresponding to a program
    static public String[] semestersForProgram(String p){
        String[] s = new String[2];
        switch (p){
            case "1":
                s[0]="01";
                s[1]="02";
                break;
            case "2":
                s[0]="03";
                s[1]="04";
                break;
            case "3":
                s[0]="05";
                s[1]="06";
                break;
            case "4":
                s[0]="07";
                s[1]="08";
                break;
            case "5":
                s[0]="09";
                s[1]="10";
                break;
            default:
                s[0]="0";
        }
        return s;
    }


    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getProgram() {
        return Program;
    }

    public List<Grades> getFirst() {
        return First;
    }

    public List<Grades> getSecond() {
        return Second;
    }
}
