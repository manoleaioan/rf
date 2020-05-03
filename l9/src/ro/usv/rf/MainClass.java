package ro.usv.rf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;

public class MainClass {

	public static void main(String[] args) {
		double[][] learningSet;

		try {
			learningSet = FileUtils.readLearningSetFromFile("in.txt");
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length;
			double[][] output = new double[numberOfPatterns][numberOfFeatures];
			System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns,
					numberOfFeatures));

			int noofclusters = 2;
			double centroid[][] = new double[][] { { 2, 10 }, { 5, 8 } };
			double prevCentroid[][] = new double[][] { { 0, 0 }, { 0, 0 } };

			Map<Double, List<double[]>> clousters_elem = getCentroid(learningSet, noofclusters, centroid, prevCentroid);

			int line = 0;
			for (Entry<Double, List<double[]>> entry : clousters_elem.entrySet()) {
				double clouster = entry.getKey();
				for (int i2 = 0; i2 < entry.getValue().size(); i2++) {
					double x = entry.getValue().get(i2)[0];
					double y = entry.getValue().get(i2)[1];
					output[line] = new double[] { x, y, clouster };
					line++;
				}
			}
			FileUtils.writeLearningSetToFile("out.csv", output);
		} catch (

		USVInputFileCustomException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("\n\nFinished learning set operations");
		}
	}

	public static Map<Double, List<double[]>> getCentroid(double data[][], int noofclusters, double centroid[][],
			double previousCentroid[][]) {
		Map<Double, List<double[]>> clousters_elem = new HashMap<Double, List<double[]>>();

		for (int i = 0; i < data.length; i++) {
			double[][] distances = new double[data.length][noofclusters];
			// calculeaza distantele dintre fiecare punct si cluster
			for (int j = 0; j < noofclusters; j++) {
				distances[i][j] = distance(data[i], centroid[j]);
				System.out.printf("%.1f \t", distance(data[i], centroid[j]));
			}

			// determina clusterul cel mai apropiat
			double min = Arrays.stream(distances[i]).min().getAsDouble();
			double[] selectedDist = distances[i];
			double clouster = IntStream.range(0, distances[i].length).filter(v -> min == selectedDist[v]).findFirst()
					.orElse(-1) + 1;

			// memoreaza punctele in clusterul detectat
			if (clousters_elem.containsKey(clouster)) {
				clousters_elem.get(clouster).add(data[i]);
			} else {
				List<double[]> list = new ArrayList<>();
				list.add(data[i]);
				clousters_elem.put(clouster, list);
			}

			System.out.printf("C%.0f", clouster);
			System.out.println();
		}

		// reacalculeaza pozitia clusterului
		for (Entry<Double, List<double[]>> entry : clousters_elem.entrySet()) {
			double clouster = entry.getKey();
			System.out.printf("\nC%.0f : ", entry.getKey());
			for (int i2 = 0; i2 < entry.getValue().size(); i2++) {
				double x = entry.getValue().get(i2)[0];
				double y = entry.getValue().get(i2)[1];
				System.out.printf("[%.0f, %.0f] ", x, y);
			}

			double[] newCentroid = recalculate_cluster_center(entry.getValue());
			System.out.printf("=> new center: (%.2f, %.2f)", newCentroid[0], newCentroid[1]);
			previousCentroid[(int) (clouster - 1)] = centroid[(int) (clouster - 1)];
			centroid[(int) (clouster - 1)] = newCentroid;
		}

		System.out.println("\n\n========================================\n");
		// opreste cautarea daca noile clustere contine aceleasi puncte
		boolean found = Arrays.deepEquals(centroid, previousCentroid);
		if (!found)
			getCentroid(data, noofclusters, centroid, previousCentroid);

		return clousters_elem;
	}

	public static double[] recalculate_cluster_center(List<double[]> points) {
		double x = 0.0;
		double y = 0.0;
		int len = points.size();
		for (int i = 0; i < len; i++) {
			x += points.get(i)[0];
			y += points.get(i)[1];
		}
		return new double[] { x / len, y / len };
	}

	public static double distance(double[] p1, double[] p2) {
		return Math.abs(p2[0] - p1[0]) + Math.abs(p2[1] - p1[1]);
	}
}