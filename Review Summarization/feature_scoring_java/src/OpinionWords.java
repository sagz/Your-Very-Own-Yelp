import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class OpinionWords {
	
	public static void main(String args[]) {
		OpinionWords ow = new OpinionWords();
		ow.constructDictionary();
	}

	public void constructDictionary(){
		
		HashMap<String,Integer> sentiments = new HashMap<String,Integer>();
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("positive-words.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				sentiments.put(line,3);
			}
			br.close();
			
			br = new BufferedReader(new FileReader("negative-words.txt"));
			while ((line = br.readLine()) != null) {
				sentiments.put(line,1);
			}
			br.close();
			
			br = new BufferedReader(new FileReader("stanford_adjs.txt"));
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(" ");
				Integer val = Integer.parseInt(parts[1]);
					if(!sentiments.containsKey(parts[0])) {
						if(val==0 )
							sentiments.put(parts[0],1);
						else if(val==4 )
							sentiments.put(parts[0],3);
					}
					else if(sentiments.get(parts[0])==1 && val==0 )
						sentiments.put(parts[0],0);
					else if(sentiments.get(parts[0])==3 && val==4 )
						sentiments.put(parts[0],4);
			}
			br.close();
			
			br = new BufferedReader(new FileReader("sentiwordnet_adjs.txt"));
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(" ");
				Integer val = Integer.parseInt(parts[1]);
					if(!sentiments.containsKey(parts[0])) {
						if(val==0)
							sentiments.put(parts[0],1);
						else if(val==4)
							sentiments.put(parts[0],3);
					}
					else if(sentiments.get(parts[0])==1 && val==0 )
						sentiments.put(parts[0],0);
					else if(sentiments.get(parts[0])==3 && val==4 )
						sentiments.put(parts[0],4);
			}
			br.close();
			
			br = new BufferedReader(new FileReader("my_adjs.txt"));
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(" ");
				Integer val = Integer.parseInt(parts[1]);
					if(!sentiments.containsKey(parts[0])) {
						if(val==0 )
							sentiments.put(parts[0],1);
						else if(val==4 )
							sentiments.put(parts[0],3);
					}
					else if(sentiments.get(parts[0])==1 && val==0 )
						sentiments.put(parts[0],0);
					else if(sentiments.get(parts[0])==3 && val==4 )
						sentiments.put(parts[0],4);
			}
			br.close();
//			
			br = new BufferedReader(new FileReader("adjList.txt"));
			PrintWriter writer = new PrintWriter("adj_sentiments.txt", "UTF-8");
			while ((line = br.readLine()) != null) {
					if(!sentiments.containsKey(line)) {
						writer.println(line + " " + 2);
					}
					else {
						writer.println(line + " " + sentiments.get(line));
					}
			}
			br.close();
			writer.close();

			System.out.println("Dictionary created..");
		}
		catch (FileNotFoundException e) {  
		   e.printStackTrace();  
		} 
		catch (IOException e) {  
		   e.printStackTrace();  
		} 
	}

}
