/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.profilergui;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandFailureException;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.Configuration;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jxl.*;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import javax.swing.JOptionPane;




import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;




/**
 *
 * @author UKANCDE
 */
public class Profiler {
    
//    public void Profiler()
//    {
//        
//    }
    
    private MongoClient OpenMongoConn = null ;
    LogWriter log = new LogWriter();
    
    
    
    public MongoClient OpenMongoDBConnection(String serverIP, int serverPort )
    {
        try {
     
       MongoClient mongo = new MongoClient(serverIP, serverPort);
       
      
       
       return mongo;
              
    } catch (Exception e)
    {
            log.LogWriteToTextFile(e.toString());
            JOptionPane.showMessageDialog(null, "Please check Server Name and Port No and re-try", "Database Connection Error",JOptionPane.ERROR_MESSAGE);
    
    }
       return null; 
    }
    
    
    
    
        public MongoClient OpenMongoDBConnection(String serverIP, int serverPort , String userName , String password, String database ) 
    {
        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
        
       // MongoCredential credential = MongoCredential.createMongoCRCredential(userName, database, password.toCharArray());
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        credentials.add(MongoCredential.createMongoCRCredential(userName,database,password.toCharArray()
    )
);
        try {
        seeds.add( new ServerAddress(serverIP,serverPort) );
       MongoClient mongo = new MongoClient(seeds,credentials);

       return mongo;
             
     }catch(Exception e){
            log.LogWriteToTextFile(e.toString());
            JOptionPane.showMessageDialog(null, "Please check Server Details and re-try", "Database Connection Error",JOptionPane.ERROR_MESSAGE);
     
    }
       return null; 
    }
    
