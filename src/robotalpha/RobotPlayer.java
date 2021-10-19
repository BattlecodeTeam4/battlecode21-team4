package robotalpha;
import battlecode.common.*;

public strictfp class RobotPlayer
{
    static RobotController rc;

    static final RobotType[] spawnableRobot = {
        RobotType.POLITICIAN,
        RobotType.SLANDERER,
        RobotType.MUCKRAKER,
    };

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

    static int turnCount;

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        RobotPlayer.rc = rc;

        turnCount = 0;

        //System.out.println("I'm a " + rc.getType() + " and I just got created!");
        while (true) {
            turnCount += 1;
            // Try/catch blocks stop unhandled exceptions, which cause your robot to freeze
            try {
                // Here, we've separated the controls into a different method for each RobotType.
                // You may rewrite this into your own control structure if you wish.
                //System.out.println("I'm a " + rc.getType() + "! Location " + rc.getLocation());
                switch (rc.getType()) {
                    case ENLIGHTENMENT_CENTER: runEnlightenmentCenter(); break;
                    case POLITICIAN:           runPolitician();          break;
                    case SLANDERER:            runSlanderer();           break;
                    case MUCKRAKER:            runMuckraker();           break;
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            }
        }
    }

    static void runEnlightenmentCenter() throws GameActionException {
        RobotType toBuild = randomSpawnableRobotType();
        //RobotType toBuild = RobotType.POLITICIAN;
        int influence = 50;
        for (Direction dir : directions) {
            if (rc.canBuildRobot(toBuild, dir, influence)) {
                rc.buildRobot(toBuild, dir, influence);
            } else {
                break;
            }
        }
    }

    static void runPolitician() throws GameActionException {
        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;
        RobotInfo[] attackable = rc.senseNearbyRobots(actionRadius, enemy);
        RobotInfo[] neutral = rc.senseNearbyRobots(actionRadius, Team.NEUTRAL);
        if ((attackable.length != 0 || neutral.length != 0) && rc.canEmpower(actionRadius)) {
            System.out.println("empowering...");
            rc.empower(actionRadius);
            System.out.println("empowered");
            return;
        }
        if (tryMove(randomDirection())){}
    }

    static void runSlanderer() throws GameActionException {
        Team enemies = rc.getTeam().opponent();
        // scan for enemies in surrounding
        int actionRadius = rc.getType().actionRadiusSquared;
        int moveX = 0;
        int moveY = 0;
        // checking if Muckraker's found, if found, then move
        for (RobotInfo enemy : rc.senseNearbyRobots(actionRadius, enemies)) {
            if (enemy.getType() == RobotType.MUCKRAKER) {
                MapLocation enemyLoc = enemy.location;
                if (enemyLoc.x > rc.getLocation().x) {
                    moveX--;
                } else {
                    moveX++;
                }

                if (enemyLoc.y > rc.getLocation().y) {
                    moveY--;
                } else {
                    moveY++;
                }

            }
            MapLocation destination = rc.getLocation().translate(moveX, moveY);
            System.out.println("My next runaway destination is: " + destination);
            Direction direct = rc.getLocation().directionTo(destination);
            tryMove(direct);
        }
        //move randomly if enemy not detected
        tryMove(randomDirection());

    }

    /**
     *
     * Muckraker Flags:
     * 1 - Neutral Enlightenment Centers
     * 2 - Enemy Politician
     * 3 - Enemy Muckraker
     * 4 - Enemy Enlightenment Centers
     *
     * This will later be expanded to communicate location
     * information via flags.
     *
     **/
    static void runMuckraker() throws GameActionException {
        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;
        final double passabilityLimit = 0.7;
        Direction myDirection = null;

//        // Move closer to robots it detects
//        for (MapLocation robotLocation : rc.detectNearbyRobots(actionRadius))
//        {
//            Direction d = rc.getLocation().directionTo(robotLocation);
//            if (rc.getLocation().equals(robotLocation)) {
//                break;
//            }
//            for (int i = 0; i < 2; ++i) {
//                if (rc.isReady()) {
//                    // If the space I am trying to move to is easily passable
//                    // then move
//                    if (rc.canMove(d) && rc.sensePassability(rc.getLocation().add(d)) >= passabilityLimit) {
//                        rc.move(d);
//                    } else if (myDirection == null) {
//                            myDirection = d.rotateRight();
//                    }
//                }
//            }
//        }

        // Sense enemy robots
        for (RobotInfo robot : rc.senseNearbyRobots(actionRadius, enemy))
        {
            if (robot.type.canBeExposed())
            {
                // It's a slanderer... go get them!
                if (rc.canExpose(robot.location))
                {
                    System.out.println("e x p o s e d");
                    rc.expose(robot.location);
                    return;
                }
            }

            // It's a politician
            if (robot.type.canEmpower())
            {
                rc.setFlag(1);
                System.out.println("I found a politician and set my flag to 2");
            }

            // It's an enlightenment center
            if (robot.type.canBid())
            {
                rc.setFlag(4);
                System.out.println("I found an enemy EC and set my flag to 4");
            }

            // It's a muckraker
            if (robot.type.canExpose())
            {
                rc.setFlag(3);
                System.out.println("I found a muckraker and set my flag to 3");
            }
        }

        // Sense neutral robots
        for (RobotInfo robot : rc.senseNearbyRobots(actionRadius, Team.NEUTRAL)) {
            if (robot.type.canBid()) {
                    rc.setFlag(1);
                    System.out.println("I found a neutral EC and set my flag to 1 ");
            }
        }

        // Simple movement and passability check
        if (rc.isReady()) {
            Direction d = randomDirection();
            // If the space I am trying to move to is easily passable
            // then move
            if (rc.canMove(d) && rc.sensePassability(rc.getLocation().add(d)) >= passabilityLimit) {
                tryMove(d);
            }
        }
    }

    /**
     * Returns a random Direction.
     *
     * @return a random Direction
     */
    static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }

    /**
     * Returns a random spawnable RobotType
     *
     * @return a random RobotType
     */
    static RobotType randomSpawnableRobotType() {
        return spawnableRobot[(int) (Math.random() * spawnableRobot.length)];
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
        if (rc.canMove(dir)) {
            rc.move(dir);
            return true;
        } else return false;
    }
}
