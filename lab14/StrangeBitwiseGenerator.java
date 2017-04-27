import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator{
    int period;
    int state;

    StrangeBitwiseGenerator(int t) {
        period = t;
        state = 0;
    }
    @Override
    public double next() {
        int weirdState = state & (state >>> 3) % period;
        double value = normalize(weirdState);
        state++;
        return value;
    }

    double normalize (int remainder) {
        return (((remainder * 2.0) / period) - 2.0) / 2.0;
    }
}