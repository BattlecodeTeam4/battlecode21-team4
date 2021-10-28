package robotbeta;

import battlecode.common.*;

@SuppressWarnings({"JavaDoc", "RedundantThrows", "unused", "UnusedReturnValue", "DuplicatedCode"})
public abstract class Robot extends RobotPlayer {

    static MapLocation homeLoc;
    static int homeID;

    static Team enemy = rc.getTeam().opponent();
    static int actionRadius = 0;
    static int senseRadius = 0;

    static int slaThreshold = 290;

    static MapLocation target = null;
    final static double passAbilityLimit = 0.5;

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
     * @return
     */
    static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }

    /**
     *
     */
    static void updateActionRadius() {
        actionRadius = rc.getType().actionRadiusSquared;
    }

    /**
     * @throws GameActionException
     */
    static void init() throws GameActionException {
        findHome();
    }

    /**
     * @throws GameActionException
     */
    static void findHome() throws GameActionException {
        MapLocation curr = rc.getLocation();
        for(int i = directions.length; --i >= 0;)
        {
            MapLocation parseLoc = curr.add(directions[i]);
            if(rc.onTheMap(parseLoc)){
                RobotInfo currBot = rc.senseRobotAtLocation(parseLoc);
                if (currBot != null && currBot.type == RobotType.ENLIGHTENMENT_CENTER && currBot.team == rc.getTeam()) {
                    if(homeLoc == null){
                        homeLoc = currBot.getLocation();
                        homeID = currBot.getID();
                    }
                }
            }
        }
    }

    static void updateSenseRadius() {
        senseRadius = rc.getType().sensorRadiusSquared;
    }

    /**
     * @param dir
     * @return
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir) throws GameActionException
    {
        if (rc.canMove(dir)) {
            rc.move(dir);
            return true;
        } else
            return false;
    }

    /**
     * @param dir
     * @return
     * @throws GameActionException
     */
    static boolean pathfinding(Direction dir) throws GameActionException{
        if (rc.canMove(dir)) {
            if (rc.sensePassability(rc.getLocation().add(dir)) >= passAbilityLimit) {
                rc.move(dir);
                return true;
            } else if (rc.sensePassability(rc.getLocation()) < passAbilityLimit) {
                rc.move(dir);
                return true;
            } else
                return false;
        }
        return false;
    }

    /**
     * @param loc
     * @return
     * @throws GameActionException
     */
    static boolean moveLocation(MapLocation loc) throws GameActionException
    {
        if(loc == null){
            tryMove(randomDirection());
            return true;
        }
        Direction dir = rc.getLocation().directionTo(loc);
        if(dir != Direction.CENTER)
        {
            if(tryMove(dir))
            {
                return true;
            }
            else {
                return tryMove(randomDirection());
            }
        }
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
