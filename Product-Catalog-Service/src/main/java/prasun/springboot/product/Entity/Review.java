package prasun.springboot.product.Entity;

import org.bson.codecs.pojo.annotations.BsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Review {
	@JsonProperty("_id")
	@BsonIgnore
	private int reviewId;
	private int stars;
	private String author;
	private String body;
}
