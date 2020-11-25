import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static int version;
    private static double validPercent;
    private static int maxPowTwo;
    private static File trainingSet;
    private static File outputTree;
    private static List<Picture> validPicSet;

    public static void main(String[] args) {
        version = Integer.parseInt(args[0]);
        validPercent = Double.parseDouble(args[1]);
        validPercent = validPercent/100;
        maxPowTwo = Integer.parseInt(args[2]);
        outputTree = new File (args[4]);
//        int[] NArray = new int[10];
//        Node root = new Node(1, new Question(12),NArray );
        try {

            CSVReader myReader = new CSVReader(args[3]);
            int validSize = (int)Math.ceil(validPercent*myReader.getNumLines());
            for (int i = 0; i < validSize && myReader.hasNextLine(); i++) {
                int [] pixelVector = myReader.getPixelNextVector();
                int label = pixelVector[0];
                pixelVector =  Arrays.copyOfRange(pixelVector, 1, pixelVector.length);
                Picture myPic = new Picture(label, pixelVector);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("There is a problem with the training set.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("There is a problem with I/O.");
            e.printStackTrace();
        }
    }
}
