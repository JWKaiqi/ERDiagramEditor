import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by Jennifer on 2016-10-12.
 */
public class ERModelTest {
    @Test
    public void getLastIndex() throws Exception {
        ERModel m = new ERModel();
        assertEquals(0, m.GetEntityLastIndex());
        assertEquals(0, m.GetRelationLastIndex());

        m.addEntity(0,0);
        m.addEntity(2,2);
        m.addEntity(4,4);
        assertEquals(3, m.GetEntityLastIndex());
        assertEquals(0, m.GetRelationLastIndex());

        m.addRelation(0,1);
        m.addRelation(0,2);
        m.addRelation(1,2);
        m.addRelation(2,1);
        m.addRelation(2,0);
        assertEquals(5, m.GetRelationLastIndex());

        m.addRelation(1,1);
        m.addRelation(0,0);
        assertEquals(5, m.GetRelationLastIndex());

        m.addRelation(0,1);
        m.addRelation(0,2);
        m.addRelation(1,2);
        assertEquals(5, m.GetRelationLastIndex());
    }

    @Test
    public void CheckState() throws Exception{
        ERModel m = new ERModel();
        assertEquals(0, m.GetEntityLastIndex());
        assertEquals(0, m.GetRelationLastIndex());

        m.addEntity(0,0);
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(0));

