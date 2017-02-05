import java.util.ArrayList;
import java.util.Iterator;
/*Note that: Assume that only one entity can be selected at a time */
// View interface
interface IView {
    public void updateView();
}

public class ERModel {

    private int EntityLastIndex = 0;
    private int RelationLastIndex = 0;

    public ArrayList<Entity> EntityList;
    public ArrayList<Relation> RelationList;
    public ArrayList<IView> Views;

    public boolean selectedMag = false;

    public enum State{
        UNSELECT,
        SELECTED,
        HOVER
    };

    public ERModel() {
        this.EntityLastIndex = 0;
        this.RelationLastIndex = 0;
        this.EntityList = new ArrayList<Entity>();
        this.RelationList = new ArrayList<Relation>();
        this.Views = new ArrayList<IView>();
    }

    /*========For Testing=======*/
    public int GetEntityLastIndex(){
        return this.EntityLastIndex;
    }

    public int GetRelationLastIndex(){
        return this.RelationLastIndex;
    }


    public State CheckEntityState(int eid){
        State s = State.UNSELECT;
        for(Entity e: EntityList){
            if(e.GetId() == eid){
                s = e.GetState();
                break;
            }
        }
        return s;
    }

    public State CheckRelationState(int rid){
        State s = State.UNSELECT;
        for(Relation r: RelationList){
            if(r.GetId() == rid){
                s = r.GetState();
                break;
            }
        }
        return s;
    }

    public boolean CheckRelationExisted(int E1, int E2){
        boolean exist = false;
        for(Relation r: RelationList){
            if(r.GetE1().GetId() == E1 && r.GetE2().GetId() == E2){
                exist = true;
                break;
            }
        }
        return exist;
    }

    public boolean CheckAllEntitiesAreUnselect(){
        boolean Unselect = true;
        for(Entity e: EntityList){
            if(e.GetState() != State.UNSELECT){
                Unselect = false;
                break;
            }
        }
        return Unselect;
    }

    public boolean CheckAllArrowsAreUnselect(){
        boolean Unselect = true;
        for(Relation r: RelationList){
            if(r.GetState() != State.UNSELECT){
                Unselect = false;
                break;
            }
        }
        return Unselect;
    }

    public int GetEntityX(int eid){
        int x = 0;
        for(Entity e: EntityList){
            if(e.GetId() == eid){
                x = e.GetX();
                break;
            }
        }
        return x;
    }

    public int GetEntityY(int eid){
        int y = 0;
        for(Entity e: EntityList){
            if(e.GetId() == eid){
                y = e.GetY();
                break;
            }
        }
        return y;
    }

    public String CheckEntityName(int eid){
        String Name = "";
        for(Entity e: EntityList){
            if(e.GetId() == eid){
                Name = e.GetName();
                break;
            }
        }
        return Name;
    }

    public String CheckRelationName(int rid){
        String Name = "";
        for(Relation r: RelationList){
            if(r.GetId() == rid){
                Name = r.GetE1().GetName() + " -> " + r.GetE2().GetName();
                break;
            }
        }
        return Name;
    }

    public String CheckFrame(int eid, int hid){
        int x = 0;
        int y = 0;
        for(Entity e: EntityList){
            if(e.GetId() == eid){
                x = e.GetEHF(hid).GetX();
                y = e.GetEHF(hid).GetY();
                break;
            }
        }
        String r = Integer.toString(x) + "," + Integer.toString(y);
        return r;
    }

    /*============End===========*/
    public void addView(IView view){
        Views.add(view);
        // update the view to current state of the model
        view.updateView();
    }

    public void addEntity(int i, int x, int y, int EW, int EH){
        unselectAll();
        String name = "Entity " + i;
        Entity e = new Entity(i, name, x, y, 80,160,State.SELECTED);
        e.width = EW;
        e.height = EH;
        e.SetEHF(e.x,e.y);
        EntityLastIndex = i;
        EntityList.add(e);
        notifyObservers();
        //Notify View
    }

    public int CountEntityList(){
        int count = 0;
        for(Entity E: EntityList){
            count++;
        }
        return count;
    }


