import java.util.*;


public class Tree {
    // Tree
    Tree parent;
    static List<Tree> leaves = new LinkedList<>();
    Tree leftTree;
    Tree rightTree;
    Question nodeCondition;
    int[] labels;
    List<Picture> picturesSet;
    int label;
    double treeSize;
    double totalIG;


    public Tree(List<Picture> picturesSet, Tree parent) {
        this.parent = parent;
        this.leftTree = null;
        this.rightTree = null;
        this.nodeCondition = null;
        this.picturesSet = picturesSet;
        this.labels = Utils.countingPics(picturesSet);
        this.label = Utils.getMaxIndex(labels);
        this.getSize();
        //sums all the changes been made to the tree, the larger IG - the better the tree.
        this.totalIG = 0;
    }


    /**
     * This method is called by a leaf and it splits it to two different leaves and it enlarged the whole tree.
     *
     * @param pixel the condition for the leaf to be splitted by.
     */
    private void creatingTwoLeaves(int pixel) {
        this.nodeCondition = new Question(pixel);
        List<Picture> picsLeft = new LinkedList<>();
        List<Picture> picsRight = new LinkedList<>();
        splitPicListByQuestion(pixel, picsLeft, picsRight);
        this.leftTree = new Tree(picsLeft, this);
        this.rightTree = new Tree(picsRight, this);
        leaves.remove(this);
        leaves.add(this.leftTree);
        leaves.add(this.rightTree);
    }

    /**
     * This method split the pictureSet of the node to two different pictureSets by a condition
     *
     * @param pixel the condition
     * @param left  an empty list to be filled with picture from the pictureSet that the condition is applied to them
     * @param right an empty list to be filled with picture from the pictureSet that the condition isn't applied to them
     */
    private void splitPicListByQuestion(int pixel, List<Picture> left, List<Picture> right) {
        Question quest = new Question(pixel);
        for (Picture pic : picturesSet) {
            if (quest.ask(pic.pixels))
                left.add(pic);
            else
                right.add(pic);
        }
    }

    /**
     * This method calculate the entropy of a node with two leaves
     *
     * @param nl          The amount of picture that get to the root node
     * @param leftLabels  The labels of the pictures that goes to the left leaf
     * @param rightLabels The labels of the pictures that goes to the right leaf
     * @return the entropy of the root node
     */
    private double calculatingEntropy(double nl, int[] leftLabels, int[] rightLabels) {
        double entropy = 0;
        double leftEntropy = Utils.calculateEntropy(leftLabels);
        double nla = Arrays.stream(leftLabels).sum();
        double rightEntropy = Utils.calculateEntropy(rightLabels);
        double nlb = Arrays.stream(rightLabels).sum();
        entropy += leftEntropy * nla / nl;
        entropy += rightEntropy * nlb / nl;
        return entropy;
    }

    /**
     * This method find the best question for a specific leaf, means it only been called by a leaf.
     *
     * @return The best entropy possible for this leaf.
     * Side-effect: changes the condition-node of the leaf, so if the root chose this leaf to be the best,
     * it will know what is the question.
     */
    public double findingBest() {
        if (leftTree == null && rightTree == null) {
            int minEntropyQuest = 0;
            double minEntropy = Integer.MAX_VALUE;
            List<Integer> usedQuest = this.usedPixels();
            for (int i = 0; i < 784; i++) {
                if (!usedQuest.isEmpty()) {
                    for (Integer j : usedQuest) {
                        if (j == i)
                            break;
                    }
                }

                List<Picture> picsLeft = new LinkedList<>();
                List<Picture> picsRight = new LinkedList<>();
                splitPicListByQuestion(i, picsLeft, picsRight);
                double nl = Arrays.stream(this.labels).sum();
                double newEntropy = calculatingEntropy(nl, Utils.countingPics(picsLeft), Utils.countingPics(picsRight));

                if (newEntropy < minEntropy) {
                    minEntropy = newEntropy;
                    minEntropyQuest = i;
                }
            }
            this.nodeCondition = new Question(minEntropyQuest);
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
            double nl = Arrays.stream(leaf.labels).sum();
            double HX = leaf.findingBest();
            double HL = Utils.calculateEntropy(leaf.labels);
            double IG = HL - HX;
            double newIG = nl * IG;

            if (newIG > bestIG) {
                bestIG = newIG;
                bestLeaf = leaf;
                bestQuestion = leaf.nodeCondition.pixelNum;
            }
        }

        if (bestLeaf != null) {
            this.totalIG += bestIG;
            bestLeaf.creatingTwoLeaves(bestQuestion);
            this.getSize();
        }
    }

    public double getTotalEntropy() {
        double totalEntropy = 0;
        for (Tree leaf : leaves) {
            totalEntropy += Utils.calculateEntropy(leaf.labels);
        }
        return totalEntropy;
    }

    /**
     * This method go over all the parents of a leaf and check which questions were asked before.
     *
     * @return list of integers of al the pixel that were asked about in the parents nodes.
     */
    public List<Integer> usedPixels() {
        List<Integer> usedQuest = new LinkedList<>();
        Tree currentTree = this.parent;
        while (currentTree != null) {
            usedQuest.add(currentTree.nodeCondition.pixelNum);
            currentTree = currentTree.parent;
        }
        return usedQuest;
    }

    /**
     * This method calculate the size of the entire tree by calculating the size of each subtree.
     * The size is the amount of internal nodes, as described in the assigment.
     *
     * @return the size of the tree - used for recursion.
     */
    private double getSize() {
        double leftSize = 0;
        double rightSize = 0;
        if (this.leftTree != null)
            leftSize = this.leftTree.getSize();
        if (this.rightTree != null)
            rightSize = this.rightTree.getSize();
        if (this.rightTree != null || this.leftTree != null)
            this.treeSize = 1 + leftSize + rightSize;
        else
            this.treeSize = 0;
        return this.treeSize;
    }

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

    /** Function that goes over all the leaves and
     * counts all the examples that landed there, who matches
     * the label in the leaf.
     * @return the percentage of matched examples. i.e how much the tree predicted correctly.
     */
    public double getSuccessRate() {
        double matchedExamples = 0;
        double totalExamples = 0;
        for (Tree leaf : leaves) {
            for (Picture picture: leaf.picturesSet) {
                if(picture.label == leaf.label)
                    matchedExamples++;
                totalExamples++;
            }
        }
        return matchedExamples / totalExamples;
    }
}
