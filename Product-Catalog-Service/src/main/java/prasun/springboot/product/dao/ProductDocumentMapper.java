package prasun.springboot.product.dao;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prasun.springboot.product.Entity.Product;
import prasun.springboot.product.Entity.Review;
import prasun.springboot.product.util.HelperUtils;

public class ProductDocumentMapper {

    private static Logger log = LoggerFactory.getLogger(ProductDocumentMapper.class.getName());



    public static Product mapToMovie(Bson bson) {

    	Product product = new Product();
        Document document = (Document) bson;
        try {
            product.setId(HelperUtils.parseInt(document.getString("_id")));
            product.setName(document.getString("name"));
            product.setPrice(HelperUtils.parseDouble(document.get("price")));
            product.setDescription(document.getString("description"));

            if (document.containsKey("review")) {
                product.setReview(mapToReview((Document) document.get("review")));
            }
        } catch (Exception e) {
            log.warn("Unable to map document `{}` to `Movie` object: {} ", document, e.getMessage());
            log.warn("Skipping document");
        }
        return product;
    }

    private static Review mapToReview(Document document) {
    	Review review = new Review();
        if (document == null) {
            return review;
        }
    	review.setReviewId(document.getInteger("_id"));
        review.setStars(document.getInteger("stars"));
        review.setAuthor(document.getString("author"));
        review.setBody(document.getString("body"));
        return review;
    }

   
}
