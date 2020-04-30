package ro.usv.rf;

public class MainClass {
	
	
	public static void main(String[] args) {
		double[][] learningSet = null;
		try {
			learningSet = FileUtils.readLearningSetFromFile("in.txt");
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length;
			System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns, numberOfFeatures));
			double[] firstPattern = learningSet[0];
			
			for(int i=1; i<numberOfPatterns; i++) {
					double[] otherPattern = learningSet[i];
					double euclidianDistance = DistanceUtiils.calculateEuclidianDistance(firstPattern, otherPattern);
					double calculateCebisevDistance = DistanceUtiils.calculateCebisevDistance(firstPattern, otherPattern);
					double calculateCityBlockDistance = DistanceUtiils.calculateCityBlockDistance(firstPattern, otherPattern);
					double calculateMahalanobisDistance = DistanceUtiils.calculateMahalanobisDistance(firstPattern, otherPattern, numberOfPatterns);
					System.out.println("Distanta euclidian este: " + euclidianDistance);
					System.out.println("Distanta Cebisev este: " + calculateCebisevDistance);
					System.out.println("Distanta CityBlocm este: " + calculateCityBlockDistance);
					System.out.println("Distanta Mahalanobis este: " + calculateMahalanobisDistance);
			}
		} catch (USVInputFileCustomException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Finished learning set operations");
		}
	}

}
