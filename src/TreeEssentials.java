import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TreeEssentials {

    // Array of size PictureSet that saves the pointer inside
    List<Picture>[] pictureSetPointers;
    double[][] pictureSetPointerPixelEntropyArray;

    public TreeEssentials(int pictureSetSize) {
        pictureSetPointers = new List[pictureSetSize + 1];
        pictureSetPointerPixelEntropyArray = new double[pictureSetSize + 1][Utils.VEC_SIZE];
        Arrays.stream(pictureSetPointerPixelEntropyArray).forEach(doubles -> Arrays.fill(doubles, Double.NEGATIVE_INFINITY));
    }
}
