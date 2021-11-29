package robotbeta;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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