    public  void PauseProfiler(MongoClient mongo, String dbname)
    {
        try{
        DB db = mongo.getDB(dbname);
        String disPrfQry = "{'profile':0}";
        DBObject disProf =  (DBObject) JSON.parse(disPrfQry);
        db.command(disProf);
        }
        catch(Exception e)
        {
           log.LogWriteToTextFile(e.toString());
           JOptionPane.showMessageDialog(null, "Unable to stop Profiler. Please check manually", "Error",JOptionPane.ERROR_MESSAGE); 
        }
       

    }
    public  void StopProfiler(MongoClient mongo, String dbname, String name)
    {
        try
        {
        DB db = mongo.getDB(dbname);
        String disPrfQry = "{'profile':0}";
        DBObject disProf =  (DBObject) JSON.parse(disPrfQry);
        db.command(disProf);
        String qry = name;
        DBCollection table = db.getCollection("system.profile");
        table.rename(qry);
        
        
        
        String from =dbname+"."+name;
        String to ="TraceDB."+name;
        DB db1 = mongo.getDB("admin");
        DBObject cmd = new BasicDBObject();
        cmd.put("renameCollection", from);
        cmd.put("to", to);
        cmd.put("dropTarget", "true");
        CommandResult result = db1.command(cmd);
        String err = result.getErrorMessage();

        }
        catch(Exception e)
        {
            log.LogWriteToTextFile(e.toString());
            JOptionPane.showMessageDialog(null, "Unable to stop Profiler. Please check manually", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        

    }
    
    public  void StartProfiler( MongoClient mongo, String dbname, String profileLevel)
    {
        try 
        {
        DB db = mongo.getDB(dbname);
        String qry = "{'profile':"+profileLevel+"}";
        DBObject prof =  (DBObject) JSON.parse(qry);
        db.command(prof);
        }
        catch( Exception e)
        {
            log.LogWriteToTextFile(e.toString());
            JOptionPane.showMessageDialog(null, "Unable to start Profiler. Please check manually", "Error",JOptionPane.ERROR_MESSAGE);
   
        }
        
    }
    
        public  void StartProfiler( MongoClient mongo, String dbname, String profileLevel , String Slowness)
    {
        DB db = mongo.getDB(dbname);
        String qry = "{'profile':"+profileLevel+",'slowms':"+Slowness+"}";
        DBObject prof =  (DBObject) JSON.parse(qry);
        db.command(prof);
        
    }
    
    public  void DBAccessTest(MongoClient mongo)
    {
        DB db = mongo.getDB("test");
        DBCollection table = db.getCollection("sample");
        BasicDBObject searchQuery = new BasicDBObject();
	searchQuery.put("_id", 1);
        DBCursor cursor = table.find(searchQuery);
        	while (cursor.hasNext()) {
		System.out.println(cursor.next());
	}
    }
    public void DropCollection(MongoClient mongo,String dbName,String collName)
    {
     
        DB db = mongo.getDB(dbName);
        DBCollection table = db.getCollection(collName);
        table.drop();
        
    }
    
    public void InsertData(MongoClient mongo,String dbName,String collName, String values)
    {
        DB db = mongo.getDB(dbName);
        DBCollection table = db.getCollection(collName);
        DBObject dbObject = (DBObject) JSON.parse(values);
        table.insert(dbObject);
    }
    
    public void CloseConnection(MongoClient mongo)
    {
        mongo.close();

    }
    
    public List <String> GetDatabaseList (MongoClient mongo) throws ConnectException
    {
        try {
        List dbList = mongo.getDatabaseNames();
        return dbList;
        }catch (CommandFailureException e)
        {
            log.LogWriteToTextFile(e.toString());
            JOptionPane.showMessageDialog(null, "Authentication Failed.Please check Server Details and re-try", "Database Connection Error",JOptionPane.ERROR_MESSAGE);
        }

        catch (Exception e)
        {
            log.LogWriteToTextFile(e.toString());
            JOptionPane.showMessageDialog(null, "Authentication Failed.Please check Server Details and re-try", "Database Connection Error",JOptionPane.ERROR_MESSAGE);
        }
        
        return null;
        
    }
    
    public int isProfilerRunning(MongoClient mongo,String dbName)
    {
        
        int x = -1;
        try
        {
        DB db = mongo.getDB(dbName);
        DBObject cmd = new BasicDBObject();
        
        String qry = "db.getProfilingLevel()";
       // DBObject prof =  (DBObject) JSON.parse(qry);
        //db.command(prof);
        x = (int) Double.parseDouble(db.eval(qry).toString());
        }
        catch(Exception e)
        {
            log.LogWriteToTextFile(e.toString());
            JOptionPane.showMessageDialog(null, "Unable to check Profiler status. Check manually", "Error",JOptionPane.ERROR_MESSAGE);
 
        }
        
        
        return x;
        
        
    }
    public String getDateTime()
    {
        String dt = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();
        dt = dateFormat.format(date);
        
        return dt;
    }
    
    public DBCursor GetCollection(MongoClient mongo,String dbName, String collectionName)
    {
        List colllist = null;
         DB db = mongo.getDB(dbName);
      DBCollection coll = db.getCollection(collectionName);
      DBCursor cursor = coll.find();
      
      
        
        return cursor;
    }
    
     public void readCollection(MongoClient mongo,String dbName, String collectionName)
      
    {
        try
        {
      int idx = 0;
      int f=0;
      JSONObject  xml = null;
      
      
      
      
      DB db = mongo.getDB("TraceDB");
      DBCollection coll = db.getCollection(collectionName);
      DBCursor cursor = coll.find();
      BasicDBObject dbo = (BasicDBObject)  coll.findOne();
      Set<String> keys = dbo.keySet();
      String[] skeys = new String[keys.size()];
      Iterator it = keys.iterator();
      it.next(); // skip the id
//      while (it.hasNext()) {
//        skeys[idx++] = it.next().toString();
//      }
      String[] item = new String[cursor.size()];
      
         //   JTable jtbl = null;
            
          //  jtbl = InitiateTable();
        
        List<ProfileData> pdata = new ArrayList<ProfileData>();  
        

     //  ProfileData pdata = new ProfileData(); 

      while (cursor.hasNext()){
          try {
              //  item[f++] = cursor.next().toString();
                xml = new JSONObject(cursor.next().toString());
               pdata.add(getOneProfileItem(xml));
                
          } catch (JSONException ex) {
              //Logger.getLogger(Profiler.class.getName()).log(Level.SEVERE, null, ex);
              log.LogWriteToTextFile(ex.toString());
          }
          //System.out.print(XML.toString(xml));
          //x = x + XML.toString(xml);
      }
          ProfileDataTableModel model = new ProfileDataTableModel(pdata);
          
          JTable table = new JTable(model) {
                    @Override
                    public Dimension getPreferredScrollableViewportSize() {
                        return new Dimension(900, 400);
                    }
                };
          table.setAutoCreateRowSorter(true);
          TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
          table.setRowSorter(sorter);
          
          //fillData(table, new File("D:\\result.xls"),"");
               
              //  JOptionPane.showMessageDialog(null, new JScrollPane(table));
          JFrame frame = new JFrame();
         
          
          frame.setTitle("Table view of profiler ");
          frame.setLayout(new BorderLayout());
          frame.add(new JScrollPane(table));
          frame.pack();
          frame.setLocationRelativeTo(null);
          frame.setVisible(true);
        }catch (NullPointerException e)
        {
            log.LogWriteToTextFile(e.toString());
            JOptionPane.showMessageDialog(null, "No Records available", "Information",JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e)
        {
             log.LogWriteToTextFile(e.toString());      
            JOptionPane.showMessageDialog(null, "Error occured while reading profile data", "Error",JOptionPane.ERROR_MESSAGE);
        }
          

    }
     
     public ProfileData getOneProfileItem(JSONObject x)
     {
         ProfileData item1 = new ProfileData();
         String i1="None",i2="None",i3="None",i4="None",i5="None",i6="None",i7="None",i8="None",i9="None",i10="None",i11 ="None",i12="None",i13="None",i14="None",i15="None";
         
         
        try {
            
             if (x.isNull("ts")== false)
             {
               i1 = x.get("ts").toString();  
             }
             
             if(x.isNull("op")== false)
             {
               i2 = x.get("op").toString();  
             }
             
             if(x.isNull("ns")== false)
             {
               i3 = x.get("ns").toString();  
             }
             
             if(x.isNull("query") == false)
             {
               i4 = x.get("query").toString(); 
             }
             
             
             if (x.isNull("nscanned") == false)
             {
                i5 = x.get("nscanned").toString();
             }
             
             if (x.isNull("lockStats")== false)
             {
                i6 = x.get("lockStats").toString();
             }
             
             if (x.isNull("millis") == false)
             {
                i7 = x.get("millis").toString(); 
             }
             
             if (x.isNull("client") == false)
             {
                i8 = x.get("client").toString(); 
             }
             
             if(x.isNull("user") == false)
             {
                i9 = x.get("user").toString(); 
             }
             if(x.isNull("nmoved")== false)
             {
                 i10 = x.get("nmoved").toString();
             }
             if (x.isNull("responseLength") == false)
             {
                 i11 = x.get("responseLength").toString();
             }
             if (x.isNull("keyUpdates")== false)
             {
                 i12 = x.get("keyUpdates").toString();
             }
             if (x.isNull("updateobj")== false)
             {
                 i13 = x.get("updateobj").toString();
             }
             if (x.isNull("nupdated")== false)
             {
                 i14 = x.get("nupdated").toString();
             }
             if (x.isNull("nreturned")== false)
             {
                 i15 = x.get("nreturned").toString();
             }
     
 
        } catch (JSONException ex) {
            
            log.LogWriteToTextFile(ex.toString());
        }

         item1.setTS(i1);
         item1.setOP(i2);
         item1.setNS(i3);
         item1.setQuery(i4);
         item1.setNScanned(i5);
         item1.setLockStats(i6);
         item1.setMillis(i7);
         item1.setClient(i8);
         item1.setUser(i9);
         item1.setNMoved(i10);
         item1.setResponseLength(i11);
         item1.setKeyUpdates(i12);
         item1.setUpdateObj(i13);
         item1.setNupdated(i14);
         item1.setNReturned(i15);
         
         
         
         return item1;
     }
     
        // Create function jTable with required columns
//     public JTable InitiateTable( )
//     {
//        
//         String[] columnList = {"ts",
//                                "op",
//                                "ns",
//                                "query",
//                                "nscanned",
//                                "lockStats",
//                                "millis",
//                                "client",
//                                "user"
//                                };
//          JTable jtb = new JTable(null,columnList);
//          
//
//         
//         return jtb;
//         
//         
//     }
//     public void  AddItemsIntoJTable(Document doc){
//         
//         NodeList[] nl = new NodeList[9];
//      nl[0] =  doc.getElementsByTagName("ts");
//      nl[1] =  doc.getElementsByTagName("op");
//      nl[2] =  doc.getElementsByTagName("ns");
//      nl[3] =  doc.getElementsByTagName("query");
//      nl[4] =  doc.getElementsByTagName("nscanned");
//      nl[5] =  doc.getElementsByTagName("lockStats");
//      nl[6] =  doc.getElementsByTagName("millis");
//      nl[7] =  doc.getElementsByTagName("client");
//      nl[8] =  doc.getElementsByTagName("user");
//      
//      String[] data = new String[9];
//      
//      for (int i = 0 ; i < 9 ; i++)
//      {
//          data[i] = nl[i].item(i).getFirstChild().getNodeValue();
//          
//      }
//      
//         
//     }
     
      
      // Create function with add row to that jTable ( parameter)
     
//     public void ShowTablePanel()
//     {
//    Vector rowData = new Vector();
//    for (int i = 0; i < 5; i++) {
//      Vector colData = new Vector(Arrays.asList("qq"));
//      rowData.add(colData);
//    }
//    
//    String[] columnNames = {"a"};
//    
//    Vector columnNamesV = new Vector(Arrays.asList(columnNames));
//
//    JTable table = new JTable(rowData, columnNamesV);
//    JFrame f = new JFrame();
//    f.setSize(300, 300);
//    f.add(new JScrollPane(table));
//    f.setVisible(true);
//     }
     
     
      public  MongoClient MongoClient(MongoClient m)
      {
          this.OpenMongoConn = m;
          
          return this.OpenMongoConn;
      }
      
      public Set <String> GetProfilersList (MongoClient m , String dbName)
      {
          Set <String>  list = null;
          
          
          try
          {
          
          DB db = m.getDB("dbName");
          list  = db.getCollectionNames();
          }
          catch(Exception e)
          {
              log.LogWriteToTextFile(e.toString());
            JOptionPane.showMessageDialog(null, "Unable to get Profiler List. Check manually", "Error",JOptionPane.ERROR_MESSAGE);
 
              
          }
          
    
          return list;
      }
      
      
            public ArrayList  GetProfilersListR (MongoClient m , String dbName)
      {
          Set <String>  list = null;
          ArrayList  listR = new ArrayList();
          

          try
          {
          
          DB db = m.getDB("TraceDB");
          list  = db.getCollectionNames();
          
          List l = new ArrayList(list);
         // list.clear();
        String coll = "";
        String pattern ="^"+dbName+"_(.*)";
        
        for(int i = 0 ; i <l.size() ; i++)
        {
            coll = l.get(i).toString();
            
            if (coll.matches(pattern))
            {
            
                listR.add(coll);
            }
        }
          }
          catch(Exception e)
          {
              log.LogWriteToTextFile(e.toString());
            JOptionPane.showMessageDialog(null, "Unable to get Profiler List. Check manually", "Error",JOptionPane.ERROR_MESSAGE);
 
              
          }
          
           
          
          
          return listR;
      }
      
      
      boolean fillData(JTable table, File file ) {

        try {

            WritableWorkbook workbook1 = Workbook.createWorkbook(file);
            WritableSheet sheet1 = workbook1.createSheet("Sheet1", 0);
            TableModel model = table.getModel();

            for (int i = 0; i < model.getColumnCount(); i++) {
                Label column = new Label(i, 0, model.getColumnName(i));
                sheet1.addCell(column);
            }
            int j = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                for (j = 0; j < model.getColumnCount(); j++) {
                    Label row = new Label(j, i + 1,
                            model.getValueAt(i, j).toString());
                    sheet1.addCell(row);
                }
            }
            workbook1.write();
            
            workbook1.close();
            return true;
        } catch (Exception ex) {
          //  ex.printStackTrace();
            log.LogWriteToTextFile(ex.toString());
            JOptionPane.showMessageDialog(null, "Unable to Export to Excel. Check manually", "Error",JOptionPane.ERROR_MESSAGE);
                        

        }
        return false;
    }
      
      void ExportProfileToExcel(MongoClient mongo, String dbName, String collectionName)
      {
                JSONObject  xml = null;
      
      
      
      
      DB db = mongo.getDB("TraceDB");
      DBCollection coll = db.getCollection(collectionName);
      DBCursor cursor = coll.find();
          String[] item = new String[cursor.size()];
      
           // JTable jtbl = null;
            
          //  jtbl = InitiateTable();
        
        List<ProfileData> pdata = new ArrayList<ProfileData>();    
     //  ProfileData pdata = new ProfileData();     
      while (cursor.hasNext()){
          try {
              //  item[f++] = cursor.next().toString();
                xml = new JSONObject(cursor.next().toString());
               pdata.add(getOneProfileItem(xml));
                
          } catch (JSONException ex) {
              //Logger.getLogger(Profiler.class.getName()).log(Level.SEVERE, null, ex);
          }
          //System.out.print(XML.toString(xml));
          //x = x + XML.toString(xml);
      }
          ProfileDataTableModel model = new ProfileDataTableModel(pdata);
          
          JTable table = new JTable(model) {
//                    @Override
//                    public Dimension getPreferredScrollableViewportSize() {
//                        return new Dimension(900, 400);
//                    }
                };
          table.setAutoCreateRowSorter(true);
          TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
          table.setRowSorter(sorter);

         
          
         String filePath = CreateFile(collectionName,".xls");
          File file = new File(filePath);
          
          if(fillData(table,file))
                JOptionPane.showMessageDialog(null, "Data saved at " +filePath, "Message",JOptionPane.INFORMATION_MESSAGE);
          
      //   }



          
      }
      
      void ExportProifleToJSON(MongoClient mongo, String dbName, String collectionName) 
      {
          
          JSONObject  xml = null;
          String str ="";
          DB db = mongo.getDB("TraceDB");
      DBCollection coll = db.getCollection(collectionName);
      DBCursor cursor = coll.find();
          
            
      
  
     //  ProfileData pdata = new ProfileData();     
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

      while (cursor.hasNext()){
          try {
              //  item[f++] = cursor.next().toString();
               xml = new JSONObject(cursor.next().toString());

               String jsonOutput = gson.toJson(xml);
               str = str + jsonOutput;
              
              
                
          } catch (JSONException ex) {
              //Logger.getLogger(Profiler.class.getName()).log(Level.SEVERE, null, ex);
          }
          //System.out.print(XML.toString(xml));
          //x = x + XML.toString(xml);
      }
      String filepath = CreateFile(collectionName ,".json");
      
      try {
          
       

      //File file = new File("C:\\"+collectionName+".xls");
      File file = new File(filepath);
      if (!file.exists()) {
	file.createNewFile();
	}
      
      FileWriter fwriter = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fwriter);
      bw.write(str);
      bw.close();
      
      JOptionPane.showMessageDialog(null, "Data saved at " +filepath, "Message",JOptionPane.INFORMATION_MESSAGE);
     } catch (Exception e) {
	//e.printStackTrace();
         log.LogWriteToTextFile(e.toString());
         JOptionPane.showMessageDialog(null, "Unable to Export to JSON. Check manually", "Error",JOptionPane.ERROR_MESSAGE);
 
    }
      
      


             
           
      } 
      
//      public static String objectToXML(Object voObj)  
//    {  
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();  
//            XMLEncoder xmlEncoder = null;  
//            try  
//            {  
//                xmlEncoder = new XMLEncoder(new BufferedOutputStream(stream));  
//                xmlEncoder.writeObject(voObj);  
//                xmlEncoder.close();  
//                return stream.toString("UTF-8");   
//            }catch(Exception e)  
//            {                  
//                System.out.println("Inside exception from pymtHistToXML : " + e.getMessage());  
//            }  
//            return null;  
//    }  
//      
//       public static Object XMLToObject(String dataXML) {  
//           XMLDecoder d = null;  
//           try {  
//               d = new XMLDecoder(new ByteArrayInputStream(dataXML.getBytes("UTF-8")));  
//               Object voObj = d.readObject();  
//               d.close();  
//               return voObj;  
//           } catch (Exception e) {  
//               System.out.println("Error while Converting XML to VO : " + e);  
//           }  
//           return null;  
//       }  
       
       
       
       
       
       String CreateFile(String FName ,String format)
       {
           String filePath = "C:\\"+FName+""+format;
        JFileChooser fileopen = new JFileChooser(); 
        if ( format ==".xls")
        {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel File", "xls");
        fileopen.setFileFilter(filter);
        }else if ( format == ".json")
        {
         FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON File", "json");
        fileopen.setFileFilter(filter);
        }
        
        fileopen.setSelectedFile(new File(filePath));
         int ret = fileopen.showSaveDialog(null);  
         if (ret == JFileChooser.APPROVE_OPTION) {  
                        
                        filePath = fileopen.getSelectedFile().toString();
                        if(!filePath.endsWith(format))
                        {
                            filePath = filePath+format;
                        }
                        
                        
           
           
       }
         return filePath;
       }
  
 
}

