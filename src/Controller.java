import java.util.Observable;
import java.util.Observer;

/**
 * Created by Jimmy on 2015-11-30.
 */
public class Controller {
    private GUISemaphore gui;
    private Storage storage;
    private Factory prod1;
    private Factory prod2;
    private Truck truck;
    private Thread a;
    private Thread b;
    private Thread deliver;

    public Controller (GUISemaphore gui) {
        this. gui = gui;
        storage = new Storage(25, 25, 25);
        storage.addObserver(new StorageObserver());
        truck = new Truck(storage, this);
        prod1 = new Factory("a", this.storage, this);
        prod2 = new Factory("b", this.storage, this);
        truck.addObserver(new TruckObserver());
        prod1.addObserver(new FactoryObserver());
        prod2.addObserver(new FactoryObserver());
        a = new Thread(prod1);
        b = new Thread(prod2);
        deliver = new Thread(truck);
    }

    public void deliver () {
        gui.setTruckInfo(truck);
        if (deliver.isInterrupted()) {
            deliver = new Thread(truck);
        }
        deliver.start();
    }

    public void startA() {
        a.setName("Prod1");
        if (!prod1.isProducing()) {
            prod1.startProducing();
        } else {
            a.start();
        }
    }

    public void startB () {
        b.setName("Prod2");
        if (!prod2.isProducing()) {
            prod2.startProducing();
        } else {
            b.start();
        }
    }

    public void stopA () {
        prod1.halt();
    }

    public void stopB () {
        prod2.halt();
    }

    private class StorageObserver implements Observer {
        @Override
        public void update(Observable o, Object obj) {
            if (obj.equals((String)"wait")){
                truck.setStatus("Truck: waiting...");
            } else if (obj instanceof Thread) {
                if (((Thread)obj).getName().equals("Prod1")) {
                    prod1.setStatus("Producer: waiting..");
                } else if (((Thread)obj).getName().equals("Prod2")) {
                    prod2.setStatus("Producer: waiting..");
                }
            }

            gui.setStorageInfo(storage);
        }
    }

    private class TruckObserver implements Observer {

        @Override
        public void update(Observable o, Object obj) {
            if (obj instanceof FoodItem) {
                gui.setTruckInfo(((FoodItem) obj).getName());
            }
            gui.setTruckInfo(truck);
        }

    }

    private class FactoryObserver implements Observer {

        @Override
        public void update(Observable o, Object obj) {
            Factory temp = null;
            if (obj instanceof Factory) {
                temp = (Factory)obj;
            }
            gui.setFactoryInfo(temp);
        }
    }
}
