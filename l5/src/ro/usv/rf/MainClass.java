package ro.usv.rf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainClass {

	private static void knn_solution(String[][] learningSet) {
		double[][] city = new double[][] { { 25.89, 47.56 }, { 24.0, 45.15 }, { 25.33, 45.44 }, { 22.72, 45.00 } };
		int kVal = learningSet.length;

		for (int in = 0; in < city.length; in++) {

			Map<Double, String> distMap = DistanceUtils.calculateEuclidianDistanceMatrice(learningSet, city[in], kVal);
			Map<Double, String> sorteddistMap = new TreeMap<Double, String>(distMap);

			double total = 0;
			int correct = 0;

			int K = 0;

			for (Map.Entry<Double, String> entry : distMap.entrySet()) {
				double k = entry.getKey();
				String v = entry.getValue();

				total++;
				if (k < 0.9)
					correct++;

				if (correct > 0 && correct / total < 0.9) {
					break;
				}
				K++;
			}
			System.out.printf("K(%s) >> Judet: %s | Score: %s | kMax: %s\n", kVal, sorteddistMap.values().toArray()[0],
					sorteddistMap.keySet().stream().findFirst().get(), K);
		}
	}

	private static void type3Class(String[][] learningSet, int numberOfPatterns, int numberOfFeatures) {
		double[][] city = new double[][] { { 25.89, 47.56 }, { 24.0, 45.15 }, { 25.33, 45.44 }, { 22.72, 45.00 } };

		List<String> classesList = new ArrayList<String>();
		for (int i = 0; i < numberOfPatterns; i++) {
			String clasa = learningSet[i][learningSet[0].length - 1];
			if (!classesList.contains(clasa)) {
				classesList.add(clasa);
			}
		}

		double[][] W = new double[classesList.size()][numberOfFeatures - 1];

		for (int i = 0; i < W.length; i++) {
			double freeTermSum = 0.0;
			for (int j = 0; j < W[0].length - 1; j++) {
				double totalSum = 0.0;
				int totalElements = 0;
				for (int i2 = 1; i2 < learningSet.length; i2++) {
					if (learningSet[i2][numberOfFeatures - 1].equals(classesList.get(i))) {
						totalSum += Double.valueOf(learningSet[i2][j]);
						totalElements++;
					}
				}
				W[i][j] = totalSum / totalElements;
				freeTermSum += Math.pow(W[i][j], 2);
			}
			W[i][W[i].length - 1] = -0.5 * freeTermSum;
		}

		Double[][] x = new Double[][] { { 25.89 }, { 47.56 }, { 1.0 } };

		Double sigma = 0.0;
		int clasa = 0;
		for (int i = 0; i < classesList.size(); i++) {
			Double sigma1 = W[i][0] * x[0][0] + W[i][1] * x[1][0] + W[i][2] * x[2][0];
			if (sigma1 > sigma) {
				sigma = sigma1;
				clasa = i + 1;
			}
		}

		System.out.println("\n\nDistinct classes: " + classesList.size());
		System.out.println("Searched class: " + classesList.get(clasa - 1));
	}

	public static void main(String[] args) {
		String[][] learningSet;

		try {
			learningSet = FileUtils.readLearningSetFromFile("data.csv");
			double[][] m = new double[learningSet.length][learningSet.length];
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length;

			knn_solution(learningSet);
			type3Class(learningSet, numberOfPatterns, numberOfFeatures);

			System.out.println(String.format("\nThe learning set has %s patters and %s features", numberOfPatterns,
					numberOfFeatures));
		} catch (USVInputFileCustomException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Finished learning set operations");
		}
	}
}