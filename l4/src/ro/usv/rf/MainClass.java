package ro.usv.rf;

import java.util.Arrays;

public class MainClass {

	public static void main(String[] args) {
		double[][] learningSet = null;

		try {
			learningSet = FileUtils.readLearningSetFromFile("in.txt");
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length;
			System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns,
					numberOfFeatures));

			double[][] euclidian_distances = new double[numberOfPatterns][numberOfFeatures + 1];

			for (int i = 0; i < numberOfPatterns; i++) {
				double[] firstPattern = Arrays.stream(learningSet[i]).limit(numberOfPatterns - 1).toArray();
				for (int j = 0; j < numberOfPatterns; j++) {
					double[] otherPattern = Arrays.stream(learningSet[j]).limit(numberOfPatterns - 1).toArray();

					double euclidianDistance = DistanceUtiils.calculateEuclidianDistance(firstPattern, otherPattern);
					euclidian_distances[i][j] = Math.floor(euclidianDistance * 100) / 100;
					System.out.println("[" + i + "][" + j + "] = " + euclidianDistance);
				}
				System.out.println();
			}

			int searchedPatternIndex = learningSet.length - 1;
			double[] distances = euclidian_distances[searchedPatternIndex];
			int index = 0;
			double closest_PatternDistance = distances[index];

			for (int i = 0; i < distances.length - 1; i++) {
				if (distances[i] < closest_PatternDistance && searchedPatternIndex != i) {
					closest_PatternDistance = distances[i];
					index = i;
				}
			}

			System.out.println(String.format("Searched class is: %s", learningSet[index][numberOfFeatures - 1]));

			FileUtils.writeLearningSetToFile("output.csv", euclidian_distances);
		} catch (USVInputFileCustomException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Finished learning set operations");
		}
	}

}