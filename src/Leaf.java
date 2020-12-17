import java.util.List;

public class Leaf extends Tree{
    int[] labels;
    double entropy;

    public Leaf(int[] labels) {
        this.labels = labels;
    }

}
