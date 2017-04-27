import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture pic;
    private int width;
    private int height;

    public SeamCarver(Picture picture) {
        pic = new Picture(picture);
        width = pic.width();
        height = pic.height();
    }

    public Picture picture() {                  // current picture
        return new Picture(pic);
    }

    public int width() {                        // width of current picture
        return width;
    }

    public int height() {                       // height of current picture
        return height;
    }

    private void checkParams(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException();
        }
    }

    public double energy(int x, int y) {        // energy of pixel at column x and row y
        checkParams(x, y);
        int gX = pic.get(left(x), y).getGreen() - pic.get(right(x), y).getGreen();
        int rX = pic.get(left(x), y).getRed() - pic.get(right(x), y).getRed();
        int bX = pic.get(left(x), y).getBlue() - pic.get(right(x), y).getBlue();
        int gY = pic.get(x, top(y)).getGreen() - pic.get(x, bottom(y)).getGreen();
        int rY = pic.get(x, top(y)).getRed() - pic.get(x, bottom(y)).getRed();
        int bY = pic.get(x, top(y)).getBlue() - pic.get(x, bottom(y)).getBlue();

        double xSquare = Math.pow(gX, 2.0) + Math.pow(rX, 2.0) + Math.pow(bX, 2.0);
        double ySquare = Math.pow(gY, 2.0) + Math.pow(rY, 2.0) + Math.pow(bY, 2.0);

        return xSquare + ySquare;
    }

    private int left(int x) {
        if (x == 0) {
            return width - 1;
        }
        return x - 1;
    }

    private int right(int x) {
        if (x == width - 1) {
            return 0;
        }
        return x + 1;
    }

    private int top(int y) {
        if (y == 0) {
            return height - 1;
        }
        return y - 1;
    }

    private int bottom(int y) {
        if (y == height - 1) {
            return 0;
        }
        return y + 1;
    }

    public int[] findHorizontalSeam() {              // sequence of indices for horizontal seam
        Picture temp = new Picture(pic);
        int wTemp = width;
        int hTemp = height;
        Picture transposed = new Picture(height, width);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                transposed.set(y, x, temp.get(x, y));
            }
        }
        pic = transposed;
        width = hTemp;
        height = wTemp;
        int[] seam = findVerticalSeam();
        pic = temp;
        width = wTemp;
        height = hTemp;
        return seam;
    }

    public int[] findVerticalSeam() {               // sequence of indices for vertical seam
        double[][] energies = new double[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double currE = energy(x, y);
                if (y == 0) {
                    energies[x][0] = currE;
                } else {
                    if (x == 0) {
                        if (width == 1) {
                            energies[0][y] = energies[0][y - 1] + currE;
                        } else {
                            double prev = Math.min(energies[0][y - 1], energies[1][y - 1]);
                            energies[0][y] = currE + prev;
                        }
                    } else if (x == width - 1) {
                        double prev = Math.min(energies[x][y - 1], energies[x - 1][y - 1]);
                        energies[x][y] = currE + prev;
                    } else {
                        double prev = Math.min(Math.min(energies[x][y - 1], energies[x - 1][y - 1]),
                                energies[x + 1][y - 1]);
                        energies[x][y] = currE + prev;
                    }
                }
            }
        }
        int[] seam = new int[height];
        for (int y = height - 1; y > -1; y--) {
            int minIndex = 0;
            if (y == height - 1) {
                for (int x = 1; x < width; x++) {
                    if (energies[x][y] < energies[minIndex][y]) {
                        minIndex = x;
                    }
                }
            } else {
                int prev = seam[y + 1];
                minIndex = prev - 1;
                if (prev == 0) {
                    minIndex = 0;
                    if (energies[0][y] > energies[1][y]) {
                        minIndex = 1;
                    }
                } else if (prev == width - 1) {
                    if (energies[minIndex][y] > energies[minIndex + 1][y]) {
                        minIndex++;
                    }
                } else {
                    for (int x = prev; x < prev + 2; x++) {
                        if (energies[minIndex][y] > energies[x][y]) {
                            minIndex = x;
                        }
                    }
                }
            }
            seam[y] = minIndex;
        }
        return seam;
    }

    public void removeHorizontalSeam(int[] seam) {  // remove horizontal seam from picture
        if (seam.length != width) {
            throw new IllegalArgumentException();
        }
        for (int i = 1; i < seam.length; i++) {
            int dist = seam[i - 1] - seam[i];
            if (dist < -1 || dist > 1) {
                throw new IllegalArgumentException();
            }
        }

    }

    public void removeVerticalSeam(int[] seam) {    // remove vertical seam from picture
        if (seam.length != height) {
            throw new IllegalArgumentException();
        }
        for (int i = 1; i < seam.length; i++) {
            int dist = seam[i - 1] - seam[i];
            if (dist < -1 || dist > 1) {
                throw new IllegalArgumentException();
            }
        }
    }
}
