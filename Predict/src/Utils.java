import java.util.Arrays;
import java.util.List;

public class Utils {

    public static class Pair<T1, T2> {
        T1 car;
        T2 cdr;

        public Pair(T1 car, T2 cdr) {
            this.car = car;
            this.cdr = cdr;
        }
    }

    public static final int VEC_SIZE = 784;

    static int mostFrequent(int[] arr) {
        return mostFrequent(arr, arr.length);
    }

    static int mostFrequent(int[] arr, int n) {
        // Sort the array
        Arrays.sort(arr);

        // find the max frequency using linear
        // traversal
        int max_count = 1, res = arr[0];
        int curr_count = 1;

        for (int i = 1; i < n; i++) {
            if (arr[i] == arr[i - 1])
                curr_count++;
            else {
                if (curr_count > max_count) {
                    max_count = curr_count;
                    res = arr[i - 1];
                }
                curr_count = 1;
            }
        }

        // If last element is most frequent
        if (curr_count > max_count) {
            max_count = curr_count;
            res = arr[n - 1];
        }

        return res;
    }
/*
    static int getLabelsData(List<Picture> picSet, int[] output, int[] oFrequentLabel) {
        int sum = 0;
        int maxLabelValue = Integer.MIN_VALUE;
        for (Picture pic : picSet) {
            output[pic.label]++;
            if (output[pic.label] > maxLabelValue) {
                maxLabelValue = output[pic.label];
                oFrequentLabel[0] = pic.label;
            }
            sum++;
        }
        return sum;
    }

    static int getMaxIndex(int[] arr) {
        int max = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] >= arr[max])
                max = i;
        }
        return max;
    }*/

    static double calculateEntropy(int[] arr, double nl) {
        double entropy = 0;
        for (int nli : arr) {
            if (nli != 0) {
                double log = Math.log(nl / nli);
                entropy += (nli / nl) * log;
            }
        }
        return entropy;
    }

}
