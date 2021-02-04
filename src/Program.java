import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Program {

    Instant start = Instant.now();

    public void run(String inputFileName, double validPercent, int maxPowTwo) throws IOException {
        List<Picture> validationSet = new LinkedList<>();
        List<Picture> trainingSet = new LinkedList<>();

        setValidationAndTrainingSet(inputFileName, validationSet, trainingSet, validPercent);
        runTreeOnTrainingSet(trainingSet, getTreeSize(maxPowTwo, validationSet));
    }

    private void setValidationAndTrainingSet(String inputFileName, List<Picture> validationSet,
                                             List<Picture> trainingSet, double validPercent) throws IOException {
        Random random = new Random();
        CSVReader myReader = new CSVReader(inputFileName);
        while (myReader.hasNextLine()) {
            Picture picture = myReader.getNextPicture();
            int randomizeNumber = random.nextInt(100);
            if (randomizeNumber <= validPercent)
                validationSet.add(picture);
            else
                trainingSet.add(picture);
        }
        myReader.close();
    }

    private int getTreeSize(int maxPowTwo, List<Picture> validationSet) {
        double bestSize = 0;
        double bestSuccessRate = Double.MIN_VALUE;
        for (int i = 0; i <= maxPowTwo; i++) {
            double size = Math.pow(2, i);
            Tree currentTree = new Tree(validationSet, null, null);
            do {
                currentTree.act();
            } while (currentTree.root.totalNodes < size);
            double currentSuccessRate = currentTree.getSuccessRate();
            //System.out.println("debug: successRate: " + currentSuccessRate);
            if (currentSuccessRate > bestSuccessRate) {
                bestSize = size;
                bestSuccessRate = currentSuccessRate;
            }
        }
        System.out.println("debug: total time: " + Duration.between(start, Instant.now()).toSeconds());
        System.out.println("debug: best size: " + bestSize);
        System.out.println("debug: successRate: " + bestSuccessRate);
        return (int) bestSize;
    }

    private void runTreeOnTrainingSet(List<Picture> trainingSet, int treeSize) {
        Tree predicationTree = new Tree(trainingSet, null, null);
        do {
            predicationTree.act();
        } while (predicationTree.root.totalNodes < treeSize);

        System.out.println("debug: total time: " + Duration.between(start, Instant.now()).toSeconds());
        System.out.println("num: " + trainingSet.size());
        System.out.println("error: " + (100 - Math.round(predicationTree.getSuccessRate() * 100)));
        System.out.println("size: " + treeSize);
    }
}
