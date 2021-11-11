package robotgamma;

import battlecode.common.*;

@SuppressWarnings({"JavaDoc", "RedundantThrows", "unused", "UnusedReturnValue", "DuplicatedCode"})
public abstract class Robot extends RobotPlayer {

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

    static MapLocation homeLoc;
    static int homeID;

    static Team enemy = null;
    static int actionRadius = 0;
    static int senseRadius = 0;
    static int detectRadius = 0;
    static int slaThreshold = 0;
    static MapLocation target = null;
    static int targetTeam = 0;
    static Direction strDir = null;
    static int influence = 0;
    static int currRound = 0;
    static int curr = 0;
    static int conviction = 0;

    /**
     * @return
     */
    public static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }

    /**
     *
     */
    public static void updateActionRadius() {
        actionRadius = rc.getType().actionRadiusSquared;
    }

    public static void updateSenseRadius() {
        senseRadius = rc.getType().sensorRadiusSquared;
    }

    public static void updateDetectRadius() {
        detectRadius = rc.getType().detectionRadiusSquared;
    }

    /**
     * @throws GameActionException
     */
    public static void init() throws GameActionException {
        if (actionRadius == 0) updateActionRadius();
        if (senseRadius == 0) updateSenseRadius();
        if (detectRadius == 0) updateDetectRadius();
        if (enemy == null) enemy = rc.getTeam().opponent();
        if (homeLoc == null) findHome();
        if (!rc.canGetFlag(homeID)) {
            homeID = 0;
            homeLoc = null;
        }
        influence = rc.getInfluence();
        currRound = rc.getRoundNum();
        conviction = rc.getConviction();
        slaThreshold = 290;
    }

    /**
     * @throws GameActionException
     */
    public static void findHome() throws GameActionException {
        MapLocation curr = rc.getLocation();
        for (int i = directions.length; --i >= 0; ) {
            MapLocation parseLoc = curr.add(directions[i]);
            if (rc.onTheMap(parseLoc)) {
                RobotInfo currBot = rc.senseRobotAtLocation(parseLoc);
                if (currBot != null && currBot.type == RobotType.ENLIGHTENMENT_CENTER && currBot.team == rc.getTeam()) {
                    if (homeLoc == null) {
                        homeLoc = currBot.getLocation();
                        homeID = currBot.getID();
                    }
                }
            }
        }
    }

    /**
     * @param dir
     * @return
     * @throws GameActionException
     */
    public static boolean tryMove(Direction dir) throws GameActionException {
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
    public static boolean pathfinding(Direction dir) throws GameActionException {
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
    public static boolean moveLocation(MapLocation loc) throws GameActionException {
        if (loc == null) {
            tryMove(randomDirection());
            return true;
        }
        Direction dir = rc.getLocation().directionTo(loc);
        if (dir != Direction.CENTER) {
            if (tryMove(dir)) {
                return true;
            } else {
                return tryMove(randomDirection());
            }
        }
        return false;
    }

    /**
     * @throws GameActionException
     */
    public static void moveStraight() throws GameActionException {
        if (strDir == null) {
            strDir = randomDirection();
        }
        if (rc.isReady()) {
            if (!rc.canMove(strDir)) strDir = randomDirection();
            tryMove(strDir);
        }
    }

    /**
     * Encodes location and team info into a flag
     *
     * @param loc location to send
     * @param team team for EC at the location
     * @throws GameActionException
     */
    public static int sendLocation(MapLocation loc, int team) throws GameActionException {
        int x = loc.x, y = loc.y;
        int encodedLocation = 0;
        if (rc.canSetFlag(encodedLocation)) {
            encodedLocation = (x % 128) * 128 + (y % 128) + team * 128 * 128;
            rc.setFlag((encodedLocation));
        }
        return encodedLocation;
    }

    /**
     * Decodes location info from a flag
     *
     * @param flag flag received
     * @return location
     * @throws GameActionException
     */
    public static MapLocation getLocationFromFlag(int flag) throws GameActionException {
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

    /**
     * Decodes team info from flag
     *
     * @param flag flag received
     * @return int representing team
     * @throws GameActionException
     */
    public static int getTeamFromFlag(int flag) throws GameActionException {
        int team = flag / 128 / 128;
        return team;
    }
}
