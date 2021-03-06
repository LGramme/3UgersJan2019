package Domain.GameElements.Entities.Chancecard;

import Domain.GameElements.Entities.Player;
import Domain.GameElements.Fields.Field;
import Domain.GameElements.Fields.Ownable.OwnableField;
import Domain.GameElements.Fields.Ownable.ShippingField;
import UI.GUI.GuiHandler;

public class MoveToNearestChanceCard extends MoveToChanceCard {

    private Class<?> type;
    private Field[] fields;
    private boolean payDouble;

    /**
     * Constructor
     *
     * @param type The class of the field you move to. The first field of this class i selected.
     * @param fields The field array defining the fields on the board.
     * @param description The text displayed when the card is drawn.
     */
    public MoveToNearestChanceCard(Class<?> type, Field[] fields, String description){
        super(0, description);
        this.type = type;
        this.fields = fields;
        payDouble = false;
    }

    /**
     * Constructor
     * @param type The class of the field you move to. The first field of this class i selected.
     * @param fields The field array defining the fields on the board.
     * @param payDouble should the player pay double rent when getting there?
     * @param description The text displayed when the card is drawn.
     */
    public MoveToNearestChanceCard(Class<?> type, Field[] fields, boolean payDouble, String description){
        super(0, description);
        this.type = type;
        this.fields = fields;
        this.payDouble = payDouble;
    }

    /**
     * moves the player to the closest field of a specified class.
     *
     * @param p The player drawing the card.
     */
    public void action(Player p) {
        for (int i = p.getPos(); i < fields.length; i++) {
            if (fields[i].getClass() == type) {
                if (payDouble && ((ShippingField) fields[i]).getOwner() != null && p.getJailTime() < 0)
                    payDouble(p, i);
                super.value = i;
                super.action(p);
                return;
            }
        }



        for (int i = 0; i < p.getPos(); i++) {
            if (fields[i].getClass() == type) {
                payDouble(p, i);
                super.value = i;
                super.action(p);
                return;
            }
        }
        throw new RuntimeException("No Field of the specified type found");
    }

    /**
     * A method for paying double rent instead of only playing normal rent
     *
     * @param p The player drawing the card
     * @param i The location/index of the destination
     */
    private void payDouble(Player p, int i) {
        ShippingField f;
        f = (ShippingField)fields[i];
        p.setPos(i);
        p.getAccount().changeScore(-f.getRent(p));
        f.getOwner().getAccount().changeScore(f.getRent(p));
    }

}
