package files;

/**
 * Class implementation for the Chef Object
 * @author Mohammad Alkhaledi 101162465
 */
public class Chef extends Thread{
    private String ingredient;
    private String[] ingredientsNeeded;
    private boolean acquiredIngredients;
    private Table table;
    /**
     * Default constructor for chef object
     * @param ingredient ingredient of which the chef has an infinite supply
     */
    public Chef(String ingredient, String[] ingredientsNeeded, Table table){
        this.ingredient = ingredient;
        this.ingredientsNeeded = ingredientsNeeded;
        acquiredIngredients = false;
        this.table = table;
    }


    /**
     * @return true if dinner is ready
     */
    private boolean getDinnerStatus(){
        return acquiredIngredients;
    }

    /**
     * eats dinner by setting acquiredIngredients to false.
     */
    private void eatDinner(){
        acquiredIngredients = false;
    }

    /**
     * the code that the chef thread will execute.
     */
    @Override
    public void run(){
        while(true){
            acquiredIngredients = table.getRemainingIngredients(ingredientsNeeded);
            if(getDinnerStatus()){
                eatDinner();
                System.out.println(this + " ate dinner");
                try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
    }

    public static void main(String[] args) {
        Table table = new Table();
        Chef Adam = new Chef("rice", new String[]{"chicken","broccoli"}, table);
        Chef Bill = new Chef("chicken", new String[]{"rice","broccoli"}, table);
        Chef Chad = new Chef("broccoli", new String[]{"rice","chicken"}, table);

        Agent John = new Agent(0, table);

        Adam.start();
        Bill.start();
        Chad.start();
        John.start();
    }
}
