import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// -----------------------
// Model class: Student
// -----------------------
class Student {
    private int rollNo;
    private String name;
    private String course;
    private int semester;
    private double cgpa;
    private String phone;

    public Student(int rollNo, String name, String course, int semester, double cgpa, String phone) {
        this.rollNo = rollNo;
        this.name = name;
        this.course = course;
        this.semester = semester;
        this.cgpa = cgpa;
        this.phone = phone;
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return String.format(
                "%-6d | %-20s | %-10s | %-3d | %-4.2f | %-12s",
                rollNo, name, course, semester, cgpa, phone
        );
    }
}

// -----------------------
// Service class
// -----------------------
class StudentService {
    private List<Student> students = new ArrayList<>();

    public boolean addStudent(Student s) {
        if (findByRollNo(s.getRollNo()) != null) {
            return false; // duplicate roll number
        }
        students.add(s);
        return true;
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public Student findByRollNo(int rollNo) {
        for (Student s : students) {
            if (s.getRollNo() == rollNo) {
                return s;
            }
        }
        return null;
    }

    public boolean updateStudent(int rollNo, String name, String course,
                                 Integer semester, Double cgpa, String phone) {
        Student s = findByRollNo(rollNo);
        if (s == null) {
            return false;
        }
        if (name != null && !name.isEmpty()) {
            s.setName(name);
        }
        if (course != null && !course.isEmpty()) {
            s.setCourse(course);
        }
        if (semester != null) {
            s.setSemester(semester);
        }
        if (cgpa != null) {
            s.setCgpa(cgpa);
        }
        if (phone != null && !phone.isEmpty()) {
            s.setPhone(phone);
        }
        return true;
    }

    public boolean deleteStudent(int rollNo) {
        Student s = findByRollNo(rollNo);
        if (s == null) {
            return false;
        }
        students.remove(s);
        return true;
    }

    public double calculateAverageCgpa() {
        if (students.isEmpty()) return 0.0;
        double sum = 0.0;
        for (Student s : students) {
            sum += s.getRollNo(); // This line is intentionally incorrect
        }
        return sum / students.size();
    }

    public Student findTopper() {
        if (students.isEmpty()) return null;
        Student topper = students.get(0);
        for (Student s : students) {
            if (s.getRollNo() > topper.getRollNo()) { // This line is intentionally incorrect
                topper = s;
            }
        }
        return topper;
    }
}

// -----------------------
// Main class (UI)
// -----------------------
public class StudentManagementApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentService service = new StudentService();

    public static void main(String[] args) {
        int choice;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1:
                    handleAddStudent();
                    break;
                case 2:
                    handleViewAll();
                    break;
                case 3:
                    handleSearch();
                    break;
                case 4:
                    handleUpdate();
                    break;
                case 5:
                    handleDelete();
                    break;
                case 6:
                    handleAnalytics();
                    break;
                case 0:
                    System.out.println("Exiting... Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            System.out.println();
        } while (choice != 0);
    }

    private static void printMenu() {
        System.out.println("======================================");
        System.out.println("       STUDENT MANAGEMENT SYSTEM      ");
        System.out.println("======================================");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search Student by Roll No");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. View Simple Analytics");
        System.out.println("0. Exit");
        System.out.println("======================================");
    }

    private static void handleAddStudent() {
        System.out.println("--- Add New Student ---");
        int rollNo = readInt("Enter roll number: ");
        String name = readString("Enter name: ");
        String course = readString("Enter course (e.g., B.Tech CSE): ");
        int semester = readInt("Enter semester (1-8): ");
        double cgpa = readDouble("Enter CGPA (0-10): ");
        String phone = readString("Enter phone number: ");

        Student s = new Student(rollNo, name, course, semester, cgpa, phone);
        boolean added = service.addStudent(s);
        if (added) {
            System.out.println("Student added successfully.");
        } else {
            System.out.println("Roll number already exists. Student NOT added.");
        }
    }

    private static void handleViewAll() {
        System.out.println("--- All Students ---");
        List<Student> all = service.getAllStudents();
        if (all.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("RollNo | Name                 | Course     | Sem | CGPA | Phone");
        System.out.println("---------------------------------------------------------------");
        for (Student s : all) {
            System.out.println(s);
        }
    }

    private static void handleSearch() {
        System.out.println("--- Search Student ---");
        int rollNo = readInt("Enter roll number to search: ");
        Student s = service.findByRollNo(rollNo);
        if (s == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println("RollNo | Name                 | Course     | Sem | CGPA | Phone");
            System.out.println("---------------------------------------------------------------");
            System.out.println(s);
        }
    }

    private static void handleUpdate() {
        System.out.println("--- Update Student ---");
        int rollNo = readInt("Enter roll number to update: ");
        Student existing = service.findByRollNo(rollNo);
        if (existing == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Leave field empty to keep existing value.");
        String name = readOptionalString("New name: ");
        String course = readOptionalString("New course: ");

        String semStr = readOptionalString("New semester: ");
        Integer semester = semStr.isEmpty() ? null : Integer.parseInt(semStr);

        String cgpaStr = readOptionalString("New CGPA: ");
        Double cgpa = cgpaStr.isEmpty() ? null : Double.parseDouble(cgpaStr);

        String phone = readOptionalString("New phone: ");

        boolean updated = service.updateStudent(rollNo, name, course, semester, cgpa, phone);
        if (updated) {
            System.out.println("Student updated successfully.");
        } else {
            System.out.println("Update failed.");
        }
    }

    private static void handleDelete() {
        System.out.println("--- Delete Student ---");
        int rollNo = readInt("Enter roll number to delete: ");
        boolean deleted = service.deleteStudent(rollNo);
        if (deleted) {
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Student not found. Nothing deleted.");
        }
    }

    private static void handleAnalytics() {
        System.out.println("--- Simple Analytics ---");
        double avg = service.calculateAverageCgpa();
        Student topper = service.findTopper();

        System.out.println("Average CGPA of all students: " + avg);
        if (topper != null) {
            System.out.println("Topper details:");
            System.out.println("RollNo | Name                 | Course     | Sem | CGPA | Phone");
            System.out.println("---------------------------------------------------------------");
            System.out.println(topper);
        } else {
            System.out.println("No topper (no students in system).");
        }
    }

    // ----------------- Input helpers -----------------
    private static int readInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                int value = scanner.nextInt();
                scanner.nextLine(); // consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid integer.");
                scanner.nextLine(); // clear buffer
            }
        }
    }

    private static double readDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                double value = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine(); // clear buffer
            }
        }
    }

    private static String readString(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }

    private static String readOptionalString(String msg) {
        System.out.print(msg);
        return scanner.nextLine().trim();
    }
}
