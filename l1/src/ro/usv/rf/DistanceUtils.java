package ro.usv.rf;

import java.util.TreeMap;

public class DistanceUtils {

	public static double calculateEuclidianDistance(double pattern1[], double pattern2[]) {

		double suma = 0.0;
		for (int i = 0; i < pattern1.length; i++) {
			suma += Math.pow((pattern1[i] - pattern2[i]), 2);
		}

		return Math.floor(Math.sqrt(suma) * 100.0) / 100.0;
	}

	public static double calculateCebesivDistance(double pattern1[], double pattern2[]) {
		double max = 0.0;

		for (int i = 0; i < pattern1.length; i++) {
			max = Double.max(max, Math.abs(pattern1[i] - pattern2[i]));
		}
		return max;
	}

	public static String[][] ObtainingNearestNeighbors(TreeMap<Double, String> treeMap) {
		String[][] finalSet = new String[31][2];
		int nearestNeighbor = 0;
		for (java.util.Map.Entry<Double, String> entry : treeMap.entrySet()) {
			if (nearestNeighbor < 31) {
				finalSet[nearestNeighbor][0] = String.valueOf(entry.getKey());
				finalSet[nearestNeighbor][1] = entry.getValue();
				nearestNeighbor++;
			} else {
				break;
			}
		}
		return finalSet;
	}
}
