package robotdelta;

import battlecode.common.*;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MuckrakerTest {
    @Mock
    RobotController mockRC = mock(RobotController.class);
    MapLocation target = new MapLocation(100, 100);

    @Test
    public void resetFlagEqualsTest() throws GameActionException {
        Muckraker.homeID = 200;
        when(mockRC.canGetFlag(200)).thenReturn(true);
        when(mockRC.getFlag(200)).thenReturn(1);

        when(mockRC.getID()).thenReturn(400);
        when(mockRC.getFlag(mockRC.getID())).thenReturn(1);

        Muckraker.rc = mockRC;

        Assert.assertEquals(0, Muckraker.resetFlag());
    }

    @Test
    public void resetFlagEqualsNotEqualsTest() throws GameActionException {
        Muckraker.homeID = 200;
        when(mockRC.canGetFlag(200)).thenReturn(true);
        when(mockRC.getFlag(200)).thenReturn(1);

        when(mockRC.getID()).thenReturn(400);
        when(mockRC.getFlag(mockRC.getID())).thenReturn(2);

        Muckraker.rc = mockRC;
        Muckraker.resetFlag();
        Assert.assertEquals(1, Muckraker.resetFlag());
    }

    @Test
    public void resetFlagEqualsNoHomeIDTest() throws GameActionException {
        Muckraker.homeID = 200;
        when(mockRC.canGetFlag(200)).thenReturn(false);

        Muckraker.rc = mockRC;
        Muckraker.resetFlag();

        Assert.assertEquals(0, Muckraker.resetFlag());
    }

    @Test
    public void scanForTargetEmptyTest() throws GameActionException {
        Muckraker.senseRadius = 30;
        when(mockRC.senseNearbyRobots(Muckraker.senseRadius)).thenReturn(new RobotInfo[]{});
        Muckraker.rc = mockRC;

        Assert.assertEquals(0, Muckraker.scanForTarget());
    }

    @Test
    public void scanForTargetEnemyTest() throws GameActionException {
        Muckraker.senseRadius = 30;
        when(mockRC.getTeam()).thenReturn(Team.A);
        when(mockRC.getTeam().opponent()).thenReturn(Team.B);
        when(mockRC.senseNearbyRobots(Muckraker.senseRadius)).thenReturn(new RobotInfo[]{new RobotInfo(100, Team.B,
                RobotType.ENLIGHTENMENT_CENTER, 100, 100, target)});
        when(mockRC.canSetFlag(12900)).thenReturn(true);
        Muckraker.rc = mockRC;
        Assert.assertEquals(1, Muckraker.scanForTarget());
    }

    @Test
    public void scanForTargetNeutralTest() throws GameActionException {
        Muckraker.senseRadius = 30;
        when(mockRC.getTeam()).thenReturn(Team.A);
        when(mockRC.getTeam().opponent()).thenReturn(Team.B);
        when(mockRC.senseNearbyRobots(Muckraker.senseRadius)).thenReturn(new RobotInfo[]{new RobotInfo(100,
                Team.NEUTRAL, RobotType.ENLIGHTENMENT_CENTER, 100, 100, target)});
        when(mockRC.canSetFlag(12900)).thenReturn(true);
        Muckraker.rc = mockRC;
        Assert.assertEquals(1, Muckraker.scanForTarget());
    }

    @Test
    public void scanAndEmpowerTest() throws GameActionException {
        Muckraker.actionRadius = 12;
        Muckraker.enemy = Team.B;

        RobotInfo fakeSla = new RobotInfo(100, Team.B, RobotType.SLANDERER, 100,
                100, target);
        when(mockRC.senseNearbyRobots(Muckraker.actionRadius, Muckraker.enemy)).thenReturn(new RobotInfo[]
                {fakeSla});
        when(mockRC.canExpose(fakeSla.getLocation())).thenReturn(true);
        when(mockRC.isReady()).thenReturn(true);
        when(mockRC.getLocation()).thenReturn(new MapLocation(200, 200));
        Muckraker.rc = mockRC;

        Assert.assertEquals(1, Muckraker.scanAndEmpower());
    }

    @Test
    public void hoverAroundTargetSameTeamTest() throws GameActionException {
        Muckraker.enemy = Team.B;
        Muckraker.target = target;
        when(mockRC.getTeam()).thenReturn(Team.A);
        when(mockRC.canSenseLocation(target)).thenReturn(true);
        when(mockRC.senseRobotAtLocation(target)).thenReturn(new RobotInfo(100, Team.A,
                RobotType.SLANDERER, 100, 100, target));
        Muckraker.rc = mockRC;
        Muckraker.hoverAroundTarget();
    }

    @Test
    public void lastTargetMathGreaterTest() throws GameActionException {
        Muckraker.roundSinceLastTarget = Muckraker.lastHoverRoundThresh + 1;
        Muckraker.lastTargetMath();
    }

    @Test
    public void lastTargetMathLesserTest() throws GameActionException {
        Muckraker.roundSinceLastTarget = Muckraker.lastHoverRoundThresh - 1;
        Muckraker.lastTargetMath();
    }


    @Test
    public void hoverAroundTargetTest() throws GameActionException {
        when(mockRC.getTeam()).thenReturn(Team.A);
        Muckraker.enemy = Team.B;
        Muckraker.target = target;
        when(mockRC.canSenseLocation(target)).thenReturn(true);
        when(mockRC.senseRobotAtLocation(target)).thenReturn(new RobotInfo(100, Muckraker.enemy,
                RobotType.ENLIGHTENMENT_CENTER, 100, 100, target));
        when(mockRC.getLocation()).thenReturn(new MapLocation(150, 150));
        Muckraker.rc = mockRC;
        Muckraker.hoverAroundTarget();
    }

    @Test
    public void hoverAroundTargetAdjacentTest() throws GameActionException {
        when(mockRC.getTeam()).thenReturn(Team.A);
        Muckraker.enemy = Team.B;
        Muckraker.target = target;
        when(mockRC.canSenseLocation(target)).thenReturn(true);
        when(mockRC.senseRobotAtLocation(target)).thenReturn(new RobotInfo(100, Muckraker.enemy,
                RobotType.ENLIGHTENMENT_CENTER, 100, 100, target));
        when(mockRC.getLocation()).thenReturn(new MapLocation(101, 100));
        Muckraker.rc = mockRC;
        Muckraker.hoverAroundTarget();
    }

    @Test
    public void moveAdjacentTest() throws GameActionException {
        when(mockRC.getLocation()).thenReturn(target);
        when(mockRC.canSenseLocation(target.add(Direction.NORTH))).thenReturn(true);
        when(mockRC.senseRobotAtLocation(target.add(Direction.NORTH))).thenReturn(null);
        Muckraker.rc = mockRC;
        Muckraker.moveAdjacent();
    }

    @Test
    public void findSpotTest() throws GameActionException {
        Muckraker.target = target;
        MapLocation north = target.add(Direction.NORTH);
        for (Direction dir : Muckraker.directions) {
            when(mockRC.canSenseLocation(target.add(dir))).thenReturn(true);
        }
        when(mockRC.getLocation()).thenReturn(target);
        when(mockRC.getTeam()).thenReturn(Team.A);
        when(mockRC.senseRobotAtLocation(north)).thenReturn(null);
        Muckraker.rc = mockRC;
        Muckraker.findSpot();
    }

    @Test
    public void findSpotSameLocTest() throws GameActionException {
        Muckraker.target = target;
        MapLocation north = target.add(Direction.NORTH);
        for (Direction dir : Muckraker.directions) {
            when(mockRC.canSenseLocation(target.add(dir))).thenReturn(true);
        }
        when(mockRC.getLocation()).thenReturn(north);
        when(mockRC.getTeam()).thenReturn(Team.A);
        when(mockRC.senseRobotAtLocation(north)).thenReturn(new RobotInfo(100, Team.A,
                RobotType.MUCKRAKER, 1, 1, north));
        Muckraker.rc = mockRC;
        Muckraker.findSpot();
    }

    @Test
    public void findSpotSurroundedTest() throws GameActionException {
        Muckraker.target = target;
        for (Direction dir : Muckraker.directions) {
            when(mockRC.canSenseLocation(target.add(dir))).thenReturn(true);
            when(mockRC.senseRobotAtLocation(target.add(dir))).thenReturn(new RobotInfo(100, Team.A,
                    RobotType.MUCKRAKER, 1, 1, target.add(dir)));
            when(mockRC.getLocation()).thenReturn(target.add(dir));
        }

        Muckraker.rc = mockRC;
        Assert.assertFalse(Muckraker.findSpot());
    }

    @Test
    public void findSlandererTest() throws GameActionException {
        Muckraker.senseRadius = 30;
        Muckraker.enemy = Team.B;
        when(mockRC.getTeam()).thenReturn(Team.A);
        when(mockRC.getLocation()).thenReturn(new MapLocation(110, 100));
        when(mockRC.senseNearbyRobots(Muckraker.senseRadius, Muckraker.enemy)).thenReturn(new RobotInfo[] {new
                RobotInfo(100, Team.A, RobotType.SLANDERER, 1, 1, new MapLocation(100, 100))});
        Muckraker.rc = mockRC;
        Assert.assertNotNull(Muckraker.findSlanderer());
    }
    @Test
    public void findNotSlandererTest() throws GameActionException {
        Muckraker.senseRadius = 30;
        Muckraker.enemy = Team.B;
        when(mockRC.getTeam()).thenReturn(Team.A);
        when(mockRC.getLocation()).thenReturn(new MapLocation(110, 100));
        when(mockRC.senseNearbyRobots(Muckraker.senseRadius, Muckraker.enemy)).thenReturn(new RobotInfo[] {new
                RobotInfo(100, Team.A, RobotType.MUCKRAKER, 1, 1, new MapLocation(100, 100))});
        Muckraker.rc = mockRC;
        Assert.assertNull(Muckraker.findSlanderer());
    }

    @Test
    public void RunMuckrakerTest() throws GameActionException {
        //Set to Run no code
        Muckraker.senseRadius = 30;
        when(mockRC.senseNearbyRobots(Muckraker.senseRadius)).thenReturn(new RobotInfo[]{});

        //Set to Run no code
        Muckraker.actionRadius = 12;
        Muckraker.enemy = Team.B;
        when(mockRC.senseNearbyRobots(Muckraker.actionRadius, Muckraker.enemy)).thenReturn(new RobotInfo[]{});
        when(mockRC.senseNearbyRobots(Muckraker.senseRadius, Muckraker.enemy)).thenReturn(new RobotInfo[]{});

        Muckraker.rc = mockRC;
        Muckraker.runMuckraker();
    }

    @Test
    public void RunMuckrakerTargetTest() throws GameActionException {
        //Set to Run no code
        Muckraker.senseRadius = 30;
        Muckraker.target = new MapLocation(100, 100);
        when(mockRC.canSenseLocation(target)).thenReturn(false);
        when(mockRC.getLocation()).thenReturn(new MapLocation(200, 200));
        when(mockRC.senseNearbyRobots(Muckraker.senseRadius)).thenReturn(new RobotInfo[]{});

        //Set to Run no code
        Muckraker.actionRadius = 12;
        Muckraker.enemy = Team.B;
        when(mockRC.senseNearbyRobots(Muckraker.actionRadius, Muckraker.enemy)).thenReturn(new RobotInfo[]{});

        Muckraker.rc = mockRC;
        Muckraker.runMuckraker();
    }

    @Test
    public void RunMuckrakerSlandererTest() throws GameActionException {
        Muckraker.senseRadius = 30;
        Muckraker.actionRadius = 12;
        Muckraker.enemy = Team.B;
        Muckraker.target = null;
        when(mockRC.getTeam()).thenReturn(Team.A);
        when(mockRC.getLocation()).thenReturn(new MapLocation(110, 100));
        when(mockRC.senseNearbyRobots(Muckraker.senseRadius)).thenReturn(new RobotInfo[]{});

        //Set to Run no code
        when(mockRC.senseNearbyRobots(Muckraker.actionRadius, Muckraker.enemy)).thenReturn(new RobotInfo[]{});
        when(mockRC.senseNearbyRobots(Muckraker.senseRadius, Muckraker.enemy)).thenReturn(new RobotInfo[] {new
                RobotInfo(100, Team.A, RobotType.SLANDERER, 1, 1, new MapLocation(100, 100))});
        Muckraker.rc = mockRC;
        Muckraker.runMuckraker();
    }
}
