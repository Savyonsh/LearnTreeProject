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

    public boolean ask(int[] arr) {
        if (this.version == 1)
            return arr[this.pixelNum] >= 128;
        else {
            boolean ans = false;
            if (askForOther(pixelNum, arr))
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
                ans = true;
            return ans;
        }
    }

    public static boolean ask(int[] arr, int pixelNum, int version) {
        if (version == 1)
            return arr[pixelNum] >= 128;
        else {
            boolean ans = false;
            if (askForOther(pixelNum, arr))
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
                ans = true;
            return ans;
        }
    }
}
