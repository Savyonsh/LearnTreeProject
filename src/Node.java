public class Node {
    public int label;
    public Question question;
    public int[] NArray;
    public Node leftTree;
    public Node rightTree;

    public Node(int label, Question question, int[] NArray){
        this.label = label;
        this.question = question;
        this.NArray = NArray;
        this.leftTree = null;
        this.rightTree = null;
    }

    public Node act(int[] pic, int num){
        if(question.ask(pic)){
            if(rightTree == null){
                this.rightTree = new Node(num, new Question(12), NArray);
            }
            return rightTree;
        }
        if(leftTree == null){
            this.leftTree = new Node(num, new Question(12), NArray);
        }
        return leftTree;
    }
}
