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
    private static double bestSizeTree;

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
            double bestSize = 0;
            double bestIG = Integer.MIN_VALUE;
            for (int i = 0; i <= maxPowTwo; i++) {
                double size = Math.pow(2, i);
                Tree currentTree = new Tree(validationSet, null);
                Tree.leaves.clear();
                Tree.leaves.add(currentTree);
                do {
                    currentTree.act();
                } while (currentTree.treeSize < size);
                double IG = currentTree.totalIG;
                if (IG > bestIG) {
                    bestSize = size;
                    bestIG = IG;
                }
            }
            System.out.println("Best Size is: " + bestSize + "\nwith IG: " + bestIG);
            bestSizeTree = bestSize;

            /*
             * to pass with the validation set to find the best tree size
             * later, pass with the training set to create the actual tree
             * */
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
