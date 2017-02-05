/**
 * Created by bwbecker on 2016-10-10.
 */
public class EntityHoverFrame {
    private int id;
    private int x;
    private int y;
    private int state; //0-unselect; 1-selected; 2-hover

    public EntityHoverFrame(int id, int x, int y, int state){
        this.id = id;
        this.x = x;
        this.y = y;
        this.state = state;
    }

    public void SetPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void SetState(int state){
        this.state = state;
    }

    public int GetId(){
        return this.id;
    }
    public int GetX(){
        return this.x;
    }
    public int GetY(){
        return this.y;
    }
    public int GetState(){
        return this.state;
    }

}
