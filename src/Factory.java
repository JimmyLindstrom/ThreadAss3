
import java.util.Observable;
import java.util.Random;

/**
 * Created by Jimmy on 2015-11-29.
 */
public class Factory extends Observable implements Runnable{

    private String name;
    private FoodItem[] foodBuffer;
    private Storage storage;
    private Random rand = new Random();
    private Controller controller;

    private boolean producing;
    private String status = "";

    public Factory (String name, Storage storage, Controller controller) {
        this.name = name;
        this.storage = storage;
        this.controller = controller;
        producing = true;
        initFoodItems();
    }
    
    public void initFoodItems () {
        foodBuffer = new FoodItem[20];

        foodBuffer[0] = new FoodItem(1.1, 1, "Milk");
        foodBuffer[1] = new FoodItem(0.9, 0.8, "Cheese");
        foodBuffer[2] = new FoodItem(0.7, 1.2, "Bread");
        foodBuffer[3] = new FoodItem(1.2, 1, "Soda");
        foodBuffer[4] = new FoodItem(0.8, 0.7, "Cream");
        foodBuffer[5] = new FoodItem(0.6, 1.5, "Cereal");
        foodBuffer[6] = new FoodItem(1.2, 0.6, "Salt");
        foodBuffer[7] = new FoodItem(0.9, 0.5, "Pepper");
        foodBuffer[8] = new FoodItem(1.2, 0.8, "Sugar");
        foodBuffer[9] = new FoodItem(1.0, 1.2, "Ham");
        foodBuffer[10] = new FoodItem(1.5, 1.8, "Chicken");
        foodBuffer[11] = new FoodItem(2.1, 1.9, "Apple");
        foodBuffer[12] = new FoodItem(1.9, 1.7, "Orange");
        foodBuffer[13] = new FoodItem(1.5, 2.2, "Banana");
        foodBuffer[14] = new FoodItem(1.0, 1.1, "Beer");
        foodBuffer[15] = new FoodItem(1.7, 1.9, "Pear");
        foodBuffer[16] = new FoodItem(0.5, 0.7, "CocaCola");
        foodBuffer[17] = new FoodItem(1.2, 1, "HotDogs");
        foodBuffer[18] = new FoodItem(0.6, 1.2, "Salat");
        foodBuffer[19] = new FoodItem(0.8, 0.6, "Donut");
    }

    public void halt () {
        producing = false;
    }

    @Override
    public void run() {
        while (true) {
            while (producing) {
                status = "Producer: working";
                setChanged();
                notifyObservers(this);
                FoodItem newItem = foodBuffer[rand.nextInt(20)];
                if (storage.isFull()) {
                    status = "Producer: Waiting...";
                    setChanged();
                    notifyObservers(this);
                } else {
                    status = "Producer: Working...";
                    setChanged();
                    notifyObservers(this);
                }
                storage.put(newItem);
                System.out.println(name + " producerar" + newItem.getName());
                try {
                    Thread.sleep(rand.nextInt(2000));
                } catch (InterruptedException e) {
                    break;
                }
            }
            status = "Producer: stopped..";
            setChanged();
            notifyObservers(this);
        }
    }

    public String getStatus () {
        return status;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public boolean isProducing () {
        return producing;
    }

    public void startProducing() {
        producing = true;
    }
}
