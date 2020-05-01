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

	public static Map<Double, String> calculateEuclidianDistanceMatrice(String[][] learningSet, double[] city, int nr) {
		double[][] mat = new double[learningSet.length][2];

		Map<Double, String> distMap = new HashMap<Double, String>();
		nr = nr == 0 ? learningSet.length : (nr <= learningSet.length ? nr : learningSet.length);

		for (int i = 1; i < nr; i++) {
			double[] c = new double[] { Double.parseDouble(learningSet[i][0]), Double.parseDouble(learningSet[i][1]) };
			mat[i][0] = calculateEuclidianDistance(city, c);

			distMap.put(mat[i][0], learningSet[i][3]);
		}

		return distMap;
	}

}