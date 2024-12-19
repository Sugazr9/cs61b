public class NBody {
	public static double readRadius(String file) {
		In universe_info = new In(file);
		universe_info.readInt();
		double R_universe = universe_info.readDouble();
		return R_universe;
	}

	public static Planet[] readPlanets(String file) {
		In universe_info = new In(file);
		int num_planets = universe_info.readInt();
		Planet[] system = new Planet[num_planets];
		universe_info.readDouble();
		for (int i = 0; i < num_planets; i++) {
			double posX = universe_info.readDouble();
			double posY = universe_info.readDouble();
			double velX = universe_info.readDouble();
			double velY = universe_info.readDouble();
			double mass = universe_info.readDouble();
			String img = universe_info.readString();
			system[i] = new Planet(posX, posY, velX, velY, mass, img);
		}
		return system;
	}

	public static void main(String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double R_universe = readRadius(filename);
		Planet[] system = readPlanets(filename);
		int num_planets = system.length;
		StdAudio.play("audio/Carol_of_the_Bells.wav");
		StdDraw.setScale(R_universe*-1, R_universe);
		StdDraw.picture(0, 0, "images/starfield.jpg", R_universe*2, R_universe*2);
		for (Planet p : system) {
			p.draw();
		}
		double track_time = 0;
		while (track_time <= T) {
			double[] xForces = new double[num_planets];
			double[] yForces = new double[num_planets];
			for (int i = 0; i < num_planets; i++) {
				xForces[i] = system[i].calcNetForceExertedByX(system);
				yForces[i] = system[i].calcNetForceExertedByY(system);
			}
			for (int i = 0; i < num_planets; i++) {
				system[i].update(dt, xForces[i], yForces[i]);
			}
			StdDraw.picture(0, 0, "images/starfield.jpg", R_universe*2, R_universe*2);
			for (Planet curr : system) {
				curr.draw();
			}
			StdDraw.show(10);
			track_time += dt;
		}
		StdOut.printf("%d\n", num_planets);
		StdOut.printf("%.2e\n", R_universe);
		for (Planet p : system) {
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
   			p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);
		}
	}
}
