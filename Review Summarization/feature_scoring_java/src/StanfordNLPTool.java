import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.OriginalTextAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;


public class StanfordNLPTool {

	Properties props = null;
	StanfordCoreNLP pipeline;
	List<CoreMap> sentences;
	Iterator<CoreMap> sentIt;
	CoreMap curSentence;

	public StanfordNLPTool() {
		// creates a StanfordCoreNLP object, with POS tagging
		props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos");
		pipeline = new StanfordCoreNLP(props);
	}

	public void getParsed(String text) {
		
		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);
		sentences = document.get(SentencesAnnotation.class);
		sentIt = sentences.iterator();
	}

	public boolean hasNextSentence() {
		return sentIt.hasNext();
	}

	public void processNextSentence() {
		curSentence = sentIt.next();
	}

	public String[] getPOS() {
		List<CoreLabel> tokenList = curSentence.get(TokensAnnotation.class);
		String[] ans = new String[tokenList.size()];
		int i = 0;
		Iterator<CoreLabel> tokit = tokenList.iterator();
		while (tokit.hasNext()) {
			CoreLabel token = tokit.next();
			String pos= token.get(PartOfSpeechAnnotation.class);
			String word= token.get(OriginalTextAnnotation.class);
			ans[i]= word + "/" + pos;
			i++;
		}
		return ans;
	}
	
	public String getLine() {
		List<CoreLabel> tokenList = curSentence.get(TokensAnnotation.class);
		StringBuffer ans = new StringBuffer();
		int i = 0;
		Iterator<CoreLabel> tokit = tokenList.iterator();
		while (tokit.hasNext()) {
			CoreLabel token = tokit.next();
			String word= token.get(OriginalTextAnnotation.class);
			ans.append(word + " ");
			i++;
		}
		return ans.toString();
	}
	
	public static void main(String[] args)
	{	
		StanfordNLPTool nlpTool = new StanfordNLPTool();
		String s = "After doing a little shopping at the OTFM on Saturday, I headed over here to try it out based on the good reviews from Yelp! What a pleasant surprise! I'm not a huge pancake person, so I decided to try the Greek egg scramble with egg whites and the BF had the yogurt with fruit, since he had already downed a GF cupcake and cookie from the GF bakery. The scramble was delicious, perfectly seasoned and I ate every last bite of it! Also, the wheat toast was fantastic!! I also had a chai tea which was different, but really good! So many chai teas are overly seasoned with ginger and this one you could really taste the black tea! The BF had a latte as well and he loved it!! The only thing that wasn't awesome was the seating process. There was a definite awkward process of getting seated...not really knowing do you seat yourself, do they seat you. A more defined seating process would make this a solid 4.5 stars!!!";
			nlpTool.getParsed(s);
//			StringBuffer taggedLine = new StringBuffer();
			ArrayList<String> all_lines = new ArrayList<String>();
//			int lines = 0;
			while(nlpTool.hasNextSentence())
			{	nlpTool.processNextSentence();
//				String[] data = nlpTool.getPOS();
				all_lines.add(nlpTool.getLine());
//				for(String elem:data) {
//					taggedLine.append(elem + " ");
//					System.out.print(elem + " ");
//				}
//				System.out.println();
				System.out.println(nlpTool.getLine());
//				lines++;
			}
//			System.out.println(taggedLine);
//			System.out.println(lines);
	}
}

