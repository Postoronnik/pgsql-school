FROM openjdk:8

RUN apt-get update
RUN apt-get install -y maven

COPY ../src /usr/local/src
COPY ../pom.xml /usr/local/pom.xml

WORKDIR /usr/local

EXPOSE 5432

RUN mvn package

CMD ["java","-jar","target/sql-jdbc-school-1.0-jar-with-dependencies.jar","ua.com.foxminded.start.point.Main"]