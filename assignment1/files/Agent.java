package files;
/**
 * Class implementation for the Agent Object
 * @author Mohammad Alkhaledi 101162465
 */
public class Agent extends Thread{
    private int dinnerCount;
    private Table table;
    /**
     * Default Constructor for the agent object
     * @param dinnerCount keeps track of dinners made by chefs
     */
    public Agent(int dinnerCount, Table table){
        this.dinnerCount = dinnerCount;
        this.table = table;
    }


    /**
     * @return true if 20 or more dinners have been eaten
     */
    private boolean checkDinnerCount(){
        return dinnerCount >= 20;
    }

    /**
     * Code to be executed by the agent thread
     */
    @Override
    public void run(){
        while(true){
            table.addIngredients();

            ++dinnerCount;
            System.out.printf("%d dinners eaten.\n", dinnerCount);
            try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (checkDinnerCount()){
                System.exit(0);
            }

        }
    }
}
