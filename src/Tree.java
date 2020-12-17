import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tree {
    // Tree
    Tree parent;
    Tree leftTree;
    Tree rightTree;
    Question nodeCondition;

    public Tree(Tree parent, Question nodeCondition){
        this.parent = parent;
        this.nodeCondition = nodeCondition;
    }

    public Tree(Tree parent, Question nodeCondition, Tree leftTree, Tree rightTree){
        this.parent = parent;
        this.nodeCondition = nodeCondition;
        this.leftTree = leftTree;
        this.rightTree = rightTree;
    }

    public Tree() {

    }

/*    private void CalculateEntropyValue() {
        double nl = Arrays.stream(labels).sum();
        for(int nli : labels) {
            double log = Math.log( nli / nl);
            entropy += (nli / nl) * log;
        }
    }*/

    public void act(int[] picPixels){
        Set<Leaf> leaves = new HashSet<>();
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
        else bestLeaf.parent.rightTree = newNode;
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

    private void getSetOfLeaves(Set<Leaf> leaves) {
        if(this instanceof Leaf) {
            leaves.add((Leaf) this);
            return;
        }
        if(leftTree != null) leftTree.getSetOfLeaves(leaves);
        if(rightTree != null) rightTree.getSetOfLeaves(leaves);
    }

    private double IGValue(Leaf leaf, Question question) {
        int[] labels = Arrays.copyOf(leaf.labels, leaf.labels.length);
        labels[question.label]++;
        Leaf lr = new Leaf(labels);
        Leaf ll = new Leaf(leaf.labels);
        double lrValue = getHValue(lr);
        double llValue = getHValue(ll);
        double HX = ((getNOfLeaf(lr) * lrValue) + getNOfLeaf(ll) * llValue) / getNOfLeaf(leaf);
        return getHValue(leaf) - HX;
    }

    private double getHValue(Leaf leaf) {
        double entropy = 0;
        double nl = getNOfLeaf(leaf);
        for(int nli : leaf.labels) {
            double log = Math.log( nli / nl);
            entropy += (nli / nl) * log;
        }
        return entropy;
    }

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
}
