import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Tree {
    // Tree
    Tree parent;
    Tree leftTree;
    Tree rightTree;
    static List<Tree> leaves = new LinkedList<>();
    static double totalNodes = 0;
    //Question nodeCondition;
    int nodePixel;
    int[] labels;
    int labelSum;
    // Try to change to list of arrays
    List<Picture> picturesSet;
    int label;
    double totalIG;
    BitSet usedPixels;
    //int checkingAmount = 0;

    public Tree(List<Picture> picturesSet, Tree parent) {
        this.parent = parent;
        leftTree = null;
        rightTree = null;
        nodePixel = -1;
        //nodeCondition = null;
        this.picturesSet = picturesSet;
        labels = new int[10];
        setLabelsInformation();
        if (parent != null) {
            usedPixels.or(parent.usedPixels);
            usedPixels.flip(parent.nodePixel); // Turn that bit on.
        }
        //this is the case this is the root
        else {
            leaves.clear();
            leaves.add(this);
            totalNodes = 0;
        }
    }


    /**
     * This method is called by a leaf and it splits it to two different leaves and it enlarged the whole tree.
     *
     * @param pixel the condition for the leaf to be splitted by.
     */
    private void creatingTwoLeaves(int pixel) {
        //nodeCondition = new Question(pixel);
        nodePixel = pixel;
        totalNodes++;
        List<Picture> picsLeft = new LinkedList<>();
        List<Picture> picsRight = new LinkedList<>();
        splitPicListByQuestion(pixel, picsLeft, picsRight);
        leftTree = new Tree(picsLeft, this);
        rightTree = new Tree(picsRight, this);
        leaves.remove(this);
        leaves.add(leftTree);
        leaves.add(rightTree);
    }

    /**
     * This method split the pictureSet of the node to two different pictureSets by a condition
     *
     * @param pixel the condition
     * @param left  an empty list to be filled with picture from the pictureSet that the condition is applied to them
     * @param right an empty list to be filled with picture from the pictureSet that the condition isn't applied to them
     */
    private void splitPicListByQuestion(int pixel, List<Picture> left, List<Picture> right) {
        //Question quest = new Question(pixel);
        for (Picture pic : picturesSet) {
            if (Question.ask(pic.pixels, pixel))
                left.add(pic);
            else
                right.add(pic);
        }
    }

    /**
     * This method calculate the entropy of a node with two leaves
     *
     * @param nl          The amount of pictures that get to the root node
     * @param leftLabels  The labels of the pictures that goes to the left leaf
     * @param rightLabels The labels of the pictures that goes to the right leaf
     * @return the entropy of the root node
     */
    private double calculatingEntropy(double nl, int[] leftLabels, int leftNl, int[] rightLabels, int rightNl) {
        double entropy = 0;
        double leftEntropy = Utils.calculateEntropy(leftLabels, leftNl);
        double rightEntropy = Utils.calculateEntropy(rightLabels, rightNl);
        entropy += leftEntropy * leftNl / nl;
        entropy += rightEntropy * rightNl / nl;
        return entropy;
    }

    /**
     * This method find the best question for a specific leaf, means it only been called by a leaf.
     *
     * @return The best entropy possible for this leaf.
     * Side-effects: changes the condition-node of the leaf, so if the root chose this leaf to be the best,
     * it will know what is the question.
     */
    public double findingBest() {
        if (leftTree == null && rightTree == null) {
            int minEntropyQuest = 0;
            double minEntropy = Integer.MAX_VALUE;

            for (int i = 0; i < 784; i++) {
                if (usedPixels.get(i)) continue;

                /*int[] leftLabels = new int[10];
                int[] rightLabels = new int[10];
                List<Picture> picsLeft = new LinkedList<>();
                List<Picture> picsRight = new LinkedList<>();
                splitPicListByQuestion(i, picsLeft, picsRight);
                int leftNl = countingPicsAndReturnSum(picsLeft, leftLabels);
                int rightNl = countingPicsAndReturnSum(picsRight, rightLabels);
                double newEntropy = calculatingEntropy(labelSum, leftLabels, leftNl, rightLabels, rightNl);*/
                double newEntropy = getNewEntropy(i);
                if (newEntropy < minEntropy) {
                    minEntropy = newEntropy;
                    minEntropyQuest = i;
                }
            }
            //this.nodeCondition = new Question(minEntropyQuest);
            nodePixel = minEntropyQuest;
            return minEntropy;
        }
        return 0;
    }

    /**
     * This method happens only in the root.
     * Here the root try to find the best leaf to split into two new leaves.
     * It been called by the main class, it doesn't get any argument and it doesn't return any value,
     * all it does is side-effect to the tree.
     */
    public void act() {
        double bestIG = Integer.MIN_VALUE;
        Tree bestLeaf = null;
        int bestQuestion = 0;
        for (Tree leaf : leaves) {
            double nl = leaf.labelSum;
            double HX = leaf.findingBest();
            double HL = Utils.calculateEntropy(leaf.labels, nl);
            double IG = HL - HX;
            double newIG = nl * IG;

            if (newIG > bestIG) {
                bestIG = newIG;
                bestLeaf = leaf;
                //bestQuestion = leaf.nodeCondition.pixelNum;
                bestQuestion = leaf.nodePixel;
            }
        }

        if (bestLeaf != null) {
            totalIG += bestIG;
            bestLeaf.creatingTwoLeaves(bestQuestion);
        }
    }

    public double getTotalEntropy() {
        double totalEntropy = 0;
        for (Tree leaf : leaves) {
            totalEntropy += Utils.calculateEntropy(leaf.labels, labelSum);
        }
        return totalEntropy;
    }

/*
    public String toString() {
        String str = "";
        String leftStr = "left: ";
        String rightStr = "right: ";
        if (this.leftTree == null && this.rightTree == null) {
            str = "label: " + this.label;
        } else {
            if (this.leftTree != null)
                leftStr += this.leftTree.toString();
            if (this.rightTree != null)
                rightStr += this.rightTree.toString();
            str = this.nodeCondition.toString() + "\n" + leftStr + "\n" + rightStr;
        }
        return str;
    }
*/

    /**
     * Function that goes over all the leaves and
     * counts all the examples that landed there, who matches
     * the label in the leaf.
     *
     * @return the percentage of matched examples. i.e how much the tree predicted correctly.
     */
    public double getSuccessRate() {
        double matchedExamples = 0;
        double totalExamples = 0;
        for (Tree leaf : leaves) {
            for (Picture picture : leaf.picturesSet) {
                if (picture.label == leaf.label)
                    matchedExamples++;
                totalExamples++;
            }
        }
        return matchedExamples / totalExamples;
    }

    /**
     * Iterates over all the pictures, to set the labels array,
     * find the max label and sum the array.
     */
    private void setLabelsInformation() {
        labelSum = 0;
        int maxLabelValue = Integer.MIN_VALUE;
        for (Picture pic : picturesSet) {
            labels[pic.label]++;
            if (labels[pic.label] > maxLabelValue) {
                maxLabelValue = labels[pic.label];
                label = pic.label;
            }
            labelSum++;
        }
    }


    static int countingPicsAndReturnSum(List<Picture> picSet, int[] output) {
        int sum = 0;
        for (Picture pic : picSet) {
            output[pic.label]++;
            sum += output[pic.label];
        }
        return sum;
    }


    private double getNewEntropy(int pixel) {
        int[] rightLabels = new int[10];
        int[] leftLabels = new int[10];
        double entropy = 0;

        int leftNl = 0, rightNl = 0;
        for (Picture pic : picturesSet) {
            if (Question.ask(pic.pixels, pixel)) {
                leftLabels[pic.label]++;
                leftNl += leftLabels[pic.label];
            } else {
                rightLabels[pic.label]++;
                rightNl += rightLabels[pic.label];
            }
        }
        double leftEntropy = Utils.calculateEntropy(leftLabels, leftNl);
        double rightEntropy = Utils.calculateEntropy(rightLabels, rightNl);
        entropy += leftEntropy * leftNl / labelSum;
        entropy += rightEntropy * rightNl / labelSum;
        return entropy;
    }
}




