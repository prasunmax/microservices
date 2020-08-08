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