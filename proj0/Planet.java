public class Planet {
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;

	public Planet(double xPos, double yPos, double xVel, 
				  double yVel, double m, String img) {
		xxPos = xPos;
		yyPos = yPos;
		xxVel = xVel;
		yyVel = yVel;
		mass = m;
		imgFileName = img;
	}

	public Planet(Planet p) {
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;	 
	}

	public double calcDistance(Planet other_p) {
		double distance = Math.sqrt((other_p.xxPos - xxPos)*(other_p.xxPos - xxPos) + (other_p.yyPos - yyPos)*(other_p.yyPos - yyPos));
		return distance;
	}

	public double calcForceExertedBy(Planet other_p) {
		double the_force = 6.67e-11*other_p.mass*mass/((calcDistance(other_p))*(calcDistance(other_p)));
		return the_force;
	}

	public double calcForceExertedByX(Planet other_p) {
		double the_forcex = calcForceExertedBy(other_p)*(other_p.xxPos - xxPos)/calcDistance(other_p);
		return the_forcex;
	}

	public double calcForceExertedByY(Planet other_p) {
		double the_forcey = calcForceExertedBy(other_p)*(other_p.yyPos - yyPos)/calcDistance(other_p);
		return the_forcey;
	}

	public double calcNetForceExertedByX(Planet[] system) {
		double net_forcex = 0.0;
		int counter = 0;
		while (counter < system.length) {
			if (this.equals(system[counter])) {
			counter += 1;
			}
			else {
				net_forcex += calcForceExertedByX(system[counter]);
				counter += 1;
			}
		}
		return net_forcex;
	}

	public double calcNetForceExertedByY(Planet[] system) {
		double net_forcey = 0.0;
		int counter = 0;
		while (counter < system.length) {
			if (this.equals(system[counter])) {
			counter += 1;
			}
			else {
				net_forcey += calcForceExertedByY(system[counter]);
				counter += 1;
			}
		}
		return net_forcey;
	}

	public void update(double dt, double fX, double fY) {
		double aX = fX/mass;
		double aY = fY/mass;
		xxVel += aX*dt;
		yyVel += aY*dt;
		xxPos += xxVel*dt;
		yyPos += yyVel*dt;
	}
}

