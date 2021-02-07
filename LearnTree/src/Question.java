public class Question {
    int pixelNum;

    public Question(int pixelNum) {
        this.pixelNum = pixelNum;
    }

    private static boolean askForOther(int num, int[] arr) {
        if (num < arr.length && num >= 0)
            return arr[num] >= 128;
        return true;
    }

    public static boolean ask(int[] arr, int pixelNum, int version) {
        if(version ==1)
            return arr[pixelNum] >= 128;
        else{
            return checkRange(arr, pixelNum);
        }
    }

    public static boolean ask(int[] arr, int[] pixelNum) {

        boolean ans = false;
        for (int i:pixelNum) {
            ans = ans|| checkRange(arr, i);
        }
        return ans;
    }

    private static boolean checkRange(int[] picturesPixels, int i) {
        return (askForOther(i, picturesPixels)) ||
                (askForOther(i + 1, picturesPixels)) ||
                (askForOther(i + 27, picturesPixels)) ||
                (askForOther(i + 28, picturesPixels)) ||
                (askForOther(i + 29, picturesPixels)) ||
                (askForOther(i - 1, picturesPixels)) ||
                (askForOther(i - 27, picturesPixels)) ||
                (askForOther(i - 28, picturesPixels)) ||
                (askForOther(i - 29, picturesPixels));
    }
//    public static boolean ask(int[] arr, int pixelNum, int startRange, int endRange) {
//        return arr[pixelNum] >= startRange && arr[pixelNum] <= endRange;
//    }
}
