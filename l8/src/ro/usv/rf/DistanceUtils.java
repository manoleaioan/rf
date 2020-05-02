package ro.usv.rf;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Map;

public class DistanceUtils {
	protected static double calculateEuclidianDistance(double[] pattern1, double[] pattern2) {
		double sum = 0.0;
		for (int i = 0; i < pattern1.length; i++) {
			sum += Math.pow(pattern1[i] - pattern2[i], 2);
		}
		return Math.sqrt(sum);
	}

	protected static double calculateCebisevDistance(double[] pattern1, double[] pattern2) {
		double[] res = new double[pattern1.length];

		for (int i = 0; i < pattern1.length; i++)
			res[i] = Math.abs(pattern1[i] - pattern2[i]);

		DoubleSummaryStatistics stat = Arrays.stream(res).summaryStatistics();

		double max = stat.getMax();
		return max;
	}

	protected static double calculateCityBlockDistance(double[] pattern1, double[] pattern2) {
		double sum = 0.0;

		for (int i = 0; i < pattern1.length; i++)
			sum += Math.abs(pattern1[i] - pattern2[i]);

		return sum;
	}

	protected static double calculateMahalanobisDistance(double[] pattern1, double[] pattern2, int nr) {
		double sum = 0.0;
		for (int i = 0; i < pattern1.length; i++)
			sum += Math.pow(pattern1[i] - pattern2[i], nr);

		return Math.pow(sum, 1.0 / nr);
	}

	public static Map<Double, String> calculateEuclidianDistanceMatrice(String[][] learningSet,
			String[][] trainingSet) {

		Map<Double, String> distMap = new HashMap<Double, String>();

		for (int i = 0; i < learningSet.length; i++) {
			double[] set1 = new double[] { Double.parseDouble(trainingSet[i][0]) };
			double[] set2 = new double[] { Double.parseDouble(learningSet[i][0]) };
			distMap.put(calculateEuclidianDistance(set1, set2), trainingSet[i][4]);
		}

		return distMap;
	}

}
