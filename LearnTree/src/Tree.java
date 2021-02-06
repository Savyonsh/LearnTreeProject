import java.util.*;

public class Tree {
    int version;
    Tree parent;
    Tree leftTree;
    Tree rightTree;
    Tree root;

    // Saved only at root
    Tree[] leaves;
    int leavesIndex;
    int totalNodes;
    TreeEssentials treeEssentials;

    // Inner nodes
    int nodePixel;
    QuestionV2 questionV2;
    int label;

    // Leaves
    int[] labels;
    int labelSum;
    BitSet usedPixels;
    List<Picture> picturesSet;

    public Tree(List<Picture> picturesSet, Tree parent, Tree root, int futureSize, int version) {
        this.version = version;
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
            leaves = new Tree[futureSize+1];
            leavesIndex = 0;
            leaves[leavesIndex] = this;
            leavesIndex++;
            this.totalNodes = 0;
            treeEssentials = new TreeEssentials(picturesSet);
        }
    }


    /**
     * This method is called by a leaf and it splits it to two different leaves and it enlarged the whole tree.
     *
     * @param pixel the condition for the leaf to be splitted by.
     */
    private void creatingTwoLeaves(int pixel, int leafIndex) {
        nodePixel = pixel;
        List<Picture> picsLeft = new LinkedList<>();
        List<Picture> picsRight = new LinkedList<>();
        splitPicListByQuestion(pixel, picsLeft, picsRight);
        this.leftTree = new Tree(picsLeft, this, this.root, 0, this.version);
        this.rightTree = new Tree(picsRight, this, this.root, 0, this.version);
        root.leaves[leafIndex] = this.leftTree;
        root.leaves[root.leavesIndex] = this.rightTree;
        root.leavesIndex++;
        root.totalNodes++;
    }

    private void creatingTwoLeaves(QuestionV2 questionV2, int leafIndex) {
        this.questionV2 = questionV2;
        List<Picture> picsLeft = new LinkedList<>();
        List<Picture> picsRight = new LinkedList<>();
        splitPicListByQuestion(questionV2, picsLeft, picsRight);
        this.leftTree = new Tree(picsLeft, this, this.root, 0, this.version);
        this.rightTree = new Tree(picsRight, this, this.root, 0, this.version);
        root.leaves[leafIndex] = this.leftTree;
        root.leaves[root.leavesIndex] = this.rightTree;
        root.leavesIndex++;
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
            if (Question.ask(pic.pixels, pixel, this.version))
                left.add(pic);
            else
                right.add(pic);
        }
    }

    private void splitPicListByQuestion(QuestionV2 questionV2, List<Picture> left, List<Picture> right) {
        for (Picture pic : picturesSet) {
            if (questionV2.ask(pic.pixels))
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
            if (root.treeEssentials.questionV2List.isEmpty()) {
                for (int i = 0; i < Utils.VEC_SIZE; i++) {
                    // Check if the bit on index i is 1 - i.e on, and
                    // already been used by parent.
                    if (usedPixels.get(i)) continue;

                    double newEntropy = -1;

                    if (root.treeEssentials.pictureSetPointers[picturesSet.size()] == picturesSet) {
                        // Going to be all left
                        if (root.treeEssentials.vectorTypes[i] == 1 &&
                                root.treeEssentials.pictureSetPointerPixelEntropyArray[picturesSet.size()][root.treeEssentials.typeOneIndex] != Double.NEGATIVE_INFINITY)
                            newEntropy = root.treeEssentials.pictureSetPointerPixelEntropyArray[picturesSet.size()][root.treeEssentials.typeOneIndex];
                            // Going to be all right
                        else if (root.treeEssentials.vectorTypes[i] == 2 &&
                                root.treeEssentials.pictureSetPointerPixelEntropyArray[picturesSet.size()][root.treeEssentials.typeTwoIndex] != Double.NEGATIVE_INFINITY)
                            newEntropy = root.treeEssentials.pictureSetPointerPixelEntropyArray[picturesSet.size()][root.treeEssentials.typeTwoIndex];
                        else if (root.treeEssentials.pictureSetPointerPixelEntropyArray[picturesSet.size()][i] != Double.NEGATIVE_INFINITY)
                            newEntropy = root.treeEssentials.pictureSetPointerPixelEntropyArray[picturesSet.size()][i];
                    }
                    if (newEntropy == -1) {
                        newEntropy = getNewEntropy(i);
                        root.treeEssentials.pictureSetPointers[picturesSet.size()] = picturesSet;
                        root.treeEssentials.pictureSetPointerPixelEntropyArray[picturesSet.size()][i] = newEntropy;
                        if (root.treeEssentials.vectorTypes[i] == 1)
                            root.treeEssentials.pictureSetPointerPixelEntropyArray[picturesSet.size()][root.treeEssentials.typeOneIndex] = newEntropy;
                        else if (root.treeEssentials.vectorTypes[i] == 2)
                            root.treeEssentials.pictureSetPointerPixelEntropyArray[picturesSet.size()][root.treeEssentials.typeTwoIndex] = newEntropy;
                    }
                    if (newEntropy < minEntropy) {
                        minEntropy = newEntropy;
                        minEntropyQuest = i;
                    }
                }
                nodePixel = minEntropyQuest;

            } else {
                QuestionV2 chosenQ = null;
                for (QuestionV2 q : root.treeEssentials.questionV2List) {
                    double currentEntropy = getNewEntropy(q);
                    if (currentEntropy < minEntropy){
                        minEntropy = currentEntropy;
                        chosenQ = q;
                    }
                }
                root.treeEssentials.questionV2List.remove(chosenQ);
                questionV2 = chosenQ;
            }
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
        int bestLeafIndex = 0;
        int bestQuestion = 0;
        QuestionV2 bestQuestionV2 = null;

        for (int j = 0; j < this.leavesIndex; j++) {
            Tree leaf = this.leaves[j];
            double nl = leaf.labelSum;
            double HX = leaf.findingBest();
            double HL = Utils.calculateEntropy(leaf.labels, nl);
            double IG = HL - HX;
            double newIG = nl * IG;

            if (newIG > bestIG) {
                bestIG = newIG;
                bestLeaf = leaf;
                bestLeafIndex = j;
                bestQuestion = leaf.nodePixel;
                bestQuestionV2 = leaf.questionV2;
            }
        }

        if (bestLeaf != null)
            if(bestQuestionV2 == null)
            bestLeaf.creatingTwoLeaves(bestQuestion, bestLeafIndex);
            else bestLeaf.creatingTwoLeaves(bestQuestionV2, bestLeafIndex);


    }

    public String toString() {
        String str = "";
        String leftStr = "";
        String rightStr = "";
        if (this.leftTree == null && this.rightTree == null) {
            str = "L" + this.label ;
        } else {
            str += this.nodePixel;
            if (this.leftTree != null)
                leftStr += this.leftTree.toString();
            if (this.rightTree != null)
                rightStr += this.rightTree.toString();
            str += " " + leftStr + " " + rightStr;
        }
        return str;
    }

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
            if (Question.ask(pic.pixels, pixel, this.version)) {
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


    private double getNewEntropy(QuestionV2 questionV2) {
        int[] rightLabels = new int[10];
        int[] leftLabels = new int[10];
        double entropy = 0;

        int leftNl = 0, rightNl = 0;
        for (Picture pic : picturesSet) {
            if (questionV2.ask(pic.pixels)) {
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
