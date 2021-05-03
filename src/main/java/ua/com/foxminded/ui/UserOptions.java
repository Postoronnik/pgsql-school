package ua.com.foxminded.ui;

import ua.com.foxminded.data.dao.DaoException;
import ua.com.foxminded.data.dao.jdbc.JdbcCourseDao;
import ua.com.foxminded.data.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.data.dao.jdbc.JdbcStudentDao;
import ua.com.foxminded.data.domain.Course;
import ua.com.foxminded.data.domain.Group;
import ua.com.foxminded.data.domain.Student;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * @author Ind_us
 * @version 1.0
 * Class provides user options to work with university
 * @see java.lang.Thread
 * */
public class UserOptions extends Thread{

    private static final Scanner scanner = new Scanner(System.in);
    /**
     * Says thread to end session
     * */
    private boolean endSession = false;
    /**
     * Course dao options
     * */
    private final JdbcCourseDao jdbcCourse = new JdbcCourseDao();
    /**
     * Group dao options
     * */
    private final JdbcGroupDao jdbcGroup = new JdbcGroupDao();
    /**
     * Student dao options
     * */
    private final JdbcStudentDao jdbcStudent = new JdbcStudentDao();

    /**
     *Method provides selecting options while user would`t enter to exit
     * */
    @SneakyThrows
    @Override
    public void run(){
        while(!endSession){
            optionsList();
            chooseOption(enterOption());
        }
    }

