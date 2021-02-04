import java.util.*;

public class Tree {
    Tree parent;
    Tree leftTree;
    Tree rightTree;
    Tree root;

    // Saved only at root
    List<Tree> leaves;
    int totalNodes;
    TreeEssentials treeEssentials;

    // Inner nodes
    int nodePixel;
    int label;

    // Leaves
    int[] labels;
    int labelSum;
    BitSet usedPixels;
    List<Picture> picturesSet;

    public Tree(List<Picture> picturesSet, Tree parent, Tree root) {
        this.parent = parent;
        this.root = root;
        this.picturesSet = picturesSet;
        usedPixels = new BitSet(Utils.VEC_SIZE);
        labels = new int[10];
        setLabelsInformation();

        if (parent != null) {
            usedPixels.or(parent.usedPixels);
            usedPixels.flip(parent.nodePixel); // Turn that bit on.

        } else { // This is root
            this.root = this;
            leaves = new LinkedList<>();
            leaves.add(this);
            this.totalNodes = 0;
            treeEssentials = new TreeEssentials(picturesSet.size());
        }
    }

    public int predict(int[] pixels){
        Tree currentTree = this;
        while(currentTree.leftTree != null || currentTree.rightTree != null){
            if(Question.ask(pixels, currentTree.nodePixel)){
                if(currentTree.leftTree != null)
                    currentTree = currentTree.leftTree;
                else
                    return currentTree.label;
            }
            else{
                if(currentTree.rightTree != null)
                    currentTree = currentTree.rightTree;
                else
                    return currentTree.label;
            }
        }
        return currentTree.label;
    }
    /**
     * This method is called by a leaf and it splits it to two different leaves and it enlarged the whole tree.
     *
     * @param pixel the condition for the leaf to be splitted by.
     */
    private void creatingTwoLeaves(int pixel) {
        nodePixel = pixel;
        List<Picture> picsLeft = new LinkedList<>();
        List<Picture> picsRight = new LinkedList<>();
        splitPicListByQuestion(pixel, picsLeft, picsRight);
        leftTree = new Tree(picsLeft, this, this.root);
        rightTree = new Tree(picsRight, this, this.root);
        root.leaves.remove(this);
        root.leaves.add(leftTree);
        root.leaves.add(rightTree);
        root.totalNodes++;
    }

    /**
     * This method split the pictureSet of the node to two different pictureSets by a condition
     *
     * @param pixel the condition
     * @param left  an empty list to be filled with picture from the pictureSet that the condition is applied to them
     * @param right an empty list to be filled with picture from the pictureSet that the condition isn't applied to them
     */
    private void splitPicListByQuestion(int pixel, List<Picture> left, List<Picture> right) {
        for (Picture pic : picturesSet) {
            if (Question.ask(pic.pixels, pixel))
                left.add(pic);
            else
                right.add(pic);
        }
    }


    /**
     * This method find the best question for a specific leaf, means it only been called by a leaf.
     *
     * @return The best entropy possible for this leaf.
     * Side-effects: changes the condition-node of the leaf, so if the root chose this leaf to be the best,
     * it will know what is the question.
     */
    private double findingBest() {
        if (leftTree == null && rightTree == null) {
            int minEntropyQuest = 0;
            double minEntropy = Integer.MAX_VALUE;

            for (int i = 0; i < Utils.VEC_SIZE; i++) {
                // Check if the bit on index i is 1 - i.e on, and
                // already been used by parent.
                if (usedPixels.get(i)) continue;

                // One function that "splits" the picture,
                // While calculating the sums of the two arrays (left and right)
                // And calculating the total entropy
                double newEntropy;

                if(root.treeEssentials.pictureSetPointers[picturesSet.size()] != null &&
                    root.treeEssentials.pictureSetPointers[picturesSet.size()] == picturesSet &&
                root.treeEssentials.pictureSetPointerPixelEntropyArray[picturesSet.size()][i] != Double.NEGATIVE_INFINITY) {
                    newEntropy = root.treeEssentials.pictureSetPointerPixelEntropyArray[picturesSet.size()][i];
                } else {
                    newEntropy = getNewEntropy(i);
                    root.treeEssentials.pictureSetPointers[picturesSet.size()] = picturesSet;
                    root.treeEssentials.pictureSetPointerPixelEntropyArray[picturesSet.size()][i] = newEntropy;
                }
                if (newEntropy < minEntropy) {
                    minEntropy = newEntropy;
                    minEntropyQuest = i;
                }
            }
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
        for (Tree leaf : this.leaves) {
            double nl = leaf.labelSum;
            double HX = leaf.findingBest();
            double HL = Utils.calculateEntropy(leaf.labels, nl);
            double IG = HL - HX;
            double newIG = nl * IG;

            if (newIG > bestIG) {
                bestIG = newIG;
                bestLeaf = leaf;
                bestQuestion = leaf.nodePixel;
            }
        }

        if (bestLeaf != null)
            bestLeaf.creatingTwoLeaves(bestQuestion);

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
        for (Tree leaf : root.leaves) {

            matchedExamples += leaf.labels[leaf.label];
            totalExamples += leaf.labelSum;
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

    /**
     * Function that calculates total entropy for a question in one
     * It iterates over the picture set, splits all the pictures according
     * to the pixel, counting the left labels and right, with their sum,
     * and then calculate their individual entropy to get the total entropy for
     * this question.
     * @param pixel pixel number of the question
     * @return combined entropy
     */
    private double getNewEntropy(int pixel) {
        int[] rightLabels = new int[10];
        int[] leftLabels = new int[10];
        double entropy = 0;

        int leftNl = 0, rightNl = 0;
        for (Picture pic : picturesSet) {
            if (Question.ask(pic.pixels, pixel)) {
                leftLabels[pic.label]++;
                leftNl++;
            } else {
                rightLabels[pic.label]++;
                rightNl++;
            }
        }
        double leftEntropy = Utils.calculateEntropy(leftLabels, leftNl);
        double rightEntropy = Utils.calculateEntropy(rightLabels, rightNl);
        entropy += leftEntropy * leftNl / labelSum;
        entropy += rightEntropy * rightNl / labelSum;
        return entropy;
    }
}
