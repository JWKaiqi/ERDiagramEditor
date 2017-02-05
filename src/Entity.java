/**
 * Created by bwbecker on 2016-10-10.
 */
public class Entity {
    public int id;
    public String name;
    public int x;
    public int y;
    public int height;
    public int width;
    public ERModel.State state; //0-unselect; 1-selected; 2-hover

    public EntityHoverFrame hoverFrames[] = new EntityHoverFrame[12];
    //Note: if state is 1, and the mouse double checks the entity again,
    //then SetName
    public Entity(){
    }

    public Entity(int id, String name, int x, int y, int height, int width, ERModel.State state){
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.state = state;
        int w = this.width/4;
        int h = this.height/4;
        int j = 1;
        for(int i = 0; i < 3; i++){
            this.hoverFrames[i] = new EntityHoverFrame(i,this.x+j*w,this.y,0);
            j++;
        }
        j = 1;
        for(int i = 3; i < 6; i++){
            this.hoverFrames[i] = new EntityHoverFrame(i, this.x + this.width, this.y+j*h, 0);
            j++;
        }
        j = 1;
        for(int i = 6; i < 9; i++){
            this.hoverFrames[i] = new EntityHoverFrame(i, this.x + this.width - j * w, this.y + this.height, 0);
            j++;
        }
        j = 1;
        for(int i = 9; i < 12; i++){
            this.hoverFrames[i] = new EntityHoverFrame(i, this.x, this.y + this.height - j * h, 0);
            j++;

        }
    }

    public void SetEHF(int x, int y){
        int w = this.width/4;
        int h = this.height/4;
        int j = 1;
        for(int i = 0; i < 3; i++){
            this.hoverFrames[i].SetPosition(x+j*w,y);
            j++;
        }
        j = 1;
        for(int i = 3; i < 6; i++){
            this.hoverFrames[i].SetPosition(x+this.width, y+j*h);
            j++;
        }
        j = 1;
        for(int i = 6; i < 9; i++){

            this.hoverFrames[i].SetPosition(x + this.width - j * w, y + this.height);
            j++;
        }
        j = 1;
        for(int i = 9; i < 12; i++){
            this.hoverFrames[i].SetPosition(x, y + this.height - j * h);
            j++;
        }

    }

    public void SetName(String name){
        this.name = name;
    }

    public void SetPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void SetState(ERModel.State state){
        this.state = state;
    }

    public void SetW(int w){
        this.width = w;
    }
    public void SetH(int h){this.height = h;}

    public int GetId(){
        return this.id;
    }
    public int GetX(){
        return this.x;
    }
    public int GetY(){
        return this.y;
    }
    public int GetW(){
        return this.width;
    }
    public int GetH(){
        return this.height;
    }
    public ERModel.State GetState(){
        return this.state;
    }

    public EntityHoverFrame GetEHF(int id){
        return hoverFrames[id];
    }

    public String GetName(){
        return this.name;
    }

    public void SetUnselect_HoverFrame(int id){
        this.hoverFrames[id].SetState(0);
    }

    public void SetSelect_HoverFrame(int id){
        for(int i = 0; i < 12; i++){
           this.hoverFrames[i].SetState(0);
        }
        this.hoverFrames[id].SetState(1);
    }

    public void SetHover_HoverFrame(int id){
        int state =  this.hoverFrames[id].GetState();
        for(int i = 0; i < 12; i++){
            if(this.hoverFrames[i].GetState() != 1){
                this.hoverFrames[i].SetState(0);
            }
        }
        if(this.hoverFrames[id].GetState() != 1) {
            this.hoverFrames[id].SetState(2);
        }
    }

    public int GetSelectedMagnetId(){
        int index = -1;
        for(int i = 0; i < 12; i++){
            if(this.hoverFrames[i].GetState() == 1){
                index = i;
                break;
            }
        }
        return index;
    }
}
