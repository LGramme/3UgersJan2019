package Domain.GameElements.Entities.Chancecard;

import Domain.GameElements.Board;
import Domain.GameElements.Entities.Player;
import TechnicalServices.GameLogic.GameLogic;
import UI.GUI.GuiHandler;

public class MoveChanceCard extends ChanceCard {
    protected int value;

    /**
     * Constructor. The amount is the number of fields the player traverses.
     * The description is the text presented to the player.
     *
     * @param amount The amount the player is being moved
     * @param description The text being displayed on the ChanceCard
     */
    public MoveChanceCard (int amount, String description){
        super(description);
        value = amount;
    }

    /**
     * This method moves a player by a specific amount and makes sure you get money if you pass the start field.
     *
     * @param player The player drawing the card
     */
    @Override
    public void action(Player player){
        int destination;

        if ((player.getPos() + value) < 0 )
            destination = 40 + (player.getPos() + value);
        else
            destination = (player.getPos() + value)%40;

        if (value >= 0){
            GameLogic.movingPastStart(player, destination);
            player.setPos(destination);
            GuiHandler.getInstance().updatePlayerPos(player, Board.getInstance().getPlayers());
        } else {
            player.setPos(destination);
            GuiHandler.getInstance().movePlayerBackwards(player, Board.getInstance().getPlayers());

        }

        Board.getInstance().getFields()[player.getPos()].landOnAction(player);
    }
}
