import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class POSTagger {

	public Map<String,ArrayList<String>> tagReviews() {
		Map<String,ArrayList<String>> reviews = new HashMap<String,ArrayList<String>>();
		StanfordNLPTool nlpTool = new StanfordNLPTool();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("reviews_clustered.txt"));
			PrintWriter writer = new PrintWriter("reviews_postagged.txt", "UTF-8");
			String line;
			String restId = "";
			ArrayList<String> texts = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				if(line.startsWith("Restaurant_ID")) {
					restId = line.split(" ")[1];
					texts = new ArrayList<String>();
					writer.println(line);
				}
				else if(line.startsWith("End -----")) {
					reviews.put(restId, texts);
//					System.out.println(restId + " " + texts.size());
					writer.println(texts.size() + " " +line);
				}
				else if(line.trim() != "") {
					nlpTool.getParsed(line);
					StringBuffer taggedLine = new StringBuffer();
					while(nlpTool.hasNextSentence())
					{	nlpTool.processNextSentence();
						String[] data = nlpTool.getPOS();
						for(String elem:data)
							taggedLine.append(elem + " ");
					}
					texts.add(taggedLine.toString());
					writer.println(taggedLine.toString());
				}
			}
			
			br.close();
			writer.close();
			System.out.println("Reviews Read!!");
		}
		catch (FileNotFoundException e) {  
		   e.printStackTrace();  
		} 
		catch (IOException e) {  
		   e.printStackTrace();  
		}
		
		return reviews;
	}
	
	public Map<String,ArrayList<String>> getNounPhrases(){
		Map<String,ArrayList<String>> noun_phrases = new HashMap<String,ArrayList<String>>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("reviews_postagged.txt"));
			PrintWriter writer = new PrintWriter("nounphrases.txt", "UTF-8");
			String line;
			String restId = "";
			ArrayList<String> nouns = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				if(line.startsWith("Restaurant_ID")) {
					restId = line.split(" ")[1];
					nouns = new ArrayList<String>();
					writer.println(line);
				}
				else if(line.contains("End -----")) {
					noun_phrases.put(restId, nouns);
					writer.println(line);
				}
				else if(line.trim() != "") {
					String[] words = line.split(" ");
					boolean begin =false;
					StringBuffer phrase = new StringBuffer();
					for(String w:words) {
						String[] parts = w.split("/");
						if(parts.length < 2) {
//							System.out.println("error");
						}
						else{
							if(!begin && parts[1].startsWith("NN")) {
								begin = true;
								phrase = new StringBuffer();
								phrase.append(parts[0] + " ");
							}
							else if(parts[1].startsWith("NN")) {
								phrase.append(parts[0] + " ");
							}
							else {
								if(begin) {
									nouns.add(phrase.toString().trim());
									writer.println(phrase.toString().trim().toLowerCase());
								}
								begin = false;
							}
						}
						
					}
				}
			}
			
			br.close();
			writer.close();
			System.out.println("Noun Phrases Found!!");
		}
		catch (FileNotFoundException e) {  
		   e.printStackTrace();  
		} 
		catch (IOException e) {  
		   e.printStackTrace();  
		}
		
		return noun_phrases;
	}
	
	public void getNounPhrasesSupport(){
		Map<String,Integer> noun_phrases = new HashMap<String,Integer>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("nounphrases.txt"));
			PrintWriter writer = new PrintWriter("nounphrases_support.txt", "UTF-8");
			String line;
			while ((line = br.readLine()) != null) {
				if(line.startsWith("Restaurant_ID")) {
					noun_phrases = new HashMap<String,Integer>();
					writer.println(line);
				}
				else if(line.contains("End -----")) {
					for (Map.Entry entry : noun_phrases.entrySet()) {
						writer.println(entry.getKey() + " Support=" + entry.getValue());
					}
					writer.println(line);
				}
				else if(line.trim() != "") {
					if(!noun_phrases.containsKey(line)) {
						noun_phrases.put(line, 1);
					}
					else {
						noun_phrases.put(line, noun_phrases.get(line)+1);
					}
				}
			}
			
			br.close();
			writer.close();
			System.out.println("Noun Phrases Support Computed!!");
		}
		catch (FileNotFoundException e) {  
		   e.printStackTrace();  
		} 
		catch (IOException e) {  
		   e.printStackTrace();  
		}
	}
	
	public Map<String,ArrayList<String>> getFrequentFeatures1(){
		Map<String,ArrayList<String>> reviewFeatures = new HashMap<String,ArrayList<String>>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("nounphrases_support.txt"));
			PrintWriter writer = new PrintWriter("nounphrases_support1.txt", "UTF-8");
			String line;
			String restId = "";
			Map<String,Integer> features = new HashMap<String,Integer>();
			ArrayList<String> frequentFeatures = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				if(line.startsWith("Restaurant_ID")) {
					restId = line;
					features = new HashMap<String,Integer>();
					frequentFeatures = new ArrayList<String>();
					writer.println(line);
				}
				else if(line.contains("End -----")) {
					int i = line.indexOf("End -----");
					long min_support = Math.round(Integer.parseInt(line.substring(0, i).trim())*0.10);
					int f_count = 0;
					for (Map.Entry entry : features.entrySet()) {
						int support = (int) entry.getValue();
						if(support >= min_support) {
							f_count++;
							writer.println(entry.getKey() + " Support=" + entry.getValue());
							frequentFeatures.add((String)entry.getKey());
						}
					}
					System.out.println(f_count);
					writer.println(min_support + " End -------------------------------------------------------------");
					reviewFeatures.put(restId,frequentFeatures);
				}
				else if(line.trim() != "") {
					String[] parts = line.split(" Support=");
					String[] words = parts[0].trim().split(" ");
						for(String w:words){
							if(!features.containsKey(w))
								features.put(w,Integer.parseInt(parts[1]));
							else
								features.put(w,Integer.parseInt(parts[1]) + features.get(w));
						}
				}
			}
			
			br.close();
			writer.close();
			System.out.println("Frequent Itemset 1 generated");
		}
		catch (FileNotFoundException e) {  
		   e.printStackTrace();  
		} 
		catch (IOException e) {  
		   e.printStackTrace();  
		}

		return reviewFeatures;
	}
	
	public Map<String,ArrayList<String>> getFrequentFeatures2(Map<String,ArrayList<String>> features1){
		Map<String,ArrayList<String>> reviewFeatures = new HashMap<String,ArrayList<String>>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("nounphrases_support.txt"));
			PrintWriter writer = new PrintWriter("nounphrases_support2.txt", "UTF-8");
			String line;
			String restId = "";
			Map<String,Integer> features = new HashMap<String,Integer>();
			ArrayList<String> frequentFeatures = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				if(line.startsWith("Restaurant_ID")) {
					restId = line;
					features = new HashMap<String,Integer>();
					frequentFeatures = new ArrayList<String>();
					writer.println(line);
					ArrayList<String> features1_rest = features1.get(restId);
					for(int i=0;i<features1_rest.size();i++){
						for(int j=i+1;j<features1_rest.size();j++){
							String candidateFeature2 = features1_rest.get(i) + " " + features1_rest.get(j);
							features.put(candidateFeature2,0);
						}
					}
				}
				else if(line.contains("End -----")) {
					int i = line.indexOf("End -----");
					long min_support = Math.round(Integer.parseInt(line.substring(0, i).trim())*0.10);
					int f_count = 0;
					for (Map.Entry entry : features.entrySet()) {
						int support = (int) entry.getValue();
						if(support >= min_support) {
							f_count++;
							writer.println(entry.getKey() + " Support=" + entry.getValue());
							frequentFeatures.add((String)entry.getKey());
						}
					}
					System.out.println(f_count);
					writer.println(min_support + " End -------------------------------------------------------------");
					reviewFeatures.put(restId,frequentFeatures);
				}
				else if(line.trim() != "") {
					String[] parts = line.split(" Support=");
					String[] words_arr = parts[0].trim().split(" ");
					List<String> words = Arrays.asList(words_arr);
					for (Map.Entry<String,Integer> entry : features.entrySet()) {
						String[] feature2_parts = entry.getKey().split(" ");
						if(words.contains(feature2_parts[0]) && words.contains(feature2_parts[1]))
							features.put(entry.getKey(),Integer.parseInt(parts[1]) + features.get(entry.getKey()));
					}
								
				}
			}
			
			br.close();
			writer.close();
			System.out.println("Frequent Itemset 2 generated");
		}
		catch (FileNotFoundException e) {  
		   e.printStackTrace();  
		} 
		catch (IOException e) {  
		   e.printStackTrace();  
		}

		return reviewFeatures;
	}
	
	public Map<String,HashMap<String,Integer>> pruneFeatures(){
		Map<String,HashMap<String,Integer>> reviewFeatures = new HashMap<String,HashMap<String,Integer>>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("nounphrases_support1.txt"));
			String line;
			String restId = "";
			HashMap<String,Integer> features = new HashMap<String,Integer>();
			while ((line = br.readLine()) != null) {
				if(line.startsWith("Restaurant_ID")) {
					restId = line;
					features = new HashMap<String,Integer>();
				}
				else if(line.contains("End -----")) {
					reviewFeatures.put(restId, features);
				}
				else if(line.trim() != "") {
					String[] parts = line.split(" Support=");
					features.put(parts[0],Integer.parseInt(parts[1]));
				}
			}
			
			br.close();
			
			br = new BufferedReader(new FileReader("nounphrases_support2.txt"));
			PrintWriter writer = new PrintWriter("pruned_features.txt", "UTF-8");
			while ((line = br.readLine()) != null) {
				if(line.startsWith("Restaurant_ID")) {
					restId = line;
					features = reviewFeatures.get(restId);
				}
				else if(line.contains("End -----")) {
					writer.println("Restaurant_ID " + restId);
					int i = line.indexOf("End -----");
					int min_psupport = Integer.parseInt(line.substring(0, i).trim());
					HashMap<String,Integer> map = sortByValues(features);
					HashMap<String,Integer> prunedSet = new HashMap<String,Integer>();
					int k=0;
					for (Map.Entry<String,Integer> entry : map.entrySet()) {
						if(entry.getValue() >= min_psupport) {
							writer.println(entry.getKey() + " PSupport=" + entry.getValue());
							k++;
							prunedSet.put(entry.getKey() , entry.getValue());
						}
					}
					reviewFeatures.put(restId, prunedSet);
//					System.out.println(k);
					writer.println("End ---------------------------------------------------------------------------------");
				}
				else if(line.trim() != "") {
					String[] parts = line.split(" Support=");
					int parent_sup = Integer.parseInt(parts[1]);
					features.put(parts[0],parent_sup);
					String[] words_arr = parts[0].trim().split(" ");
					for(String w:words_arr) {
						features.put(w,features.get(w)-parent_sup);
					}
				}
			}
			
			br.close();
			writer.close();
			System.out.println("Features Pruned");
		}
		catch (FileNotFoundException e) {  
		   e.printStackTrace();  
		} 
		catch (IOException e) {  
		   e.printStackTrace();  
		}

		return reviewFeatures;
	}
	
	private static HashMap<String,Integer> sortByValues(HashMap<String,Integer> map) { 
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	                  .compareTo(((Map.Entry) (o1)).getValue());
	            }
	       });

	       HashMap<String,Integer> sortedHashMap = new LinkedHashMap<String,Integer>();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put((String)entry.getKey(), (Integer)entry.getValue());
	       } 
	       return sortedHashMap;
	}
	
	public static void main(String[] args) {
		POSTagger pos = new POSTagger();
//		Map<String,ArrayList<String>> reviews = pos.tagReviews();
//		pos.getNounPhrases();
//		pos.getNounPhrasesSupport();
//		Map<String,ArrayList<String>> features1 = pos.getFrequentFeatures1();
//		Map<String,ArrayList<String>> features2 = pos.getFrequentFeatures2(features1);
		pos.pruneFeatures();
	}
}
