public class Planet {
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;

	public Planet(double xxPos, double yyPos, double xxVel,
				  double yyVel, double mass, String imgFileName) {
		this.xxPos = xxPos;
		this.yyPos = yyPos;
		this.xxVel = xxVel;
		this.yyVel = yyVel;
		this.mass = mass;
		this.imgFileName = imgFileName;
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
		double distance = Math.sqrt(Math.pow(other_p.xxPos - xxPos, 2) + Math.pow(other_p.yyPos - yyPos, 2));
		return distance;
	}

	public double calcForceExertedBy(Planet other_p) {
		double the_force = 6.67e-11*other_p.mass*mass/(Math.pow(calcDistance(other_p), 2));
		return the_force;
	}

	public double calcForceExertedByX(Planet other_p) {
		double the_xforce = calcForceExertedBy(other_p)*(other_p.xxPos - xxPos)/calcDistance(other_p);
		return the_xforce;
	}

	public double calcForceExertedByY(Planet other_p) {
		double the_yforce = calcForceExertedBy(other_p)*(other_p.yyPos - yyPos)/calcDistance(other_p);
		return the_yforce;
	}

	public double calcNetForceExertedByX(Planet[] system) {
		double net_xforce = 0.0;
        for (Planet curr_planet : system) {
            if (!this.equals(curr_planet)) {
                net_xforce += calcForceExertedByX(curr_planet);
            }
        }
		return net_xforce;
	}

	public double calcNetForceExertedByY(Planet[] system) {
		double net_yforce = 0.0;
		for (Planet curr_planet : system) {
			if (!this.equals(curr_planet)) {
				net_yforce += calcForceExertedByY(curr_planet);
			}
		}
		return net_yforce;
	}

	public void update(double dt, double fX, double fY) {
		double aX = fX/mass;
		double aY = fY/mass;
		xxVel += aX*dt;
		yyVel += aY*dt;
		xxPos += xxVel*dt;
		yyPos += yyVel*dt;
	}

	public void draw() {
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}
}

