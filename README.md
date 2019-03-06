# SoteroTesting

Update the SQL Queries in jdbc.properties file 
  1. While Passing the sql.query there should be limit given in the query.
    Ex: SELECT * FROM SBTEST1 LIMIT 500000;
        SELECT ID, FIRST_NAME,LAST_NAME FROM INDIVIDUAL_PROFILE_SCHEMA LIMIT 100000;
        
  Note: Limit is Mandatory
  
  2. Update the api.host and api.path
  
  
  Note: Make Runnable Jars both JSONCreation and APItesting.
   
   Run JSONCreation: 
   
         Ex: java -jar JSONCreation.jar <BATCH_SIZE>
             java -jar JSONCreation.jar 10000
         Note: Batch size will start from 10000 only
         
         
   Run APITesting:       
             
         Ex: java -jar APITesting.jar <Folder_Path>
             java -jar APITesting.jar /home/ec2-user/JSONCreation/
         Note: We need to give only folder path
