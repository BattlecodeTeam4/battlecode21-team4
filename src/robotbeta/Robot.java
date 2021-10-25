package robotbeta;

import battlecode.common.*;

public abstract class Robot extends RobotPlayer {

    static MapLocation homeLoc;
    static int homeID;
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
    static MapLocation target = null;
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

    static void init() throws GameActionException {
        findHome();
        System.out.println(homeID);
    }

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

    static void updateSensorRadius() {
        sensorRadius = rc.getType().sensorRadiusSquared;
    }

    static boolean tryMove(Direction dir) throws GameActionException
    {
        //System.out.println("I am trying to move " + dir + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(dir));
        if (rc.canMove(dir)) {
            rc.move(dir);
            return true;
        } else
            return false;
    }

    static boolean pathfinding(Direction dir) throws GameActionException{
        if (rc.canMove(dir)) {
            if (rc.sensePassability(rc.getLocation().add(dir)) >= passabilityLimit) {
                rc.move(dir);
                return true;
            } else if (rc.sensePassability(rc.getLocation()) < passabilityLimit) {
                rc.move(dir);
                return true;
            } else
                return false;
        }
        return false;
    }

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
}
