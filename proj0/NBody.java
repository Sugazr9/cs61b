public class NBody {
	public static double readRadius(String file) {
		In ready = new In(file);
		ready.readInt();
		double R = ready.readDouble();
		return R;
	}

	public static Planet[] readPlanets(String file) {
		In ready = new In(file);
		Planet[] system = new Planet[ready.readInt()];
		ready.readDouble();
		int counter = 0;
		while(counter < system.length) {
			double posX = ready.readDouble();
			double posY = ready.readDouble();
			double velX = ready.readDouble();
			double velY = ready.readDouble();
			double mass = ready.readDouble();
			String img = ready.readString();
			system[counter] = new Planet(posX, posY, velX, velY, mass, img);
			counter += 1;
		}
		return system;
	}

	public static void main(String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double radius = readRadius(filename);
		Planet[] system = readPlanets(filename);
		StdDraw.setScale(radius*-1, radius);
		StdDraw.picture(0, 0, "starfield.jpg", radius, radius);
		for (Planet curr : system) {
			curr.draw();
		}
	}
}
