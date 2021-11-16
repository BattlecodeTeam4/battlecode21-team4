package robotgamma;

import battlecode.common.*;
import org.junit.*;
import org.mockito.*;

import static org.mockito.Mockito.*;

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
        when(mockRC.senseNearbyRobots(Muckraker.senseRadius)).thenReturn(new RobotInfo[] {});
        Muckraker.rc = mockRC;

        Assert.assertEquals(0, Muckraker.scanForTarget());
    }

    @Test
    public void scanForTargetEnemyTest() throws GameActionException {
        Muckraker.senseRadius = 30;
        when(mockRC.getTeam()).thenReturn(Team.A);
        when(mockRC.getTeam().opponent()).thenReturn(Team.B);
        when(mockRC.senseNearbyRobots(Muckraker.senseRadius)).thenReturn(new RobotInfo[] {new RobotInfo(100, Team.B,
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
        when(mockRC.senseNearbyRobots(Muckraker.senseRadius)).thenReturn(new RobotInfo[] {new RobotInfo(100,
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
    public void RunMuckrakerTest() throws GameActionException {
        //Set to Run no code
        Muckraker.senseRadius = 30;
        when(mockRC.senseNearbyRobots(Muckraker.senseRadius)).thenReturn(new RobotInfo[] {});

        //Set to Run no code
        Muckraker.actionRadius = 12;
        Muckraker.enemy = Team.B;
        when(mockRC.senseNearbyRobots(Muckraker.actionRadius, Muckraker.enemy)).thenReturn(new RobotInfo[] {});

        Muckraker.rc = mockRC;
        Muckraker.runMuckraker();
    }
}
