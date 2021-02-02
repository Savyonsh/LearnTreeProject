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

    public Tree(List<Picture> picturesSet) {
        this.leftTree = null;
        this.rightTree = null;
        this.nodeCondition = null;
        this.picturesSet = picturesSet;
        this.labels = Utils.countingPics(picturesSet);
        this.label = Utils.getMaxIndex(labels);
    }

 /*   public Tree(Question nodeCondition, List<Picture> picturesSet){
        this.nodeCondition = nodeCondition;
        this.picturesSet = picturesSet;
    }

    public Tree(Question nodeCondition, Tree leftTree, Tree rightTree, List<Picture> picturesSet){
        this.nodeCondition = nodeCondition;
        this.leftTree = leftTree;
        this.rightTree = rightTree;
        this.picturesSet = picturesSet;
    }*/

    public double getEntropy(){
        double nl = Arrays.stream(labels).sum();
        if(leftTree == null  && rightTree == null)
            return Utils.calculateEntropy(labels);

        double leftTreeEntropy = 0;
        double rightTreeEntropy = 0;
        if(leftTree != null) {
            leftTreeEntropy = leftTree.getEntropy();
            double nla = Arrays.stream(leftTree.labels).sum();
            leftTreeEntropy *= nla/nl;
        }
        if(rightTree != null) {
            rightTreeEntropy = rightTree.getEntropy();
            double nlb = Arrays.stream(rightTree.labels).sum();
            rightTreeEntropy *= nlb/nl;
        }
        return leftTreeEntropy + rightTreeEntropy;
    }

    private double checkingForEntropy(int pixel){
        this.nodeCondition = new Question(pixel);
        List<Picture> picsLeft = new LinkedList<>();
        List<Picture> picsRight = new LinkedList<>();
        for (Picture pic: picturesSet) {
            if(this.nodeCondition.ask(pic.pixels))
                picsLeft.add(pic);
            else
                picsRight.add(pic);
        }
        this.leftTree = new Tree(picsLeft);
        this.rightTree = new Tree(picsRight);
        this.leftTree = new Tree(picsLeft);
        this.rightTree = new Tree(picsRight);
        return this.getEntropy();
    }

    public double findingBest(){
        if(leftTree == null && rightTree == null) {
            int minEntropyQuest = 0;
            double minEntropy = Integer.MAX_VALUE;
            List<Integer> usedQuest = this.usedPixels();
            for (int i = 0; i < 784; i++) {
                if(!usedQuest.isEmpty()){
                    for (Integer j : usedQuest) {
                        if (j == i)
                            break;
                    }
                }
                double newEntropy = checkingForEntropy(i);
                if(newEntropy< minEntropy){
                    minEntropy = newEntropy;
                    minEntropyQuest = i;
                }
            }
            return checkingForEntropy(minEntropyQuest);
        }
        return 0;
    }

    public void act(){
        double bestIG = Integer.MIN_VALUE;
        Tree bestLeaf = null;
        int bestQuestion = 0;
        for (Tree leaf:leaves) {
            double nl = Arrays.stream(leaf.labels).sum();
            double newEntropy = leaf.findingBest();
            double newIG = nl*getIGValue(leaf);
            if(newIG> bestIG){
                bestIG = newIG;
                bestLeaf = leaf;
                bestQuestion = leaf.nodeCondition.pixelNum;
            }
            leaf.nodeCondition = null;
            leaf.leftTree = null;
            leaf.rightTree = null;
        }

        if(bestLeaf != null) {
            bestLeaf.checkingForEntropy(bestQuestion);
            leaves.remove(bestLeaf);
            leaves.add(bestLeaf.leftTree);
            leaves.add(bestLeaf.rightTree);

        }





        /*Set<Leaf> leaves = new HashSet<>();
        getSetOfLeaves(leaves);
        Question bestQuestion = null;
        Leaf bestLeaf = null;

        for(Leaf leaf : leaves) {
            double maxNLIGLValue = Double.MIN_VALUE;
            for(Question question : QuestionSet.questionSet) {
                double currentNLIGLValue = IGValue(leaf, question);
                if (currentNLIGLValue > maxNLIGLValue) {
                    currentNLIGLValue = maxNLIGLValue;
                    bestQuestion = question;
                    bestLeaf = leaf;
                }
            }
        }

        assert bestLeaf != null;
        int[] labels = Arrays.copyOf(bestLeaf.labels, bestLeaf.labels.length);
        labels[bestQuestion.label]++;
        Leaf lr = new Leaf(labels);
        Leaf ll = new Leaf(bestLeaf.labels);
        Tree newNode = new Tree(bestLeaf.parent, bestQuestion, ll, lr);
        if(bestLeaf.parent.leftTree == bestLeaf) bestLeaf.parent.leftTree = newNode;
        else bestLeaf.parent.rightTree = newNode;*/
/*        // Choose a leaf

        if(question.ask(pic)){
            if(rightTree == null){
                this.rightTree = new Tree(num, new Question(12), nArray);
            }
            return rightTree;
        }
        if(leftTree == null){
            this.leftTree = new Tree(num, new Question(12), nArray);
        }
        return leftTree;*/
    }

    public double getIGValue(Tree leaf){
        double HL = Utils.calculateEntropy(leaf.labels);
        double GL = leaf.getEntropy();
        return  HL-GL;
    }

    private void getSetOfLeaves(Set<Leaf> leaves) {
        if(this instanceof Leaf) {
            leaves.add((Leaf) this);
            return;
        }
        if(leftTree != null) leftTree.getSetOfLeaves(leaves);
        if(rightTree != null) rightTree.getSetOfLeaves(leaves);
    }

    public List<Integer> usedPixels(){
        List<Integer> usedQuest = new LinkedList<>();
        Tree currentTree = this.parent;
        while(currentTree != null){
            usedQuest.add(currentTree.nodeCondition.pixelNum);
            currentTree = currentTree.parent;
        }
        return usedQuest;
    }

