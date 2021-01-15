package bdma.bigdata.project.rest.core;

import java.util.Random;

public class Student {
    private String ID = "0";
    private String name = "";
    private String program = "0";

    protected Student() {
    }

    static public Student create() {
        Student student = new Student();
        Random rand = new Random();
        student.ID = Integer.toString(2000 + rand.nextInt(18));
        student.ID += String.format("%06d", rand.nextInt(100));
        student.name = "Iam " + student.ID;
        student.program = Integer.toString(1 + rand.nextInt(5));
        return student;
    }

    static public Student create(String ID, String name, String program) {
        Student student = new Student();
        student.ID = ID;
        student.name = name;
        student.program = program;
        return student;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getProgram() {
        return program;
    }
}
