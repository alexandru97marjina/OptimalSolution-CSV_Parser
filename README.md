# CSV_READER for parsing data from a csv file to H2 mem Database using ORM excluding dump data to time output/bad-data-<timestamp>.csv

***
### Start application

1. __clone repository, go to project folder and open terminal__ 
2. __run ``mvn clean install``__  
3. __run``java -jar target/csv-reader-0.0.1-SNAPSHOT.jar``__  
4. __to run csvReader open http://localhost:8081/load__
5. __to view dataBase table with sorted Records from input file in /resources/Interview-task-data-osh.csv
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
**Spring Devtools**     
**Hibernate**   
**Jpa**    
**H2 Database**    
  
    
  
    

    

