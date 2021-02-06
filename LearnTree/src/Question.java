public class Question {
    int pixelNum;
    int version;

    public Question(int pixelNum, int version) {
        this.pixelNum = pixelNum;
        this.version = version;
    }

    private static boolean askForOther(int num, int[] arr) {
        if (num < arr.length && num >= 0)
            return arr[num] >= 128;
        return true;
    }
//
    public static boolean ask(int[] arr, int pixelNum, int version) {
        if (version == 1)
            return arr[pixelNum] >= 128;
        else {
            return askForOther(pixelNum, arr) ||
                    askForOther(pixelNum + 1, arr) ||
                    askForOther(pixelNum - 1, arr) ||
                    askForOther(pixelNum + 27, arr) ||
                    askForOther(pixelNum - 27, arr) ||
                    askForOther(pixelNum + 28, arr) ||
                    askForOther(pixelNum - 28, arr) ||
                    askForOther(pixelNum + 29, arr) ||
                    askForOther(pixelNum - 29, arr);
            /*if (askForOther(pixelNum, arr))
                ans = true;
            else if (askForOther(pixelNum + 1, arr))
                ans = true;
            else if (askForOther(pixelNum - 1, arr))
                ans = true;
            else if (askForOther(pixelNum + 27, arr))
                ans = true;
            else if (askForOther(pixelNum + 28, arr))
                ans = true;
            else if (askForOther(pixelNum + 29, arr))
                ans = true;
            else if (askForOther(pixelNum - 27, arr))
                ans = true;
            else if (askForOther(pixelNum - 28, arr))
                ans = true;
            else if (askForOther(pixelNum - 29, arr))
                ans = true;*/
            //return ans;
        }
    }

    public static boolean ask(int[] arr, int pixelNum, int startRange, int endRange) {
        return arr[pixelNum] >= startRange && arr[pixelNum] <= endRange;
    }
}
