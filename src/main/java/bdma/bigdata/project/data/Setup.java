package bdma.bigdata.project.data;

import bdma.bigdata.project.Namespace;
import bdma.bigdata.project.data.Configuration;
import bdma.bigdata.project.data.random.Course;
import bdma.bigdata.project.data.random.Instructor;
import bdma.bigdata.project.data.random.Student;
import bdma.bigdata.project.data.util.Random;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;


import java.io.IOException;
import java.util.*;

public class Setup {

    private final TreeMap<TableName, List<String>> schemaMap = new TreeMap<>();

    private Connection connection = null;

    private Setup() {
        try {
            connection = ConnectionFactory.createConnection(HBaseConfiguration.create());
        } catch (IOException e) {
            System.err.println("Failed to connect to HBase.");
            System.exit(0);
        }
        addTable(Namespace.getCourseTableName(), Namespace.getCourseFamilies());
        addTable(Namespace.getGradeTableName(), Namespace.getGradeFamilies());
        addTable(Namespace.getInstructorTableName(), Namespace.getInstructorFamilies());
        addTable(Namespace.getStudentTableName(), Namespace.getStudentFamilies());
    }

    public static void createTable(String table, String families) {
        try {
            Connection connection = ConnectionFactory.createConnection(HBaseConfiguration.create());
            Admin admin = connection.getAdmin();
            try {
                admin.createNamespace(NamespaceDescriptor.create(Namespace.get()).build());
            } catch (Exception ignored) {
            }
            TableName tableName = Namespace.getTableName(table);
            try {
                admin.disableTable(tableName);
                admin.deleteTable(tableName);
            } catch (Exception ignored) {
            }
            TableDescriptorBuilder tdb = TableDescriptorBuilder.newBuilder(tableName);
            for (String familyName : families.split(" ")) {
                ColumnFamilyDescriptorBuilder cdb = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(familyName));
                tdb.setColumnFamily(cdb.build());
            }
            admin.createTable(tdb.build());
            connection.close();
        } catch (IOException e) {
            System.err.println("Failed to create table: " + table);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        Setup setup = new Setup();
        setup.run();
    }

    private void addTable(TableName tableName, String families) {
        List<String> columnFamilies = Arrays.asList(families.split(" "));
        this.schemaMap.put(tableName, columnFamilies);
    }

