import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Program {

    public void run(String inputFileName, double validPercent, int maxPowTwo) {
        Instant start = Instant.now();
        /*version = Integer.parseInt(args[0]);
        validPercent = Double.parseDouble(args[1]);
        //validPercent = validPercent / 100;
        maxPowTwo = Integer.parseInt(args[2]);
        outputTree = new File(args[4]);*/
        List<Picture> validationSet = new LinkedList<>();
        List<Picture> trainingSet = new LinkedList<>();
        Random random = new Random();
        try {
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
            double bestSize = 0;
            double bestSuccessRate = Double.MIN_VALUE;
            for (int i = 0; i <= maxPowTwo; i++) {
                double size = Math.pow(2, i);
                Tree2 currentTree = new Tree2(validationSet, null, null);
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
/*            System.out.println("debug: total time: " + Duration.between(start, Instant.now()).toSeconds());
            System.out.println("debug: best size: " + bestSize);
            System.out.println("debug: successRate: " + bestSuccessRate);*/

            Tree2 predicationTree = new Tree2(trainingSet, null, null);
            do {
                predicationTree.act();
            } while (predicationTree.root.totalNodes < bestSize);

            System.out.println("debug: total time: " + Duration.between(start, Instant.now()).toSeconds());
            System.out.println("num: " + trainingSet.size());
            System.out.println("error: " + (100 - Math.round(predicationTree.getSuccessRate() * 100)));
            System.out.println("size: " + bestSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
