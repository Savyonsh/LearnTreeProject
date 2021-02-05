import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Predict {
    public static void main(String[] args) throws IOException {
        String treePath = args[0];
        String[] treeStr = null;
        try {
            File myObj = new File(treePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                treeStr = myReader.nextLine().split(" ");
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        assert treeStr != null;
        PredictTree tree = parseTree(treeStr);

        List<Picture> testSet = new LinkedList<>();
        setTestSet(args[1], testSet);
        double accuracy = predict(testSet, tree);
//        System.out.println("accuracy: " + accuracy);
    }

    private static void setTestSet(String inputFileName, List<Picture> testSet) throws IOException {
        CSVReader myReader = new CSVReader(inputFileName);
        while (myReader.hasNextLine()) {
            Picture picture = myReader.getNextPicture();
            testSet.add(picture);
        }
        myReader.close();
    }

    private static PredictTree parseTree(String[] treeStr) {
        PredictTree root = new PredictTree(null, -1, new Question(Integer.parseInt(treeStr[0])));
        PredictTree currentTree = root;
        int i = 1;
        while (i < treeStr.length) {
            if (currentTree.getLeftTree() == null) {
                if (treeStr[i].charAt(0) == 'L') {
                    PredictTree tree = new PredictTree(currentTree, treeStr[i].charAt(1) - '0', null);
                    currentTree.setLeftTree(tree);
                } else {
                    PredictTree tree = new PredictTree(currentTree, -1, new Question(Integer.parseInt(treeStr[i])));
                    currentTree.setLeftTree(tree);
                    currentTree = tree;
                }
                i++;
            } else if (currentTree.getRightTree() == null) {
                if (treeStr[i].charAt(0) == 'L') {
                    currentTree.setRightTree(new PredictTree(currentTree, treeStr[i].charAt(1) - '0', null));
                    currentTree = currentTree.parent;
                } else {
                    PredictTree tree = new PredictTree(currentTree, -1, new Question(Integer.parseInt(treeStr[i])));
                    currentTree.setRightTree(tree);
                    currentTree = currentTree.getRightTree();
                }
                i++;
            } else if (currentTree.parent != null) {
                currentTree = currentTree.parent;
            }
        }

        return root;
    }

    private static double predict(List<Picture> testSet, PredictTree tree) {
        double right = 0;
        double total = 0;
        for (Picture pic : testSet) {
            int prediction = tree.predict(pic.pixels);
            System.out.println(prediction);
            if (prediction == pic.label) {
                right++;
            }
            total++;
        }

        return (right / total);
    }
}
