package robotbeta;

import battlecode.common.*;
import org.junit.Test;
import org.mockito.*;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import battlecode.common.GameActionException;

public class PoliticianTest {

    @Mock
    RobotController mockRC = mock(RobotController.class);
    MapLocation flag = new MapLocation(100, 100);
    Politician poli = mock(Politician.class);

    @Test
    public void runPoliticianTest() throws GameActionException {
        Politician.rc = mockRC;
        when(mockRC.canSetFlag(13068)).thenReturn(false);
        when(mockRC.getType()).thenReturn(RobotType.POLITICIAN);
        when(mockRC.canEmpower(9)).thenReturn(false);
        //when(mockRC.getType().actionRadiusSquared).thenReturn(9);
        Politician.runPolitician();
    }
}