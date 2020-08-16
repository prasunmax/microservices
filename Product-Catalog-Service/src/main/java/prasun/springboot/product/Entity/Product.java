package prasun.springboot.product.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Product {

	@JsonProperty("_id")
	private int id;
	private String name;
	private double price;
	private String description;
	private Review review;
}
