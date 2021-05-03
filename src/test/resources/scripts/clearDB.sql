drop table if exists university.courses_students;

drop table university.courses;

drop table university.students;

drop table university.groups;


drop function university.create_student(firstName text, lastName text);
drop function university.create_student(groupId int, firstName text, lastName text);
drop function university.create_group(groupName text);
drop function university.create_course(courseName text, courseDescription text);
drop function university.get_students_related_to_course(courseName text);
drop function  university.add_student_to_course(courseId int, studentId int);
drop function university.remove_student_from_course(courseId int, studentId int);