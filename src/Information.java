import java.io.Serializable;
import java.util.ArrayList;

// The Information class is used to store the game data that needs to be saved
public class Information implements Serializable {

    // ArrayLists to store the indices of white and black buttons
    ArrayList<Integer> white;
    ArrayList<Integer> black;

    // Variables to store the grid's dimensions and the number of random cells
    int row;
    int col;
    int random;

    // Constructor to initialize the Information object with the game data
    public Information(ArrayList<Integer> white, ArrayList<Integer> black, int row, int col, int random) {
        this.row = row;
        this.col = col;
        this.random = random;

        // Create new ArrayLists to store the button indices to avoid referencing the same ArrayLists
        this.white = new ArrayList<>(white);
        this.black = new ArrayList<>(black);
    }
}

