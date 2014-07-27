import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class Sentence {

	String rest_id;
	String text;
	double sentiment;
	int opinionCount;
	HashSet<String> opinion_words;
	Map<String,String> features;
	
	public Sentence (String rest_id,String text) {
		this.rest_id = rest_id;
		this.text = text;
		sentiment = 0;
		opinionCount = 0;
		opinion_words = new HashSet<String>();
		features = new HashMap<String,String>();
	}
	
}
