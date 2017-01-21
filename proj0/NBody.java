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

}
