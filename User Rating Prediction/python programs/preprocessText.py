import string
import nltk
from nltk.tokenize import RegexpTokenizer
from nltk.corpus import stopwords
import re

sentence = "At eight o'clock on Thursday morning Arthur didn't feel very good. French-Fries"
sentence = sentence.lower()
tokenizer = RegexpTokenizer(r'\w+')
tokens = tokenizer.tokenize(sentence)
filtered_words = [w for w in tokens if not w in stopwords.words('english')]
# tagged = nltk.pos_tag(tokens)