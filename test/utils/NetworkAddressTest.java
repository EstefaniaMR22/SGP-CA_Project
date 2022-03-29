package utils;

import assets.utils.NetworkAddress;
import org.junit.Assert;
import org.junit.Test;

import java.net.SocketException;

public class NetworkAddressTest {
    @Test
    public void getLocalAddressTest() throws SocketException {
        String unexpectedAddress = "38:56:a4:g5:46:5g";
        String actualAddress = NetworkAddress.getLocalAdress();
        Assert.assertNotEquals(unexpectedAddress, actualAddress);
    }
}
