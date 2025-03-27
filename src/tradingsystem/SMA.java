package tradingsystem;

import java.util.LinkedList;
import java.util.Queue;

public class SMA {
    private final Queue<Integer> dataQueue;
    private final int numberOfData;
    private int sum;

    public SMA(int numberOfData) {
        this.numberOfData = numberOfData;
        if (numberOfData <= 0) {
            throw new IllegalArgumentException("Number of data has to be positive");
        }
        sum = 0;
        dataQueue = new LinkedList<>();
    }

    public void nextValue(int value) {
        if (dataQueue.size() >= numberOfData) {
            sum -= dataQueue.remove();
        }
        sum += value;
        dataQueue.add(value);
    }

    public double average() {
        if (dataQueue.size() < numberOfData) {
            throw new UnsupportedOperationException("SMA does not exist yet");
        }
        return (double) sum / numberOfData;
    }
}
