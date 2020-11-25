

public class Question {
    public int pixelNum;
    public Question(int num){
        pixelNum = num;
    }

    public boolean ask(int[] arr){
        if (arr[pixelNum] >= 128){
            return true;
        }
        return false;
    }
}
