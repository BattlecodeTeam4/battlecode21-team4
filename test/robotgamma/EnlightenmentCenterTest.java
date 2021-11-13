package robotgamma;

import battlecode.common.*;
import org.junit.*;
import org.mockito.*;

import java.util.HashSet;

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
    public void checkIfExistMuckraker() throws GameActionException {
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.mucIDList = new HashSet<>();
        EnlightenmentCenter.mucIDList.add(200);
        when(mockRC.canGetFlag(200)).thenReturn(true);
        Assert.assertEquals(1,EnlightenmentCenter.checkIfExistMuckraker());
    }

    @Test
    public void checkIfExistPolitician() throws GameActionException {
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.polIDList= new HashSet<>();
        EnlightenmentCenter.polIDList.add(200);
        EnlightenmentCenter.polIDList.add(300);
        when(mockRC.canGetFlag(200)).thenReturn(true);
        when(mockRC.canGetFlag(300)).thenReturn(true);
        Assert.assertEquals(2 ,EnlightenmentCenter.checkIfExistPolitician());
    }

    @Test
    public void checkIfExistSlandererNot() throws GameActionException {
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.slaIDList= new HashSet<>();
        EnlightenmentCenter.slaIDList.add(200);
        when(mockRC.canGetFlag(200)).thenReturn(false);
        Assert.assertEquals(0,EnlightenmentCenter.checkIfExistSlanderer());
    }

    @Test
    public void checkIfExistSlandererYes() throws GameActionException {
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.slaIDList= new HashSet<>();
        EnlightenmentCenter.slaIDList.add(100000);
        when(mockRC.canGetFlag(100000)).thenReturn(true);
        Assert.assertEquals(1,EnlightenmentCenter.checkIfExistSlanderer());
    }

    @Test
    public void bidByThreshold() throws GameActionException {
        when(mockRC.getInfluence()).thenReturn(10);
        when(mockRC.canBid(anyInt())).thenReturn(false);
        EnlightenmentCenter.rc = mockRC;
        EnlightenmentCenter.bidByThreshold();
        verify(mockRC, times(1)).canBid(anyInt());
    }

    @Test
    public void spawnRandom() {
    }

    @Test
    public void runEnlightenmentCenter() {
    }
}