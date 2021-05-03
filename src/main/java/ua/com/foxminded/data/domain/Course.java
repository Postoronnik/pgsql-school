package ua.com.foxminded.data.domain;

import lombok.*;

/**
 * POJO class of course
 * @see lombok.Data
 * @see lombok.RequiredArgsConstructor
 * @see lombok.AllArgsConstructor
 * @see lombok.NoArgsConstructor
 * */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    /**
     * Id of course
     * */
    private int courseID;
    /**
     * Name of course
     * */
    @NonNull
    private String courseName;
    /**
     * Surname of course
     * */
    @NonNull
    private String courseDescription;
    /**
     * Overrided toString method
     * @return string of class
     * */
    @Override
    public String toString(){
        return "Course\n" +
                "Id: " + courseID + "\n" +
                "Name: " + courseName + "\n" +
                "Description: " + courseDescription + "\n";
    }
}
