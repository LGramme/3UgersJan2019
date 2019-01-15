package Domain.Controller;


import Domain.GameElements.Entities.Chancecard.MoveToNearestChanceCard;
import Domain.GameElements.Entities.Player;
import Domain.GameElements.Fields.Ownable.OwnableField;
import Domain.GameElements.Fields.Ownable.PropertyField;
import Domain.GameElements.Fields.Field;
import Domain.GameElements.Entities.Account;
import Domain.GameElements.Board;
import UI.GUI.GuiHandler;

public class PawnController {

    private boolean fieldIsPropertyField;
    private PropertyField propertyField;
    private GuiHandler guiHandler;
    private Account account = new Account();
    private Board board;


    /**
     * constructor
     *
     */
    public PawnController() {
        guiHandler = GuiHandler.getInstance();
        board = Board.getInstance();
    }

    /**
     *
     * @param player
     */
    public void runCase(Player player){
        OwnableField chosenField;
        String[] fieldNames;

        String pawnChoice = guiHandler.makeButtons("VIl du pante en grund eller købe en grund tilbage?","Pante", "Købe tilbage");
        if(pawnChoice.equalsIgnoreCase("Pante")) {
            fieldNames = getTradeFields(player);
            chosenField = getChosenUnpawnedField(player, fieldNames);
            if (chosenField == null) {
                guiHandler.makeButtons("Du har ingen grunde at pante", "Ok");
                return;
            }

            pawnProperty(chosenField);
        }
        else {
            fieldNames = getTradeFields(player);
            chosenField = getChosenPawnedField(player, fieldNames);
            if (chosenField == null) {
                guiHandler.makeButtons("Du har ingen grunde, du kan købe tilbage", "Ok");
                return;
            }
            unPawn(chosenField);
        }

        guiHandler.updateBalance(board.getPlayers());
    }
    /**
     * Creates a button for each ownableField which hasn't been pawned and returns the field chosen by the user
     *
     * @param owner      The player who's fields can be chosen
     * @param fieldNames An array of all the possible fields to trade
     * @return the chosen field
     */
    private OwnableField getChosenUnpawnedField(Player owner, String[] fieldNames) {
        //Select a field to trade based on user input
        if (fieldNames.length > 0) {
            String fieldString = guiHandler.makeButtons("Vælg et felt at pante", fieldNames);
            for (int n = 0; n < owner.getOwnedFields().size(); n++) {
                if (fieldString.equals(fieldNames[n]) && !owner.getOwnedFields().get(n).getIsPawned()) {
                    return owner.getOwnedFields().get(n);
                }
            }
        } else {
            return null;
        }
        throw new RuntimeException("getChosenField() returned no value");
    }
    /**
     * Creates a button for each ownableField which has been pawned and returns the field chosen by the user
     *
     * @param owner      The player who's fields can be chosen
     * @param fieldNames An array of all the possible fields to trade
     * @return the chosen field
     */
    private OwnableField getChosenPawnedField(Player owner, String[] fieldNames) {
        //Select a field to trade based on user input
        if (fieldNames.length > 0) {
            String fieldString = guiHandler.makeButtons("Vælg et felt at købe tilbage", fieldNames);
            for (int n = 0; n < owner.getOwnedFields().size(); n++) {
                if (fieldString.equals(fieldNames[n]) && owner.getOwnedFields().get(n).getIsPawned()) {
                    return owner.getOwnedFields().get(n);
                }
            }
        } else {
            return null;
        }
        throw new RuntimeException("getChosenField() returned no value");
    }

    /**
     * Creates a list of the fields owned by a player
     * @param owner the player
     * @return list of fields
     */
    private String[] getTradeFields(Player owner) {
        String[] fieldNames = new String[owner.getOwnedFields().size()];
        for (int n = 0; n < owner.getOwnedFields().size(); n++) {
            fieldNames[n] = owner.getOwnedFields().get(n).getName();
        }
        return fieldNames;
    }
    /**
     * Boolean checking if the field has any hotels or houses
     *
     * @return
     */
    private boolean hasBuildings() {
        if (propertyField.getHouses() == 0 && propertyField.getHotel()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method checks if the field is a property field
     *
     * @return
     */
    private boolean isPropertyField(OwnableField ownableField) {
        if (ownableField.getClass() == PropertyField.class) {
            fieldIsPropertyField = true;
        } else {
            fieldIsPropertyField = false;
        }
        return fieldIsPropertyField;
    }

    private int pawnValue(OwnableField ownableField) {
        return ownableField.getPrice() / 2;

    }

    /**
     * Method that pawn our properties.
     */
    private void pawnProperty(OwnableField ownableField) {
        int buildingsWorth;
        buildingsWorth = ownableField.getWorth() - ownableField.getPrice();

        if (!hasBuildings() && isPropertyField(ownableField)) {
            int numberOfHouses = ownableField.getHouses();
            ownableField.removeHouse(numberOfHouses);
            account.changeScore(buildingsWorth);
        } else {
            account.changeScore(pawnValue(ownableField));
        }
            ownableField.setIsPawned(true);
    }

    /**
     * Multiplication of
     * Cast the double as an int.
     */
    private void buyPawnBack(OwnableField ownableField) {

        account.changeScore((int) (-pawnValue(ownableField) * 1.1 - ((int) (pawnValue(ownableField) * 1.1) % 50)));

    }

    /**
     * Getting boolean from ownableField and allows you to unpawn it.
     */
    private void unPawn(OwnableField ownableField) {
        if (ownableField.getIsPawned()) {
            buyPawnBack(ownableField);
            ownableField.setIsPawned(false);
        }

    }

}
