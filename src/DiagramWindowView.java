/**
 * Created by Jennifer on 2016-11-08.
 */
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;

public class DiagramWindowView extends JPanel implements IView {
    private ERModel model;
    private ERDiagramView DiagramView;

    boolean init = false;
    int ValX = 0;
    int ValY = 0;
    DiagramWindowView(ERModel model, ERDiagramView DiagramView){
        //set the model
        super();
        setLayout(null);
        this.model = model;
        model.addView(this);
        this.DiagramView = DiagramView;
        int Width = 160;
        int Height = 130;
        init = true;
        this.setSize(Width,Height);
        this.setBackground(new Color(237,232,222));
        this.setLocation(800-160, 0);
        this.setVisible(false);
    }
    public void PanelResize(int h, int w){
        this.setLocation(w-350, 0);

    }
    public void ViewZoomIn(){
        if(DiagramView.scale > 1.3){
            setVisible(true);
        }
        else {
            setVisible(false);
        }
        if(DiagramView.scale == 1){
            ValX = 0;
            ValY = 0;
        }
        else if(DiagramView.scale > 1){
            ValX += DiagramView.Width * 0.003;
            ValY = ValX * 7 / 8;
        }
    }
    public void ViewZoomOut(){
        if(DiagramView.scale > 1.3){
            setVisible(true);
        }
        else {
            setVisible(false);
        }
        if(DiagramView.scale <= 1){
            ValX = 0;
            ValY = 0;
        }
        else if(DiagramView.scale > 1){
            ValX -= DiagramView.Width * 0.003;
            ValY = ValX * 7 / 8;
        }
    }

    public void CurViewZoomIn(){
        if(DiagramView.scale > 1.3){
            setVisible(true);
        }
        else {
            setVisible(false);
        }
        if(DiagramView.scale == 1){
            ValX = 0;
            ValY = 0;
        }
        else if(DiagramView.scale > 1){
            ValX += DiagramView.Width * 0.02;
            ValY = ValX * 7 / 8;
        }
    }

    public void CurViewZoomOut() {
        if (DiagramView.scale > 1.3) {
            setVisible(true);
        } else {
            setVisible(false);
        }
        if (DiagramView.scale <= 1) {
            ValX = 0;
            ValY = 0;
        } else if(DiagramView.scale > 1){
            ValX -= DiagramView.Width * 0.02;
            ValY = ValX * 7 / 8;
        }
    }

