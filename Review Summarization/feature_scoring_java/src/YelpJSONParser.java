import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;  

public class YelpJSONParser {

	public Map<String,Integer> getRestIds(List<String> top50){
		
		Map<String,Integer> rests = new HashMap<String,Integer>();
		JSONParser parser = new JSONParser();  
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("yelp_academic_dataset_business.txt"));
			PrintWriter writer = new PrintWriter("rests_locs.txt", "UTF-8");
			String line;
			while ((line = br.readLine()) != null) {
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;  
				
				JSONArray categories = (JSONArray) jsonObject.get("categories"); 
					if(categories.contains("Restaurants")){
						String businessID = (String) jsonObject.get("business_id");
						String businessName = (String) jsonObject.get("name");
						double stars = (double) jsonObject.get("stars");
						double lat = (double) jsonObject.get("latitude");
						double longi = (double) jsonObject.get("longitude");
						writer.println(businessName + "," + lat + "," + longi);
						long review_count = (long) jsonObject.get("review_count");
//						System.out.println(businessID + " " + review_count); 
						rests.put(businessID,(int) review_count);
					}
			}
			
			br.close();
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
		  
		return rests;  
	}
	
	public void getRestPrediction(List<String> top50){
		
		Map<String,Restaurant> rests = new HashMap<String,Restaurant>();
		JSONParser parser = new JSONParser();  
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("yelp_academic_dataset_business.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;  
				String businessID = (String) jsonObject.get("business_id");
					if(top50.contains(businessID)){
						String businessName = (String) jsonObject.get("name");
						double lat = (double) jsonObject.get("latitude");
						double longi = (double) jsonObject.get("longitude");
						Restaurant rest = new Restaurant(businessName,lat,longi);
						rests.put(businessID,rest);
					}
			}
			br.close();

			PrintWriter writer = new PrintWriter("userrests_ratings.txt", "UTF-8");
			br = new BufferedReader(new FileReader("ratings.csv"));
			String userId = "";
			JSONObject user = new JSONObject();
			JSONObject restList = new JSONObject();
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if(!userId.equals(parts[0].trim())){
					if(!userId.equals("")){
						user.put(userId, restList);
						writer.println(user.toJSONString());
					}
					userId = parts[0].trim();
					user = new JSONObject();
					restList = new JSONObject();
					String restId = parts[1].trim();
					String restName = rests.get(restId).name;
					String rating = parts[2].trim();
					restList.put(restName,rating);
				}
				else{
					String restId = parts[1].trim();
					String restName = rests.get(restId).name;
					String rating = parts[2].trim();
					restList.put(restName,rating);
				}
				
			}
			br.close();
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
	
	public void getUserRatings(List<String> top50){
		
		JSONParser parser = new JSONParser(); 
		HashMap<String,Integer> allUsers = new HashMap<String,Integer>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("yelp_academic_dataset_review.txt"));
			PrintWriter writer = new PrintWriter("user_details.txt", "UTF-8");
			String line;
			while ((line = br.readLine()) != null) {
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;  
				String businessID = (String) jsonObject.get("business_id");
				String userID = (String) jsonObject.get("user_id");
				long stars = (long) jsonObject.get("stars");
				if(top50.contains(businessID)){
					writer.println(userID + "," + businessID + "," + stars);
					if(allUsers.containsKey(userID))
						allUsers.put(userID,allUsers.get(userID)+1);
					else
						allUsers.put(userID,1);
				}
			}
			br.close();
			writer.close();
			
			HashMap<String,String> rests = new HashMap<String,String>();
			br = new BufferedReader(new FileReader("rests_details.txt"));
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				rests.put(parts[0], parts[1]);
			}
			br.close();
			
			br = new BufferedReader(new FileReader("user_details.txt"));
			writer = new PrintWriter("topuser_details.txt", "UTF-8");
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if(allUsers.get(parts[0])>=10) {
					String restName = rests.get(parts[1]);
					writer.println(parts[0]+","+restName+","+parts[2]);
				}
			}
			br.close();
			writer.close();
			
			int count = 0;
			for (Map.Entry<String,Integer> entry : allUsers.entrySet()) {
				if(entry.getValue()>=10) count++;
			}
			System.out.println(count);
			
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

	public void getRelevantReviews(List<String> restIds){
		
		JSONParser parser = new JSONParser();  
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("yelp_academic_dataset_review.txt"));
			PrintWriter writer = new PrintWriter("rests_reviews.txt", "UTF-8");
			String line;
			while ((line = br.readLine()) != null) {
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;  
				
				String businessID = (String) jsonObject.get("business_id"); 
				if(restIds.contains(businessID)) {
					writer.println(line);
				}
			}
			
			br.close();
			writer.close();
			System.out.println("File created");
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

	public Map<String,ArrayList<String>> getReviewPerRest() {
		Map<String,ArrayList<String>> reviews = new HashMap<String,ArrayList<String>>();
		JSONParser parser = new JSONParser();  
		try {
			BufferedReader br = new BufferedReader(new FileReader("rests_reviews.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;  
				
				String businessID = (String) jsonObject.get("business_id"); 
				String text = (String) jsonObject.get("text"); 
				if(!reviews.containsKey(businessID)) {
					reviews.put(businessID,new ArrayList<String>());
				}
				
				ArrayList<String> reviewTexts = reviews.get(businessID);
				reviewTexts.add(text);
			}
			
			br.close();
			System.out.println("Reviews Clustered!!");
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
		  
		return reviews;  
	}
	
	public void readFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("yelp_dataset.json"));
			String line;
			PrintWriter writer = null;
			while ((line = br.readLine()) != null) {
				if(line.contains(".json")){
					if(writer!=null) writer.close();
					System.out.println(line);
					int index = line.indexOf(".json");
					String fname = line.substring(0,index).trim() + ".txt";
					System.out.println(fname);
					writer = new PrintWriter(fname, "UTF-8");
				}
				else {
					writer.println(line);
				}
					
			}
			
			br.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public List<String> getTop50() {
		List<String> restIds = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("top50_restreviews.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				restIds.add(line.split(" ")[0]);
			}
			
			br.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return restIds;
	}
	
	public static void main(String[] args) {
		YelpJSONParser yelpParser = new YelpJSONParser();
		List<String> top50 = yelpParser.getTop50();
//		Map<String,Integer> rests= yelpParser.getRestIds(top50);
		yelpParser.getRestPrediction(top50);
//		yelpParser.getRelevantReviews(restIDs);
//		yelpParser.getUserRatings(top50);
//		Map<String,ArrayList<String>> reviews = yelpParser.getReviewPerRest();
//
//		try {
//			PrintWriter writer = new PrintWriter("reviews_clustered.txt", "UTF-8");
//			for (Map.Entry entry : reviews.entrySet()) {
//				String restId = entry.getKey().toString();
//				if(top50.contains(restId)) {
//					 	writer.println("Restaurant_ID " +restId);
//					    System.out.println(restId);
//					    ArrayList<String> reviewTexts = (ArrayList<String>) entry.getValue();
////					    int review_count = rests.get(entry.getKey());
////					    if(reviewTexts.size() != review_count) {
////					    	System.out.println("Check: " + entry.getKey() + " " + review_count + " " + reviewTexts.size());
////					    }
////					    System.out.println(entry.getKey() + " " + reviewTexts.size());
//					    for (String s : reviewTexts) {
//					    	writer.println(s.trim());
//					    }
//					    writer.println("End -------------------------------------------------------------------------");
//				}
//			}
//	
//			writer.close();
//		}
//		catch (FileNotFoundException e) {  
//			   e.printStackTrace();  
//		} 
//		catch (IOException e) {  
//			   e.printStackTrace();  
//	    }
		
	}
}

class Restaurant {

	String name;
	double lat;
	double longi;
	
	public Restaurant(String name,double lat,double longi){
		this.name = name;
		this.lat = lat;
		this.longi = longi;
	}
}
