package ro.usv.rf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class MainClass {

	private static void type3Class(String[][] evaluationSet, String[][] trainingSet) {
		int numberOfFeatures = trainingSet[0].length - 1;

		List<String> classesList = new ArrayList<String>();
		for (int i = 0; i < trainingSet.length; i++) {

			String clasa = trainingSet[i][trainingSet[0].length - 1];
			if (!classesList.contains(clasa)) {
				classesList.add(clasa);
			}
		}

		double[][] W = new double[classesList.size()][numberOfFeatures + 1];
		for (int i = 0; i < W.length; i++) {
			double freeTermSum = 0.0;
			for (int j = 0; j < W[0].length; j++) {
				double totalSum = 0.0;
				int totalElements = 0;
				for (int i2 = 1; i2 < evaluationSet.length; i2++) {
					if (evaluationSet[i2][numberOfFeatures - 1].equals(classesList.get(i))) {
						totalSum += Double.valueOf(evaluationSet[i2][j]);
						totalElements++;
					}
				}
				W[i][j] = totalSum / totalElements;
				freeTermSum += Math.pow(W[i][j], 2);
			}
			W[i][W[i].length - 1] = -0.5 * freeTermSum;
		}

		Double[][] x = new Double[][] { { Double.parseDouble(evaluationSet[0][0]) },
				{ Double.parseDouble(evaluationSet[0][1]) }, { Double.parseDouble(evaluationSet[0][2]) },
				{ Double.parseDouble(evaluationSet[0][3]) }, { 1.0 } };

		Double sigma = 0.0;
		int clasa = 0;
		for (int i = 0; i < classesList.size(); i++) {
			Double sigma1 = W[i][0] * x[0][0] + W[i][1] * x[1][0] + W[i][2] * x[2][0] + W[i][3] * x[3][0]
					+ W[i][4] * x[4][0];
			if (sigma1 > sigma) {
				sigma = sigma1;
				clasa = i + 1;
			}
		}
		double error = Math.abs((0 - clasa) * 100);

		System.out.println("\n\nDistinct classes: " + classesList.size());
		System.out.printf("Searched class: %s, distance: %s, Err: %s%%\n\n ", classesList.get(clasa), clasa, error);
	}

	public static void main(String[] args) {
		String[][] learningSet;
		try {
			learningSet = FileUtils.readLearningSetFromFile("iris.csv");
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length - 1;

			System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns,
					numberOfFeatures));

			Map<String, Integer> classesMap = new HashMap<String, Integer>();

			// create map with distinct classes and number of occurence for each class
			for (int i = 0; i < numberOfPatterns; i++) {
				String clazz = learningSet[i][learningSet[i].length - 1];
				if (classesMap.containsKey(clazz)) {
					Integer nrOfClassPatterns = classesMap.get(clazz);
					classesMap.put(clazz, nrOfClassPatterns + 1);
				} else {
					classesMap.put(clazz, 1);
				}
			}
			Random random = new Random();
			// map that keeps for each class the random patterns selected for evaluation set
			Map<String, List<Integer>> classesEvaluationPatterns = new HashMap<String, List<Integer>>();
			Integer evaluationSetSize = 0;
			for (Map.Entry<String, Integer> entry : classesMap.entrySet()) {
				String className = entry.getKey();
				Integer classMembers = entry.getValue();
				Integer evaluationPatternsNr = Math.round(classMembers * 15 / 100);
				evaluationSetSize += evaluationPatternsNr;
				List<Integer> selectedPatternsForEvaluation = new ArrayList<Integer>();
				for (int i = 0; i < evaluationPatternsNr; i++) {
					Integer patternNr = random.nextInt(classMembers) + 1;
					while (selectedPatternsForEvaluation.contains(patternNr)) {
						patternNr = random.nextInt(classMembers) + 1;
					}
					selectedPatternsForEvaluation.add(patternNr);
				}
				classesEvaluationPatterns.put(className, selectedPatternsForEvaluation);
			}

			String[][] evaluationSet = new String[evaluationSetSize][numberOfPatterns];
			String[][] trainingSet = new String[numberOfPatterns - evaluationSetSize][numberOfPatterns];
			int evaluationSetIndex = 0;
			int trainingSetIndex = 0;
			Map<String, Integer> classCurrentIndex = new HashMap<String, Integer>();
			for (int i = 0; i < numberOfPatterns; i++) {
				String className = learningSet[i][numberOfFeatures];
				if (classCurrentIndex.containsKey(className)) {
					int currentIndex = classCurrentIndex.get(className);
					classCurrentIndex.put(className, currentIndex + 1);
				} else {
					classCurrentIndex.put(className, 1);
				}
				if (classesEvaluationPatterns.get(className).contains(classCurrentIndex.get(className))) {
					evaluationSet[evaluationSetIndex] = learningSet[i];
					evaluationSetIndex++;
				} else {
					trainingSet[trainingSetIndex] = learningSet[i];
					trainingSetIndex++;
				}
			}

			int[] K = new int[] { 1, 3, 5, 7, 9, 11, 13 };
			Map<Double, String> distMap = DistanceUtils.calculateEuclidianDistanceMatrice(evaluationSet, trainingSet);

			Map<Double, String> randomSelectedData = new TreeMap<Double, String>();

			List<Integer> smallErrors = new ArrayList<Integer>();
			for (int i = 0; i < K.length; i++) {
				for (int j = 0; j < K[i]; j++) {
					Object randomName = distMap.keySet().toArray()[new Random()
							.nextInt(distMap.keySet().toArray().length)];
					randomSelectedData.put(Double.parseDouble(randomName.toString()), distMap.get(randomName));
				}
				Map<Double, String> sorteddistMap = new TreeMap<Double, String>(randomSelectedData);

				String className = (String) sorteddistMap.values().toArray()[0];
				double classScore = (double) sorteddistMap.keySet().toArray()[0];

				double error = Math.abs((0 - classScore) * 100);

				if (error <= 50)
					smallErrors.add(K[i]);

				System.out.printf("K(%s) %.4f - %s Err: %.0f%% \n", K[i], classScore, className, error);
			}
			double avg = smallErrors.stream().mapToInt((x) -> x).summaryStatistics().getAverage();
			System.out.printf("\nOptimal K: %.0f", avg);

			type3Class(evaluationSet, trainingSet);

			FileUtils.writeLearningSetToFile("eval.txt", evaluationSet);
			FileUtils.writeLearningSetToFile("train.txt", trainingSet);

		} catch (USVInputFileCustomException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Finished learning set operations");
		}
	}

}
