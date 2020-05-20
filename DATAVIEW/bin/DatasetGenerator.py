import pandas as pd
import numpy as np

# Feature names
feature_names = ['mass', 'diameter', 'density', 'gravity', 'distance_to_sun', 'mean_temperature', 'orbital_period']

# N Samples for Train / Test to generate
NUM_TRAIN_SAMPLES = 500  # how many samples should be generated in the training set
NUM_TEST_SAMPLES = 100  # how many samples should be generated in the test set

# Returns a normalized list of floats to add up to 1.0 for composition percentages
# def get_composition_pctgs():
#     values = np.absolute(np.random.randn(len(pctg_feature_names)))  # wrap in np.absolute so we have no negative values
#     return values / np.sum(values)

MIN_MASS, MAX_MASS = 0.073, 1898.   # 1 * 10^24 kg
MIN_DIAMETER, MAX_DIAMETER = 2370., 142984. # km
MIN_DENSITY, MAX_DENSITY = 687., 5514.  # kg / m^3
MIN_GRAVITY, MAX_GRAVITY = 0.7, 23.1   # m / s^2
MIN_DISTANCE_TO_SUN, MAX_DISTANCE_TO_SUN = 57.9, 5906.4 # 1 * 10^6 km
MIN_MEAN_TEMP, MAX_MEAN_TEMP = -225., 464.  # C
MIN_ORBITAL_PERIOD, MAX_ORBITAL_PERIOD = 88., 90560. # Days

train_data = []
# Generates the samples and appends them to the dataframe
for i in range(NUM_TRAIN_SAMPLES):
    # Declare feature variables
    mass = np.random.randint(MIN_MASS, MAX_MASS)
    diameter = np.random.randint(MIN_DIAMETER, MAX_DIAMETER)
    density = np.random.randint(MIN_DENSITY, MAX_DENSITY)
    gravity = np.random.randint(MIN_GRAVITY, MAX_GRAVITY)
    distance_to_sun = np.random.randint(MIN_DISTANCE_TO_SUN, MAX_DISTANCE_TO_SUN)
    mean_temp = np.random.randint(MIN_MEAN_TEMP, MAX_MEAN_TEMP)
    orbital_period = np.random.randint(MIN_ORBITAL_PERIOD, MAX_ORBITAL_PERIOD)
    # Append to list
    train_data.append([mass, diameter, density, gravity, distance_to_sun, mean_temp, orbital_period])

# Write the training data to a CSV file
train_df = pd.DataFrame(train_data, columns=feature_names)
train_df.to_csv('../WebContent/workflowTaskDir/planetary_data_train.csv', index=False, sep=':')

test_data = []
for i in range(NUM_TEST_SAMPLES):
    # Declare feature variables
    id = i + 1
    mass = np.random.randint(MIN_MASS, MAX_MASS)
    diameter = np.random.randint(MIN_DIAMETER, MAX_DIAMETER)
    density = np.random.randint(MIN_DENSITY, MAX_DENSITY)
    gravity = np.random.randint(MIN_GRAVITY, MAX_GRAVITY)
    distance_to_sun = np.random.randint(MIN_DISTANCE_TO_SUN, MAX_DISTANCE_TO_SUN)
    mean_temp = np.random.randint(MIN_MEAN_TEMP, MAX_MEAN_TEMP)
    orbital_period = np.random.randint(MIN_ORBITAL_PERIOD, MAX_ORBITAL_PERIOD)
    # Append to list
    test_data.append([id, mass, diameter, density, gravity, distance_to_sun, mean_temp, orbital_period])

# Write the test data to a CSV file
test_df = pd.DataFrame(test_data, columns=["id"] + feature_names)
test_df.to_csv('../WebContent/workflowTaskDir/planetary_data_test.csv', index=False, sep=':', header=False)
