import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TreeEssentials {

    // Array of size PictureSet that saves the pointer inside
    List<Picture>[] pictureSetPointers;
    double[][] pictureSetPointerPixelEntropyArray;
    double[][] pictureSetPointerTypesArray;
    int[] vectorTypes;
    int typeOneIndex;
    int typeTwoIndex;

    public TreeEssentials(List<Picture> pictureSet) {
        pictureSetPointers = new List[pictureSet.size() + 1];
        pictureSetPointerPixelEntropyArray = new double[pictureSet.size() + 1][Utils.VEC_SIZE];
        Arrays.stream(pictureSetPointerPixelEntropyArray).forEach(doubles -> Arrays.fill(doubles, Double.NEGATIVE_INFINITY));
        pictureSetPointerTypesArray = new double[pictureSet.size() + 1][2];
        vectorTypes = new int[Utils.VEC_SIZE];
        typeOneIndex = -1;
        typeTwoIndex = -1;
        setRecurrencesArray(pictureSet);
    }

    public void setRecurrencesArray(List<Picture> validationSet) {
        boolean allLeft = true;
        boolean allRight = true;
        for (int i = 0; i < Utils.VEC_SIZE; i++) {
            for (Picture picture : validationSet) {
                allLeft = allLeft && picture.pixels[i] >= 128;
                allRight = allRight && picture.pixels[i] < 128;
            }
            if(allLeft) {
                vectorTypes[i] = 1;
                if(typeOneIndex == -1) typeOneIndex = i;
            }
            if(allRight) {
                vectorTypes[i] = 2;
                if(typeTwoIndex == -1) typeTwoIndex = i;
            }
            allLeft = true;
            allRight = true;
        }
    }
}
