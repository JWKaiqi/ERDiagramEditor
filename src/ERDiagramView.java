/**
 * Created by Jennifer on 2016-10-21.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ERDiagramView extends JPanel implements IView {
    // the model that this view is showing
    private int EntityLastIndex = 0;
    private ERModel model;
    JButton AddEntity = new JButton("Add Entity");
    JButton DeleteEntity = new JButton("Delete Entity");
    private ArrayList<TextField> TextFieldList;
    boolean drawArrow;
    boolean doubleClicked = false;
    boolean doubleClickedEntity = false;
    boolean draggingEntity = false;
    int draggEntityID = -1;
    DiagramWindowView DiagramWindow;
    JTextField EntityNameTF = new JTextField(20);
    JLabel Label = new JLabel("Please enter the name");

    //DiagramWindowView DiagramWindow;
    AffineTransform at;

    int Width = 800;
    int Height = 700;

    int cursorX;
    int cursorY;

    double scale;
    double translateX;
    double translateY;
    int E1 = -1;
    int E2 = -1;
    int E1MID = -1;
    int E2MID = -1;
    int EW = 160;
    int EH = 80;
    int settingText = -1;
    double referenceX;
    double referenceY;
    AffineTransform initialTransform;
    Point2D XFormedPoint; // storage for a transformed mouse point

    ERDiagramView(ERModel model) {
        //set the model
        super();
        setLayout(null);
        scale = 1;
        translateX = 0;
        translateY = 0;
        //
        setPreferredSize(new Dimension(Width, Height));
        //setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        this.model = model;
        this.TextFieldList = new ArrayList<TextField>();
        setBackground(new Color(237, 232, 222));
        AddEntity.setBounds(250, 500, 80, 20);
        AddEntity.setSize(new Dimension(100, 40));

        DeleteEntity.setBounds(450, 500, 80, 20);
        DeleteEntity.setSize(new Dimension(100, 40));

        EntityNameTF.setVisible(false);
        EntityNameTF.setBounds(260, 20, 200, 30);
        Label.setVisible(false);
        Label.setBounds(260, 0, 200, 30);
        this.drawArrow = false;
        this.add(EntityNameTF);
        this.add(Label);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_CLOSE_BRACKET) {
                    if (e.isControlDown()) {
                        ZoomOut();
                    }
                } else if (key == KeyEvent.VK_OPEN_BRACKET) {
                    if (e.isControlDown()) {
                        ZoomIn();

                    }
                }
            }
        });
        // setup the event to go to the "controller"
        // (this anonymous class is essentially the controller)
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                boolean NoEntitySelected = true;
                try {
                    int newX = (int) at.inverseTransform(e.getPoint(), null).getX();
                    int newY = (int) at.inverseTransform(e.getPoint(), null).getY();
                    for (Entity E : model.EntityList) {
                        if (MouseOnEntity(newX, newY, E.x, E.y, E.width, E.height)) {
                            NoEntitySelected = false;
                            int MID = MouseOnHoverMagnet(newX, newY, E.GetId());
                            if (MID != -1) {
                                model.hoverEntity(E.GetId());
                                model.setEHMState(E.GetId(), MID, 1);
                                model.selectedMag = true;
                                if (E1 == -1) {
                                    E1 = E.GetId();
                                    E1MID = MID;
                                    model.NoSelectEntity();
                                } else if (E1 > -1 && E1 != E.GetId()) {
                                    E2 = E.GetId();
                                    E2MID = MID;
                                    CreateNewRelation();
                                }
                                break;
                            } else {
                                model.selectEntity(E.id);
                                if (settingText == E.id) {
                                    String name = EntityNameTF.getText();
                                    EntityNameTF.setVisible(false);
                                    Label.setVisible(false);
                                    model.setEntityName(E.id, name);
                                    settingText = -1;
                                }
                                // }
                                if (e.getClickCount() == 2) {
                                    EntityNameTF.setText(E.GetName());
                                    Point p = new Point(E.x, E.y);
                                    int TX = (int) at.inverseTransform(p, null).getX();
                                    int TY = (int) at.inverseTransform(p, null).getY();
                                    EntityNameTF.setVisible(true);
                                    Label.setVisible(true);
                                    settingText = E.id;
                                    doubleClickedEntity = true;
                                    repaint();
                                    //Set Name;
                                }
                            }
                        } else {
                            if (settingText == E.id) {
                                String name = EntityNameTF.getText();
                                EntityNameTF.setVisible(false);
                                Label.setVisible(false);
                                model.setEntityName(E.id, name);
                                settingText = -1;
                            }
                        }
                    }
                    if (NoEntitySelected) {
                        E1 = -1;
                        E2 = -1;
                        E1MID = -1;
                        E2MID = -1;
                        model.unselectAll();
                        model.selectedMag = false;

                        if (e.getClickCount() == 2) {
                            if (!(scale < 1 && newY > Height)) {
                                if (newX < 0) {
                                    newX = 0;
                                }
                                if (newY < 0) {
                                    newY = 0;
                                }
                                int x = Math.min(newX, Width - EW);
                                int y = Math.min(newY, Height - EH);
                                model.addEntity(EntityLastIndex, x, y, EW, EH);
                                EntityLastIndex++;
                                //CreateNewEntityView(x, y);
                                doubleClicked = true;
                            }
                        }
                    }
                } catch (NoninvertibleTransformException x) {
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    referenceX = at.inverseTransform(e.getPoint(), null).getX();
                    referenceY = at.inverseTransform(e.getPoint(), null).getY();
                    initialTransform = at;
                    int newX = (int) at.inverseTransform(e.getPoint(), null).getX();
                    int newY = (int) at.inverseTransform(e.getPoint(), null).getY();

                    for (Entity E : model.EntityList) {
                        if (MouseOnEntity(newX, newY, E.x, E.y, E.width, E.height)) {
                            cursorX = newX - E.x;
                            cursorY = newY - E.y;
                            break;
                        }
                    }

                } catch (NoninvertibleTransformException x) {
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggingEntity) {
                    draggingEntity = false;
                    draggEntityID = -1;
                }
            }
        });


        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                try {
                    if (at != null) {
                        int newX = (int) at.inverseTransform(e.getPoint(), null).getX();
                        int newY = (int) at.inverseTransform(e.getPoint(), null).getY();
                        super.mouseMoved(e);
                        for (Entity E : model.EntityList) {
                            if (MouseOnEntity(newX, newY, E.x, E.y, E.width, E.height)) {
                                model.hoverEntity(E.GetId());
                                int MID = MouseOnHoverMagnet(newX, newY, E.GetId());
                                if (MID != -1) {
                                    model.setEHMState(E.GetId(), MID, 2);
                                    //break;
                                }
                            } else {
                                if (!HoverMagnetIsSelected(E.hoverFrames) &&
                                        E.GetState() == ERModel.State.HOVER) {
                                    model.unhoverEntity(E.GetId());
                                }
                            }

                        }
                    }
                } catch (NoninvertibleTransformException x) {

                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                boolean NoEntityDragged = true;

                // first transform the mouse point to the pan and zoom
                // coordinates. We must take care to transform by the
                // initial tranform, not the updated transform, so that
                // both the initial reference point and all subsequent
                // reference points are measured against the same origin.
                try {
                    int newX = (int) at.inverseTransform(e.getPoint(), null).getX();
                    int newY = (int) at.inverseTransform(e.getPoint(), null).getY();
                    for (Entity E : model.EntityList) {
                        if (!draggingEntity || draggEntityID == E.id) {
                            if (MouseOnEntity(newX, newY, E.x, E.y, E.width, E.height)) {
                                draggEntityID = E.id;
                                NoEntityDragged = false;
                                draggingEntity = true;
                                int MID = MouseOnHoverMagnet(newX, newY, E.GetId());
                                if (MID == -1) {
                                    model.moveEntity(E.id, newX, newY, cursorX, cursorY, Width, Height);
                                }
                                break;
                                //}
                            }
                        }
                    }
                    if (NoEntityDragged && !draggingEntity) {
                        // the size of the pan translations
                        // are defined by the current mouse location subtracted
                        // from the reference location
                        XFormedPoint = initialTransform.inverseTransform(e.getPoint(), null);
                        double deltaX = XFormedPoint.getX() - referenceX;
                        double deltaY = XFormedPoint.getY() - referenceY;

                        // make the reference point be the new mouse point.
                        referenceX = XFormedPoint.getX();
                        referenceY = XFormedPoint.getY();

                        translateX += deltaX;
                        translateY += deltaY;
                        // schedule a repaint.
                        repaint();
                    }
                } catch (NoninvertibleTransformException te) {
                    System.out.println(te);
                }

            }
        });
        AddEntity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addEntity(EntityLastIndex, getWidth() / 3, getHeight() / 3, EW, EH);
                EntityLastIndex++;
            }
        });

        DeleteEntity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.deleteSelectedEntity();
            }
        });

        add(AddEntity);
        add(DeleteEntity);

    }

    public boolean MouseOnEntity(int MX, int MY, int EX, int EY, int EW, int EH) {
        boolean MouseOnEntity = false;
        if ((EX <= MX && MX <= EX + EW) && (EY <= MY && MY <= EY + EH)) {
            MouseOnEntity = true;
        }
        return MouseOnEntity;
    }

    public void PanelResize(int h, int w) {
        AddEntity.setBounds(w / 4, h - h / 5, AddEntity.getWidth(), AddEntity.getHeight());
        DeleteEntity.setBounds(w / 3 + w / 10, h - h / 5, DeleteEntity.getWidth(), DeleteEntity.getHeight());
        EntityNameTF.setBounds(w / 4 + w / 25, 35, 200, 30);
        Label.setBounds(w / 4 + w / 15, 5, 200, 30);
    }

    public boolean HoverMagnetIsSelected(EntityHoverFrame EHF[]) {
        boolean IsSelected = false;
        for (int i = 0; i < 12; i++) {
            if (EHF[i].GetState() == 1) {
                IsSelected = true;
                break;
            }
        }
        return IsSelected;
    }

    public int MouseOnHoverMagnet(int ex, int ey, int Eid) {
        int MouseOn = -1;
        Entity e = model.GetEntity(Eid);
        for (int i = 0; i < 12; i++) {
            int x = e.hoverFrames[i].GetX();
            int y = e.hoverFrames[i].GetY();
            if ((ex - 10 <= x && x <= ex + 10) &&
                    (ey - 10 <= y && y <= ey + 10)) {
                MouseOn = i;
                break;
            }
        }
        return MouseOn;
    }

    public boolean MouseOnResizeMagnet(int ex, int ey, Entity E) {
        boolean MouseOn = false;
        if (((ex - 10 <= E.x && E.x <= ex + 10) && (ey - 10 <= E.y && E.y <= ey + 10)) ||
                ((ex - 10 <= E.x + E.width && E.x + E.width <= ex + 10) && (ey - 10 <= E.y && E.y <= ey + 10)) ||
                ((ex - 10 <= E.x && E.x <= ex + 10) && (ey - 10 <= E.y + E.height && E.y + E.height <= ey + 10)) ||
                ((ex - 10 <= E.x + E.width && E.x + E.width <= ex + 10) &&
                        (ey - 10 <= E.y + E.height && E.y + E.height <= ey + 10))) {
            MouseOn = true;
        }

        return MouseOn;
    }

    public void CreateNewRelation() {
        model.selectedMag = false;

        Entity e1 = model.GetEntity(E1);
        Entity e2 = model.GetEntity(E2);
        EntityHoverFrame e1m = model.GetEntity(E1).hoverFrames[E1MID];
        EntityHoverFrame e2m = model.GetEntity(E2).hoverFrames[E2MID];
        model.addRelation(e1, e2, e1m, e2m);

        model.unhoverEntity(E1);
        model.unhoverEntity(E2);

        E1 = -1;
        E2 = -1;
        E1MID = -1;
        E2MID = -1;
    }

    // IView interface
    public void updateView() {
        repaint();
    }

    public void ZoomOut() {
        if (scale > 0.1) {
            scale -= 0.1;
            DiagramWindow.CurViewZoomOut();
            repaint();
        }
    }

    public void ZoomIn() {
        if (scale < 3.0) {
            scale += 0.1;
            DiagramWindow.CurViewZoomIn();
            repaint();
        }
    }

    public void drawArrowHead(Graphics2D g2, Point tip, Point tail, Color color) {
        g2.setPaint(color);
        double phi = Math.toRadians(30);
        int barb = 10;
        double dy = tip.y - tail.y;
        double dx = tip.x - tail.x;
        double theta = Math.atan2(dy, dx);
        double x, y, rho = theta + phi;
        for (int j = 0; j < 2; j++) {
            x = tip.x - barb * Math.cos(rho);
            y = tip.y - barb * Math.sin(rho);
            g2.draw(new Line2D.Double(tip.x, tip.y, x, y));
            rho = theta - phi;
        }
    }

    public void drawJog(Graphics2D g2, Relation R, Point p1, Point p2, Point p3, Point p4) {
        if (R.GetState() == ERModel.State.SELECTED) {
            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(255, 140, 140));
            g2.draw(new Line2D.Double(p1, p2));
            g2.draw(new Line2D.Double(p2, p3));
            g2.draw(new Line2D.Double(p3, p4));
            drawArrowHead(g2, p4, p3, new Color(255, 140, 140));
        } else {
            g2.setStroke(new BasicStroke(2));
            g2.setColor(new Color(104, 99, 96));
            g2.draw(new Line2D.Double(p1, p2));
            g2.draw(new Line2D.Double(p2, p3));
            g2.draw(new Line2D.Double(p3, p4));
            drawArrowHead(g2, p4, p3, new Color(104, 99, 96));
        }
    }

    public void drawOrthogonal(Graphics2D g2, Relation R, Point p1, Point p2, Point p3) {
        if (R.GetState() == ERModel.State.SELECTED) {
            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(255, 140, 140));
            g2.draw(new Line2D.Double(p1, p2));
            g2.draw(new Line2D.Double(p2, p3));
            drawArrowHead(g2, p3, p2, new Color(255, 140, 140));
        } else {
            g2.setStroke(new BasicStroke(2));
            g2.setColor(new Color(104, 99, 96));
            g2.draw(new Line2D.Double(p1, p2));
            g2.draw(new Line2D.Double(p2, p3));
            drawArrowHead(g2, p3, p2, new Color(104, 99, 96));
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        AffineTransform saveTransform = g2.getTransform();


        at = new AffineTransform((saveTransform));
        //at = g2.getTransform();
        at.translate(800 / 2, 700 / 2);
        at.scale(scale, scale);
        at.translate(-800 / 2, -700 / 2);
        at.translate(translateX, translateY);
        this.setPreferredSize(new Dimension(Width, Height));
        g2.setTransform(at);

        super.paintComponent(g);

        g2.setColor(new Color(249, 247, 244));
        g2.fillRect(0, 0, this.Width, this.Height);

        if (!doubleClicked) {
            g2.setColor(new Color(104, 99, 96));
            g2.drawString("* Double click anywhere to add entity.",
                    this.getWidth() / 3, this.getHeight() / 20);

        }

        if (!doubleClickedEntity) {
            g2.setColor(new Color(104, 99, 96));
            if (!doubleClicked) {
                g2.drawString("* Double click a entity to change its name,",
                        this.getWidth() / 3, this.getHeight() / 13);
                g2.drawString(" and click anywhere to save the name.",
                        this.getWidth() / 3 + 5, this.getHeight() / 10);

            } else {
                g2.drawString("* Double click a entity to change its name,",
                        this.getWidth() / 3, this.getHeight() / 20);

                g2.drawString(" and click anywhere to save the name.",
                        this.getWidth() / 3 + 5, this.getHeight() / 13);
            }

        }
        g2.setColor(new Color(229, 223, 213));
        for (Entity E : model.EntityList) {
            g2.setColor(new Color(104, 99, 96));
            g2.drawRect(E.GetX(), E.GetY(), E.GetW(), E.GetH());

            g2.setColor(new Color(229, 223, 213));
            g2.fillRect(E.GetX(), E.GetY(), E.GetW(), E.GetH());
            if (E.GetState() == ERModel.State.SELECTED) {
                g2.setColor(new Color(145, 229, 100));
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(E.GetX(), E.GetY(), E.GetW(), E.GetH());
                g2.setStroke(new BasicStroke(1));
                g2.setColor(new Color(229, 223, 213));
            }
            if (E.GetState() == ERModel.State.HOVER) {
                //System.out.println("@@HOVER Entity : " +  E.GetId());
                g2.setColor(new Color(57, 219, 234));
                g2.drawRect(E.GetX(), E.GetY(), E.GetW(), E.GetH());
                for (int i = 0; i < 12; i++) {
                    g2.setColor(Color.red);
                    int x = E.hoverFrames[i].GetX();
                    int y = E.hoverFrames[i].GetY();
                    int state = E.hoverFrames[i].GetState();
                    g2.drawLine(x - 3, y + 3, x + 3, y - 3);
                    g2.drawLine(x - 3, y - 3, x + 3, y + 3);

                    if (state == 1) {
                        //System.out.println( E.GetId() + ": state == 1 || state == 2");
                        int alpha = 127;
                        g2.setColor(new Color(216, 88, 88, alpha));
                        int X = x - 10;
                        int Y = y - 10;
                        g2.fillOval(X, Y, 20, 20);
                    }

                    if (state == 2) {
                        //System.out.println( E.GetId() + ": state == 1 || state == 2");
                        int alpha = 127;
                        g2.setColor(new Color(134, 206, 103, alpha));
                        int X = x - 10;
                        int Y = y - 10;
                        g2.fillOval(X, Y, 20, 20);
                    }

                    g2.setColor(new Color(229, 223, 213));
                }
            }
            g2.setColor(Color.black);
            g2.drawString(E.GetName(), E.x + 50, E.y + 45);
            g2.setColor(new Color(229, 223, 213));
        }
        for (Relation R : model.RelationList) {
            int M1ID = R.E1M.GetId();
            int M2ID = R.E2M.GetId();
            if (((0 <= M1ID && M1ID <= 2) || (6 <= M1ID && M1ID <= 8)) &&
                    ((9 <= M2ID && M2ID <= 11) || (3 <= M2ID && M2ID <= 5))) {
                Point p1 = new Point(R.GetE1M().GetX(), R.GetE1M().GetY());
                Point p2 = new Point(R.GetE1M().GetX(), R.GetE2M().GetY());
                Point p3 = new Point(R.GetE2M().GetX(), R.GetE2M().GetY());
                drawOrthogonal(g2, R, p1, p2, p3);
            } else if (((3 <= M1ID && M1ID <= 5) || (9 <= M1ID && M1ID <= 11)) &&
                    ((0 <= M2ID && M2ID <= 2) || (6 <= M2ID && M2ID <= 8))) {
                Point p1 = new Point(R.GetE1M().GetX(), R.GetE1M().GetY());
                Point p2 = new Point(R.GetE2M().GetX(), R.GetE1M().GetY());
                Point p3 = new Point(R.GetE2M().GetX(), R.GetE2M().GetY());
                drawOrthogonal(g2, R, p1, p2, p3);

            } else if ((0 <= M1ID && M1ID <= 2) || (6 <= M1ID && M1ID <= 8) &&
                    (0 <= M2ID && M2ID <= 2) || (6 <= M2ID && M2ID <= 8)) {
                Point p1 = new Point(R.GetE1M().GetX(), R.GetE1M().GetY());
                int y = (R.GetE2M().GetY() - R.GetE1M().GetY()) / 2 + R.GetE1M().GetY();
                Point p2 = new Point(R.GetE1M().GetX(), y);
                Point p3 = new Point(R.GetE2M().GetX(), y);
                Point p4 = new Point(R.GetE2M().GetX(), R.GetE2M().GetY());
                drawJog(g2, R, p1, p2, p3, p4);

            } else if ((3 <= M1ID && M1ID <= 5) || (9 <= M1ID && M1ID <= 11) &&
                    (3 <= M2ID && M2ID <= 5) || (9 <= M2ID && M2ID <= 11)) {
                Point p1 = new Point(R.GetE1M().GetX(), R.GetE1M().GetY());
                int x = (R.GetE2M().GetX() - R.GetE1M().GetX()) / 2 + R.GetE1M().GetX();
                Point p2 = new Point(x, R.GetE1M().GetY());
                Point p3 = new Point(x, R.GetE2M().GetY());
                Point p4 = new Point(R.GetE2M().GetX(), R.GetE2M().GetY());
                drawJog(g2, R, p1, p2, p3, p4);
            }
            g2.setStroke(new BasicStroke(1));
            g2.setColor(new Color(229, 223, 213));
            //g2.setTransform(saveTransform);
        }

        g2.setTransform(saveTransform);
    }
}
