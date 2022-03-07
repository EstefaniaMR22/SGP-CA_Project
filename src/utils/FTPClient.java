/*
* @author Estefanía
* @version v1.0
* Last modification date: 18-06-2021
*/

package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FTPClient {
    
//    private org.apache.commons.net.ftp.FTPClient client = new org.apache.commons.net.ftp.FTPClient();

    private void stablishConnection(){
//        Scanner input = null;
//        try{
//            input = new Scanner(new File("fileAccess.txt"));
//            client.connect(input.nextLine());
//            client.login(input.nextLine(), input.nextLine());
//            client.changeWorkingDirectory(input.nextLine());
//            client.setFileType(org.apache.commons.net.ftp.FTPClient.BINARY_FILE_TYPE);
//            client.setFileTransferMode(org.apache.commons.net.ftp.FTPClient.BINARY_FILE_TYPE);
//            client.enterLocalPassiveMode();
//        }catch(IOException ioe){
//            Logger.getLogger(FTPClient.class.getName()).log(Level.SEVERE, null, ioe);
//        }catch(IllegalStateException ex){
//            Logger.getLogger(FTPClient.class.getName()).log(Level.SEVERE, null, ex);
//        }finally{
//            input.close();
//        }
    }
    
    public void closeConnection(){
//        try{
//            client.logout();
//            client.disconnect();
//        }catch(IOException ex){
//            Logger.getLogger(FTPClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    public String saveFileIntoFilesSystem(String fileSelectedPath, String destinationFileName){
        return "";
//        this.stablishConnection();
//        BufferedInputStream fileSelected = null;
//        try{
//            fileSelected = new BufferedInputStream(new FileInputStream(fileSelectedPath));
//            client.storeFile(destinationFileName, fileSelected);
//            fileSelected.close();
//        }catch(IOException ex){
//            Logger.getLogger(FTPClient.class.getName()).log(Level.SEVERE, null, ex);
//        }finally{
//            this.closeConnection();
//            return destinationFileName;
//        }
    }
    
    public boolean downloadFileFromFilesSystemByName(String fileName, String directorySelectedPath){
        return true;
//        this.stablishConnection();
//        boolean correctRetrieved = false;
//        try{
//            File localfile = new File(directorySelectedPath + ((char)92) + fileName);
//            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localfile));
//            client.retrieveFile(fileName, outputStream);
//            outputStream.close();
//            correctRetrieved = true;
//        }catch(IOException ex){
//            Logger.getLogger(FTPClient.class.getName()).log(Level.SEVERE, null, ex);
//        }finally{
//            this.closeConnection();
//            return correctRetrieved;
//        }
    }
    
    public boolean deleteFileFromFilesSystemByName(String fileName){
        return true;
//        boolean correctDelete = false;
//        this.stablishConnection();
//        try{
//            client.deleteFile(fileName);
//            correctDelete = true;
//        }catch(IOException ex){
//            Logger.getLogger(FTPClient.class.getName()).log(Level.SEVERE, null, ex);
//        }finally{
//            this.closeConnection();
//            return correctDelete;
//        }
    }
    
    public boolean checkExistFile(String fileToSearch){
        boolean existFile = false;
        return existFile;
//        try{
//            this.stablishConnection();
//            String fileSize = client.getSize(fileToSearch);
//            if(fileSize != null){
//                existFile = true;
//            }
//        }catch(IOException ex){
//            Logger.getLogger(FTPClient.class.getName()).log(Level.SEVERE, null, ex);
//        }finally{
//            this.closeConnection();
//            return existFile;
//        }
    }
}
    
