"# microservices" 
- Add Mongo DB details
- Create the flight repository in Docker

- Mongo restore command
-- mongorestore --username root --password example --gzip --archive=/tmp/sampledata.archive.gz

- Adding users
-- login to the mongo docker 
	use the command mongo -u <rootuser> -p <rootpassword>
	paste the sample command
	
	
	--------------------------------------------------------------------
	use admin;
db.createUser(
{	
	user: "mflixAppUser",
	pwd: "mflixAppPwd",
	roles:[{role: "readWrite" , db:"assignment"}]})
	
	--------------------------------------------------------------------
	
- Add Jib support for dockerising : https://www.baeldung.com/jib-dockerizing
- Put in layered approach for final docker imaging   
- JPA using OGM for flights search feature
- Normal mongo implementation for Booking engine
- User management, authentication and authorisation using jwt in a seaprate container
- Eureka registry for user authentication
- nginx for request routing  
- mvn test -Dtest=PagingTest

from Siva Prasad Reddy B to All Participants:
are we going to use Jenkins here
from Super User 042 to All Participants:
https://console.lambda.store/login
from Siva Prasad Reddy B to All Participants:
ok
from Super User 042 to All Participants:
https://www.cloudamqp.com/