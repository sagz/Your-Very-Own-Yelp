import numpy as np
import random

IN_FILE = "topuser_details.txt"
OUT_FILE = "yelp_top.txt"
TRAIN_FILE = "yelp_top.train"
VALIDATION_FILE = "yelp_top.validate"
PERCENTAGE_SPLIT = 0.8

# Read the data from the file into np array and returns the array
def get_data(filename, delimiter_type):
    data = np.genfromtxt(filename, delimiter=delimiter_type, dtype=[('user_id', '|S50'), ('business_id', '|S50'), ('rating', '<f8')])
    return data

def format():
    data = get_data("ratings.txt", ",")
    print data.shape

    indices = np.argsort(data[:, 0])
    data = data[indices]
    data = data.astype(int)

    # Create the business map
    count = 0
    business_map= {}
    for i in range(1500):
        if data[i, 1] not in business_map:
            business_map[data[i,1]] = count
            count += 1

    print "Number of business", len(business_map)

    OUT_FILE = "truncated_ratings.txt"
    f = open(OUT_FILE,'w')
    for i in range(1500):
        f.write(str(data[i, 0]) + ", " + str(business_map[data[i,1]]) + ", " + str(data[i, 2]) + "\n")
    f.close()

    np.savetxt("ratings.txt", data, fmt='%u', delimiter=",")

if __name__ == "__main__":

    # Load the Data
    data = get_data(IN_FILE, ",")
    print data.shape

    # Find the unique users, bussinesses and ratings
    users = np.unique(data['user_id'])
    business = np.unique(data['business_id'])
    ratings = np.unique(data['rating'])

    print "Number of Users:", len(users)
    print "Number of Business:", len(business)

    # Create the user map
    count = 1
    user_map = {}
    for user in users:
        user_map[user] = count
        count += 1



    # Create the business map
    count = 1
    business_map= {}
    for bus in business:
        business_map[bus] = count
        count += 1

    train_file = open(TRAIN_FILE,'w')
    validation_file = open(VALIDATION_FILE, 'w')

    trainCount = 0
    validationCount = 0
    total = 0

    # f = open(OUT_FILE,'w')
    for entry in data:
        # f.write(str(user_map[entry['user_id']]) + ", " + str(business_map[entry['business_id']]) + ", " + str(entry['rating']) + "\n")
        # print user_map[entry['user_id']], ",", business_map[entry['business_id']], ",", entry['rating']
        total += 1
        if random.random() < PERCENTAGE_SPLIT:
            train_file.write(str(user_map[entry['user_id']]) + " " + str(business_map[entry['business_id']]) + " " + str(entry['rating']) + "\n")
            trainCount += 1
        else:
            validation_file.write(str(user_map[entry['user_id']]) + " " + str(business_map[entry['business_id']]) + " " + str(entry['rating']) + "\n")
            validationCount += 1

    print "Total:", total
    print "Train Count/Total:", trainCount, "/", total
    print "Validation Count/Total", validationCount, "/", total
    print "Train Percentage:", float(trainCount)/total

    train_file.close()
    validation_file.close()

    # f.close()

    # Store the reverse map from id to user
    user_file = open("user-map.txt", 'w')
    for key, value in user_map.iteritems():
        user_file.write(str(value) + ", " + str(key) + "\n")
    user_file.close()

    # Store the reverse map from id to business
    # Store the reverse map from id to user
    business_file = open("business-map.txt", 'w')
    for key, value in business_map.iteritems():
        business_file.write(str(value) + ", " + str(key) + "\n")
    business_file.close()