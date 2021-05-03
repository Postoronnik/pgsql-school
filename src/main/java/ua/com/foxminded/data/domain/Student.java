package ua.com.foxminded.data.domain;

import lombok.*;
/**
 * POJO class of student
 * @see lombok.Data
 * @see lombok.RequiredArgsConstructor
 * @see lombok.AllArgsConstructor
 * @see lombok.NoArgsConstructor
 * */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Student{
    /**
     * Id of student
     * */
    private int studentId;
    /**
     * Group of student
     * */
    private int groupId;
    /**
     * Student name
     * */
    @NonNull
    private String firstName;
    /**
     * Student surname
     * */
    @NonNull
    private String lastName;
    /**
     * Constructor for
     * @param firstName student name
     * @param lastName student surname
     * @param groupId students group id
     * */
    public Student(int groupId, String firstName, String lastName){
        this.groupId = groupId;
        this.firstName =  firstName;
        this.lastName = lastName;
    }
    /**
     * Overrided toString method
     * @return string of class
     * */
    @Override
    public String toString(){
        return "Student\n" +
                "Id: " + studentId + "\n" +
                "First name: " + firstName + "\n" +
                "Last name: " + lastName + "\n" +
                "Group id: " + groupId + "\n";
    }
}
