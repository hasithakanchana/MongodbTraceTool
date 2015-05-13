/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.profilergui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author UKANCDE
 */
public class LogWriter {
    
    public LogWriter()
    {
        
        
    }
    
    public void LogWriteToTextFile(String x) 
    {
                 String dt = "";
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
                Date date = new Date();
                dt = dateFormat.format(date);
			File file = new File("C:\\MongodbProfiler.log");
 
			// if file doesnt exists, then create it
                        try{

                        
                        FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
                        bw.newLine();
                        
                        bw.write(dt);
                        
                        
			bw.write(" : "+x);
                        bw.newLine();
			bw.close();
                        }
                        catch (IOException e)
                        {
                            
                        }
        
    }
    
}
