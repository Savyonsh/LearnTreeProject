public class PredictTree {

    PredictTree leftTree;
    PredictTree rightTree;
    PredictTree parent;
    int label;
    Question cond;

    public PredictTree(PredictTree parent, int label, Question cond){
        this.parent = parent;
        this.label = label;
        this.cond = cond;
        this.leftTree = null;
        this.rightTree = null;
    }

    public int predict(int[] pixels){
        if(this.leftTree != null || this.rightTree != null){
            if(cond.ask(pixels)){
                if(this.leftTree != null)
                   return this.leftTree.predict(pixels);
                else
                    return this.label;
            }
            else{
                if(this.rightTree != null)
                    return this.rightTree.predict(pixels);
                else
                    return this.label;
            }
        }
        return this.label;
    }

    public String toString() {
        String str = "";
        String leftStr = "";
        String rightStr = "";
        if (this.leftTree == null && this.rightTree == null) {
            str = "L" + this.label ;
        } else {
            str += this.cond.pixelNum;
            if (this.leftTree != null)
                leftStr += this.leftTree.toString();
            if (this.rightTree != null)
                rightStr += this.rightTree.toString();
            str += " " + leftStr + " " + rightStr;
        }
        return str;
    }

    public PredictTree getLeftTree() {
        return leftTree;
    }

    public void setLeftTree(PredictTree leftTree) {
        this.leftTree = leftTree;
    }

    public PredictTree getRightTree() {
        return rightTree;
    }

    public void setRightTree(PredictTree rightTree) {
        this.rightTree = rightTree;
    }
}
