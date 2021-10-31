package robotbeta;

import battlecode.common.GameActionException;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.*;

public class EnlightenmentCenterTest {

    @Test
    public void checkIfExistMuckraker() throws GameActionException {
        EnlightenmentCenter ec = mock(EnlightenmentCenter.class);
        doNothing().when(ec).checkIfExistSlanderer();
        ec.checkIfExistSlanderer();
        verify(ec, times(1)).checkIfExistSlanderer();
    }
}