 public void deleteSelectedEntity() {
     Iterator<Entity> aIter = this.EntityList.iterator();
     while (aIter.hasNext()) {
         Entity e = aIter.next();
         if (e.state == State.SELECTED) {
             int eid = e.id;
             Iterator<Relation> Iter = this.RelationList.iterator();
             while (Iter.hasNext()){
                 Relation r = Iter.next();
                 if(r.GetE1().id == eid || r.GetE2().id == eid){
                     Iter.remove();
                 }
             }
             aIter.remove();
         }
     }
     notifyObservers();
 }

 public void deleteSelectedRelation(){
     Iterator<Relation> Iter = this.RelationList.iterator();
     while (Iter.hasNext()){
         Relation r = Iter.next();
         if(r.GetState() == State.SELECTED){
             Iter.remove();
         }
     }
     notifyObservers();
 }


    public Entity GetEntity(int eid){
        Entity E = new Entity();
        for(Entity e: EntityList){
            if(e.GetId() == eid){
                E = e;
                break;
            }
        }
        return E;
    }

    public void addRelation(Entity E1, Entity E2, EntityHoverFrame E1M, EntityHoverFrame E2M) {
        boolean Exist = false;
        if (E1.GetId() != E2.GetId()) {
            for (Relation r : RelationList) {
                //Check if this relation has already exist
                if (r.GetE1().GetId() == E1.GetId() && r.GetE2().GetId() == E2.GetId()) {
                    Exist = true;
                    selectRelation(r.GetId());
                    break;
                    //Notify View that this relation has already exist!
                }
            }
            if (!Exist) {
                Relation r = new Relation(RelationLastIndex,State.SELECTED,E1,E2,E1M,E2M);
                RelationList.add(r);
                selectRelation(RelationLastIndex);
                RelationLastIndex++;
                //Notify View
                notifyObservers();
            }
        }
    }

    public void selectEntity(int eid){
        for(Entity e: EntityList){
            if(e.GetId() == eid){
                e.SetState(State.SELECTED);
            }
            else{
                e.SetState(State.UNSELECT); //set other entities as unselected
            }
        }
        //any relation arrows connected to this entity
        //should also be selected
        for(Relation r: RelationList){
            if(r.GetE1().GetId() == eid || r.GetE2().GetId() == eid){
                r.SetState(State.SELECTED);
            }
            else{ //unselect everything else
                r.SetState(State.UNSELECT);
            }
        }
        //should Notify View Right here
         notifyObservers();
    }

    public void selectRelation(int rid){
        for(Relation r: RelationList){
            if(r.GetId() == rid){
                r.SetState(State.SELECTED);
                for(Entity e: EntityList){
                    e.SetState(State.UNSELECT);
                }
                r.GetE1().SetState(State.SELECTED);
                r.GetE2().SetState(State.SELECTED);
            }
            else{
                r.SetState(State.UNSELECT);
            }
        }
         notifyObservers();
    }

    /*Unselect all entities and relations*/
    public void unselectAll(){
        if(EntityList.size() > 0){
            for(Entity e: EntityList){
                e.SetState(State.UNSELECT);
                for(int i = 0; i < 12; i++){
                    e.hoverFrames[i].SetState(0);
                }
            }
        }

        if(RelationList.size() > 0) {
            for (Relation r : RelationList) {
                r.SetState(State.UNSELECT);
            }
        }
         notifyObservers();
    }

    public void NoSelectEntity(){
        for(Entity e: EntityList){
            if(e.GetState() == State.SELECTED){
                e.SetState(State.UNSELECT);
                for(int i = 0; i < 12; i++){
                    e.hoverFrames[i].SetState(0);
                }
            }
        }
        notifyObservers();
    }


    public void hoverEntity(int eid){
        for(Entity e: EntityList){
            if(e.GetId() == eid){
                if(e.GetState() == State.UNSELECT){
                    e.SetState(State.HOVER);
                    break;
                }
            }
        }
        notifyObservers();
    }