    // IView interface
    public void updateView(){
        if(init) {
            if(DiagramView.scale > 1.3){
                setVisible(true);
            }
            else {
                setVisible(false);
            }
        }

        repaint();
    }




    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(0.2,0.2);
        g2.setColor(Color.black);
        g2.drawRect(0,0,DiagramView.Width,DiagramView.Height);

        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.RED);
        int w = (int)Math.floor(DiagramView.Width/(DiagramView.scale));
        int h = (int)Math.floor(DiagramView.Height/(DiagramView.scale));
        int RX = (int)Math.floor(DiagramView.translateX);
        int RY = (int)Math.floor(DiagramView.translateY);

        g2.drawRect(ValX-RX,ValY-RY,w+50,h+40);


        g2.setColor(new Color(216, 206, 197));
        for(Entity E: model.EntityList){
            g2.fillRect(E.GetX(), E.GetY(), E.GetW(),E.GetH());
            if(E.GetState() == ERModel.State.SELECTED){
                g2.setColor(new Color(145, 229, 100));
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(E.GetX(), E.GetY(), E.GetW(),E.GetH());
                g2.setStroke(new BasicStroke(1));
                g2.setColor(new Color(216, 206, 197));
            }
            if(E.GetState() == ERModel.State.HOVER){
                //System.out.println("@@HOVER Entity : " +  E.GetId());
                g2.setColor(new Color(57, 219, 234));
                g2.drawRect(E.GetX(), E.GetY(), E.GetW(),E.GetH());
                for(int i = 0; i < 12; i++){
                    g2.setColor(Color.red);
                    int x = E.hoverFrames[i].GetX();
                    int y = E.hoverFrames[i].GetY();
                    int state = E.hoverFrames[i].GetState();
                    g2.drawLine(x-3,y+3,x+3,y-3);
                    g2.drawLine(x-3,y-3,x+3,y+3);

                    if(state == 1){
                        int alpha = 127;
                        g2.setColor(new Color(216, 88, 88, alpha));
                        int X = x - 10;
                        int Y = y - 10;
                        g2.fillOval(X,Y,20,20);
                    }
                    if(state == 2){
                        int alpha = 127;
                        g2.setColor(new Color(134, 206, 103, alpha));
                        int X = x - 10;
                        int Y = y - 10;
                        g2.fillOval(X,Y,20,20);
                    }

                    g2.setColor(new Color(216, 206, 197));
                }
            }
            g2.setColor(Color.black);
            g2.drawString(E.GetName(),E.x+40,E.y+40);
            g2.setColor(new Color(216, 206, 197));
        }
        for(Relation R: model.RelationList){
            int M1ID = R.E1M.GetId();
            int M2ID = R.E2M.GetId();
            if(((0 <= M1ID && M1ID <= 2) || (6 <= M1ID && M1ID <= 8)) &&
                    ((9 <= M2ID && M2ID <= 11) || (3 <= M2ID && M2ID <= 5))){
                Point p1 = new Point(R.GetE1M().GetX(),R.GetE1M().GetY());
                Point p2 = new Point(R.GetE1M().GetX(),R.GetE2M().GetY());
                Point p3 = new Point(R.GetE2M().GetX(),R.GetE2M().GetY());
                DiagramView.drawOrthogonal(g2,R,p1,p2,p3);
            }

            else if(((3 <= M1ID && M1ID <= 5) || (9 <= M1ID && M1ID <= 11)) &&
                    ((0 <= M2ID && M2ID <= 2) || (6 <= M2ID && M2ID <= 8))){
                Point p1 = new Point(R.GetE1M().GetX(),R.GetE1M().GetY());
                Point p2 = new Point(R.GetE2M().GetX(),R.GetE1M().GetY());
                Point p3 = new Point(R.GetE2M().GetX(),R.GetE2M().GetY());
                DiagramView.drawOrthogonal(g2,R,p1,p2,p3);

            }

            else if((0 <= M1ID && M1ID <= 2) || (6 <= M1ID && M1ID <= 8) &&
                    (0 <= M2ID && M2ID <= 2) || (6 <= M2ID && M2ID <= 8)) {
                Point p1 = new Point(R.GetE1M().GetX(),R.GetE1M().GetY());
                int y = (R.GetE2M().GetY() - R.GetE1M().GetY())/2 + R.GetE1M().GetY();
                Point p2 = new Point(R.GetE1M().GetX(),y);
                Point p3 = new Point(R.GetE2M().GetX(),y);
                Point p4 = new Point(R.GetE2M().GetX(),R.GetE2M().GetY());
                DiagramView.drawJog(g2,R,p1,p2,p3,p4);

            }

            else if((3 <= M1ID && M1ID <= 5) || (9 <= M1ID && M1ID <= 11) &&
                    (3 <= M2ID && M2ID <= 5) || (9 <= M2ID && M2ID <= 11)) {
                Point p1 = new Point(R.GetE1M().GetX(),R.GetE1M().GetY());
                int x = (R.GetE2M().GetX() - R.GetE1M().GetX())/2 + R.GetE1M().GetX();
                Point p2 = new Point(x, R.GetE1M().GetY());
                Point p3 = new Point(x, R.GetE2M().GetY());
                Point p4 = new Point(R.GetE2M().GetX(),R.GetE2M().GetY());
                DiagramView.drawJog(g2,R,p1,p2,p3,p4);
            }
            g2.setStroke(new BasicStroke(1));
            g2.setColor(new Color(216, 206, 197));
        }

        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.red);

        g2.setStroke(new BasicStroke(1));
        g2.setColor(new Color(216, 206, 197));


    }
}
