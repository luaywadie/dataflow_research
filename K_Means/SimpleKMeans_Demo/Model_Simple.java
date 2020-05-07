import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.converters.CSVLoader;
import weka.classifiers.trees.J48;
import weka.classifiers.*;
import java.util.Random;
import java.io.File;

public class Model_Simple {
  // Data Storage and Conversion
  private static DataSource inputData;
  private static Instances retrievedData;
  private static int numOfClusters;
  // Main Method
  public static void main(String[] args) throws Exception {
    // Read in from a .arff file and decode it
    inputData = new DataSource("data/weather.arff");
    // Instances = every true data entry that is read in
    retrievedData = inputData.getDataSet();
    // Set the number of clusters
    numOfClusters = 2;
    // Kmeans();
    KmeansCSV();
  }
  public static void KmeansCSV() throws Exception {
    CSVLoader loader = new CSVLoader();
    loader.setSource(new File("data/planetary_data.csv"));

    String[] options = new String[1];
    options[0] = "-H";
    loader.setOptions(options);

    Instances data = loader.getDataSet();

    // Init SimpleKMeans from weka
    SimpleKMeans kmeans_instance = new SimpleKMeans();
    // Set random seed generator so output doesn't change
   kmeans_instance.setSeed(1);
    try {
      // Keep the order of Instances relative to their read-in state
     kmeans_instance.setPreserveInstancesOrder(true);
      // Number of clusters to generate, we want 2 since our Y-classifer can either be a yes / no
     kmeans_instance.setNumClusters(10);
      // Generate the clustors given the Instances(data)
     kmeans_instance.buildClusterer(data);
      // For each cluster, store its assigned instances into assignments
      int[] assignments = kmeans_instance.getAssignments();
      // Loop over every assignment in assignments and print the cluster and instance prediction
      int i = 0;
      for (int clusterNum : assignments) {
        System.out.printf("Instance %d => Cluster %d\n",i,clusterNum);
        i++;
      }
      // Get each clusters center point and its classified attributes
      Instances centers = kmeans_instance.getClusterCentroids();
      // for (i = 0; i < 10; i++) {
      //     System.out.print("Cluster #: " + i + " \t| Count: " +kmeans_instance.getClusterSizes()[i]);
      //     System.out.println(" \t| Center: " + centers.instance(i));
      // }
      Instances obj = kmeans_instance.getClusterCentroids();
      System.out.println("test");
    } catch (Exception e) {
      // N/A
    }

  }
  // K-means-clustering Algorithm Training / Testing
  public static void Kmeans() throws Exception {
    // Init SimpleKMeans from weka
    SimpleKMeans kmeans_instance = new SimpleKMeans();
    // Set random seed generator so output doesn't change
   kmeans_instance.setSeed(1);
    try {
      // Keep the order of Instances relative to their read-in state
     kmeans_instance.setPreserveInstancesOrder(true);
      // Number of clusters to generate, we want 2 since our Y-classifer can either be a yes / no
     kmeans_instance.setNumClusters(numOfClusters);
      // Generate the clustors given the Instances(data)
     kmeans_instance.buildClusterer(retrievedData);
      // For each cluster, store its assigned instances into assignments
      int[] assignments = kmeans_instance.getAssignments();
      // Loop over every assignment in assignments and print the cluster and instance prediction
      int i = 0;
      for (int clusterNum : assignments) {
        System.out.printf("Instance %d => Cluster %d\n",i,clusterNum);
        i++;
      }
      // Get each clusters center point and its classified attributes
      Instances centers = kmeans_instance.getClusterCentroids();
      for (i = 0; i < numOfClusters; i++) {
          System.out.print("Cluster #: " + i + " \t| Count: " +kmeans_instance.getClusterSizes()[i]);
          System.out.println(" \t| Center: " + centers.instance(i));
      }
    } catch (Exception e) {
      // N/A
    }
  }
}
