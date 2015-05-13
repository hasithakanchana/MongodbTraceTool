/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.profilergui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author UKANCDE
 */
public class ProfileDataTableModel extends AbstractTableModel {
    
    private List<ProfileData> pdata = new ArrayList<ProfileData>();
    private String[] columnNames =  {"Timestamp",
                                "Operation",
                                "ns",
                                "query",
                                "updateobj",
                                "nscanned",
                                "nreturned",
                                "nupdated",
                                "lockStats",
                                "nmoved",
                                "responseLength",
                                "keyUpdates",
                                "millis",
                                "client",
                                "user"
                                };
    
    public ProfileDataTableModel() {}

    public ProfileDataTableModel(List<ProfileData> pdata) {
        this.pdata = pdata;
    }
    
        @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return pdata.size();
    
}
    
        public void addUser(ProfileData pdata) {
        this.pdata.add(pdata);
        fireTableDataChanged();
    }
        
        @Override
    public Object getValueAt(int row, int column) {
        Object userAttribute = null;
        ProfileData pObject = pdata.get(row);
        switch(column) {
            case 0: userAttribute = pObject.getTS(); break;          
            case 1: userAttribute = pObject.getOP(); break;
            case 2: userAttribute = pObject.getNS(); break;
            case 3: userAttribute = pObject.getQuery(); break;
            case 4: userAttribute = pObject.getUpdateObj(); break;
            case 5: userAttribute = pObject.getNScanned(); break;
            case 6: userAttribute = pObject.getNReturned(); break;
            case 7: userAttribute = pObject.getNupdated(); break;
            case 8: userAttribute = pObject.getLockStats(); break;
            case 9: userAttribute = pObject.getNMoved(); break;
            case 10: userAttribute = pObject.getResponseLength(); break;
            case 11: userAttribute = pObject.getKeyUpdates(); break;
            case 12: userAttribute = pObject.getMillis();break;
            case 13: userAttribute = pObject.getClient(); break;
            case 14: userAttribute = pObject.getUser(); break;

            
           // case 2: userAttribute = userObject.lastName.getTestColumn2(); break;
            default: break;
        }
        return userAttribute;
    }
        
        
        

}
