import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * Created by Jimmy on 2015-11-29.
 */
public class Storage extends Observable{
    private Queue<FoodItem> buffer;
    private final BigDecimal maxWeight;
    private final BigDecimal maxVolume;
    private final int maxItems;

    private BigDecimal weight;
    private BigDecimal volume;
    private int items;

    private Semaphore mutex = new Semaphore(1);
    private Semaphore fillCount = new Semaphore(0);
    private Semaphore emptyCount;

    public Storage (double weight, double volume, int items) {
        buffer = new LinkedList<>();
        this.weight = this.volume = BigDecimal.valueOf(0);
        this.items = 0;
        this.maxWeight = BigDecimal.valueOf(weight);
        this.maxVolume = BigDecimal.valueOf(volume);
        this.maxItems = items;
        emptyCount = new Semaphore(items);
    }

    public void put (FoodItem item) {
        try {
            emptyCount.acquire();
            mutex.acquire();
            buffer.add(item);
            increaseStock(item);
            setChanged();
            notifyObservers(this);
        } catch (InterruptedException e) {
        } finally {
            mutex.release();
            fillCount.release();
        }
    }

    public FoodItem get () {
        FoodItem temp = null;
        try {
            fillCount.acquire();
            mutex.acquire();
            temp = buffer.remove();
            decreaseStock(temp);
        } catch (InterruptedException e) {
        } finally {
            setChanged();
            notifyObservers(this);
            mutex.release();
            emptyCount.release();
        }
        return temp;
    }

    private void increaseStock (FoodItem item) {
        weight = weight.add(BigDecimal.valueOf(item.getWeight()));
        volume = volume.add(BigDecimal.valueOf(item.getVolume()));
        items++;

    }

    private void decreaseStock (FoodItem item) {
        this.weight = weight.subtract(BigDecimal.valueOf(item.getWeight()));
        this.volume = volume.subtract(BigDecimal.valueOf(item.getVolume()));
        this.items--;

    }

    public int getMaxItems() {
        return maxItems;
    }

    public BigDecimal getMaxVolume() {
        return maxVolume;
    }

    public BigDecimal getVolume() {
        return volume;
    }


    public int getItems() {
        return items;
    }

    public boolean isFull () {
        return items - maxItems == 0;
    }

    public boolean isEmpty () {
        return items == 0;
    }
}
