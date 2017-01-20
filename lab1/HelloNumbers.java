public class HelloNumbers {
    public static void main(String[] args) {
        int x = 0;
        int counter = 0;
        int total = 0;
        while (x < 10) {
            while (counter <= x) {
            total = total + counter;
            counter += 1;
            }
            System.out.print(total + " ");
            x = x + 1;
            total = 0;
            counter = 0;
        }
    }
}