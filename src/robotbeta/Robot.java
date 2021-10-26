package robotbeta;

import battlecode.common.*;

import java.util.Map;

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


    static void updateActionRadius() {
        actionRadius = rc.getType().actionRadiusSquared;
    }

    static void updateSensorRadius() {
        sensorRadius = rc.getType().sensorRadiusSquared;
    }

    /**
     * Attempts to move in a given direction.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
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

    /**
     * Encodes location info into a flag
     *
     * @param loc location to send
     * @throws GameActionException
     */
    static void sendLocation(MapLocation loc) throws GameActionException {
        int x = loc.x, y = loc.y;
        int encodedLocation = (x % 128) * 128 + (y % 128);
        if (rc.canSetFlag(encodedLocation)) {
            rc.setFlag((encodedLocation));
        }
    }

    /**
     * Decodes location info from a flag
     *
     * @param flag flag received
     * @return location
     * @throws GameActionException
     */
    static MapLocation getLocationFromFlag(int flag) throws GameActionException {
        int y = flag % 128;
        int x = (flag / 128) % 128;

        MapLocation currentLocation = rc.getLocation();
        int offsetX128 = currentLocation.x / 128;
        int offsetY128 = currentLocation.y / 128;
        MapLocation actualLocation = new MapLocation(offsetX128 * 128 + x, offsetY128 * 128 + y);

        MapLocation alt = actualLocation.translate(-128, 0);
        if (rc.getLocation().distanceSquaredTo(alt) < rc.getLocation().distanceSquaredTo(actualLocation)) {
            actualLocation = alt;
        }

        alt = actualLocation.translate(128, 0);
        if (rc.getLocation().distanceSquaredTo(alt) < rc.getLocation().distanceSquaredTo(actualLocation)) {
            actualLocation = alt;
        }

        alt = actualLocation.translate(0, -128);
        if (rc.getLocation().distanceSquaredTo(alt) < rc.getLocation().distanceSquaredTo(actualLocation)) {
            actualLocation = alt;
        }

        alt = actualLocation.translate(0, 128);
        if (rc.getLocation().distanceSquaredTo(alt) < rc.getLocation().distanceSquaredTo(actualLocation)) {
            actualLocation = alt;
        }

        return actualLocation;
    }

}
