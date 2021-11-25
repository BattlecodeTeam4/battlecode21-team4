package robotdelta;

import battlecode.common.*;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PoliticianTest {

    @Mock
    RobotController mockRC = mock(RobotController.class);
    MapLocation flag = new MapLocation(100, 100);

    @Test
    public void resetIfTargetNullAndFlagNotZeroTest() throws GameActionException {
        Politician.rc = mockRC;
        Robot.target = null;
        when(mockRC.getID()).thenReturn(100);
        when(mockRC.canGetFlag(100)).thenReturn(true);
        when(mockRC.getFlag(100)).thenReturn(200);

        Assert.assertEquals(200, Politician.resetIfTargetNullAndFlagNotZero());
    }

    @Test
    public void resetIfTargetNullAndFlagNotZeroCantGetFlagTest() throws GameActionException {
        Politician.rc = mockRC;
        Robot.target = null;
        when(mockRC.getID()).thenReturn(100);
        when(mockRC.canGetFlag(100)).thenReturn(false);
        when(mockRC.getFlag(100)).thenReturn(200);

        Assert.assertEquals(0, Politician.resetIfTargetNullAndFlagNotZero());
    }

    @Test
    public void updateTargetTest() throws GameActionException {
        Politician.rc = mockRC;
        Robot.target = null;
        Robot.targetTeam = 0;
        Robot.homeID = 200;

        when(mockRC.getID()).thenReturn(100);
        when(mockRC.getFlag(100)).thenReturn(0);

        when(mockRC.canGetFlag(Robot.homeID)).thenReturn(true);
        when(mockRC.getFlag(Robot.homeID)).thenReturn(12900);

        when(mockRC.getLocation()).thenReturn(new MapLocation(100, 100));

        Assert.assertEquals(new MapLocation(100, 100), Politician.updateTarget());
    }

    @Test
    public void empowerEnemyTest() throws GameActionException {
        Politician.actionRadius = 9;
        Politician.enemy = Team.B;
        Politician.rc = mockRC;

        RobotInfo fakeEC = new RobotInfo(100, Team.B, RobotType.ENLIGHTENMENT_CENTER, 100,
                100, new MapLocation(100, 100));
        when(mockRC.senseNearbyRobots(Politician.actionRadius, Politician.enemy)).thenReturn(new RobotInfo[]
                {fakeEC});
        when(mockRC.senseNearbyRobots(Politician.actionRadius, Team.NEUTRAL)).thenReturn(new RobotInfo[]{});
        when(mockRC.canEmpower(Politician.actionRadius)).thenReturn(true);

        Politician.empowerEnemy();
    }

    @Test
    public void empowerEnemyNeutralTest() throws GameActionException {
        Politician.actionRadius = 9;
        Politician.enemy = Team.B;
        Politician.rc = mockRC;

        RobotInfo fakeEC = new RobotInfo(100, Team.NEUTRAL, RobotType.ENLIGHTENMENT_CENTER, 100,
                100, new MapLocation(100, 100));
        when(mockRC.senseNearbyRobots(Politician.actionRadius, Politician.enemy)).thenReturn(new RobotInfo[]{});
        when(mockRC.senseNearbyRobots(Politician.actionRadius, Team.NEUTRAL)).thenReturn(new RobotInfo[]{fakeEC});
        when(mockRC.canEmpower(Politician.actionRadius)).thenReturn(true);

        Politician.empowerEnemy();
    }

    @Test
    public void targetActionNullTest() throws GameActionException {
        Politician.target = null;
        Politician.rc = mockRC;

        Politician.actionRadius = 9;
        Politician.enemy = Team.B;

        RobotInfo fakeEC = new RobotInfo(100, Team.NEUTRAL, RobotType.ENLIGHTENMENT_CENTER, 100,
                100, new MapLocation(100, 100));
        when(mockRC.senseNearbyRobots(Politician.actionRadius, Politician.enemy)).thenReturn(new RobotInfo[]{});
        when(mockRC.senseNearbyRobots(Politician.actionRadius, Team.NEUTRAL)).thenReturn(new RobotInfo[]{fakeEC});
        when(mockRC.canEmpower(Politician.actionRadius)).thenReturn(true);

        Politician.targetActions();
    }

    @Test
    public void targetActionTargetNotTeamTest() throws GameActionException {
        Politician.target = new MapLocation(100, 100);
        Politician.actionRadius = 9;
        Politician.rc = mockRC;
        Politician.targetTeam = 1;

        when(mockRC.canSenseLocation(Politician.target)).thenReturn(true);
        when(mockRC.senseRobotAtLocation(Politician.target)).thenReturn(new RobotInfo(100, Team.NEUTRAL,
                RobotType.ENLIGHTENMENT_CENTER, 100, 100, new MapLocation(102, 102)));
        when(mockRC.getTeam()).thenReturn(Team.A);

        Politician.enemy = Team.B;

        RobotInfo fakeEC = new RobotInfo(100, Team.NEUTRAL, RobotType.ENLIGHTENMENT_CENTER, 100,
                100, new MapLocation(100, 100));
        when(mockRC.senseNearbyRobots(Politician.actionRadius, Politician.enemy)).thenReturn(new RobotInfo[]{});
        when(mockRC.senseNearbyRobots(Politician.actionRadius, Team.NEUTRAL)).thenReturn(new RobotInfo[]{fakeEC});
        when(mockRC.canEmpower(Politician.actionRadius)).thenReturn(true);

        Politician.targetActions();
    }

    @Test
    public void targetActionTargetTeamTest() throws GameActionException {
        Politician.target = new MapLocation(100, 100);
        Politician.actionRadius = 9;
        Politician.targetTeam = 1;

        Politician.rc = mockRC;

        when(mockRC.canSenseLocation(Politician.target)).thenReturn(true);
        when(mockRC.senseRobotAtLocation(Politician.target)).thenReturn(new RobotInfo(100, Team.A,
                RobotType.ENLIGHTENMENT_CENTER, 100, 100, new MapLocation(102, 102)));
        when(mockRC.getTeam()).thenReturn(Team.A);

        Politician.targetActions();
    }

    @Test
    public void defendHomeNullTest() throws GameActionException {
        Politician.homeLoc = null;
        Politician.rc = mockRC;
        Politician.defendHome();
    }

    @Test
    public void defendHomeNearHomeTest() throws GameActionException {
        Politician.actionRadius = 9;
        Politician.enemy = Team.B;
        Politician.homeLoc = new MapLocation(100, 100);
        when(mockRC.getTeam()).thenReturn(Team.A);
        when(mockRC.canSenseLocation(Politician.homeLoc)).thenReturn(true);
        when(mockRC.senseNearbyRobots(Politician.actionRadius, Politician.enemy)).thenReturn(new RobotInfo[]{
                new RobotInfo(100, Team.NEUTRAL, RobotType.ENLIGHTENMENT_CENTER, 100, 100,
                        new MapLocation(102, 102))});
        Politician.rc = mockRC;
        Politician.defendHome();
    }

    @Test
    public void runPoliticianTest() throws GameActionException {
        Politician.rc = mockRC;
        Politician.homeLoc = null;
        Politician.target = null;
        when(mockRC.canGetFlag(100)).thenReturn(false);

        Politician.actionRadius = 9;
        Politician.enemy = Team.B;
        when(mockRC.senseNearbyRobots(Politician.actionRadius, Politician.enemy)).thenReturn(new RobotInfo[]{});
        when(mockRC.senseNearbyRobots(Politician.actionRadius, Team.NEUTRAL)).thenReturn(new RobotInfo[]{});

        Politician.runPolitician();
    }
}