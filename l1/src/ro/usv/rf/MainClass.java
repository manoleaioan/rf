package ro.usv.rf;

import java.util.ArrayList;
import java.util.List;

public class MainClass {

	public static void main(String[] args) {

		double[][] learningSet = FileUtils.readLearningSetFromFile("in.txt");
		FileUtils.writeLearningSetToFile("out.csv", normalizeLearningSet(learningSet));
	}

	private static double[][] normalizeLearningSet(double[][] learningSet) {
		double[][] normalizedLearningSet = new double[learningSet.length][];
		List<Double> minList = new ArrayList<>();
		List<Double> maxList = new ArrayList<>();

		for (int i = 0; i < learningSet.length; i++) {
			normalizedLearningSet[i] = new double[learningSet[i].length];
			double min = 999999;
			double max = -99999;
			for (int j = 0; j < learningSet[i].length; j++) {
				if (min > learningSet[i][j]) {
					min = learningSet[i][j];
				}

				if (max < learningSet[i][j]) {
					max = learningSet[i][j];
				}
			}
			minList.add(min);
			maxList.add(max);
		}

		for (int i = 0; i < learningSet.length; i++) {
			for (int j = 0; j < learningSet[i].length; j++) {
				normalizedLearningSet[i][j] = (learningSet[i][j] - minList.get(j)) / (maxList.get(j) - minList.get(j));
			}
		}
		return normalizedLearningSet;
	}

}