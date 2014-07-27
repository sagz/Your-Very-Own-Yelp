import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class OpinionExtraction {
	public static void main(String args[]) {
		POSTagger pos = new POSTagger();
		Map<String,HashMap<String,Integer>> rest_features = pos.pruneFeatures();
		OpinionExtraction oe = new OpinionExtraction();
		HashMap<String,Sentence> sentences = oe.getOpinionSents(rest_features);
		HashMap<String,RestaurantFeature> unnormalized = oe.getSentenceScore(sentences);
		oe.normalizeScore(unnormalized);
		oe.writeResults(unnormalized);
		System.out.println("Run Complete");
	}
	
	public HashMap<String,Sentence> getOpinionSents(Map<String,HashMap<String,Integer>> rest_features) {
		HashMap<String,Sentence> sentences = new HashMap<String,Sentence>();
		StanfordNLPTool nlpTool = new StanfordNLPTool();
//		HashSet<String> adjectives = new HashSet<String>();
		HashSet<String> dict =getDictionary();
		HashMap<String,Double> sentiments = getSentiments();
		try {
			BufferedReader br = new BufferedReader(new FileReader("reviews_clustered.txt"));
			String line;
			String restId = "";
			HashMap<String,Integer> frequentFeatures = new HashMap<String,Integer>();
			while ((line = br.readLine()) != null) {
				if(line.startsWith("Restaurant_ID")) {
					restId = line;
					frequentFeatures = rest_features.get(restId);
				}
				else if(line.contains("End -----")) {
					
				}
				else if(line.trim() != "") {
					nlpTool.getParsed(line);
					ArrayList<String> all_lines = new ArrayList<String>();
					while(nlpTool.hasNextSentence()){	
						nlpTool.processNextSentence();
						all_lines.add(nlpTool.getLine());
					}
						
					for(String aline:all_lines){
						aline = aline.replaceAll("-", " ").toLowerCase();
						Sentence opinionSent;
						String sentId = restId+"/"+aline;
						for (String key : frequentFeatures.keySet() ) {
								if((aline.contains(" " + key + " ") || aline.startsWith(key + " ") || aline.endsWith(" " + key + ".")) && !isOpinionWord(key,sentiments)) {
									String[] f_parts = key.split(" ");
									if(!sentences.containsKey(sentId)){
										opinionSent = new Sentence(restId,aline);
										sentences.put(sentId, opinionSent);
									}
									else {
										opinionSent = sentences.get(sentId);
									}
	
									nlpTool.getParsed(aline);
									while(nlpTool.hasNextSentence())
									{	nlpTool.processNextSentence();
										String[] data = nlpTool.getPOS();
										int k = -1;
										for(int i=0;i<data.length;i++) {
											if(isOpinionWord(data[i],sentiments)){
												String adj = data[i].split("/")[0];
												opinionSent.opinion_words.add(adj);
//												adjectives.add(adj);
												System.out.println(adj);
												double wordSentiment = sentiments.get(adj);
												//negation check
												for(int a=1;a<2;a++){
	//												if(i+a < data.length) {
	//												String w = data[i+a].split("/")[0];
		//												if(w.equals("not") || w.contains("n't")) {
		//													reverse *= -1;
		//												}
	//												}
																	
													if(i-a >= 0) {
													String w = data[i-a].split("/")[0];
														if(w.equals("not") || w.contains("n't")) {
															wordSentiment = reversePolarity(wordSentiment);
														}
													}
												}
												opinionSent.sentiment += (wordSentiment);
												opinionSent.opinionCount++;
											}
											else if(data[i].split("/")[0].equals(f_parts[0])) {
												k = i;
											}
										}
										if(k!=-1){
											for(int i=k+1,j=k-1;i<data.length || j>=0;i++,j--){
	
												if(j>=0 && isOpinionWord(data[j],sentiments)){
													opinionSent.features.put(key, data[j].split("/")[0]);
													break;
												}
												else if(i<data.length && isOpinionWord(data[i],sentiments)) {
													opinionSent.features.put(key, data[i].split("/")[0]);
													break;
												}
											}
										}
										
									}
								}
						}	
//						if(sentences.containsKey(sentId)){
//							opinionSent = sentences.get(sentId);
//							System.out.println("--");
//						}
					}
				}
			}
			
//			PrintWriter writer = new PrintWriter("adjList.txt", "UTF-8");
//			for(String s: adjectives) {
//				writer.println(s);
//			}
//			writer.close();
			br.close();
			System.out.println("Opinion Sentences Found");
		}
		catch (FileNotFoundException e) {  
		   e.printStackTrace();  
		} 
		catch (IOException e) {  
		   e.printStackTrace();  
		}
		
		return sentences;
	}
	
	public HashMap<String,RestaurantFeature> getSentenceScore(HashMap<String,Sentence> sentences) {
		System.out.println("Finding Sentence Sentiment based on Features");
		HashMap<String,RestaurantFeature> unnormalized = new HashMap<String,RestaurantFeature>();
		HashMap<String,Double> sentiments = getSentiments();
		for(String key: sentences.keySet()) {
			Sentence sent = sentences.get(key);
			Map<String,String> sentFeatures = sent.features;
			for(String feature: sentFeatures.keySet()) {
				String effectiveOpinion = sentFeatures.get(feature).toLowerCase();
				if(!sentiments.containsKey(effectiveOpinion)) {
					System.out.println("-----------------Effective Sentiment not found for: " + effectiveOpinion);
				}
				else {
					double wordSentiment = sentiments.get(effectiveOpinion);
					String[] data = sent.text.split(" ");
					int i = -1;
					for(int j=0;j<data.length;j++) {
						if(data[j].equalsIgnoreCase(effectiveOpinion)) {
							i = j;
							break;
						}
					}
					//negation check
					for(int a=1;a<2;a++){
//						if(i+a < data.length) {
//							String w = data[i+a].toLowerCase();
//							if(w.equals("not") || w.contains("n't")) {
//								reverse *= -1;
//							}
//						}
								
						if(i-a >= 0) {
							String w = data[i-a].toLowerCase();
							if(w.equals("not") || w.contains("n't")) {
								wordSentiment = reversePolarity(wordSentiment);
							}
						}
					}
					sent.sentiment += (wordSentiment);
					String id = sent.rest_id + "/" + feature;
					RestaurantFeature rf;
					if(!unnormalized.containsKey(id)){
						rf = new RestaurantFeature(sent.rest_id,feature);
						unnormalized.put(id, rf);
					}
					else {
						rf = unnormalized.get(id);
					}
					rf.sentiment += ((sent.sentiment)/ (sent.opinionCount));
					rf.opinion_count++;
				}
			}
		}
		return unnormalized;
	}
	
	public void normalizeScore(HashMap<String,RestaurantFeature> unnormalized) {
		Double min = 1000.00;
		Double max = -1000.00;
		for(String key: unnormalized.keySet()) {
			RestaurantFeature rf = unnormalized.get(key);
			rf.sentiment /= rf.opinion_count;
				if(rf.sentiment > max) max = rf.sentiment;
				if(rf.sentiment < min) min = rf.sentiment;
		}
		
		for(String key: unnormalized.keySet()) {
			RestaurantFeature rf = unnormalized.get(key);
			double avgSentiment = rf.sentiment;
			rf.sentiment = ((avgSentiment - min)/(max-min))*10;
		}
		
	}
	
	public void writeResults(HashMap<String,RestaurantFeature> normalized) {
		HashMap<String,String> restIds = new HashMap<String,String>();
		for(String key: normalized.keySet()) {
			RestaurantFeature rf = normalized.get(key);
			restIds.put(rf.restaurant,"");
		}
		
		JSONParser parser = new JSONParser();  
			
		try {
			BufferedReader br = new BufferedReader(new FileReader("yelp_academic_dataset_business.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;  
				String businessID = (String) jsonObject.get("business_id");
				String businessName = (String) jsonObject.get("name");
				String key = "Restaurant_ID " + businessID;
					if(restIds.containsKey(key)) {
						restIds.put(key,businessName);
					}
			}
			br.close();
			
			PrintWriter writer = new PrintWriter("features_scored.txt", "UTF-8");
			for(String s: restIds.keySet()) {
//				writer.println(s);
				JSONObject rest = new JSONObject();
				JSONObject features= new JSONObject();
				String restName=s;
				for(String key: normalized.keySet()) {
					RestaurantFeature rf = normalized.get(key);
					if(s.equals(rf.restaurant)) {
//						writer.println(rf.feature + " " + rf.sentiment + " " + rf.opinion_count);
						JSONObject details = new JSONObject();
						details.put("sentiment", rf.sentiment);
						details.put("opinion_count", rf.opinion_count);
						features.put(rf.feature,details);
						restName = restIds.get(rf.restaurant);
					}
				}
				rest.put(restName,features);
				writer.println(rest.toJSONString());
//				writer.println("End -----------------------------------------------------------------------------");
				writer.flush();
			}
			writer.close();
		}
		catch (FileNotFoundException e) {  
			e.printStackTrace();  
		} 
		catch (IOException e) {  
			e.printStackTrace();  
		}
		catch (ParseException e) {  
			   e.printStackTrace();  
		} 
		
		
		
	}
	
	public static HashMap<String,Double> getSentiments() {
		HashMap<String,Double> sentiments = new HashMap<String,Double>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("adj_sentiments.txt"));
			String line;
			while ((line = br.readLine()) != null) {
	          String[] parts = line.split("\t");
	          sentiments.put(parts[0], Double.parseDouble(parts[1]));
	        }
	 	}
	 	catch (FileNotFoundException e) {  
		   e.printStackTrace();  
		} 
		catch (IOException e) {  
		   e.printStackTrace();  
		}
		return sentiments;
	}
	
	public static HashSet<String> getDictionary() {
		 HashSet<String> dict_words = new HashSet<String>();
		 File dir = new File("dictionary");
		 File[] files = dir.listFiles();
		 for(File f: files){
			 try {
					BufferedReader br = new BufferedReader(new FileReader(f));
					String line;
					while ((line = br.readLine()) != null) {
			          if(!line.startsWith("%")) {
			            dict_words.add(line);
			          }
			        }
			 	}
			 	catch (FileNotFoundException e) {  
				   e.printStackTrace();  
				} 
				catch (IOException e) {  
				   e.printStackTrace();  
				}
		 }
		 return dict_words;
	}
	
	public static boolean checkDictionary(String word,HashSet<String> dict) {
		if(dict.contains(word)) return true;
		return false;
	}
	
	public static double reversePolarity(double wordSentiment){
		if(wordSentiment == 0) return 4;
		else if(wordSentiment == 1) return 3;
		else if(wordSentiment == 3) return 1;
		else if(wordSentiment == 4) return 0;
		
		return wordSentiment;
	}
	
	public static boolean isOpinionWord(String word,HashMap<String,Double> sentiments) {
		HashSet<String> specialWords = new HashSet<String>();
		specialWords.add("hate/VB");
		specialWords.add("love/VB");
		specialWords.add("like/VB");
		specialWords.add("best/RB");
		specialWords.add("fun/NN");
		specialWords.add("bit/NN");
		
		String w = word.split("/")[0];
		if( sentiments.containsKey(w) && (word.contains("/JJ") || specialWords.contains(word)) )
			return true;
		
		return false;
	}
	
	
		
}
