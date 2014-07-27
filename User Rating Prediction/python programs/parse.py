import string
import nltk
from nltk.tokenize import RegexpTokenizer
from nltk.corpus import stopwords
import re

def preprocess(sentence):
	sentence = sentence.lower()
	tokenizer = RegexpTokenizer(r'\w+')
	tokens = tokenizer.tokenize(sentence)
	filtered_words = [w for w in tokens if not w in stopwords.words('english')]
	return " ".join(filtered_words)

# String Constants
end_string = "End -----------------"
restaurant_id_sentence = "Restaurant_ID"

file_write = open('reviews.txt','w')

with open('reviews_clustered.txt', 'r') as content_file:
    content = content_file.read()

sentences = content.split("\n")
print len(sentences)

document = ""
for sentence in sentences:
	if sentence.startswith(restaurant_id_sentence):
		print "Ignored Line:", sentence[:-1]
		continue

	if sentence.startswith(end_string):
		print sentence[:-1]
		file_write.write(preprocess(document))
		file_write.write("\n")
		document = ""
	else:
		document = document + sentence[:-1]
    		
file_write.close()
content_file.close()