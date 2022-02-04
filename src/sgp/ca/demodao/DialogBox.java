/*
* @author Johann 
* @version v1.0
* Last modification date: 18-06-2021
*/

package sgp.ca.demodao;

import java.io.File;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sgp.ca.dataaccess.FtpClient;


public class DialogBox{
    
    private String fileSelectedPath = "";
    private String directorySelectedPath = "";
    private String fileNameSelected = "";
    private Stage stage;
    private final FtpClient FTPCLIENT = new FtpClient();

    public DialogBox(Stage stage){
        this.stage = stage;
    }
    
    public DialogBox(){
    }

    public String getFileSelectedPath(){
        this.openDialogFileSelector();
        return fileSelectedPath;
    }

    public String getFileNameSelected(){
        return fileNameSelected;
    }

    public void setFileNameSelected(String fileNameSelected) {
        this.fileNameSelected = fileNameSelected;
    }

    public void setFileSelectedPath(String fileSelectedPath){
        this.fileSelectedPath = fileSelectedPath;
    }
    
    public String openDialogFileSelector(){
        String pathForReturn = null;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecciona un archivo para guardar");
        File selectedFile = chooser.showOpenDialog(stage);
        if(selectedFile != null){
            this.fileSelectedPath = selectedFile.getAbsolutePath();
            this.fileNameSelected = selectedFile.getName();
            pathForReturn = this.fileSelectedPath;
        }
        return pathForReturn;
    }
    
    public boolean openDialogDirectorySelector(String urlFile){
        boolean correctDownload = false;
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Selecciona un deirectorio de destino");
        File selectedDirectory = chooser.showDialog(stage);
        if(selectedDirectory != null){
            this.directorySelectedPath = selectedDirectory.getAbsolutePath();
            correctDownload = FTPCLIENT.downloadFileFromFilesSystemByName(urlFile, directorySelectedPath);
        }
        return correctDownload;
    }
    
}