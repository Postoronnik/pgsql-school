create schema if not exists university;

create table if not exists university.groups(
    group_id SERIAL primary key,
    group_name varchar(150) unique not null
);

create table if not exists university.students(
    student_id SERIAL primary key ,
    group_id int references university.groups(group_id) on delete cascade,
    first_name varchar(100) not null ,
    last_name varchar(150) not null
);

create table if not exists university.courses(
    course_id SERIAL primary key,
    course_name varchar(100) unique not null,
    course_description varchar(500) not null
);

create table if not exists university.courses_students(
    course_id int references university.courses(course_id) on delete cascade on update cascade,
    student_id int references  university.students(student_id) on delete cascade on update cascade
);

create or replace function university.create_student(groupId int, firstName text, lastName text) returns void as $$
    begin
    INSERT INTO university.students (group_id, first_name,last_name) VALUES (groupId,firstName,lastName);
    end;
$$language 'plpgsql';

create or replace function university.create_student(firstName text, lastName text) returns void as $$
begin
    INSERT INTO university.students (first_name,last_name) VALUES (firstName,lastName);
end;
$$language 'plpgsql';

create or replace function university.create_group(groupName text) returns void as $$
    begin
    INSERT INTO university.groups (group_name) VALUES (groupName);
    end;
$$language 'plpgsql';

create or replace function university.create_course(courseName text, courseDescription text) returns void as $$
    begin
    INSERT INTO university.courses (course_name, course_description) VALUES (courseName,courseDescription);
    end;
$$language 'plpgsql';

create or replace function university.add_student_to_course(courseId int, studentId int) returns void as $$
    begin
    INSERT INTO university.courses_students (course_id, student_id) VALUES (courseId,studentId);
    end;
$$language 'plpgsql';

create or replace function university.remove_student_from_course(courseId int, studentId int) returns void as $$
    begin
    DELETE FROM university.courses_students WHERE course_id = courseId AND student_id = studentId;
    end;
$$language 'plpgsql';

create or replace function university.get_students_related_to_course(courseName text)
    RETURNS TABLE(
                     student_id INTEGER,
                     group_id INTEGER,
                     first_name VARCHAR,
                     last_name VARCHAR
                 )
AS $$
begin
    RETURN QUERY SELECT
    s.* FROM university.courses as c
    RIGHT JOIN university.courses_students as sc
    ON c.course_id = sc.course_id LEFT JOIN university.students as s
    ON sc.student_id = s.student_id GROUP BY s.student_id, course_name HAVING c.course_name = courseName;
end;
$$language 'plpgsql';

select * from university.groups;