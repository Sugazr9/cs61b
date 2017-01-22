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
			String img = "images/" + ready.readString();
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
		StdAudio.play("audio/Carol_of_the_Bells.wav");
		StdDraw.setScale(radius*-1, radius);
		StdDraw.picture(0, 0, "images/starfield.jpg", radius*2, radius*2);
		for (Planet curr : system) {
			curr.draw();
		}
		double track_time = 0;
		while (track_time <= T) {
			double[] xForces = new double[system.length];
			double[] yForces = new double[system.length];
			for (int i = 0; i < system.length; i++) {
				xForces[i] = system[i].calcNetForceExertedByX(system);
				yForces[i] = system[i].calcNetForceExertedByY(system);
			}
			for (int i = 0; i < system.length; i++) {
				system[i].update(dt, xForces[i], yForces[i]);
			}
			StdDraw.picture(0, 0, "images/starfield.jpg", radius*2, radius*2);
			for (Planet curr : system) {
				curr.draw();
			}
			StdDraw.show(10);
			track_time += dt;
		}
		StdOut.printf("%d\n", system.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < system.length; i++) {
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
   			system[i].xxPos, system[i].yyPos, system[i].xxVel, system[i].yyVel, system[i].mass, system[i].imgFileName);	
		}
	}
}
