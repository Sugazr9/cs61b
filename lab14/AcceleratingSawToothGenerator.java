import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator{
    int period;
    int state;
    double factor;

    AcceleratingSawToothGenerator(int t, double acceleration) {
        period = t;
        state = 0;
        factor = acceleration;
    }
    @Override
    public double next() {
        double value = normalize(state % period);
        state++;
        if (state % period == 0) {
            state = 0;
            period *= factor;
        }
        return value;
    }

    double normalize (int remainder) {
        return remainder * 2.0 / period - 1.0;
    }
}
