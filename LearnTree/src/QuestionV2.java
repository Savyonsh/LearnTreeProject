
import java.util.Arrays;
import java.util.Map;

public class QuestionV2 {}
/*
    int[] setOfPixels;
    String question;

    public QuestionV2(int[] setOfPixels, String question) {
        this.setOfPixels = setOfPixels;
        this.question = question;
    }

    private static boolean askForOther(int num, int[] arr) {
        if (num < arr.length && num >= 0)
            return arr[num] >= 128;
        return true;
    }

    public boolean ask(int[] picturesPixels) {
        boolean ans = false;
        for (int setOfPixel : setOfPixels) {
            ans = ans || checkRange(picturesPixels, setOfPixel);
        }
        return ans;
    }

    public static boolean ask(int[] picturesPixels, int[]setOfPixels) {
        boolean ans = false;
        for (int setOfPixel : setOfPixels) {
            ans = ans || checkRange(picturesPixels, setOfPixel);
        }
        return ans;
    }

    public String toString(){
        return this.question + " " + Arrays.toString(this.setOfPixels);
    }

    private static boolean checkRange(int[] picturesPixels, int i) {
        return (askForOther(i,picturesPixels)) ||
                (askForOther(i+1,picturesPixels)) ||
                (askForOther(i+27,picturesPixels)) ||
                (askForOther(i+28,picturesPixels)) ||
                (askForOther(i+29,picturesPixels)) ||
                (askForOther(i-1,picturesPixels)) ||
                (askForOther(i-27,picturesPixels)) ||
                (askForOther(i-28,picturesPixels)) ||
                (askForOther(i-29,picturesPixels)) ;
//                    picturesPixels[i] >= 128 ||
//                (i + 27 < picturesPixels.length && picturesPixels[i + 27] >= 128) ||
//                (i + 28 < picturesPixels.length && picturesPixels[i + 28] >= 128) ||
//                (i + 29 < picturesPixels.length && picturesPixels[i + 29] >= 128) ||
//                (i - 29 >= 0 && picturesPixels[i - 29] >= 128) ||
//                (i - 28 >= 0 && picturesPixels[i - 28] >= 128) ||
//                (i - 27 >= 0 && picturesPixels[i - 27] >= 128) ||
//                (i - 1 >= 0 && picturesPixels[i - 1] >= 128);
    }
}
*/
