package mflix.lessons.utils;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

public class ActorWithStringId {
  private String id;

  private String name;
  private Date dateOfBirth;

  private List<Document> awards;
  private int numMovies;

  public ActorWithStringId() { // constructor
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public List<Document> getAwards() {
    return awards;
  }

  public void setAwards(List<Document> awards) {
    this.awards = awards;
  }

  public int getNumMovies() {
    return numMovies;
  }

  public void setNumMovies(int numMovies) {
    this.numMovies = numMovies;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ActorWithStringId withNewId() {
    setId(new ObjectId().toHexString());
    return this;
  }
}
