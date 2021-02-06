import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Program {

    Instant start = Instant.now();
    int version;

    public void run(int version, String inputFileName, String outputFileName,  double validPercent, int maxPowTwo) throws IOException {
        this.version = version;
        List<Picture> validationSet = new LinkedList<>();
        List<Picture> trainingSet = new LinkedList<>();

        setValidationAndTrainingSet(inputFileName, validationSet, trainingSet, validPercent);
        Tree decisionTree = runTreeOnTrainingSet(trainingSet, getTreeSize(maxPowTwo, validationSet));
        write(decisionTree, outputFileName);

    }

    private void write(Tree tree, String path) throws IOException {
        String str = this.version + " ";
        str += tree.toString();
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        try {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.close();
//        System.out.println("debug: total time: " + Duration.between(start, Instant.now()).toSeconds());
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
            Tree currentTree = new Tree(validationSet, null, null, (int) size, this.version);
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
//        System.out.println("debug: total time: " + Duration.between(start, Instant.now()).toSeconds());
//        System.out.println("debug: best size: " + (int) bestSize);
//        System.out.printf("debug: successRate: %.3f\n", bestSuccessRate);
        return (int) bestSize;
    }

    private Tree runTreeOnTrainingSet(List<Picture> trainingSet, int treeSize) {
        Tree predicationTree = new Tree(trainingSet, null, null, treeSize, this.version);
        do {
            predicationTree.act();
        } while (predicationTree.root.totalNodes < treeSize);

        System.out.println("debug: total time: " + Duration.between(start, Instant.now()).toSeconds());
        System.out.println("num: " + trainingSet.size());
        System.out.println("error: " + (100 - Math.round(predicationTree.getSuccessRate() * 100)));
        System.out.println("size: " + treeSize);
        return  predicationTree;
    }


}
