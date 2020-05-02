package ro.usv.rf;

import java.util.ArrayList;
import java.util.List;

public class MainClass {

	public static void main(String[] args) {
		String[][] learningSet;

		try {
			learningSet = FileUtils.readLearningSetFromFile("in.txt");
			double[][] m = new double[learningSet.length][learningSet.length];
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length - 1;
			System.out.println(String.format("The learning set has %s patters and %s features\n", numberOfPatterns,
					numberOfFeatures));

			List<String> classesList = new ArrayList<String>();
			for (int i = 0; i < numberOfPatterns; i++) {
				String clasa = learningSet[i][learningSet[0].length - 1];
				if (!classesList.contains(clasa)) {
					classesList.add(clasa);
				}
			}

			System.out.println("Distinct classes: " + classesList.size());

			double[][] W = new double[classesList.size()][numberOfFeatures + 1];

			for (int i = 0; i < W.length; i++) {
				double freeTermSum = 0.0;
				for (int j = 0; j < W[0].length - 1; j++) {
					double totalSum = 0.0;
					int totalElements = 0;
					for (int i2 = 0; i2 < learningSet.length; i2++) {
						if (learningSet[i2][numberOfFeatures].equals(classesList.get(i))) {
							totalSum += Double.valueOf(learningSet[i2][j]);
							totalElements++;
						}
					}
					W[i][j] = totalSum / totalElements;
					freeTermSum += Math.pow(W[i][j], 2);
				}
				W[i][W[i].length - 1] = -0.5 * freeTermSum;
			}

			Double[][] x = new Double[][] { { 1.0 }, { 3.0 }, { 1.0 } };

			Double sigma = 0.0;
			int clasa = 0;
			for (int i = 0; i < classesList.size(); i++) {
				Double sigma1 = W[i][0] * x[0][0] + W[i][1] * x[1][0] + W[i][2] * x[2][0];
				if (sigma1 > sigma) {
					sigma = sigma1;
					clasa = i + 1;
				}
			}

			System.out.println("Searched class: " + clasa);

		} catch (

		USVInputFileCustomException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("\nFinished learning set operations");
		}
	}

}