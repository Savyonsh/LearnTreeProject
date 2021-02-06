public class Question {
    int pixelNum;
    int version;

    public Question(int pixelNum, int version) {
        this.pixelNum = pixelNum;
        this.version = version;
    }

    private boolean askForOther(int num, int[] arr) {
        if (num < arr.length && num >= 0)
            return arr[num] >= 128;
        return true;
    }


    private boolean askNine(int center, int[] arr) {
        boolean ans = false;
        if (askForOther(center, arr))
            ans = true;
        else if (askForOther(center + 1, arr))
            ans = true;
        else if (askForOther(center - 1, arr))
            ans = true;
        else if (askForOther(center + 2, arr))
            ans = true;
        else if (askForOther(center - 2, arr))
            ans = true;
        else if (askForOther(center + 27, arr))
            ans = true;
        else if (askForOther(center + 28, arr))
            ans = true;
        else if (askForOther(center + 29, arr))
            ans = true;
        else if (askForOther(center - 27, arr))
            ans = true;
        else if (askForOther(center - 28, arr))
            ans = true;
        else if (askForOther(center - 29, arr))
            ans = true;
        return ans;
    }

    public boolean ask(int[] arr) {
        if (this.version == 1)
            return arr[this.pixelNum] >= 128;
        else
            return askNine(this.pixelNum, arr);
    }
}
