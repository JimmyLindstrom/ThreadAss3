import java.util.LinkedList;
import java.util.Observable;

/**
 * Created by Jimmy on 2015-11-29.
 */
public class Truck extends Observable implements Runnable {
    private Storage storage;
    private Controller controller;
    private LinkedList<FoodItem> foodItems;
    private boolean transporting;
    private String status = "";
    private boolean delivering = false;

    private final double maxVolume = 15;
    private final double maxWeight = 15;;
    private final double maxItems = 20;;
    private double volume = 0;
    private double weight = 0;
    private double items = 0;

    public Truck (Storage storage, Controller controller) {
        this.storage = storage;
        this.controller = controller;
        this.transporting = true;
        volume = weight = items = 0;
        foodItems = new LinkedList<>();
    }

    public void halt () {
        transporting = false;
    }

    @Override
    public void run() {
        while (transporting) {
            while (items < maxItems) {
                status = "New Truck: Loading...";
                delivering = false;
                setChanged();
                notifyObservers();
                if (storage.isEmpty()) {
                    status = "Truck: Waiting...";
                    setChanged();
                    notifyObservers();
                } else {
                    status = "Truck: Loading...";
                    setChanged();
                    notifyObservers();
                }
                FoodItem newItem = storage.get();
                increaseStock(newItem);
                foodItems.add(newItem);
                setChanged();
                notifyObservers(newItem);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
            status = "Truck: Full...";
            delivering = true;
            setChanged();
            notifyObservers();
            resetStock();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasRoom(FoodItem item) {
        return ((weight + item.getWeight()) <= maxWeight && (volume + item.getVolume()) <= maxVolume && (items +1) <= maxItems);
    }

    private void increaseStock (FoodItem item) {
        this.weight += item.getWeight();
        this.volume += item.getVolume();
        this.items += 1;
    }

    public void resetStock () {
        volume = weight = items = 0;
        foodItems.clear();
        setChanged();
        notifyObservers(this);
    }

    public String getStatus () {
        return status;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    public boolean isDelivering () {
        return delivering;
    }

    public double getMaxItems() {
        return maxItems;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public double getMaxVolume() {
        return maxVolume;
    }

    public LinkedList<FoodItem> getFoodItems() {
        return foodItems;
    }
}
