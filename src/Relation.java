/**
 * Created by bwbecker on 2016-10-10.
 */
public class Relation {
    private int id;
    private ERModel.State state;
    private Entity E1;
    private Entity E2;
    public EntityHoverFrame E1M;
    public EntityHoverFrame E2M;

    public Relation(int id, ERModel.State state, Entity E1, Entity E2, EntityHoverFrame E1M, EntityHoverFrame E2M){
        this.id = id;
        this.state = state;
        this.E1 = E1;
        this.E2 = E2;
        this.E1M = E1M;
        this.E2M = E2M;
    }

    public void SetRelation(Entity E1, Entity E2, EntityHoverFrame E1M, EntityHoverFrame E2M){
        this.E1 = E1;
        this.E2 = E2;
        this.E1M = E1M;
        this.E2M = E2M;
    }

    public void SetState(ERModel.State state){
        this.state = state;
    }
    
    public int GetId(){
        return this.id;
    }
    public Entity GetE1(){
        return this.E1;
    }
    public Entity GetE2(){
        return this.E2;
    }
    public EntityHoverFrame GetE1M(){
        return this.E1M;
    }
    public EntityHoverFrame GetE2M(){
        return this.E2M;
    }

    public ERModel.State GetState(){
        return this.state;
    }
}
