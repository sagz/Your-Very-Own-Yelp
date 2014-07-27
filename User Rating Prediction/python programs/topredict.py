import numpy as np

def make_to_predict_list():
    f = open("yelp_top.predict",'w')
    for user_id in range(1, 295):
        for bussiness_id in range(1, 51):
            f.write(str(user_id) + " " + str(bussiness_id) + "\n")
    f.close()

# Read the data from the file into np array and returns the array
def get_data(filename, delimiter_type):
    data = np.genfromtxt(filename, delimiter=delimiter_type, dtype=[('count', 'i4'),('id_string', '|S50')])
    return data

if __name__ == "__main__":
    user_data = get_data("user-map.txt", ",")
    business_data = get_data("business-map.txt", ",")


    userMap = {}
    businessMap = {}

    for row in user_data:
        userMap[row['count']] = row['id_string']

    for row in business_data:
        # print row['count'], row['id_string']
        businessMap[row['count']] = row['id_string']

    recommendation_data = np.genfromtxt("biassgd-yelp-top-run1", delimiter=",")

    output_file = open("biassgd-output.txt", "w")
    for row in recommendation_data:
        output_file.write(str(userMap[row[0]]) + ", " + str(businessMap[row[1]]) + ", " + str(row[2]) + "\n")
    output_file.close()
    print recommendation_data.shape