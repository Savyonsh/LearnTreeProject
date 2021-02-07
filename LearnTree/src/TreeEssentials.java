import java.util.*;

public class TreeEssentials {

    // Array of size PictureSet that saves the pointer inside
    List<Picture>[] pictureSetPointers;
    double[][] pictureSetPointerPixelEntropyArray;
    double[][] pictureSetPointerTypesArray;
//    List<QuestionV2> questionV2List;
    Map<Integer, int[]> questionV2Map;

    int[] vectorTypes;
    int typeOneIndex;
    int typeTwoIndex;
    int mapSize;

    public TreeEssentials(List<Picture> pictureSet) {
        pictureSetPointers = new List[pictureSet.size() + 1];
        pictureSetPointerPixelEntropyArray = new double[pictureSet.size() + 1][Utils.VEC_SIZE];
        Arrays.stream(pictureSetPointerPixelEntropyArray).forEach(doubles -> Arrays.fill(doubles, Double.NEGATIVE_INFINITY));
        pictureSetPointerTypesArray = new double[pictureSet.size() + 1][2];
        vectorTypes = new int[Utils.VEC_SIZE];
        typeOneIndex = -1;
        typeTwoIndex = -1;
        setRecurrencesArray(pictureSet);
//        questionV2List = new LinkedList<>();
        int key = Utils.VEC_SIZE;
        questionV2Map = new HashMap<>();
        questionV2Map.put(key, new int[]{232, 180, 205});
        questionV2Map.put(key+1, new int[]{180, 70, 200});
        questionV2Map.put(key+2, new int[]{182, 209, 236,263, 290, 319});
        questionV2Map.put(key+3, new int[]{176, 232, 288, 344, 400});
        questionV2Map.put(key+4, new int[]{289, 294, 299});
        questionV2Map.put(key+5, new int[]{123, 179, 235, 291, 347, 403, 459});
        questionV2Map.put(key+6, new int[]{455, 465, 475, 485});
        questionV2Map.put(key+7, new int[]{177, 178, 179, 206, 207, 208, 233, 234, 235});
        questionV2Map.put(key+8, new int[]{317, 318, 319, 345, 346, 347, 373, 374, 375});
        /*questionV2Map.put(key, new QuestionV2(new int[]{232, 180, 205}, "Wide Round top"));
        questionV2Map.put(key+1, new QuestionV2(new int[]{180, 70, 200}, "Narrow Round top"));
        questionV2Map.put(key+2, new QuestionV2(new int[]{182, 209, 236,263, 290, 319}, "Diagonal line"));
        questionV2Map.put(key+3, new QuestionV2(new int[]{176, 232, 288, 344, 400}, "Colored straight left line"));
        questionV2Map.put(key+4, new QuestionV2(new int[]{289, 294, 299}, "Colored horizontal center line"));
        questionV2Map.put(key+5, new QuestionV2(new int[]{123, 179, 235, 291, 347, 403, 459}, "Colored vertical line in middle"));
        questionV2Map.put(key+6, new QuestionV2(new int[]{455, 465, 475, 485}, "Flat end"));
        questionV2Map.put(key+7, new QuestionV2(new int[]{177, 178, 179, 206, 207, 208, 233, 234, 235}, "Colored high center"));
        questionV2Map.put(key+8, new QuestionV2(new int[]{317, 318, 319, 345, 346, 347, 373, 374, 375}, "Colored low center"));*/
        this.mapSize = questionV2Map.size();
    }

    public void setRecurrencesArray(List<Picture> validationSet) {
        boolean allLeft = true;
        boolean allRight = true;
        for (int i = 0; i < Utils.VEC_SIZE; i++) {
            for (Picture picture : validationSet) {
                allLeft = allLeft && picture.pixels[i] >= 128;
                allRight = allRight && picture.pixels[i] < 128;
            }
            if (allLeft) {
                vectorTypes[i] = 1;
                if (typeOneIndex == -1) typeOneIndex = i;
            }
            if (allRight) {
                vectorTypes[i] = 2;
                if (typeTwoIndex == -1) typeTwoIndex = i;
            }
            allLeft = true;
            allRight = true;
            //
        }
    }
}
