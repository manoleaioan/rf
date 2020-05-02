package ro.usv.rf;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class MainClass {

	public static void main(String[] args) {
		String[][] learningSet;

		try {
			learningSet = FileUtils.readLearningSetFromFile("gradesClasses.txt");
			double[][] m = new double[learningSet.length][learningSet.length];
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length;

			double[][] grades = new double[][] { { 3.80 }, { 5.75 }, { 6.25 }, { 7.25 }, { 8.5 }, { 9.1 } };

			int k = 17;

			for (int in = 0; in < grades.length; in++) {
				Map<Double, String> distMap = DistanceUtils.calculateEuclidianDistanceMatrice(learningSet, grades[in]);

				Map<Double, String> randomSelectedData = new TreeMap<Double, String>();

				for (int i = 0; i < k; i++) {
					Object randomName = distMap.keySet().toArray()[new Random()
							.nextInt(distMap.keySet().toArray().length)];
					randomSelectedData.put(Double.parseDouble(randomName.toString()), distMap.get(randomName));
				}

				Map<Double, String> sorteddistMap = new TreeMap<Double, String>(randomSelectedData);

				double total = 0;
				int correct = 0;

				int K = 0;

				for (Map.Entry<Double, String> entry : distMap.entrySet()) {
					double key = entry.getKey();
					String v = entry.getValue();

					total++;
					if (k < 0.9)
						correct++;

					if (correct > 0 && correct / total < 0.9) {
						break;
					}
					K++;
				}

				System.out.printf("K(%s), grade %s: >> output: %s | Score: %s | kMax: %s\n", k, grades[in][0],
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