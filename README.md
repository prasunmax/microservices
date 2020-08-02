"# microservices" 
- Add Mongo DB details
- Create the flight repository in Docker

- Mongo restore command
-- mongorestore --username root --password example --gzip --archive=/tmp/sampledata.archive.gz

- Adding users
-- login to the mongo docker 
	use the command mongo -u <rootuser> -p <rootpassword>
	paster the sample command
db.createUser(
{	_id: "admin.mflixAppUser",
	user: "mflixAppUser",
	pwd: "mflixAppPwd",
	db: 'admin',
	roles:[{role: "readWrite" , db:"sample_mflix"}]})
