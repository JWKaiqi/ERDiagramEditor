/**
 * Created by Jennifer on 2016-10-22.
 */
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;

public class EREntityListView extends JPanel implements IView{
    private ERModel model;
    JScrollPane SP = new JScrollPane();
    JTable EntityListTbl = new JTable();
    DefaultTableModel dtm = new DefaultTableModel(0, 0);
    ArrayList<EntityMap> TheEntityMap = new ArrayList<EntityMap>();
    ArrayList<Integer> SelectRows = new ArrayList<>();

    private class EntityMap{
        int row;
        int id;
        String name;
    }


    EREntityListView(ERModel model){

        setLayout(null);
        this.model = model;
        setSize(200,350);
        setBackground(new Color(225, 239, 238));

        String ColName[] = new String[]{"Entity Name"};

        this.dtm.setColumnIdentifiers(ColName);
        EntityListTbl.setModel(dtm);
        EntityListTbl.setSize(new Dimension(this.getWidth(),this.getHeight()));

        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer)
                EntityListTbl.getTableHeader().getDefaultRenderer();

        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setBackground(new Color(233, 239, 225));

        EntityListTbl.setCellSelectionEnabled(true);
        EntityListTbl.setRowSelectionAllowed(true);
        ListSelectionModel cellSelectionModel = EntityListTbl.getSelectionModel();

        EntityListTbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column)
            {
                //MyTableModel model = (MyTableModel) table.getModel();
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(233, 239, 225));
                for(int r: SelectRows){
                    if(r == row){
                        c.setBackground(new Color(204, 237, 144));
                    }
                }
                return c;
            }

        });
        ListSelectionListener sl = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(EntityListTbl.getSelectedRow() != -1){
                    //System.out.println(EntityListTbl.getValueAt(EntityListTbl.getSelectedRow(), 0).toString());
                    int eid = SearchMap(EntityListTbl.getSelectedRow());
                    if(eid > -1) {
                        model.selectEntity(eid);
                    }
                }
            }
        };

        cellSelectionModel.addListSelectionListener(sl);

        SP.setSize(205,350);
        SP.getViewport().add(EntityListTbl);
        this.add(SP);
        SP.getViewport().setBackground(new Color(233, 239, 225));
        SP.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
    }

    public void PanelResize(int h, int w){
        //EntityListTbl.setSize(new Dimension(w,h));
        SP.setSize(SP.getWidth(),h/2 - 20);
    }

    public int SearchMap(int row){
        int eid = -1;
        for(EntityMap EM: TheEntityMap){
            if(EM.row == row){
                eid = EM.id;
                break;
            }
        }
        return eid;
    }

    // IView interface
    public void updateView(){
        SelectRows.clear();
        DefaultTableModel dtm = (DefaultTableModel) EntityListTbl.getModel();
        dtm.setRowCount(0);
        TheEntityMap.clear();

        for (Entity E: model.EntityList) {
            dtm.addRow(new Object[] { E.GetName()});
            int row = dtm.getRowCount() - 1;
            if(E.GetState() == ERModel.State.SELECTED){
                EntityListTbl.setRowSelectionInterval(row, row);
                SelectRows.add(row);
            }
            EntityMap EM = new EntityMap();
            EM.row = row;
            EM.id = E.GetId();
            EM.name = E.GetName();
            TheEntityMap.add(EM);
        }
    }
}
