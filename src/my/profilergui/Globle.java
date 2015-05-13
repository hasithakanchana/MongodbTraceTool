/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.profilergui;

import com.mongodb.MongoClient;
import java.util.List;

/**
 *
 * @author UKANCDE
 */
public class Globle {
    
    private String IP= "";
    private int Port =0;
    private String username ="";
    private String pwd="";
    private List <String> dblist = null;
    
    private MongoClient MClient = null;
    
   public void setIPAddress ( String ip)
   {
       this.IP = ip;
   }
   
   public void setPortNo ( int port)
   {
       this.Port = port;
       
   }
   
   public void setUserName(String user)
   {
       this.username = user;
   }
   
   public void setPassword(String pwd)
   {
       this.pwd = pwd;
   }
   
   public  void setdbList(List x)
   {
       this.dblist = x;
   }
   
   public List <String> getdbList()
   {
       return this.dblist;
   }
   
   
   
   
   
   
    
}
