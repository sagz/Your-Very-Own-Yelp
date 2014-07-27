This file contains LDA code that used to run LDA on the review dataset.
This code was obtained from https://github.com/shuyo/iir library at github. I have included only the relevant files for running LDA.
Run the code using the following command. Needs nltk to be installed.

./lda.py -f reviews.txt 0:20 -k 10 --alpha=0.5 --beta=0.5 -i 50