//    private double IGValue(Leaf leaf, Question question) {
//        int[] labels = Arrays.copyOf(leaf.labels, leaf.labels.length);
//        labels[question.label]++;
//        Leaf lr = new Leaf(labels);
//        Leaf ll = new Leaf(leaf.labels);
//        double lrValue = getHValue(lr);
//        double llValue = getHValue(ll);
//        double HX = ((getNOfLeaf(lr) * lrValue) + getNOfLeaf(ll) * llValue) / getNOfLeaf(leaf);
//        return getHValue(leaf) - HX;
//    }

//    private double getHValue(Leaf leaf) {
//        double entropy = 0;
//        double nl = getNOfLeaf(leaf);
//        for(int nli : leaf.labels) {
//            double log = Math.log( nli / nl);
//            entropy += (nli / nl) * log;
//        }
//        return entropy;
//    }

    private double getNOfLeaf(Leaf leaf) {
        return Arrays.stream(leaf.labels).sum();
    }

    /*private Tree getLeafWithHighestEntropyValue() {
        if (leftTree == null && rightTree == null) {
            return this;
        }
        double bestValue = Double.MIN_VALUE;
        double currentValue;
        Tree returnedLeaf = null;

        if (leftTree != null) {
            Tree leftLeaf = leftTree.getLeafWithHighestEntropyValue();
            if ((currentValue = leftLeaf.entropy) > bestValue) {
                bestValue = currentValue;
                returnedLeaf = leftLeaf;
            }
        } if(rightTree != null) {
            Tree rightLeaf = rightTree.getLeafWithHighestEntropyValue();
            if (rightLeaf.entropy > bestValue) {
                returnedLeaf = rightLeaf;
            }
        }
        return returnedLeaf;
    }

    private void increaseLabelCount(Tree tree, int label) {
        if(tree == leftTree) {
            leftTree.labels[label]++;
        } else if(tree == rightTree) {
            rightTree.labels[label]++;
        }
    }*/

    public double getSize(){
        double leftSize = 0;
        double rightSize = 0;
        if(this.leftTree != null)
            leftSize = this.leftTree.getSize();
        if (this.rightTree != null)
            rightSize = this.rightTree.getSize();
        double size = 1+leftSize+rightSize;
        return size;
    }

    public String toString(){
        String str = "";
        String leftStr = "left: ";
        String rightStr = "right: ";
        if(this.leftTree == null && this.rightTree == null){
            str = "label: " + this.label;
        }
        else{
            if(this.leftTree != null)
                leftStr += this.leftTree.toString();
            if(this.rightTree != null)
                rightStr += this.rightTree.toString();
            str = this.nodeCondition.toString() +"\n"+ leftStr +"\n" + rightStr;
        }
        return str;
    }
}
