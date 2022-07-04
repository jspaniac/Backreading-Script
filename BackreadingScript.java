import com.google.gson.*;

import java.util.*;
import java.io.*;

public class BackreadingScript {
    public static final boolean FRONT_HALF = false;
    public static final String FILE_PATH = "CSE_143_Users.json";

    public static void main (String[] args) {
        try {
            Gson gson = new Gson();
            String jsonString = new Scanner(new File(FILE_PATH)).nextLine();

            Users response = gson.fromJson(jsonString, Users.class);
            Student[] users = response.getUsers();
            
            Map<String, Set<Student>> sectionToStudents = buildSections(users);
            printHalf(sectionToStudents);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private static Map<String, Set<Student>> buildSections(Student[] users) {
        Map<String, Set<Student>> sectionToStudents = new TreeMap<>();
        for (Student s : users) {
            if (s.tutorial != null) {
                if (!sectionToStudents.containsKey(s.tutorial)) {
                    sectionToStudents.put(s.tutorial, new TreeSet<>());
                }
                sectionToStudents.get(s.tutorial).add(s);
            }
        }
        return sectionToStudents;
    }

    private static void printHalf(Map<String, Set<Student>> sectionToStudents) {
        for (String section : sectionToStudents.keySet()) {
            Set<Student> students = sectionToStudents.get(section);

            System.out.println("Section: " + section);

            int halfStudents = students.size() / 2;

            int start = FRONT_HALF ? 0 : students.size() - halfStudents,
                end = start + halfStudents;

            int i = 0;
            for (Student s : students) {
                if (i >= start && i < end) {
                    System.out.println(s);
                }
                i++;
            }
            System.out.println();
        }
    }

    // Wrapper class needed because of the format of the Ed JSON String
    public class Users {
        private Student[] users;

        public Users(Student[] users) {
            this.users = users;
        }

        public Student[] getUsers() {
            return users;
        }
    }

    public class Student implements Comparable<Student> {
        private String tutorial;
        private String name;
        private String[] name_separated;

        // A bunch of extra fields since they might be needed in the future
        public Student(String tutorial, String name, int id, String email, String username,
                       String course_role, int submissions, boolean completed, boolean feedback,
                       String feedback_status, int feedback_comment_count) {
            this.tutorial = tutorial;
            this.name = name;

            this.name_separated = name.split("\\s+");
        }

        public String getFirstName() {
            return name_separated[0];
        }

        public String getLastName() {
            return name_separated[name_separated.length - 1];
        }

        public String getFullName() {
            return name;
        }

        @Override
        public String toString() {
            return getFullName();
        }

        @Override
        public int compareTo(Student other) {
            return this.getFullName().compareToIgnoreCase(other.getFullName());
        }
    }
}
