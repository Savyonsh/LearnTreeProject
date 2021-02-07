public class Question {
    int pixelNum;
    int version;
    int[] pixelsNums;

    public Question(int pixelNum, int version) {
        this.pixelNum = pixelNum;
        this.version = version;
        this.pixelsNums = null;
    }
    public Question(int[] pixelsNums){
        this.pixelNum = -1;
        this.version = 2;
        this.pixelsNums = pixelsNums;
    }

    private static boolean askForOther(int num, int[] arr) {
        if (num < arr.length && num >= 0)
            return arr[num] >= 128;
        return true;
    }

    private boolean checkRange(int[] picturesPixels, int i) {
        return  (askForOther(i, picturesPixels)) ||
                (askForOther(i + 1, picturesPixels)) ||
                (askForOther(i + 27, picturesPixels)) ||
                (askForOther(i + 28, picturesPixels)) ||
                (askForOther(i + 29, picturesPixels)) ||
                (askForOther(i - 1, picturesPixels)) ||
                (askForOther(i - 27, picturesPixels)) ||
                (askForOther(i - 28, picturesPixels)) ||
                (askForOther(i - 29, picturesPixels));
    }

    public boolean ask(int[] arr) {
        if (this.version == 1)
            return arr[this.pixelNum] >= 128;
        else {
            if(this.pixelsNums != null){
                boolean ans = false;
                for (int pix:pixelsNums) {
                    ans  = ans || checkRange(arr, pix);
                }
                return ans;
            }
            return checkRange(arr, this.pixelNum);
        }
    }
}
