package mflix.api.daos;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.Map;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import mflix.api.models.Session;
import mflix.api.models.User;

public class UserDaoFromUniversity extends AbstractMFlixDao {

	  private final MongoCollection<User> usersCollection;
	  private final MongoCollection<Session> sessionsCollection;
	  private final Logger log;

	  @Autowired
	  public UserDaoFromUniversity(
	    MongoClient mongoClient, @Value("${spring.mongodb.database}") String databaseName) {
	    super(mongoClient, databaseName);
	    CodecRegistry pojoCodecRegistry =
	        fromRegistries(
	          MongoClientSettings.getDefaultCodecRegistry(),
	          fromProviders(PojoCodecProvider.builder().automatic(true).build()));

	    usersCollection = db.getCollection("users", User.class).withCodecRegistry(pojoCodecRegistry);
	    log = LoggerFactory.getLogger(this.getClass());
	    sessionsCollection =
	      db.getCollection("sessions", Session.class).withCodecRegistry(pojoCodecRegistry);
	    }
	  /*
	  * @param user - User object to be added
	  * @return True if successful, false otherwise.
	  */
	  public boolean addUser(User user) {
	      usersCollection.withWriteConcern(WriteConcern.MAJORITY).insertOne(user);
	      return true;
	    }

	  /*
	  * Creates session using userId and jwt token.
	  *
	  * @param userId - user string identifier
	  * @param jwt - jwt string token
	  * @return true if successful
	  */
	  public boolean createUserSession(String userId, String jwt) {
	    Bson updateFilter = new Document("user_id", userId);
	    Bson setUpdate = Updates.set("jwt", jwt);
	    UpdateOptions options = new UpdateOptions().upsert(true);
	    sessionsCollection.updateOne(updateFilter, setUpdate, options);
	    return true;
	    }

	  /*
	  * Returns the User object matching the an email string value.
	  *
	  * @param email - email string to be matched.
	  * @return User object or null.
	  */
	  public User getUser(String email) {
	    return usersCollection.find(new Document("email", email)).limit(1).first();
	  }

	  /*
	  * Given the userId, returns a Session object.
	  *
	  * @param userId - user string identifier.
	  * @return Session object or null.
	  */
	  public Session getUserSession(String userId) {
	    return sessionsCollection.find(new Document("user_id", userId)).limit(1).first();
	    }

	  public boolean deleteUserSessions(String userId) {
	    Document sessionDeleteFilter = new Document("user_id", userId);
	    DeleteResult res = sessionsCollection.deleteOne(sessionDeleteFilter);
	    if (res.getDeletedCount() < 1) {
	      log.warn("User `{}` could not be found in sessions collection.", userId);
	      }

	      return res.wasAcknowledged();
	    }

	  /**
	  * Removes the user document that match the provided email.
	  *
	  * @param email - of the user to be deleted.
	  * @return true if user successfully removed
	  */
	  public boolean deleteUser(String email) {
	  // remove user sessions
	    if (deleteUserSessions(email)) {
	      Document userDeleteFilter = new Document("email", email);
	      DeleteResult res = usersCollection.deleteOne(userDeleteFilter);

	      if (res.getDeletedCount() < 0) {
	        log.warn("User with `email` {} not found. Potential concurrent operation?!");
	      }

	      return res.wasAcknowledged();
	    }
	    return false;
	  }
	  
	  public boolean updateUserPreferences(String email, Map<String, ?> userPreferences){

		    // make sure to check if userPreferences are not null. If null, return false immediately.
		    if(userPreferences == null){
		        throw new IncorrectDaoOperation(
		          "userPreferences cannot be set to null");
		    }
		    // create query filter and update object.
		    Bson updateFilter = new Document("email", email);
		    Bson updateObject = Updates.set("preferences", userPreferences);
		    // update one document matching email.
		    UpdateResult res = usersCollection.updateOne(updateFilter, updateObject);
		    if(res.getModifiedCount() < 1){
		        log.warn("User `{}` was not updated. Trying to re-write the same `preferences` field: `{}`",
		                email, userPreferences);
		    }
		    return true;
		}
	}