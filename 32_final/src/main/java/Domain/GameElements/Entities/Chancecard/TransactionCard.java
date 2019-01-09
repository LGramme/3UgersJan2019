package Domain.GameElements.Entities.Chancecard;
import Domain.GameElements.Entities.Player;

public class TransactionCard extends ChanceCard {

    private int amount;

    /**
     * Constructor.
     * @param amount
     */
    public TransactionCard (int amount){
        this.amount = amount;
    }

    /**
     * Determines that 'action' is used to change the balance of players
     * @param p
     */
    @Override
    public void action (Player p){
        p.getAccount().changeScore(amount);
    }



}