    public void unhoverEntity(int eid){
        for(Entity e: EntityList){
            if(e.GetId() == eid){
                if(e.GetState() == State.HOVER){
                    e.SetState(State.UNSELECT);
                      for(int i = 0; i < 12; i++){
                        e.hoverFrames[i].SetState(0);
                      }
                    break;
                }

            }
        }
        notifyObservers();
    }

    public void moveEntity(int eid, int newx, int newy, int cursorX, int cursorY,
                           int PanelWidth, int PanelHeight){
        selectEntity(eid);
        for(Entity e: EntityList){
            if(e.GetId() == eid) {
                int X = newx - cursorX;
                int Y = newy - cursorY;
                if(!OutOfBound(X,Y, e.GetW(), e.GetH(),PanelWidth,PanelHeight)){
                    e.SetPosition(X, Y);
                    e.SetEHF(X, Y);
                    UpdateSelectedMagnet(e);
                }
                break;
            }
        }
        notifyObservers();
    }

    public void FindMinDistanceAndUpdate(Relation R, Entity E1, Entity E2, double CurDistance, int E1M, int E2M){
        double MinSoFar = CurDistance;
        int M1 = E1M;
        int M2  = E2M;
        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 12; j++){
                int x1 = E1.hoverFrames[i].GetX();
                int y1 = E1.hoverFrames[i].GetY();
                int x2 = E2.hoverFrames[j].GetX();
                int y2 = E2.hoverFrames[j].GetY();
                double Distance = Math.sqrt( Math.pow((x2-x1), 2) +  Math.pow((y2-y1), 2));
                if(Distance < MinSoFar){
                    MinSoFar = Distance;
                    M1 = i;
                    M2 = j;
                }
            }
        }
        EntityHoverFrame EM1 = E1.hoverFrames[M1];
        EntityHoverFrame EM2 = E2.hoverFrames[M2];
        R.E1M = EM1;
        R.E2M = EM2;
    }


    public void UpdateSelectedMagnet(Entity e){
            for(Relation r: RelationList){
                if(r.GetE1().GetId() == e.GetId() || r.GetE2().GetId() == e.GetId()){
                    Entity E1 = r.GetE1();
                    Entity E2 = r.GetE2();
                    int x1 = r.GetE1M().GetX();
                    int y1 = r.GetE1M().GetY();
                    int x2 = r.GetE2M().GetX();
                    int y2 = r.GetE2M().GetY();
                    double CurDistance = Math.sqrt( Math.pow((x2-x1), 2) +  Math.pow((y2-y1), 2));
                    FindMinDistanceAndUpdate(r,E1,E2,CurDistance,r.GetE1M().GetId(), r.GetE2M().GetId());
                }
            }

    }

    public boolean OutOfBound(int x, int y, int ew, int eh,int pw, int ph){
        boolean OutOfBound = false;
        int W  = pw;
        int H = ph;
        if(x < 0 || y < 0 || x+ew > W || y + eh > H){
            OutOfBound = true;
        }
        return OutOfBound;
    }

    public void setEntityName(int eid, String name){
        for(Entity e: EntityList){
            if(e.GetId() == eid){
                e.SetName(name);
                //Controller should notify View to update
                // the name of entity and relations.
                notifyObservers();
                break;
            }
        }
    }

    public void setEHMState(int eid, int id, int state){
        for(Entity e: EntityList){
            if(e.GetId() == eid){
                if(state == 0){
                    e.SetUnselect_HoverFrame(id);
                }
                else if(state == 1){
                    e.SetSelect_HoverFrame(id);
                }
                else if(state == 2){
                    e.SetHover_HoverFrame(id);
                }
                break;
            }
        }
        notifyObservers();
    }

    public void EntityResize(int eid, int newX, int newY, int newW, int newH){
        for (Entity e: EntityList){
            if(e.GetId() == eid){
                e.width = newW;
                e.height = newH;
                e.x = newX;
                e.y = newY;
                e.SetEHF(e.x,e.y);
                break;
            }
        }
        notifyObservers();
    }

    // notify the IView observer
    // 0 - AddEntity; 1 - AddRelation
    public void notifyObservers(){
        for (IView view : this.Views) {
            view.updateView();
        }
    }
}