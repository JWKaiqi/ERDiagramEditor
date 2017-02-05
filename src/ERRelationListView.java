import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;

/**
 * Created by Jennifer on 2016-10-22.
 */
public class ERRelationListView extends JPanel implements IView {
    private ERModel model;
    JScrollPane SP = new JScrollPane();
    JTable RelationListTbl = new JTable();
    DefaultTableModel dtm = new DefaultTableModel(0, 0);
    ArrayList<RelationMap> TheRelationMap = new ArrayList<RelationMap>();
    ArrayList<Integer> SelectRows = new ArrayList<>();
    JButton DeleteRelation = new JButton("Delete");

    private class RelationMap{
        int row;
        int id;
        String name;
    }

    ERRelationListView(ERModel model){
        super();
        setLayout(null);
        this.model = model;
        setSize(200,350);
        setBackground(new Color(239, 225, 225));

        String ColName[] = new String[]{"Relation Name"};

        this.dtm.setColumnIdentifiers(ColName);
        RelationListTbl.setModel(dtm);
        RelationListTbl.setSize(new Dimension(this.getWidth()-20,this.getHeight()-20));

        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer)
                RelationListTbl.getTableHeader().getDefaultRenderer();

        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        RelationListTbl.setCellSelectionEnabled(true);
        RelationListTbl.setRowSelectionAllowed(true);
        ListSelectionModel cellSelectionModel = RelationListTbl.getSelectionModel();

        RelationListTbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column)
            {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(239, 225, 225));
                for(int r: SelectRows){
                    if(r == row){
                        c.setBackground(new Color(244, 158, 154));
                    }
                }
                return c;
            }

        });

        ListSelectionListener sl = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(RelationListTbl.getSelectedRow() != -1){
                    int rid = SearchMap(RelationListTbl.getSelectedRow());
                    if(rid > -1) {
                        model.selectRelation(rid);
                    }
                }
            }
        };

        cellSelectionModel.addListSelectionListener(sl);
        DeleteRelation.setBounds(50,290,80,20);
        DeleteRelation.setSize(new Dimension(100, 20));

        DeleteRelation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.deleteSelectedRelation();
            }
        });
        this.add(DeleteRelation);

        SP.setSize(205,300);
        SP.getViewport().add(RelationListTbl);
        SP.getViewport().setBackground(new Color(239, 225, 225));
        SP.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        this.add(DeleteRelation);
        this.add(SP);
    }
    public void PanelResize(int h, int w){
        SP.setSize(SP.getWidth(),h/2 - 60);
        DeleteRelation.setBounds(50,h/2-59,80,20);
    }

    public int SearchMap(int row){
        int eid = -1;
        for(RelationMap RM: TheRelationMap){
            if(RM.row == row){
                eid = RM.id;
                break;
            }
        }
        return eid;
    }

    // IView interface
    public void updateView(){
        SelectRows.clear();
        DefaultTableModel dtm = (DefaultTableModel) RelationListTbl.getModel();
        dtm.setRowCount(0);
        TheRelationMap.clear();

        for (Relation R: model.RelationList) {
            dtm.addRow(new Object[] { R.GetE1().GetName() + " -> " + R.GetE2().GetName()});
            int row = dtm.getRowCount() - 1;
            if(R.GetState() == ERModel.State.SELECTED){
                RelationListTbl.setRowSelectionInterval(row, row);
                SelectRows.add(row);
            }
            RelationMap RM = new RelationMap();
            RM.row = row;
            RM.id = R.GetId();
            RM.name = R.GetE1().GetName() + " -> " + R.GetE2().GetName();
            TheRelationMap.add(RM);
        }
    }
}
