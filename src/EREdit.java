import javax.swing.*;
import java.awt.Dimension;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * Created by bwbecker on 2016-10-10.
 */
public class EREdit {
    public static void main(String[] args) {
        //System.out.println("Hello, world!");
        JFrame frame = new JFrame("ER Diagram Editor");
        frame.setPreferredSize(new Dimension(1000,700));
        //create Model and init it
        ERModel model = new ERModel();

        // create View, tell it about model (and controller)


        //frame.add(SP);
        ERDiagramView DiagramView = new ERDiagramView(model);
        DiagramView.setPreferredSize(new Dimension(800,700));
        DiagramView.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        model.addView(DiagramView);

        DiagramWindowView DiagramWindow = new DiagramWindowView(model,DiagramView);
        DiagramView.DiagramWindow = DiagramWindow;
        DiagramView.add(DiagramWindow);

        JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, 0, 300, 100);
        zoomSlider.setMajorTickSpacing(25);
        zoomSlider.setMinorTickSpacing(5);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPaintLabels(true);
        zoomSlider.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider)e.getSource();
                int zoomPercent = slider.getValue();
                // make sure zoom never gets to actual 0, or else the objects will
                // disappear and the matrix will be non-invertible.
                double oldscale = DiagramView.scale;
                DiagramView.scale = Math.max(0.00001, zoomPercent / 100.0);
                if(oldscale >= DiagramView.scale) {
                    DiagramWindow.ViewZoomOut();
                }
                else if(oldscale < DiagramView.scale ){
                    DiagramWindow.ViewZoomIn();

                }
                if(DiagramView.scale > 1.3){
                    DiagramWindow.setVisible(true);
                }
                else {
                    DiagramWindow.setVisible(false);
                }
                DiagramView.repaint();
            }
        });
        zoomSlider.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_CLOSE_BRACKET){
                    if(e.isControlDown()){
                        DiagramView.ZoomIn();
                        double scale = DiagramView.scale;
                        int Value = (int)Math.floor(scale * 100);
                        zoomSlider.setValue(Value);
                    }
                }
                else if(key == KeyEvent.VK_OPEN_BRACKET){
                    if(e.isControlDown()){
                        DiagramView.ZoomOut();
                        double scale = DiagramView.scale;
                        int Value = (int)Math.floor(scale * 100);
                        zoomSlider.setValue(Value);
                    }
                }
            }
        });

        EREntityListView ELView = new EREntityListView(model);
        model.addView(ELView);
        ELView.setPreferredSize(new Dimension(200,350));
        ELView.setBorder(BorderFactory.createLineBorder(Color.black));


        ERRelationListView RLView = new ERRelationListView(model);
        model.addView(RLView);
        RLView.setPreferredSize(new Dimension(200,350));
        RLView.setBorder(BorderFactory.createLineBorder(Color.black));
        RLView.setFocusable(true);
        RLView.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_CLOSE_BRACKET){
                    if(e.isControlDown()){
                        DiagramView.ZoomIn();
                        double scale = DiagramView.scale;
                        int Value = (int)Math.floor(scale * 100);
                        zoomSlider.setValue(Value);
                    }
                }
                else if(key == KeyEvent.VK_OPEN_BRACKET){
                    if(e.isControlDown()){
                        DiagramView.ZoomOut();
                        double scale = DiagramView.scale;
                        int Value = (int)Math.floor(scale * 100);
                        zoomSlider.setValue(Value);
                    }
                }
            }
        });

        JPanel ListPanel = new JPanel();
        ListPanel.setPreferredSize(new Dimension(200,700));
        //ListPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        ListPanel.setLayout(new BoxLayout(ListPanel,BoxLayout.Y_AXIS));
        ListPanel.add(ELView);
        ListPanel.add(RLView);

        frame.addComponentListener(new ComponentAdapter(){

            public void componentResized(ComponentEvent e) {
                Component c = e.getComponent();
                DiagramView.PanelResize(c.getSize().height, c.getSize().width);
                DiagramWindow.PanelResize(c.getSize().height, c.getSize().width);
                ELView.PanelResize(c.getSize().height, c.getSize().width);
                RLView.PanelResize(c.getSize().height, c.getSize().width);
            }
        });
        frame.setFocusable(true);
        frame.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_CLOSE_BRACKET){
                    if(e.isControlDown()){
                        DiagramView.ZoomIn();
                        double scale = DiagramView.scale;
                        int Value = (int)Math.floor(scale * 100);
                        zoomSlider.setValue(Value);
                    }
                }
                else if(key == KeyEvent.VK_OPEN_BRACKET){
                    if(e.isControlDown()){
                        DiagramView.ZoomOut();
                        double scale = DiagramView.scale;
                        int Value = (int)Math.floor(scale * 100);
                        zoomSlider.setValue(Value);
                    }
                }
            }
        });

        DiagramView.addMouseWheelListener(new MouseWheelListener(){
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();
                if(rotation < 0){
                    DiagramView.ZoomIn();
                }
                else{
                    DiagramView.ZoomOut();
                }
                double scale = DiagramView.scale;
                int Value = (int)Math.floor(scale * 100);
                zoomSlider.setValue(Value);
            }
        });
       // SP.setViewportView(DiagramView);
        //create the Window
        frame.add(ListPanel, BorderLayout.WEST);
        //frame.add(SP, BorderLayout.CENTER);
        frame.getContentPane().add(zoomSlider, BorderLayout.NORTH);
        frame.getContentPane().add(DiagramView, BorderLayout.CENTER);


        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
