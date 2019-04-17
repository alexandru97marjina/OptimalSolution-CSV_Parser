# OptimalSolution Interview Task

***
### Start application

1. ``mvn clean install``  
2. ``java -jar csvreader.war``  
3. __to run csvReader open http://localhost:8081/load__
4. __to view dataBase table with sorted Records from input file in /resources/Interview-task-data-osh.csv
   access http://localhost:8081/h2-console__  
  + __log In to devtools__  
      1. Generic H2 (Embedded)  
      2. Set JDBC URL: jdbc:h2:mem:testdb  
      3. Set User Name: sa  
  + __to check table values and Batch statistics Run SQL statements down below:__    
      ``select * from X;``  
      ``select * from BATCH_JOB_EXECUTION;``       
      ``select * from BATCH_JOB_EXECUTION_CONTEXT;``    
      ``select * from BATCH_STEP_EXECUTION;``   
      ``select * from BATCH_STEP_EXECUTION_CONTEXT;``    
5. __to view bad data from input file access output/bad-data-<timestamp>.csv__  
6. __check log file in logs/log.log__    
      * Information about application runniNg.  
      * Statistics for input file.  
        - number of received Records  
        - number of successful Records  
        - number of failed Records  
 
## Used technologies:
**SpringBoot**  
**Spring Batch**    
**Hibernate**   
**Jpa**    
**H2 Database**    
**Spring Devtools**    
    
  
    

    

