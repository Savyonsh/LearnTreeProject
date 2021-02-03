import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Main {

    private static int version;
    private static double validPercent;
    private static int maxPowTwo;
    private static File outputTree;
    private static List<Picture> validationSet;
    private static List<Picture> trainingSet;

    public static void main(String[] args) {
        Instant start = Instant.now();
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
            while (myReader.hasNextLine()) {
                Picture picture = myReader.getNextPicture();
                int randomizeNumber = random.nextInt(100);
                if (randomizeNumber <= validPercent)
                    validationSet.add(picture);
                else
                    trainingSet.add(picture);


            }
            myReader.close();
            double bestSize = 0;
            double bestSuccessRate = Double.MIN_VALUE;
            for (int i = 0; i <= maxPowTwo; i++) {
                double size = Math.pow(2, i);
                Tree currentTree = new Tree(validationSet, null);
                Tree.leaves.clear();
                Tree.leaves.add(currentTree);
                do {
                    currentTree.act();
                    System.out.println("tree size: " + currentTree.innerNode);
                } while (currentTree.innerNode < size);
                double currentSuccessRate = currentTree.getSuccessRate();
                System.out.println("debug: successRate: " + currentSuccessRate);
                if (currentSuccessRate > bestSuccessRate) {
                    bestSize = size;
                    bestSuccessRate = currentSuccessRate;
                }
            }
            System.out.println("debug: total time: " + Duration.between(start, Instant.now()).toSeconds());


            System.out.println("debug: best size: " + bestSize);
            System.out.println("debug: successRate: " + bestSuccessRate);

            /*Tree predicationTree = new Tree(trainingSet, null);
            Tree.leaves.add(predicationTree);
            do {
                predicationTree.act();
            } while (predicationTree.treeSize < bestSize);

            System.out.println("num: " + trainingSet.size());
            System.out.println("error: " + (100 - Math.round(predicationTree.getSuccessRate() * 100)));
            System.out.println("size: " + bestSize);

*/

        } catch (FileNotFoundException e) {
            System.out.println("There is a problem with the training set.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("There is a problem with I/O.");
            e.printStackTrace();
        }
    }
}
