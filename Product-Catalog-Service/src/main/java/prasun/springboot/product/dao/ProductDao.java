package prasun.springboot.product.dao;

import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.BsonField;
import com.mongodb.client.model.BucketOptions;
import com.mongodb.client.model.Facet;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Variable;


@Component
public class ProductDao extends AbstractProductDao {



	public static String PRODUCT_COLLECTION = "product";

    private MongoCollection<Document> moviesCollection;

    @Autowired
    public ProductDao(
            MongoClient mongoClient, @Value("${spring.mongodb.database}") String databaseName) {
        super(mongoClient, databaseName);
        moviesCollection = db.getCollection(PRODUCT_COLLECTION);
    }

    private Bson buildLookupStage() {
    	String from = "review";
        String as = "review";

        Variable<String> let = new Variable<String>("id", "$_id");

        Document eq = Document.parse("{'$eq':['$review_id','$$id']}");
        Bson match = match( expr ( eq ));
        //Bson sort = sort( descending("date")); //fix from forum

        return lookup(
          from,                       //from
          Arrays.asList(let),         //let
          //Arrays.asList(match, sort), //pipeline
          Arrays.asList(match), //pipeline
          as                          //as
        );

    }


    /**
     * Gets a movie object from the database.
     *
     * @param productId - Movie identifier string.
     * @return Document object or null.
     */
    public Document getMovie(Integer productId) {

        List<Bson> pipeline = new ArrayList<>();
        // match stage to find movie
        Bson match = Aggregates.match(Filters.eq("_id", productId));
        pipeline.add(match);
        // Ticket: Get Comments - implement the lookup stage that allows the comments to
        // retrieved with Movies.
        Bson joinWithComments = buildLookupStage();
        pipeline.add(joinWithComments);
        Document movie = moviesCollection.aggregate(pipeline).first();

        return movie;
    }

   

    /**
     * Finds a limited amount of movies documents, for a given sort order.
     *
     * @param limit - max number of documents to be returned.
     * @param skip  - number of documents to be skipped.
     * @param sort  - result sorting criteria.
     * @return list of documents that sorted by the defined sort criteria.
     */
    public List<Document> getProducts(int limit, int skip, Bson sort) {

        List<Document> movies = new ArrayList<>();

        moviesCollection
                .find()
                .limit(limit)
                .skip(skip)
                .sort(sort)
                .iterator()
                .forEachRemaining(movies::add);

        return movies;
    }


    /**
     * Finds all movies that contain any of the `casts` members, sorted in descending by the `sortKey`
     * field.
     *
     * @param sortKey - sort key.
     * @param limit   - number of documents to be returned.
     * @param skip    - number of documents to be skipped.
     * @param cast    - cast selector.
     * @return List of documents sorted by sortKey that match the cast selector.
     */
    public List<Document> getMoviesByCast(String sortKey, int limit, int skip, String... cast) {
        // filter and sort
        Bson castFilter = Filters.in("cast",cast);
        Bson sort = Sorts.descending(sortKey);

        List<Document> movies = new ArrayList<>();
        moviesCollection
                .find(castFilter)
                .sort(sort)
                .limit(limit)
                .skip(skip)
                .iterator()
                .forEachRemaining(movies::add);
        return movies;
    }

    /**
     * Finds all movies that match the provide `genres`, sorted descending by the `sortKey` field.
     *
     * @param sortKey - sorting key string.
     * @param limit   - number of documents to be returned.
     * @param skip    - number of documents to be skipped
     * @param genres  - genres matching string vargs.
     * @return List of matching Document objects.
     */
    public List<Document> getMoviesByGenre(String sortKey, int limit, int skip, String... genres) {
        // query filter
        Bson castFilter = Filters.in("genres", genres);
        // sort key
        Bson sort = Sorts.descending(sortKey);
        List<Document> movies = new ArrayList<>();
        // Ticket: Paging - implement the necessary cursor methods to support simple
        // pagination like skip and limit in the code below
        moviesCollection.find(castFilter).sort(sort).limit(limit).skip(skip).iterator()
        .forEachRemaining(movies::add);
        return movies;
    }

    private ArrayList<Integer> runtimeBoundaries() {
        ArrayList<Integer> runtimeBoundaries = new ArrayList<>();
        runtimeBoundaries.add(0);
        runtimeBoundaries.add(60);
        runtimeBoundaries.add(90);
        runtimeBoundaries.add(120);
        runtimeBoundaries.add(180);
        return runtimeBoundaries;
    }

