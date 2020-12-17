public class Question {
    int pixelNum;
    int label;

    public Question(int label, int pixelNum){
        this.pixelNum = pixelNum;
        this.label = label;
    }

    public boolean ask(int[] arr) {
        return arr[pixelNum] >= 128;
    }
}
