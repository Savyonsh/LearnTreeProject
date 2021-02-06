public class QuestionV2 {

    int[] setOfPixels;
    String question;
    ///

    public QuestionV2(int[] setOfPixels, String question) {
        this.setOfPixels = setOfPixels;
        this.question = question;
    }

    public boolean ask(int[] picturesPixels) {
        boolean ans = true;
        for (int setOfPixel : setOfPixels) {
            ans = ans && checkRange(picturesPixels, setOfPixel);
        }
        return ans;
    }

    private boolean checkRange(int[] picturesPixels, int i) {
        return picturesPixels[i] >= 128 ||
                (i + 1 < picturesPixels.length && picturesPixels[i + 1] >= 128) ||
                (i + 27 < picturesPixels.length && picturesPixels[i + 27] >= 128) ||
                (i + 28 < picturesPixels.length && picturesPixels[i + 28] >= 128) ||
                (i + 29 < picturesPixels.length && picturesPixels[i + 29] >= 128) ||
                (i - 29 >= 0 && picturesPixels[i - 29] >= 128) ||
                (i - 28 >= 0 && picturesPixels[i - 28] >= 128) ||
                (i - 27 >= 0 && picturesPixels[i - 27] >= 128) ||
                (i - 1 >= 0 && picturesPixels[i - 1] >= 128);
    }
}
