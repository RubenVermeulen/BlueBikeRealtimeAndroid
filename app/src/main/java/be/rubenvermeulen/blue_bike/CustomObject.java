package be.rubenvermeulen.blue_bike;

/**
 * Created by ruben on 8/10/2016.
 */

public class CustomObject {
    private String propOne;
    private String propTwo;
    private int priority;

    public CustomObject(String propOne, String propTwo, int priority) {
        this.propOne = propOne;
        this.propTwo = propTwo;
        this.priority = priority;
    }

    public String getPropTwo() {
        return propTwo;
    }

    public String getPropOne() {
        return propOne;
    }

    public int getPriority() {
        return priority;
    }
}
