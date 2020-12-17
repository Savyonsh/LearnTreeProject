import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main {

    private static int version;
    private static double validPercent;
    private static int maxPowTwo;
    private static File outputTree;
    private static List<Picture> validationSet;
    private static List<Picture> trainingSet;

    public static void main(String[] args) {
        version = Integer.parseInt(args[0]);
        validPercent = Double.parseDouble(args[1]);
        //validPercent = validPercent / 100;
        maxPowTwo = Integer.parseInt(args[2]);
        outputTree = new File(args[4]);
        validationSet = new LinkedList<>();
        trainingSet = new LinkedList<>();
        Random random = new Random();
        try {
            CSVReader myReader = new CSVReader(args[3]);
//            int validSize = (int) Math.ceil(validPercent * myReader.getNumLines());
            while (myReader.hasNextLine()) {
                int randomizeNumber = random.nextInt(100);
                if (randomizeNumber <= validPercent)
                    validationSet.add(myReader.getNextPicture());
                else
                    trainingSet.add(myReader.getNextPicture());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("There is a problem with the training set.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("There is a problem with I/O.");
            e.printStackTrace();
        }
        Utils.mostFrequent(trainingSet.stream().mapToInt(picture -> picture.label).toArray(), trainingSet.size());
    }
}
