# MathAssistant
Technical Test for Java Basic Course by GeeksForLess

First of all, to run this program, you need to set up database. To do this, 
go to 'src/main/resources/database.properties' file and set up 3 fields: url, user and password.
Also, for tests you also need to set up 'src/test/resources/database.properties' file, like the previous one.
The tables will be created automatically as for test, and for main application.

To install the project, run following command from MathAssistant folder:

```
mvn install
```

If test database is not configured, run this instead:

```
mvn install -Dmaven.test.failure.ignore=true
```

To run program, run following command:
```
mvn exec:java -Dexec.mainClass=ua.bieliaiev.kyrylo.math_assistant.Main
```
