package ro.usv.rf;

import java.util.HashMap;
import java.util.Map;

public class StatisticsUtils {

	protected static double calculateFeatureAverage(Double[] feature) {
		Map<Double, Integer> counterMap = getFeatureDistincElementsCounterMap(feature);
		double featureAverage = 0;

		double sum1 = 0;
		double sum2 = 0;

		sum1 = counterMap.keySet().stream().mapToDouble(x -> calculateSum1(x, 0.0 + counterMap.get(x))).sum();
		sum2 = counterMap.values().stream().mapToInt(x -> x).sum();
		featureAverage = sum1 / sum2;
		System.out.println("The feature average is: " + featureAverage);
		return featureAverage;
	}

	protected static Map<Double, Integer> getFeatureDistincElementsCounterMap(Double feature[]) {
		Map<Double, Integer> counterMap = new HashMap<Double, Integer>();
		for (int j = 0; j < feature.length; j++) {
			if (counterMap.containsKey(feature[j])) {
				int count = counterMap.get(feature[j]);
				counterMap.put((feature[j]), ++count);
			} else {
				counterMap.put((feature[j]), 1);
			}
		}
		return counterMap;
	}

	private static Double calculateSum1(double value, Double double1) {
		return double1 * value;
	}

	protected static double calculateFeatureWeightedAverage(Double[] feature, Double[] weights) {
		double featureWeightedAverage = 0.0;
		Map<Double, Double> weightsMap = getWeightsMap(feature, weights);

		double sum1 = weightsMap.keySet().stream().mapToDouble(x -> calculateSum1(x, weightsMap.get(x))).sum();
		double sum2 = weightsMap.values().stream().mapToDouble(x -> x).sum();

		featureWeightedAverage = sum1 / sum2;

		return featureWeightedAverage;
	}

	private static Map<Double, Double> getWeightsMap(Double[] feature, Double[] weights) {
		Map<Double, Double> weightsMap = new HashMap<Double, Double>();
		for (int j = 0; j < feature.length; j++) {
			if (weightsMap.containsKey(feature[j])) {
				double weight = weightsMap.get(feature[j]);
				weightsMap.put(feature[j], weight + weights[j]);
			} else {
				weightsMap.put(feature[j], weights[j]);
			}
		}
		return weightsMap;
	}

	protected static double calculateFrequencyOfOccurence(Map<Double, Integer> counterMap, double featureElement) {
		int nrEl = counterMap.values().stream().mapToInt(x -> x).sum();
		double nrFeature = counterMap.get(featureElement);
		return nrFeature / nrEl;
	}

	protected static double calculateFeatureDispersion(Double[] feature, double featureWeightedAverage) {
		double sum = 0.0;
		double diff = 0.0;
		double n = feature.length - 1;
		for (int i = 0; i <= n; i++) {
			diff = feature[i] - featureWeightedAverage;
			sum += Math.pow(diff, 2);
		}

		return (1 / n) * sum;
	}

	protected static double calculateCovariance(Double[] feature1, Double[] feature2, double feature1WeightedAverage,
			double feature2WeightedAverage) {
		double n = ((feature1.length + feature2.length) / 2) - 1;
		double sum = 0.0;
		double diff1 = 0.0;
		double diff2 = 0.0;

		for (int i = 0; i <= n; i++) {
			diff1 = feature1[i] - feature1WeightedAverage;
			diff2 = feature2[i] - feature2WeightedAverage;
			sum += diff1 * diff2;
		}

		return (1 / n) * sum;
	}

	protected static double calculateCorrelationCoefficient(double covariance, double feature1Dispersion,
			double feature2Dispersion) {
		return covariance / Math.sqrt(feature1Dispersion * feature2Dispersion);
	}

	protected static double calculateAverageSquareDeviation(double featureDispersion) {
		return Math.sqrt(featureDispersion);
	}
}
