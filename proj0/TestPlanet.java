public class TestPlanet {
	public static void main(String[] args) {
		Planet p1 = new Planet(3.0, 10.0, 2.0, 3.0, 10.0, "BlackHolesDontHaveImages");
		Planet p2 = new Planet(4.0, 15.0, 4.0, 1.0, 13.0, "BlackHolesDontHaveImages");
		System.out.println("Force1: " + p1.calcForceExertedBy(p2));
		System.out.println("Force1x: " + p1.calcForceExertedByX(p2));
		System.out.println("Force1y: " + p1.calcForceExertedByY(p2));
		System.out.println("Force2: " + p2.calcForceExertedBy(p1));
		System.out.println("Force2x: " + p2.calcForceExertedByX(p1));
		System.out.println("Force2y: " + p2.calcForceExertedByY(p1));
	}
}