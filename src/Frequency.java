import java.util.Random;

public class Frequency {
    private int sampling;
    private float[] frequency;

    public Frequency(int sampling) {
        this.sampling = sampling;
        frequency = new float[sampling];
    }

    public void generateRandomFrequency() {
        Random random = new Random();
        for (int i = 0; i < sampling; ++i) {
            frequency[i] = random.nextFloat();
        }
    }
}