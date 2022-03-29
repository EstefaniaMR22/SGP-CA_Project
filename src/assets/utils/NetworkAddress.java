package assets.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkAddress {
    /***
     * This method return the actual MacAddress
     * <p>
     * This method it is used to recognize a PC.
     * </p>
     * @return String the host mac address.
     */
    public static String getLocalAdress() throws SocketException {
        String firstInterface = null;
        Map<String, String> addressByNetwork = new HashMap<>();
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        while(networkInterfaces.hasMoreElements()){
            NetworkInterface network = networkInterfaces.nextElement();

            byte[] bmac = network.getHardwareAddress();
            if(bmac != null){
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < bmac.length; i++){
                    sb.append(String.format("%02X%s", bmac[i], (i < bmac.length - 1) ? ":" : ""));
                }

                if(sb.toString().isEmpty() == false){
                    addressByNetwork.put(network.getName(), sb.toString());
                }

                if(sb.toString().isEmpty()==false && firstInterface == null){
                    firstInterface = network.getName();
                }
            }
        }

        if(firstInterface != null){
            return addressByNetwork.get(firstInterface);
        }

        return null;
    }
}
