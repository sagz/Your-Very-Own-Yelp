
public class RestaurantFeature {

	String restaurant;
	String feature;
	double sentiment;
	int opinion_count;
	
	public RestaurantFeature(String restaurant,String feature) {
		this.restaurant = restaurant;
		this.feature = feature;
		sentiment = 0;
		opinion_count = 0;
	}
}
