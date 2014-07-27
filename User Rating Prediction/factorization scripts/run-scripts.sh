# Running ALS for the whole Dataset
./als --matrix ./yelpdataset/ --D=15 --max_iter=80 --lambda=0.01 --predictions=yelpdataset.als.15 --minval=1.0 --maxval=5.0
./als --matrix ./yelpdataset/ --D=20 --max_iter=80 --lambda=0.01 --predictions=yelpdataset.als.20 --minval=1.0 --maxval=5.0
./als --matrix ./yelpdataset/ --D=30 --max_iter=80 --lambda=0.01 --predictions=yelpdataset.als.30 --minval=1.0 --maxval=5.0
./als --matrix ./yelpdataset/ --D=40 --max_iter=80 --lambda=0.01 --predictions=yelpdataset.als.40 --minval=1.0 --maxval=5.0
./als --matrix ./yelpdataset/ --D=50 --max_iter=80 --lambda=0.01 --predictions=yelpdataset.als.50 --minval=1.0 --maxval=5.0

# Running SGD for the whole Dataset
./sgd --matrix ./yelpdataset/ --D=15 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.sgd.15 --minval=1.0 --maxval=5.0
./sgd --matrix ./yelpdataset/ --D=20 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.sgd.20 --minval=1.0 --maxval=5.0
./sgd --matrix ./yelpdataset/ --D=30 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.sgd.30 --minval=1.0 --maxval=5.0
./sgd --matrix ./yelpdataset/ --D=40 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.sgd.40 --minval=1.0 --maxval=5.0
./sgd --matrix ./yelpdataset/ --D=50 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.sgd.50 --minval=1.0 --maxval=5.0

# Running Bias-SGD for the whole Dataset
./biassgd --matrix ./yelpdataset/ --D=15 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.biassgd.15 --minval=1.0 --maxval=5.0
./biassgd --matrix ./yelpdataset/ --D=20 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.biassgd.20 --minval=1.0 --maxval=5.0
./biassgd --matrix ./yelpdataset/ --D=30 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.biassgd.30 --minval=1.0 --maxval=5.0
./biassgd --matrix ./yelpdataset/ --D=40 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.biassgd.40 --minval=1.0 --maxval=5.0
./biassgd --matrix ./yelpdataset/ --D=50 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.biassgd.50 --minval=1.0 --maxval=5.0

# Running SVD++ for the whole Dataset
./svdpp --matrix ./yelpdataset/ --D=15 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.svdpp.15 --minval=1.0 --maxval=5.0
./svdpp --matrix ./yelpdataset/ --D=20 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.svdpp.20 --minval=1.0 --maxval=5.0
./svdpp --matrix ./yelpdataset/ --D=30 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.svdpp.30 --minval=1.0 --maxval=5.0
./svdpp --matrix ./yelpdataset/ --D=40 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.svdpp.40 --minval=1.0 --maxval=5.0
./svdpp --matrix ./yelpdataset/ --D=50 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelpdataset.svdpp.50 --minval=1.0 --maxval=5.0


# Running ALS for the Top-Reviews Subset
./als --matrix ./yelp_top/ --D=15 --max_iter=80 --lambda=0.01 --predictions=yelp_top.als.15 --minval=1.0 --maxval=5.0
./als --matrix ./yelp_top/ --D=20 --max_iter=80 --lambda=0.01 --predictions=yelp_top.als.20 --minval=1.0 --maxval=5.0
./als --matrix ./yelp_top/ --D=30 --max_iter=80 --lambda=0.01 --predictions=yelp_top.als.30 --minval=1.0 --maxval=5.0
./als --matrix ./yelp_top/ --D=40 --max_iter=80 --lambda=0.01 --predictions=yelp_top.als.40 --minval=1.0 --maxval=5.0
./als --matrix ./yelp_top/ --D=50 --max_iter=80 --lambda=0.01 --predictions=yelp_top.als.50 --minval=1.0 --maxval=5.0

# Running SGD for the Top-Reviews Subset
./sgd --matrix ./yelp_top/ --D=15 --max_iter=80 --lambda=0.01 --gamma=0.02 --predictions=yelp_top.sgd.15 --minval=1.0 --maxval=5.0
./sgd --matrix ./yelp_top/ --D=20 --max_iter=80 --lambda=0.01 --gamma=0.02 --predictions=yelp_top.sgd.20 --minval=1.0 --maxval=5.0
./sgd --matrix ./yelp_top/ --D=30 --max_iter=80 --lambda=0.01 --gamma=0.02 --predictions=yelp_top.sgd.30 --minval=1.0 --maxval=5.0
./sgd --matrix ./yelp_top/ --D=40 --max_iter=80 --lambda=0.01 --gamma=0.02 --predictions=yelp_top.sgd.40 --minval=1.0 --maxval=5.0
./sgd --matrix ./yelp_top/ --D=50 --max_iter=80 --lambda=0.01 --gamma=0.02 --predictions=yelp_top.sgd.50 --minval=1.0 --maxval=5.0

# Running Bias-SGD for the Top-Reviews Subset
./biassgd --matrix ./yelp_top/ --D=15 --max_iter=80 --lambda=0.01 --gamma=0.02 --predictions=yelp_top.biassgd.15 --minval=1.0 --maxval=5.0
./biassgd --matrix ./yelp_top/ --D=20 --max_iter=80 --lambda=0.01 --gamma=0.02 --predictions=yelp_top.biassgd.20 --minval=1.0 --maxval=5.0
./biassgd --matrix ./yelp_top/ --D=30 --max_iter=80 --lambda=0.01 --gamma=0.02 --predictions=yelp_top.biassgd.30 --minval=1.0 --maxval=5.0
./biassgd --matrix ./yelp_top/ --D=40 --max_iter=80 --lambda=0.01 --gamma=0.02 --predictions=yelp_top.biassgd.40 --minval=1.0 --maxval=5.0
./biassgd --matrix ./yelp_top/ --D=50 --max_iter=80 --lambda=0.01 --gamma=0.02 --predictions=yelp_top.biassgd.50 --minval=1.0 --maxval=5.0

# Running SVD++ for the Top-Reviews Subset
./svdpp --matrix ./yelp_top/ --D=15 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelp_top.svdpp.15 --minval=1.0 --maxval=5.0
./svdpp --matrix ./yelp_top/ --D=20 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelp_top.svdpp.20 --minval=1.0 --maxval=5.0
./svdpp --matrix ./yelp_top/ --D=30 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelp_top.svdpp.30 --minval=1.0 --maxval=5.0
./svdpp --matrix ./yelp_top/ --D=40 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelp_top.svdpp.40 --minval=1.0 --maxval=5.0
./svdpp --matrix ./yelp_top/ --D=50 --max_iter=100 --lambda=0.01 --gamma=0.02 --predictions=yelp_top.svdpp.50 --minval=1.0 --maxval=5.0