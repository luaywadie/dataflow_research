import pandas as pd
import numpy as np

# separated out pctg features because get_commposition pctgs is easier this way
pctg_feature_names = ['pctg_oxygen', 'pctg_helium', 'pctg_iron', 'pctg_nickel', 'pctg_silicon', 'pctg_aluminum',\
                      'pctg_calcium', 'pctg_sodium', 'pctg_potassium', 'pctg_magnesium', 'pctg_other']
feature_names = ['mass', 'diameter', 'surface_temperature'] + pctg_feature_names
NUM_SAMPLES = 500  # how many samples should be generated

df = pd.DataFrame(columns=feature_names)

# returns a normalized list of floats to add up to 1.0 for composition percentages
def get_composition_pctgs():
    values = np.absolute(np.random.randn(len(pctg_feature_names)))  # wrap in np.absolute so we have no negative values
    return values / np.sum(values)


MIN_MASS, MAX_MASS = 1000000., 1000000000000.  # 1 million, 1 trillion
MIN_DIAMETER, MAX_DIAMETER = 1000., 1000000000.  # 1 thousand, 1 billion
MIN_TEMP, MAX_TEMP = -10000, 10000

data = []
# generates the samples and appends them to the dataframe
for i in range(NUM_SAMPLES):
    mass = np.random.randint(MIN_MASS, MAX_MASS)
    diameter = np.random.randint(MIN_DIAMETER, MAX_DIAMETER)
    surface_temperature = np.random.randint(MIN_TEMP, MAX_TEMP)
    pctg_oxygen, pctg_helium, pctg_iron, pctg_nickel, pctg_silicon, pctg_aluminum, pctg_calcium,\
        pctg_sodium, pctg_potassium, pctg_magnesium, pctg_other = get_composition_pctgs()
    data.append([mass, diameter, surface_temperature,pctg_oxygen, pctg_helium, pctg_iron, pctg_nickel, pctg_silicon,\
                 pctg_aluminum, pctg_calcium, pctg_sodium, pctg_potassium, pctg_magnesium, pctg_other])


# pandas docs say its quicker to build a list then append that rather than iteratively append to the df
df2 = pd.DataFrame(data, columns=feature_names)
df = df.append(df2)
df.to_csv('planetary_data.csv')
