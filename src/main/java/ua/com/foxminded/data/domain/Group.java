package ua.com.foxminded.data.domain;

import lombok.*;

/**
 * POJO class of group
 * @see lombok.Data
 * @see lombok.RequiredArgsConstructor
 * @see lombok.AllArgsConstructor
 * @see lombok.NoArgsConstructor
 * */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    /**
     * Id of group
     * */
    private int groupId;
    /**
     * Name of group. Cannot be null
     * */
    @NonNull
    private String groupName;
    /**
     * Overrided toString method
     * @return string of class
     * */
    @Override
    public String toString(){
        return "Group\n" +
                "Name : " + groupName + "\n";
    }
}