    private ArrayList<Integer> ratingBoundaries() {
        ArrayList<Integer> ratingBoundaries = new ArrayList<>();
        ratingBoundaries.add(0);
        ratingBoundaries.add(50);
        ratingBoundaries.add(70);
        ratingBoundaries.add(90);
        ratingBoundaries.add(100);
        return ratingBoundaries;
    }

    /**
     * This method is the java implementation of the following mongo shell aggregation pipeline {
     * "$bucket": { "groupBy": "$runtime", "boundaries": [0, 60, 90, 120, 180], "default": "other",
     * "output": { "count": {"$sum": 1} } } }
     */
    private Bson buildRuntimeBucketStage() {

        BucketOptions bucketOptions = new BucketOptions();
        bucketOptions.defaultBucket("other");
        BsonField count = new BsonField("count", new Document("$sum", 1));
        bucketOptions.output(count);
        return Aggregates.bucket("$runtime", runtimeBoundaries(), bucketOptions);
    }

    /*
    This method is the java implementation of the following mongo shell aggregation pipeline
    {
     "$bucket": {
       "groupBy": "$metacritic",
       "boundaries": [0, 50, 70, 90, 100],
       "default": "other",
       "output": {
       "count": {"$sum": 1}
       }
      }
     }
     */
    private Bson buildRatingBucketStage() {
        BucketOptions bucketOptions = new BucketOptions();
        bucketOptions.defaultBucket("other");
        BsonField count = new BsonField("count", new Document("$sum", 1));
        bucketOptions.output(count);
        return Aggregates.bucket("$metacritic", ratingBoundaries(), bucketOptions);
    }

    /**
     * This method is the java implementation of the following mongo shell aggregation pipeline
     * pipeline.aggregate([ {$match: {cast: {$in: ... }}}, {$sort: {tomatoes.viewer.numReviews: -1}},
     * {$skip: ... }, {$limit: ... }, {$facet:{ runtime: {$bucket: ...}, rating: {$bucket: ...},
     * movies: {$addFields: ...}, }} ])
     */
    public List<Document> getMoviesCastFaceted(int limit, int skip, String... cast) {
        List<Document> movies = new ArrayList<>();
        String sortKey = "tomatoes.viewer.numReviews";
        Bson skipStage = Aggregates.skip(skip);
        Bson matchStage = Aggregates.match(Filters.in("cast", cast));
        Bson sortStage = Aggregates.sort(Sorts.descending(sortKey));
        Bson limitStage = Aggregates.limit(limit);
        Bson facetStage = buildFacetStage();
        // Using a LinkedList to ensure insertion order
        List<Bson> pipeline = new LinkedList<>();

        //  Ticket: Faceted Search - build the aggregation pipeline by adding all stages in the
        // correct order
        // Your job is to order the stages correctly in the pipeline.
        // Starting with the `matchStage` add the remaining stages.
        pipeline.add(matchStage);
        pipeline.add(sortStage);
        pipeline.add(skipStage);
        pipeline.add(limitStage);
        pipeline.add(facetStage);


        moviesCollection.aggregate(pipeline).iterator().forEachRemaining(movies::add);
        return movies;
    }

    /**
     * This method is the java implementation of the following mongo shell aggregation pipeline
     * pipeline.aggregate([ ..., {$facet:{ runtime: {$bucket: ...}, rating: {$bucket: ...}, movies:
     * {$addFields: ...}, }} ])
     *
     * @return Bson defining the $facet stage.
     */
    private Bson buildFacetStage() {

        return Aggregates.facet(
                new Facet("runtime", buildRuntimeBucketStage()),
                new Facet("rating", buildRatingBucketStage()),
                new Facet("movies", Aggregates.addFields(new Field<String>("title", "$title"))));
    }

    /**
     * Counts the total amount of documents in the `movies` collection
     *
     * @return number of documents in the movies collection.
     */
    public long getMoviesCount() {
        return this.moviesCollection.countDocuments();
    }

    /**
     * Counts the number of documents matched by this text query
     *
     * @param keywords - set of keywords that match the query
     * @return number of matching documents.
     */
    public long getTextSearchCount(String keywords) {
        return this.moviesCollection.countDocuments(Filters.text(keywords));
    }

    /**
     * Counts the number of documents matched by this cast elements
     *
     * @param cast - cast string vargs.
     * @return number of matching documents.
     */
    public long getCastSearchCount(String... cast) {
        return this.moviesCollection.countDocuments(Filters.in("cast", cast));
    }

    /**
     * Counts the number of documents match genres filter.
     *
     * @param genres - genres string vargs.
     * @return number of matching documents.
     */
    public long getGenresSearchCount(String... genres) {
        return this.moviesCollection.countDocuments(Filters.in("genres", genres));
    }
}
