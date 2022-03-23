package utils;

import org.junit.Assert;
import org.junit.Test;

public class CryptographyTest {
    @Test
    public void cryptSHA2Test() {
        String text = "hola";
        String expected = "b221d9dbb083a7f33428d7c2a3c3198ae925614d70210e28716ccaa7cd4ddb79";
        String actual = Cryptography.cryptSHA2(text);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void cryptMD5() {
        String text = "hola";
        String expected = "4d186321c1a7f0f354b297e8914ab240";
        String actual = Cryptography.cryptMD5(text);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void generateRandomPassword() {
        String actual = Cryptography.generateRandomPassword();
        Assert.assertNotNull(actual);
    }



}
