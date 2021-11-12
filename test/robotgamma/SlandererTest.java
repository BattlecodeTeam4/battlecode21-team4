package robotgamma;

import battlecode.common.*;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class SlandererTest {
    @Mock
    RobotController mockRC = mock(RobotController.class);

    MapLocation target = new MapLocation(100, 100);

    @Test
    public void convertFlagGreaterTest() throws GameActionException {
        Slanderer.slaThreshold = 2;
        Slanderer.turnCount = 3;
        when(mockRC.canSetFlag(3)).thenReturn(true);
        Slanderer.rc = mockRC;
        Assert.assertEquals(3, Slanderer.convertFlag());
    }

    @Test
    public void convertFlagEqualsTest() throws GameActionException {
        Slanderer.slaThreshold = 2;
        Slanderer.turnCount = 2;
        when(mockRC.canSetFlag(2)).thenReturn(true);
        Slanderer.rc = mockRC;
        Assert.assertEquals(2, Slanderer.convertFlag());
    }

    @Test
    public void convertFlagLessTest() throws GameActionException {
        Slanderer.slaThreshold = 5;
        Slanderer.turnCount = 3;
        when(mockRC.canSetFlag(3)).thenReturn(true);
        Slanderer.rc = mockRC;
        Assert.assertEquals(3, Slanderer.convertFlag());
    }

    @Test
    public void moveAwayNonMuckTest() throws GameActionException {
        Slanderer.senseRadius = 12;
        Slanderer.enemy = Team.B;

        RobotInfo fakeSla = new RobotInfo(100, Team.B, RobotType.POLITICIAN, 100,
                100, target);
        when(mockRC.senseNearbyRobots(Slanderer.senseRadius, Slanderer.enemy)).thenReturn(new RobotInfo[]
                {fakeSla});
        Slanderer.rc = mockRC;
        Direction direction = Slanderer.moveAway();
    }

    @Test
    public void moveAwayMuckTest() throws GameActionException {
        Slanderer.senseRadius = 12;
        Slanderer.enemy = Team.B;

        RobotInfo fakeSla = new RobotInfo(100, Team.B, RobotType.MUCKRAKER, 100,
                100, target);
        when(mockRC.senseNearbyRobots(Slanderer.senseRadius, Slanderer.enemy)).thenReturn(new RobotInfo[]
                {fakeSla});
        when(mockRC.getLocation()).thenReturn(new MapLocation(200, 200));
        Slanderer.rc = mockRC;
        Direction direction = Slanderer.moveAway();
        verify(mockRC, times(4)).getLocation();
    }

    @Test
    public void moveAwayMuckLessTest() throws GameActionException {
        Slanderer.senseRadius = 12;
        Slanderer.enemy = Team.B;

        RobotInfo fakeSla = new RobotInfo(100, Team.B, RobotType.MUCKRAKER, 100,
                100, target);
        when(mockRC.senseNearbyRobots(Slanderer.senseRadius, Slanderer.enemy)).thenReturn(new RobotInfo[]
                {fakeSla});
        when(mockRC.getLocation()).thenReturn(new MapLocation(50, 50));
        Slanderer.rc = mockRC;
        Direction direction = Slanderer.moveAway();
        verify(mockRC, times(4)).getLocation();
    }

    @Test
    public void runSlandererTest() throws GameActionException {
        Slanderer.slaThreshold = 2;
        Slanderer.turnCount = 3;
        when(mockRC.canSetFlag(3)).thenReturn(true);
        Slanderer.senseRadius = 12;
        Slanderer.enemy = Team.B;

        RobotInfo fakeSla = new RobotInfo(100, Team.B, RobotType.MUCKRAKER, 100,
                100, target);
        when(mockRC.senseNearbyRobots(Slanderer.senseRadius, Slanderer.enemy)).thenReturn(new RobotInfo[]
                {fakeSla});
        when(mockRC.getLocation()).thenReturn(new MapLocation(50, 50));
        Slanderer.rc = mockRC;
        Slanderer.runSlanderer();
        verify(mockRC, times(4)).getLocation();
        Assert.assertEquals(3, Slanderer.convertFlag());
    }
}
