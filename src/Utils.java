import java.util.Arrays;
import java.util.List;

public class Utils {

    static int mostFrequent(int[] arr, int n)
    {
        // Sort the array
        Arrays.sort(arr);

        // find the max frequency using linear
        // traversal
        int max_count = 1, res = arr[0];
        int curr_count = 1;

        for (int i = 1; i < n; i++)
        {
            if (arr[i] == arr[i - 1])
                curr_count++;
            else
            {
                if (curr_count > max_count)
                {
                    max_count = curr_count;
                    res = arr[i - 1];
                }
                curr_count = 1;
            }
        }

        // If last element is most frequent
        if (curr_count > max_count)
        {
            max_count = curr_count;
            res = arr[n - 1];
        }

        return res;
    }

    static int[] countingPics(List<Picture> picSet){
        int[] arr = new int[10];
        for (Picture pic: picSet) {
            arr[pic.label]++;
        }
        return arr;
    }

    static int getMaxIndex(int[] arr){
        int max = 0;
        for (int i = 1; i < arr.length; i++) {
            if(arr[i]>= arr[max])
                max = i;
        }
        return max;
    }

    static double calculateEntropy(int[] arr){
        double entropy =0;
        double nl = Arrays.stream(arr).sum();
        for(int nli : arr) {
            if(nli != 0) {
                double log = Math.log(nl/ nli);
                entropy += (nli / nl) * log;
            }
        }
        return entropy;
    }

}