    private void createTables() {
        try (Admin admin = connection.getAdmin()) {
            try {
                admin.createNamespace(NamespaceDescriptor.create(Namespace.get()).build());
            } catch (Exception ignored) {
            }
            for (TableName tableName : schemaMap.keySet()) {
                try {
                    admin.disableTable(tableName);
                    admin.deleteTable(tableName);
                } catch (Exception ignored) {
                }
                TableDescriptorBuilder tdb = TableDescriptorBuilder.newBuilder(tableName);
                for (String familyName : schemaMap.get(tableName)) {
                    ColumnFamilyDescriptorBuilder cdb = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(familyName));
                    tdb.setColumnFamily(cdb.build());
                }
                try {
                    admin.createTable(tdb.build());
                    System.out.println("Table created: " + tableName);
                } catch (Exception e) {
                    System.err.println("Failed to create table: " + tableName);
                }
            }
        } catch (Exception e) {
            System.err.println("Unknown connection error.");
        }
    }

    private void insertRows() {
        insertRowsCourse();
        insertRowsStudent();
        insertRowsInstructor();
        insertRowsGrade();
    }

    private void insertRowsCourse() {
        System.out.println("Inserting rows to table: " + Namespace.getCourseTable());
        try (Table table = connection.getTable(Namespace.getCourseTableName())) {
            for (Course course : Course.getPool()) {
                Put put = new Put(Bytes.toBytes(course.getRowKey()));
                put.addColumn(Bytes.toBytes("#"), Bytes.toBytes("N"), Bytes.toBytes(course.getName()));
                int n = 0;
                for (String instructor : course.getInstructors()) {
                    put.addColumn(Bytes.toBytes("I"), Bytes.toBytes("" + ++n), Bytes.toBytes(instructor));
                }
                table.put(put);
            }
        } catch (Exception e) {
            System.err.println("Failed to open table: " + Namespace.getCourseTableName());
        }
    }

    private void insertRowsGrade() {
        System.out.println("Inserting rows to table: " + Namespace.getGradeTable());
        try (Table table = connection.getTable(Namespace.getGradeTableName())) {
            for (Student student : Student.getPool()) {
                int y = Integer.parseInt(student.getRowKey().substring(0, 4)); // Year
                for (int p = 1; p <= Integer.parseInt(student.getProgram()); ++p) { // Program
                    y += p;
                    for (int s = p * 2 - 1; s <= p * 2; ++s) { // Semester
                        for (int i = 0; i < Configuration.numberCoursesPerYear / 2; ++i) {
                            String c;
                            c = Course.getInstance().getRowKey().split("/")[0];
                            addCourse(table, student, y, s, c);
                            c = Course.getInstance(Course.OPTIONAL).getRowKey().split("/")[0];
                            addCourse(table, student, y, s, c);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to open table: " + Namespace.getGradeTableName());
        }
    }

    private void addCourse(Table table, Student student, int y, int s, String c) throws IOException {
        String rowKey;
        Put put;
        String note;
        rowKey = y + "/" + String.format("%02d", s) + "/" + student.getRowKey() + "/" + c;
        put = new Put(Bytes.toBytes(rowKey));
        note = Random.getNumber(0, 2000, 4);
        put.addColumn(Bytes.toBytes("#"), Bytes.toBytes("G"), Bytes.toBytes(note));
        table.put(put);
    }

    private void insertRowsInstructor() {
        System.out.println("Inserting rows to table: " + Namespace.getInstructorTable());
        try (Table table = connection.getTable(Namespace.getInstructorTableName())) {
            for (Instructor instructor : Instructor.getPool()) {
                Map<Integer, Set<String>> courses = instructor.getCourses();
                if (courses.isEmpty()) {
                    continue;
                }
                for (Integer year : courses.keySet()) {
                    int n = 0;
                    String rowKey = instructor.getName() + "/" + year;
                    Put put = new Put(Bytes.toBytes(rowKey));
                    for (String course : courses.get(year)) {
                        put.addColumn(Bytes.toBytes("#"), Bytes.toBytes("" + ++n), Bytes.toBytes(course));
                    }
                    table.put(put);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to open table: " + Namespace.getInstructorTableName());
        }
    }

    private void insertRowsStudent() {
        System.out.println("Inserting rows to table: " + Namespace.getStudentTable());
        try (Table table = connection.getTable(Namespace.getStudentTableName())) {
            for (Student student : Student.getPool()) {
                Put put = new Put(Bytes.toBytes(student.getRowKey()));
                put.addColumn(Bytes.toBytes("#"), Bytes.toBytes("F"), Bytes.toBytes(student.getFirstName()));
                put.addColumn(Bytes.toBytes("#"), Bytes.toBytes("L"), Bytes.toBytes(student.getLastName()));
                put.addColumn(Bytes.toBytes("#"), Bytes.toBytes("P"), Bytes.toBytes(student.getProgram()));
                put.addColumn(Bytes.toBytes("C"), Bytes.toBytes("B"), Bytes.toBytes(student.getBirthDate()));
                put.addColumn(Bytes.toBytes("C"), Bytes.toBytes("D"), Bytes.toBytes(student.getDomicileAddress()));
                put.addColumn(Bytes.toBytes("C"), Bytes.toBytes("E"), Bytes.toBytes(student.getEmailAddress()));
                put.addColumn(Bytes.toBytes("C"), Bytes.toBytes("P"), Bytes.toBytes(student.getPhoneNumber()));
                table.put(put);
            }
        } catch (Exception e) {
            System.err.println("Failed to open table: " + Namespace.getStudentTableName());
        }
    }

    private void run() {
        createTables();
        insertRows();
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Done!");
    }
}
