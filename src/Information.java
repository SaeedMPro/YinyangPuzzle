import java.io.Serializable;
import java.util.ArrayList;

public class Information implements Serializable {
    ArrayList<Integer> white;
    ArrayList<Integer> black;
    int row;
    int col;
    public Information(ArrayList<Integer> white, ArrayList<Integer> black, int row, int col) {
        this.row = row;
        this.col = col;
        this.white = new ArrayList<>(white);
        this.black = new ArrayList<>(black);
    }
}
