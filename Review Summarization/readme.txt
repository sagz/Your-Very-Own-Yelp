0. Please download the Yelp dataset from the link given in the parent readme file.
   We parse this dataset to create a subset before continuing on with our review summarization.
   The functions to generate this subset is in 'YelpJSONParser.java'
   You can also download our generated subset (rests_reviews.txt 202.2 MB) directly from - 
   https://mega.co.nz/#!gJkhlTLY!EveMOxpjeozZRWSToVSINHy_Gy5vGSYE6QnzfNbuLtM

1. In the Eclipse Project(Version: 3.7.2) "feature_scoring_java" , run class "OpinionExtraction.java"
2. It takes 15 minutes to run the code on 26,746 reviews. This time is on a decently modern machine. When the class finishes running you should see the output on console "Run Complete"
3. Find the output file "feature_scored.txt" in the root directiory of the project. This is a JSON file that includes the sentiment and opinion count for all the features of the 50 restaurant. Each line is a JSON object in the below format:
	{restaurant_name:{
	feature_1:{"sentiment":sentiment_value,"opinion_count":opinion_value},
	feature_2:{"sentiment":sentiment_value,"opinion_count":opinion_value},
	....
	feature_n:{"sentiment":sentiment_value,"opinion_count":opinion_value},
	}}