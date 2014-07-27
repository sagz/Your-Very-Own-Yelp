We ran our experiments on two versions of the Yelp Dataset.
1. The whole dataset which is in the yelpdataset folder. It contains train and the test file.
2. The top50 review based dataset which is in the yelp_top folder. It also contains the corresponding train and test file.

First you need to install graphlab. Follow these steps to do so:
1. All the dependencies can be satisfied from the repository:

sudo apt-get update
sudo apt-get install gcc g++ build-essential libopenmpi-dev default-jdk cmake zlib1g-dev


2. Clone the repository
git clone https://github.com/graphlab-code/graphlab.git
cd graphlab


3. Configure it with no MPI Support. Very Important to use no_mpi option.
./configure --no_mpi


4. Make only the collaborative filtering toolkit.
cd release/toolkits/collaborative_filtering/
make -j2

The above making will take a really long time.


5. After you have succesfully build graphlab, copy over the two datasets to the collaborative filtering directory.

cp -r yelp_top ~/<graphlab-path>/graphlab/release/toolkits/collaborative_filtering/

cp -r yelpdataset ~/<graphlab-path>/graphlab/release/toolkits/collaborative_filtering/


6. Copy the run-scripts.sh to the same directory.
cp run-scripts.sh ~/<graphlab-path>/graphlab/release/toolkits/collaborative_filtering/

7. Run it for all the algorithms.
bash run-scripts.sh

8. This will save the results for the all of the 4 algorithms that we have used.

To individually run the algorithms, use the following commands:

./als --matrix ./yelpdataset/ --D=15 --max_iter=80 --lambda=0.01 --predictions=yelpdataset.als.15 --minval=1.0 --maxval=5.0

./sgd --matrix ./yelpdataset/ --D=15 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.sgd.15 --minval=1.0 --maxval=5.0

./biassgd --matrix ./yelpdataset/ --D=15 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.biassgd.15 --minval=1.0 --maxval=5.0

./svdpp --matrix ./yelpdataset/ --D=15 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.svdpp.15 --minval=1.0 --maxval=5.0