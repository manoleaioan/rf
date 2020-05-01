package ro.usv.rf;

import java.util.Map;
import java.util.TreeMap;

public class MainClass {

	public static void main(String[] args) {
		String[][] learningSet;

		try {
			learningSet = FileUtils.readLearningSetFromFile("data.csv");
			double[][] m = new double[learningSet.length][learningSet.length];
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length;

			double[][] city = new double[][] { { 25.89, 47.56 }, { 24.0, 45.15 }, { 25.33, 45.44 }, { 22.72, 45.00 } };
			int kVal = 10000;

			for (int in = 0; in < city.length; in++) {

				Map<Double, String> distMap = DistanceUtils.calculateEuclidianDistanceMatrice(learningSet, city[in],
						kVal);
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
				System.out.printf("K(%s) >> Judet: %s | Score: %s | kMax: %s\n", kVal,
						sorteddistMap.values().toArray()[0], sorteddistMap.keySet().stream().findFirst().get(), K);
			}

			System.out.println(String.format("\nThe learning set has %s patters and %s features", numberOfPatterns,
					numberOfFeatures));

		} catch (

		USVInputFileCustomException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Finished learning set operations");
		}
	}

}