/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.profilergui;



/**
 *
 * @author UKANCDE
 */

public class ProfileData {
    

             
    private String ts ="";
    private String op ="";
    private String ns ="";   
    private String query ="";
    private String nscanned ="";
    private String lockStats ="";
    private String millis ="";
    private String client ="";
    private String user ="";
    private String nmoved="";
    private String responseLength="";
    private String keyUpdates="";
    private String updateobj = "";
    private String nupdated ="";
    private String nreturned = "";
    
    
             
    public String getTS() { return ts; }
    public String getOP() { return op; }
    public String getNS() { return ns; }
    public String getQuery() { return query; }
    public String getNScanned() { return nscanned; }
    public String getLockStats() { return lockStats; }
    public String getMillis() { return millis; }
    public String getClient() { return client; }
    public String getUser() { return user; }
    public String getNMoved(){ return nmoved;}
    public String getResponseLength(){return responseLength;}
    public String getKeyUpdates(){return keyUpdates;}
    public String getUpdateObj(){return updateobj;}
    public String getNupdated(){return nupdated;}
    public String getNReturned(){return nreturned;}
    
    
    public void setTS(String ts) { this.ts = ts; }
    public void setOP(String op) { this.op = op; }
    public void setNS(String ns) { this.ns = ns; }
    public void setQuery(String query) { this.query = query; }
    public void setNScanned(String nscanned) { this.nscanned = nscanned; }
    public void setLockStats(String lockStats) { this.lockStats = lockStats; }
    public void setMillis(String millis) { this.millis = millis; }
    public void setClient(String client) { this.client = client; }
    public void setUser(String user) { this.user = user; }
    public void setNMoved(String nmoved){ this.nmoved = nmoved;}
    public void setResponseLength( String responseLength) {this.responseLength = responseLength;}
    public void setKeyUpdates(String keyUpdates) {this.keyUpdates = keyUpdates;}
    public void setUpdateObj(String updateobj) {this.updateobj = updateobj;}
    public void setNupdated(String nupdated) {this.nupdated = nupdated;}
    public void setNReturned(String nreturned) {this.nreturned = nreturned;}
    
    
 
             
    
}
