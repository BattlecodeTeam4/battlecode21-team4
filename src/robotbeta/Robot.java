package robotbeta;

import battlecode.common.*;

public class Robot extends RobotPlayer {
    static final Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST,
    };
    /**
     * Returns a random Direction.
     *
     * @return a random Direction
     */
    static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }

    static Team enemy = rc.getTeam().opponent();
    static int actionRadius = 0;
    static int sensorRadius = 0;
    final static double passabilityLimit = 0.5;

    /**
     * Attempts to move in a given direction.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static void updateActionRadius() {
        actionRadius = rc.getType().actionRadiusSquared;
    }

    static void updateSensorRadius() {
        sensorRadius = rc.getType().sensorRadiusSquared;
    }

    static boolean tryMove(Direction dir) throws GameActionException {
        //System.out.println("I am trying to move " + dir + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(dir));
        /*if (rc.canMove(dir)) {
            if (rc.sensePassability(rc.getLocation().add(dir)) >= passabilityLimit) {
                rc.move(dir);
                return true;
            } else if (rc.sensePassability(rc.getLocation()) < passabilityLimit) {
                rc.move(dir);
                return true;
            }
            else
                return false;*/
        if (rc.canMove(dir)) {
            rc.move(dir);
            return true;
        } else
            return false;
    }
}