        m.unselectAll();
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(0));

        m.addEntity(1,1);
        m.addRelation(0,1);
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(0));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(0));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(1));

        m.addEntity(2,2);
        assertEquals(3, m.GetEntityLastIndex());
        assertEquals(1,  m.GetRelationLastIndex());
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(0));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(0));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(1));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(2));

        m.addRelation(2,1);
        assertEquals(2,  m.GetRelationLastIndex());
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(0));
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(1));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(0));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(1));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(2));

        m.addRelation(0,2);
        assertEquals(3,  m.GetRelationLastIndex());
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(0));
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(1));
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(2));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(0));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(1));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(2));

        m.unselectAll();
        assertEquals(3,  m.GetEntityLastIndex());
        assertEquals(3,  m.GetRelationLastIndex());
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(0));
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(1));
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(2));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(0));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(1));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(2));

        m.selectRelation(1);
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(0));
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(1));
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(2));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(0));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(1));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(2));

        m.selectRelation(0);
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(0));
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(1));
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(2));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(0));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(1));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(2));

        m.selectRelation(2);
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(0));
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(1));
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(2));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(0));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(1));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(2));

        m.selectEntity(0);
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(0));
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(1));
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(2));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(0));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(1));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(2));

        m.selectEntity(1);
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(0));
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(1));
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(2));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(0));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(1));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(2));

        m.selectEntity(2);
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(0));
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(1));
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(2));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(0));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(1));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(2));

        m.hoverEntity(2);
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(0));
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(1));
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(2));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(0));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(1));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(2));

        m.hoverEntity(0);
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(0));
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(1));
        assertEquals(ERModel.State.SELECTED,  m.CheckRelationState(2));
        assertEquals(ERModel.State.HOVER,  m.CheckEntityState(0));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(1));
        assertEquals(ERModel.State.SELECTED,  m.CheckEntityState(2));

        m.unhoverEntity(0);
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(0));

        m.unselectAll();
        m.hoverEntity(1);
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(0));
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(1));
        assertEquals(ERModel.State.UNSELECT,  m.CheckRelationState(2));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(0));
        assertEquals(ERModel.State.HOVER,  m.CheckEntityState(1));
        assertEquals(ERModel.State.UNSELECT,  m.CheckEntityState(2));

    }

    @Test
    public void CheckName() throws Exception{
        ERModel m = new ERModel();
        m.addEntity(0,0);
        m.addEntity(1,1);
        m.addEntity(2,2);
        assertEquals("Entity 0",  m.CheckEntityName(0));
        assertEquals("Entity 1",  m.CheckEntityName(1));
        assertEquals("Entity 2",  m.CheckEntityName(2));

        m.addRelation(0,1);
        m.addRelation(2,1);
        m.addRelation(0,2);
        m.addRelation(1,0);
        assertEquals("Entity 0 -> Entity 1",  m.CheckRelationName(0));
        assertEquals("Entity 2 -> Entity 1",  m.CheckRelationName(1));
        assertEquals("Entity 0 -> Entity 2",  m.CheckRelationName(2));
        assertEquals("Entity 1 -> Entity 0",  m.CheckRelationName(3));

        m.setEntityName(0,"E0");
        assertEquals("E0",  m.CheckEntityName(0));
        assertEquals("E0 -> Entity 1",  m.CheckRelationName(0));
        assertEquals("Entity 1 -> E0",  m.CheckRelationName(3));

        m.setEntityName(1,"E1");
        assertEquals("E0 -> E1",  m.CheckRelationName(0));
        assertEquals("Entity 2 -> E1",  m.CheckRelationName(1));
        assertEquals("E0 -> Entity 2",  m.CheckRelationName(2));
        assertEquals("E1 -> E0",  m.CheckRelationName(3));

    }

    @Test
    public void CheckInitHoverFramePosition() throws Exception{
        ERModel m = new ERModel();
        m.addEntity(0,0);
        assertEquals("10,0",m.CheckFrame(0,0));
        assertEquals("20,0",m.CheckFrame(0,1));
        assertEquals("30,0",m.CheckFrame(0,2));
        assertEquals("40,5",m.CheckFrame(0,3));
        assertEquals("40,10",m.CheckFrame(0,4));
        assertEquals("40,15",m.CheckFrame(0,5));
        assertEquals("30,20",m.CheckFrame(0,6));
        assertEquals("20,20",m.CheckFrame(0,7));
        assertEquals("10,20",m.CheckFrame(0,8));
        assertEquals("0,15",m.CheckFrame(0,9));
        assertEquals("0,10",m.CheckFrame(0,10));
        assertEquals("0,5",m.CheckFrame(0,11));

    }

    @Test
    public void MoveEntityAround() throws Exception{
        ERModel m = new ERModel();
        m.addEntity(0,0);
        m.addEntity(1,1);
        m.addRelation(0,1);

        m.moveEntity(0,0,3);
        m.moveEntity(1,1,5);
        assertEquals(0,m.GetEntityX(0));
        assertEquals(3,m.GetEntityY(0));
        assertEquals(1,m.GetEntityX(1));
        assertEquals(5,m.GetEntityY(1));
        assertEquals(true,m.CheckRelationExisted(0,1));
        assertEquals(false,m.CheckRelationExisted(1,0));

        m.moveEntity(1,100,500);
        assertEquals(100,m.GetEntityX(1));
        assertEquals(500,m.GetEntityY(1));
        assertEquals(true,m.CheckRelationExisted(0,1));
        assertEquals(false,m.CheckRelationExisted(1,0));

    }

    @Test
    public void MoveEntityAndCheckEHFPosition() throws Exception{
        ERModel m = new ERModel();
        m.addEntity(0,0);
        m.addEntity(0,0);
        m.moveEntity(0,0,3);
        assertEquals("10,3",m.CheckFrame(0,0));
        assertEquals("20,3",m.CheckFrame(0,1));
        assertEquals("30,3",m.CheckFrame(0,2));
        assertEquals("40,8",m.CheckFrame(0,3));
        assertEquals("40,13",m.CheckFrame(0,4));
        assertEquals("40,18",m.CheckFrame(0,5));
        assertEquals("30,23",m.CheckFrame(0,6));
        assertEquals("20,23",m.CheckFrame(0,7));
        assertEquals("10,23",m.CheckFrame(0,8));
        assertEquals("0,18",m.CheckFrame(0,9));
        assertEquals("0,13",m.CheckFrame(0,10));
        assertEquals("0,8",m.CheckFrame(0,11));

        assertEquals("10,0",m.CheckFrame(1,0));
        assertEquals("20,0",m.CheckFrame(1,1));
        assertEquals("30,0",m.CheckFrame(1,2));
        assertEquals("40,5",m.CheckFrame(1,3));
        assertEquals("40,10",m.CheckFrame(1,4));
        assertEquals("40,15",m.CheckFrame(1,5));
        assertEquals("30,20",m.CheckFrame(1,6));
        assertEquals("20,20",m.CheckFrame(1,7));
        assertEquals("10,20",m.CheckFrame(1,8));
        assertEquals("0,15",m.CheckFrame(1,9));
        assertEquals("0,10",m.CheckFrame(1,10));
        assertEquals("0,5",m.CheckFrame(1,11));

        m.moveEntity(0,10,10);
        assertEquals("20,10",m.CheckFrame(0,0));
        assertEquals("30,10",m.CheckFrame(0,1));
        assertEquals("40,10",m.CheckFrame(0,2));
        assertEquals("50,15",m.CheckFrame(0,3));
        assertEquals("50,20",m.CheckFrame(0,4));
        assertEquals("50,25",m.CheckFrame(0,5));
        assertEquals("40,30",m.CheckFrame(0,6));
        assertEquals("30,30",m.CheckFrame(0,7));
        assertEquals("20,30",m.CheckFrame(0,8));
        assertEquals("10,25",m.CheckFrame(0,9));
        assertEquals("10,20",m.CheckFrame(0,10));
        assertEquals("10,15",m.CheckFrame(0,11));

        m.moveEntity(1,100,200);
        assertEquals("110,200",m.CheckFrame(1,0));
        assertEquals("120,200",m.CheckFrame(1,1));
        assertEquals("130,200",m.CheckFrame(1,2));
        assertEquals("140,205",m.CheckFrame(1,3));
        assertEquals("140,210",m.CheckFrame(1,4));
        assertEquals("140,215",m.CheckFrame(1,5));
        assertEquals("130,220",m.CheckFrame(1,6));
        assertEquals("120,220",m.CheckFrame(1,7));
        assertEquals("110,220",m.CheckFrame(1,8));
        assertEquals("100,215",m.CheckFrame(1,9));
        assertEquals("100,210",m.CheckFrame(1,10));
        assertEquals("100,205",m.CheckFrame(1,11));

        m.addRelation(0,1);
        assertEquals(true,m.CheckRelationExisted(0,1));
    }


}