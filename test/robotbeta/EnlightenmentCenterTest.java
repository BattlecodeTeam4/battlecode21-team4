package robotbeta;

import battlecode.common.*;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class EnlightenmentCenterTest {
    @Mock
    RobotController mockRC = mock(RobotController.class);

    @Test
    public void sendLocationTest() throws GameActionException {
        when(mockRC.canSetFlag(12900)).thenReturn(true);
        MapLocation flag = new MapLocation(100, 100);
        Robot.rc = mockRC;
        Robot.sendLocation(flag);
    }

    @Test
    public void randomSpawnableRobotType() {
        RobotType rob = EnlightenmentCenter.randomSpawnableRobotType();
        Assert.assertNotNull(rob);
    }

    @Test
    public void bidByThreshold() throws GameActionException {
        when(mockRC.getInfluence()).thenReturn(10);
        when(mockRC.canBid(anyInt())).thenReturn(false);
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.bidByThreshold(100);
        verify(mockRC, times(1)).canBid(anyInt());
    }

    @Test
    public void spawnRandom() {
    }

    @Test
    public void runEnlightenmentCenter() {
    }
}