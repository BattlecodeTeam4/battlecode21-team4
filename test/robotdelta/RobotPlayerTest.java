package robotdelta;

import battlecode.common.*;
import org.junit.*;
import org.mockito.*;

import java.util.HashSet;
import java.util.LinkedList;

import static org.mockito.Mockito.*;

public class RobotPlayerTest {

    @Mock
    RobotController mockRC = mock(RobotController.class);
    MapLocation target = new MapLocation(100, 100);

    @Test
    public void runTestEC() throws GameActionException {
        Robot.robot = true;
        Robot.robotTest = true;
        when(mockRC.getType()).thenReturn(RobotType.ENLIGHTENMENT_CENTER);
        EnlightenmentCenter.turnCount = 100;
        EnlightenmentCenter.currRound = 100;
        EnlightenmentCenter.senseRadius = 40;
        EnlightenmentCenter.polIDList = new HashSet<>();
        EnlightenmentCenter.slaIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.targetList = new LinkedList<>();
        Muckraker.enemy = Team.B;
        when(mockRC.senseNearbyRobots(EnlightenmentCenter.senseRadius, Team.B)).thenReturn(new RobotInfo[] {});
        RobotPlayer.run(mockRC);
    }

    @Test
    public void runTestPol() throws GameActionException {
        Robot.robot = true;
        Robot.robotTest = true;
        when(mockRC.getType()).thenReturn(RobotType.POLITICIAN);
        when(mockRC.getLocation()).thenReturn(target);

        Robot.target = null;
        when(mockRC.canGetFlag(100)).thenReturn(false);

        Politician.actionRadius = 9;
        Politician.enemy = Team.B;
        when(mockRC.senseNearbyRobots(Politician.actionRadius, Politician.enemy)).thenReturn(new RobotInfo[] {});
        when(mockRC.senseNearbyRobots(Politician.actionRadius, Team.NEUTRAL)).thenReturn(new RobotInfo[] {});
        RobotPlayer.run(mockRC);
    }

    @Test
    public void runTestSla() throws GameActionException {
        Robot.robot = true;
        Robot.robotTest = true;
        when(mockRC.getType()).thenReturn(RobotType.SLANDERER);
        when(mockRC.getLocation()).thenReturn(target);

        Slanderer.slaThreshold = 2;
        Slanderer.turnCount = 3;
        when(mockRC.canSetFlag(3)).thenReturn(true);
        Slanderer.senseRadius = 12;
        Slanderer.enemy = Team.B;

        RobotInfo fakeSla = new RobotInfo(100, Team.B, RobotType.MUCKRAKER, 100,
                100, new MapLocation(75, 75));
        when(mockRC.senseNearbyRobots(Slanderer.senseRadius, Slanderer.enemy)).thenReturn(new RobotInfo[]
                {fakeSla});
        RobotPlayer.run(mockRC);
    }
    @Test
    public void runTestMuc() throws GameActionException {
        Robot.robot = true;
        Robot.robotTest = true;
        when(mockRC.getType()).thenReturn(RobotType.MUCKRAKER);
        when(mockRC.getLocation()).thenReturn(target);

        Muckraker.senseRadius = 30;
        when(mockRC.senseNearbyRobots(Muckraker.senseRadius)).thenReturn(new RobotInfo[] {});

        Muckraker.actionRadius = 12;
        Muckraker.enemy = Team.B;
        when(mockRC.senseNearbyRobots(Muckraker.actionRadius, Muckraker.enemy)).thenReturn(new RobotInfo[] {});
        RobotPlayer.run(mockRC);
    }

    @Test
    public void runTestNone() throws GameActionException {
        Robot.robot = true;
        Robot.robotTest = true;
        when(mockRC.getType()).thenReturn(null);
        RobotPlayer.run(null);
    }
}