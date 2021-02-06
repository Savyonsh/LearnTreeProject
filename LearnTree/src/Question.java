public class Question {

    private static boolean askForOther(int num, int[] arr){
        if(num < arr.length && num >=0)
            return arr[num] >= 128;
        return true;
    }

    private static boolean askNine(int center, int[] arr){
        boolean ans = false;
        if(askForOther(center, arr))
            ans = true;
        else if (askForOther(center+1, arr))
            ans = true;
        else if (askForOther(center-1, arr))
            ans = true;
        else if (askForOther(center+2, arr))
            ans = true;
        else if (askForOther(center-2, arr))
            ans = true;
        else if (askForOther(center+27, arr))
            ans = true;
        else if (askForOther(center+28, arr))
            ans = true;
        else if (askForOther(center+29, arr))
            ans = true;
        else if (askForOther(center-27, arr))
            ans = true;
        else if (askForOther(center-28, arr))
            ans = true;
        else if (askForOther(center-29, arr))
            ans = true;
        return  ans;
    }

    public static boolean ask(int[] arr, int pixelNum, int version) {
        if(version == 1)
            return arr[pixelNum] >=128;
        else
            return askNine(pixelNum, arr);
    }
}
