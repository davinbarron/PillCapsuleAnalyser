package Manager;

import Application.PillSelection;
import java.util.ArrayList;
import java.util.List;

/**
 * The PillCapsuleManager class is responsible for managing a list of PillSelections.
 * It provides methods to add a PillSelection to the list and to get the list of PillSelections.
 */
public class PillCapsuleManager {
    // A list to store the PillSelection objects.
    private final List<PillSelection> pillSelections;

    /**
     * Constructor that initializes the PillSelection list.
     */
    public PillCapsuleManager() {
        this.pillSelections = new ArrayList<>();
    }

    /**
     * Method to add a PillSelection to the list.
     *
     * @param pillSelection The PillSelection object to be added to the list.
     */
    public void addPillSelection(PillSelection pillSelection) {
        this.pillSelections.add(pillSelection);
    }

    /**
     * Method to get the list of PillSelections.
     *
     * @return The list of PillSelections.
     */
    public List<PillSelection> getPillSelections() {
        return this.pillSelections;
    }
}


