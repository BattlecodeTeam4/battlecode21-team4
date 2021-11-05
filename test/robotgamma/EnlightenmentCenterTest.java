package robotgamma;

import battlecode.common.*;
import org.junit.*;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class EnlightenmentCenterTest {
    @Mock
    RobotController mockRC = mock(RobotController.class);

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