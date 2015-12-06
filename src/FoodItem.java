/**
 * Created by Jimmy on 2015-11-29.
 */
public class FoodItem {
    private String name;
    private double weight;
    private double volume;

    public FoodItem (double weight, double volume, String name) {
        this.weight = weight;
        this.volume = volume;
        this.name = name;
    }
    public double getWeight() {
        return weight;
    }

    public double getVolume() {
        return volume;
    }

    public String getName() {
        return name;
    }

}
