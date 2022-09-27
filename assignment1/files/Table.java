package files;

import java.util.ArrayList;
import java.util.List;
/**
 * Class implementation for the Table Object
 * @author Mohammad Alkhaledi 101162465
 */
public class Table {
    private String[] table;
    private boolean tableIsFull;

    /**
     * Default constructor for the table object
     */
    public Table(){
        table = new String[]{"",""};
        tableIsFull = false;
    }

    /**
     * adds ingredients to the table (critical section)
     */
    public synchronized void addIngredients()  {
        while(tableIsFull){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ArrayList<String> ingredients = new ArrayList<>(List.of(new String[]{"rice", "chicken", "broccoli"}));
        int index1 = (int) (Math.random() * 3);
        String ingredient1 = ingredients.get(index1);
        ingredients.remove(index1);
        String ingredient2 = ingredients.get((int) (Math.random() * 2));
        table[0] = ingredient1;
        table[1] = ingredient2;
        tableIsFull = true;
        notifyAll();
    }

    /**
     * a chef takes ingredients from the table (critical section)
     * @param ingredientsNeeded ingredients needed to match those on the table
     * @return true if the chef can take the ingredients
     */
    public synchronized boolean getRemainingIngredients(String[] ingredientsNeeded) {
        while(!tableIsFull){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(table[0].equals(ingredientsNeeded[0]) || table[0].equals(ingredientsNeeded[1])){
            if(table[1].equals(ingredientsNeeded[0]) || table[1].equals(ingredientsNeeded[1])){
                table[0] = "";
                table[1] = "";
                tableIsFull = false;
                notifyAll();
                return true;
            }
        }
        return false;
    }
}
