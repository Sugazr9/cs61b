package creatures;
import edu.princeton.cs.algs4.StdRandom;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;
import java.awt.Color;
import java.util.Map;
import java.util.List;

public class Clorus extends Creature {

    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;

    /** creates clorus with energy equal to E. */
    public Clorus(double e) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }

    /** creates a clorus with energy equal to 1. */
    public Clorus() {
        this(1);
    }

    /** Should return a color with red = 99, blue = 76, and green = 0. */
    public Color color() {
        return color(r, g, b);
    }

    /** Do nothing with C, Plips are pacifists. */
    public void attack(Creature c) {
        energy += c.energy();
    }

    /** Plips should lose 0.15 units of energy when moving. If you want to
     *  to avoid the magic number warning, you'll need to make a
     *  private static final variable. This is not required for this lab.
     */
    public void move() {
        energy -= 0.03;
    }


    /** Plips gain 0.2 energy when staying due to photosynthesis. */
    public void stay() {
        energy -= 0.01;
    }

    /** Clori and their offspring each get 50% of the energy, with none
     *  lost to the process. Now that's efficiency! Returns a baby
     *  Clorus.
     */
    public Clorus replicate() {
        energy /= 2;
        return new Clorus(energy);
    }

    /** Clori take exactly the following actions based on NEIGHBORS:
     *  1. If no empty adjacent spaces, STAY.
     *  2. Otherwise, if any Plips, ATTACK one at random.
     *  3. Otherwise, if energy >= 1, REPLICATE.
     *  4. Otherwise, if nothing else, MOVE.
     *
     *  Returns an object of type Action. See Action.java for the
     *  scoop on how Actions work. See SampleCreature.chooseAction()
     *  for an example to follow.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empty = getNeighborsOfType(neighbors, "empty");
        if (empty.size() > 0) {
            List<Direction> plips = getNeighborsOfType(neighbors, "plip");
            if (plips.size() > 0) {
                Double space = StdRandom.uniform() * plips.size();
                Direction moveDir = plips.get(space.intValue());
                return new Action(Action.ActionType.ATTACK, moveDir);
            } else if (energy >= 1.0) {
                Double space = StdRandom.uniform() * empty.size();
                Direction moveDir = empty.get(space.intValue());
                return new Action(Action.ActionType.REPLICATE, moveDir);
            } else {
                Double space = StdRandom.uniform() * empty.size();
                Direction moveDir = empty.get(space.intValue());
                return new Action(Action.ActionType.MOVE, moveDir);
            }
        }
        return new Action(Action.ActionType.STAY);
    }

}