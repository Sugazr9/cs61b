import lab14lib.Generator;

public class SawToothGenerator implements Generator{
    int period;
    int state;

    SawToothGenerator(int t) {
        period = t;
        state = 0;
    }
    @Override
    public double next() {
        double value = normalize(state % period);
        state++;
        return value;
    }

    double normalize (int remainder) {
        return remainder * 2.0 / period - 1.0;
    }
}
