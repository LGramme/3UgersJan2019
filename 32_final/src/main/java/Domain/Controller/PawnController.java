package Domain.Controller;


import Domain.GameElements.Entities.Player;
import Domain.GameElements.Fields.Ownable.OwnableField;
import Domain.GameElements.Fields.Ownable.PropertyField;
import Domain.GameElements.Board;
import TechnicalServices.GameLogic.GameLogic;
import UI.GUI.GuiHandler;

public class PawnController {

    private GuiHandler guiHandler;
    private Board board;
    private static PawnController instance;

    /**
     * constructor
     *
     */
    public PawnController() {
        guiHandler = GuiHandler.getInstance();
        board = Board.getInstance();
    }

    /**
     * Enables this class to be used as a singleton
     */
    public static PawnController getInstance(){
        if (instance == null) {
            instance = new PawnController();
            return instance;
        }
        else return instance;
    }

    /**
     * Method which lets you pawn off and buy back your properties.
     * @param player The player about to pawn, usually the player who's turn it is
     */
    public void runCase(Player player){
        OwnableField chosenField;
        String[] fieldNames;

        String pawnChoice = guiHandler.makeButtons("Vil du pante en grund eller købe en grund tilbage?","Pante", "Købe tilbage");
        if(pawnChoice.equalsIgnoreCase("Pante")) {
            fieldNames = getUnpawnedTradeFields(player);
            if(fieldNames.length > 0) {
                chosenField = getChosenUnpawnedField(player, fieldNames);
                pawnProperty(chosenField, player);
            }
            else{
                guiHandler.makeButtons("Du har ingen grunde at pante", "Ok");
                return;
            }
        }
        else {
            fieldNames = getPawnedTradeFields(player);
            if(fieldNames.length > 0 && !(fieldNames[0] == null)) {
                chosenField = getChosenPawnedField(player, fieldNames);
                unPawn(chosenField, player);
            } else { guiHandler.makeButtons("Du har ingen grunde, du kan købe tilbage", "Ok");
                return;}
        }

        guiHandler.updateGui(player, board.getPlayers(), board.getFields());
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
                if (fieldString.equals(owner.getOwnedFields().get(n).getName())) {
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
                if (fieldString.equals(owner.getOwnedFields().get(n).getName())) {
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
    private String[] getUnpawnedTradeFields(Player owner) {
        String[] fieldNames;
        int count = 0;
        for(int i= 0; i< owner.getOwnedFields().size(); i++){
            if(!owner.getOwnedFields().get(i).getIsPawned())
                count++;
        }

        fieldNames = new String[count];
        int j = 0;
        for (int n = 0; n < owner.getOwnedFields().size(); n++) {
            if(!owner.getOwnedFields().get(n).getIsPawned())
                fieldNames[j++] = owner.getOwnedFields().get(n).getName();
        }
        return fieldNames;
    }


    /**
     * Creates a list of the fields owned by a player
     * @param owner the player
     * @return list of fields
     */
    private String[] getPawnedTradeFields(Player owner) {
        String[] fieldNames;
        int count = 0;
        for(int i= 0; i< owner.getOwnedFields().size(); i++){
            if(owner.getOwnedFields().get(i).getIsPawned())
                count++;
        }
        fieldNames = new String[count];
        int j = 0;
        for (int n = 0; n < owner.getOwnedFields().size(); n++) {
            if(owner.getOwnedFields().get(n).getIsPawned())
                fieldNames[j++] = owner.getOwnedFields().get(n).getName();
        }
        return fieldNames;
    }

    /**
     * Boolean checking if the field has any hotels or houses
     *
     * @return true if the field contains at least one house/hotel
     */
    private boolean hasBuildings(PropertyField propertyField) {
        if (propertyField.getHouses() == 0 && propertyField.getHotel()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method checks if the field is a property field
     *
     * @return returns true if the field is a PropertyField
     */
    private boolean isPropertyField(OwnableField ownableField) {
        if (ownableField.getClass() == PropertyField.class) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Calculates the amount of money the player gets for pawning a specific field
     *
     * @param ownableField The field having its value calculated
     * @return The pawn vale of the field
     */
    private int pawnValue(OwnableField ownableField) {
        return ownableField.getPrice() / 2;
    }

    /**
     * Method that pawns our properties.
     */
    private void pawnProperty(OwnableField ownableField, Player p) {
        if (isPropertyField(ownableField) && hasBuildings((PropertyField)ownableField)) {
            guiHandler.giveMsg("Du er nødt til at sælge dine bygninger før du kan pante.");
        } else {
            p.getAccount().changeScore(pawnValue(ownableField));
            ownableField.setIsPawned(true);
        }
    }

    /**
     * Calculation of the price for buying back property
     * Cast the double as an int.
     * @return The price
     */
    private int buyPawnBackValue(OwnableField ownableField) {

        return (int) (-pawnValue(ownableField) * 1.1 - ((int) (pawnValue(ownableField) * 1.1) % 50));

    }

    /**
     * Getting boolean from ownableField and allows you to unpawn it.
     */
    private void unPawn(OwnableField ownableField, Player p) {
        if (p.getAccount().canBuy(-buyPawnBackValue(ownableField))) {
               p.getAccount().changeScore(buyPawnBackValue(ownableField));
               ownableField.setIsPawned(false);
        }
        else
            guiHandler.giveMsg("Du har ikke råd til at købe denne grund tilbage");
    }

}

