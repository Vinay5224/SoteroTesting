# SoteroTesting
1. I have hard-coded the Folder path to create JSON files "home/ec2-user/JsonCreation", if you want to keep it dynamic then you can add it in properties file.
2. Just pass the queries in jdbc.properties with comma separated like
   Ex: sql.query=SELECT * FROM SBTEST1 LIMIT 100000;SELECT ID,NAME FROM INDIVIDUAL_PROFILE_SCHEMA LIMIT 2000000;SELECT * FROM SBTEST3 LIMIT 500000;
   
3. We need to pass the limit in each and every query.
4. After every query is executed and batches created automatically files will be deleted.
5. For every query there will be csv file is written the same directory, like individual query CSV files
   Ex: query1.csv --  SELECT * FROM SBTEST1 LIMIT 100000;
       query2.csv --  SELECT ID,NAME FROM INDIVIDUAL_PROFILE_SCHEMA LIMIT 2000000;
       query3.csv --  SELECT * FROM SBTEST3 LIMIT 500000;
       
6. After getting the csv files, take a backup or delete for further execution. Erasing the CSV files is not the part code, someone wants to save so it's not included.

Executing: Make Runnable jar fo JsonCreation.java and pass the arguments as batch size.
  Ex: java -jar JsonCreation.jar <BATCH_SIZE> 
      BATCH_SIZE = Should start from 10000 otherwise code will return with executing.
      
      java -jar JsonCreation.jar 10000
      java -jar JsonCreation.jar 50000