    /**
     * Provides user list of options
     * */
    private void optionsList(){
        System.out.println("" +
                "1 - Find all groups with less or equals student count\n" +
                "2 - Find all students related to course with given name\n" +
                "3 - Add new student\n" +
                "4 - Delete student by STUDENT_ID\n" +
                "5 - Add a student to the course (from a list)\n" +
                "6 - Remove the student from one of his or her courses\n" +
                "7 - Show all students\n" +
                "8 - Create a group\n" +
                "9 - Create a course\n" +
                "10 - End session\n"
        );
    }
    /**
     * Finding entered option and executing specified function
     * @param option variant of option that entered user
     * @throws DaoException throws DaoException from dao layer
     * */
    private void chooseOption(int option) throws DaoException {
        switch (option){
            case 1:
                findGroupsWithLessOrEqualsStudents();
                break;
            case 2:
                findAllStudentsByCourseName();
                break;
            case 3:
                addStudent();
                break;
            case 4:
                deleteStudent();
                break;
            case 5:
                addStudentToCourse();
                break;
            case 6:
                removeStudentFromCourse();
                break;
            case 7:
                showStudents();
                break;
            case 8:
                addGroup();
                break;
            case 9:
                addCourse();
                break;
            case 10:
                shutDown();
                break;
        }
    }
    /**
     * Calling dao function for finding all groups with less or equal number of students entered by user
     * */
    private void findGroupsWithLessOrEqualsStudents(){
        System.out.println("Enter students number");
        try {
            Optional<List<Group>> optionalGroups = jdbcGroup.findAllLessOrEquals(enterNumber());
            optionalGroups.ifPresent(groups -> groups
                    .forEach(System.out::println));
        } catch (DaoException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Calling dao function for finding all students related to course entered by user
     * */
    private void findAllStudentsByCourseName(){
        System.out.println("Enter course name");
        String courseName = scanner.nextLine();
        try {
            Optional<Course> optionalCourse = jdbcCourse.getByName(courseName);
            if(optionalCourse.isPresent()){
                Optional<List<Student>> optionalStudents = jdbcStudent.getAllByCourseName(optionalCourse.get());
                optionalStudents.ifPresent(students -> students
                        .forEach(System.out::println));
            }
        } catch (DaoException e){
            System.out.println(e.getMessage());
        }
    }
    /**
     * Provides data entering of new student
     * @throws DaoException throws DaoException from dao layer
     * */
    private void addStudent() throws DaoException {
        Student student = new Student();
        System.out.println("Enter student first name:");
        student.setFirstName(scanner.nextLine());
        System.out.println("Enter student last name");
        student.setLastName(scanner.nextLine());
        System.out.println("Enter student group or press 'Enter' for skipping");
        String input;

        Optional<List<Group>> optionalGroups = new JdbcGroupDao().getAll();
        List<Group> groups = null;
        if(optionalGroups.isPresent()){
            groups = optionalGroups.get();
        }
        assert groups != null;
        int[] groupsId = new int[groups.size()];
        int i = 0;
        for(Group group : groups){
            groupsId[i] = group.getGroupId();
        }
        while (true) {
            input = scanner.nextLine();
            try {
                if (!input.equals("")) {
                    int id = Integer.parseInt(input);
                    if(id < 1){
                        throw new NumberFormatException();
                    }

                    student.setGroupId(id);

                    if(!isSuchGroupExist(student.getGroupId(),groupsId)){
                        System.out.println("No such group. Enter again");
                    } else {
                        break;
                    }
                } else {
                    break;
                }

            } catch (NumberFormatException e){
                System.out.println("Enter number bigger than 1 or 'Enter'");
            }
        }
        try {
            jdbcStudent.create(student);
        } catch (DaoException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Provides deleting existing student from list by id
     * */
     private void deleteStudent(){
        System.out.println("Enter student id to delete");
        try {
            jdbcStudent.delete(enterId());
        } catch (DaoException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Add`s student from list to course from list
     * */
    private void addStudentToCourse(){
        System.out.println("Choose course");
        try {
            Optional<List<Course>> optionalCourses = jdbcCourse.getAll();
            Optional<List<Student>> optionalStudents = jdbcStudent.getAll();

            optionalCourses.ifPresent(courses -> courses.forEach(System.out::println));
            long courseId = enterId();
            System.out.println("Choose student");
            optionalStudents.ifPresent(students -> students.forEach(System.out::println));
            long studentId = enterId();
            jdbcCourse.addStudent(courseId,studentId);
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Remove`s student from list to course from list
     * */
    private void removeStudentFromCourse(){
        System.out.println("Choose course");
        try {
            Optional<List<Course>> optionalCourses = jdbcCourse.getAll();
            Optional<List<Student>> optionalStudents = jdbcStudent.getAll();

            optionalCourses.ifPresent(courses -> courses.forEach(System.out::println));
            long courseId = enterId();
            System.out.println("Choose student");
            optionalStudents.ifPresent(students -> students.forEach(System.out::println));
            long studentId = enterId();
            jdbcCourse.removeStudent(courseId,studentId);
        } catch (DaoException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Show user all stored students
     * */
    private void showStudents(){
        try {
            Optional<List<Student>> optionalStudents = jdbcStudent.getAll();
            optionalStudents.ifPresent(students -> students.forEach(System.out::println));
        } catch (DaoException e){
            System.out.println(e.getMessage());
        }
    }
    /**
     * Create`s group with parameters that enter`s user
     * */
    private void addGroup(){
        Group group = new Group();
        System.out.println("Enter group name:");
        group.setGroupName(scanner.nextLine());

        try {
            jdbcGroup.create(group);
        } catch (DaoException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
    /**
     * Create`s course with parameters that enter`s user
     * */
    private void addCourse(){
        Course course = new Course();
        String nameInput;
        System.out.println("Enter course name:");
        do{
             nameInput = scanner.nextLine();
        }while(!isNoSuchCourseName(nameInput));
        course.setCourseName(nameInput);

        System.out.println("Enter course description:");
        course.setCourseDescription(scanner.nextLine());

        try {
            jdbcCourse.create(course);
        } catch (DaoException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Exits program
     * */
    private void shutDown(){
        endSession = true;
    }
    /**
     * Provides to enter only numbers from 1 to 10
     * @return number of option
     * */
    private int enterOption(){
        int enterValue;
        while(true){
            try
            {
                enterValue = enterNumber();
                if(enterValue < 1 || enterValue > 10){
                    throw new NumberFormatException();
                }
                break;
            } catch (NumberFormatException e){
                System.out.println("Enter number from 1 to 10");
            }
        }

        return enterValue;
    }
    /**
     * Provides to enter only numbers
     * @return valid number
     * */
    private int enterNumber(){
        int enterValue;
        while(true){
            try
            {
                enterValue = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e){
                System.out.println("Enter number");
            }
        }
        return enterValue;
    }
    /**
     * Provides to enter only numbers bigger than 1
     * @return valid id
     * */
    private long enterId(){
        long studentId;
        while (true){
            try{
                studentId = Integer.parseInt(scanner.nextLine());
                if(studentId < 1){
                    throw new NumberFormatException();
                }
                break;
            }catch (NumberFormatException e){
                System.out.println("Enter natural number bigger than 1");
            }
        }
        return studentId;
    }
    /**
     * Checks if entered course name is validate
     * @param courseName name of course
     * @return true if course name is new and false if such name is in database
     * */
    private boolean isNoSuchCourseName(String courseName){
        try {
            Optional<List<Course>> optionalCourses = jdbcCourse.getAll();
            if(optionalCourses.isPresent()){
                Optional<Course> result = optionalCourses.get()
                        .stream()
                        .filter(course -> course.getCourseName().equals(courseName))
                        .findFirst();
                if(result.isPresent()){
                    System.out.println("Course with such name is exist. Enter again");
                    return false;
                }
            }
        }catch (DaoException e){
            System.out.println(e.getMessage());
        }
        return true;
    }
    /**
     * Checks if entered group name is existing
     * @param groupsId id`s array of all groups
     * @param inputId user entered id
     * @return true if inputId is in groupsId array and false if isn`t
     * */
    private boolean isSuchGroupExist(int inputId, int[] groupsId){
        for (int j : groupsId) {
            if (inputId == j) {
                return true;
            }
        }
        return false;
    }
}
