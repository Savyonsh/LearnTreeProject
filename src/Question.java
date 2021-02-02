public class Question {
    int pixelNum;

    public Question(int pixelNum){
        this.pixelNum = pixelNum;
    }

    public boolean ask(int[] arr) {
        return arr[pixelNum] >= 128;
    }

    public String toString(){
        return "Is the pixel " + this.pixelNum + " is dark?";
    }
}
