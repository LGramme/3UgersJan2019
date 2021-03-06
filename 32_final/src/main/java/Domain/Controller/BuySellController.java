package Domain.Controller;

import Domain.GameElements.Board;
import Domain.GameElements.Entities.Player;
import Domain.GameElements.Fields.Ownable.OwnableField;
import Domain.GameElements.Fields.Ownable.PropertyField;
import TechnicalServices.GameLogic.Values;
import UI.GUI.GuiHandler;


import java.awt.*;

public class BuySellController {
    private GuiHandler guiHandler = GuiHandler.getInstance();
    private Board board;

    private static BuySellController instance;

    /**
     * Making the BuySellController a singleton
     *
     * @return
     */
    public static BuySellController getInstance() {
        if (instance == null) {
            instance = new BuySellController();
            return instance;
        } else {
            return instance;
        }
    }

    /**
     * Constructor
     */
    private BuySellController() {
        board = Board.getInstance();
    }

    /**
     * The method running the use case.
     * @param player The player who is buying or selling houses.
     */
    public void runCase(Player player) {

        String buttons = guiHandler.makeButtons("Vil du købe eller sælge huse/hoteller?", "Køb", "Sælg");

        if (buttons.equalsIgnoreCase("Køb")) {
            buy(player);
        } else {
            sell(player);
        }


        guiHandler.updateGui(player, board.getPlayers(), board.getFields());
    }

    /**
     * The method handeling the selling part of this use case
     * @param player The player selling
     */
    private void sell(Player player) {
        String[] possibleFields;
        String choice;
        PropertyField chosenField;

        //counts the number of fields the player owns which have houses on them
        int count = 0;
        for (OwnableField field : player.getOwnedFields()){
            if (field.getClass().equals(PropertyField.class))
                if (((PropertyField)field).getHouses() > 0)
                    count++;
        }

        //ads all the owned fields' names to the possibleFields array
        possibleFields = new String[count];
        int j = 0;
        for (int i = 0; i < player.getOwnedFields().size(); i++){
            OwnableField field = player.getOwnedFields().get(i);
            if (field.getClass().equals(PropertyField.class))
                if (((PropertyField)field).getHouses() > 0)
                    possibleFields[j] = field.getName();
        }

        //checks if any fields were found
        if (possibleFields.length < 1){
            guiHandler.giveMsg("Du har ingen grunde med huse på.");

        } else {
            //presents the player with their choices. String manipulation takes place for the sake of user experience.
            choice = guiHandler.makeButtons("Vælg en grund du vil sælge et hus fra. Antallet af huse på feltet " +
                    "står i parantes.", possibleFields);

            //Finds the chosen field and assigns it to chosenField
            chosenField = null;
            for (OwnableField field : player.getOwnedFields()){
                if (field.getName().equalsIgnoreCase(choice)) {
                    chosenField = (PropertyField) field;
                    break;
                }
            }

            if (chosenField != null) {
                player.getAccount().changeScore(Values.housePrice(chosenField.getBgColor()) / 2);
                chosenField.removeHouse(1);
            } else {
                throw new NullPointerException("the chosen field was not found");
            }
        }


    }

    /**
     * The method for buying houses
     *
     * @param player The player buying
     */
    private void buy(Player player){
        Color[] triedColors = new Color[0];
        String[] ownableFields = new String[0];
        PropertyField chosenField;


        for (OwnableField ownedField : player.getOwnedFields()) {

            //checks if color has already been checked
            boolean isChecked = false;
            for (Color triedColor : triedColors) {
                if (ownedField.getBgColor().equals(triedColor)) {
                    isChecked = true;
                    break;
                }
            }

            if (!isChecked) {
                triedColors = colorArrayAddition(triedColors, new Color[]{ownedField.getBgColor()});


                boolean sameOwner = true;
                for (OwnableField colorField : ownedField.getFieldsOfColor()) {
                    if (colorField.getOwner() != player) {
                        sameOwner = false;
                        break;
                    }
                }

                if (sameOwner) {
                    //if you are here the player owns all fields of that color (for the specified type)

                    //fill the array with all the names of the fields of one color
                    String[] colorFields = new String[ownedField.getFieldsOfColor().length];

                    for (int i = 0; i < ownedField.getFieldsOfColor().length; i++) {
                        colorFields[i] = ownedField.getFieldsOfColor()[i].getName();
                    }

                    try {
                        colorFields = validateFields(player, colorFields);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        return;
                    }

                    //add the array of newly found field names to the full list of fields able to get houses
                    ownableFields = stringArrayAddition(ownableFields, colorFields);
                }
            }
        }

        if (ownableFields.length <= 0) {
            guiHandler.giveMsg("Du har ingen grunde du kan købe huse på.");
        } else {
            chosenField = getChosenField(player, ownableFields);
            if (chosenField == null){
                guiHandler.giveMsg("Noget gik galt :(");
            } else {
                //Checks if the player wants to by the house
                if (guiHandler.makeButtons("Vil du bygge et hus/hotel på " + chosenField.getName() + " for kr. " +
                                Values.housePrice(chosenField.getBgColor()) + "?",
                        "Ja", "Nej").equalsIgnoreCase("Ja")) {
                    if (player.getAccount().canBuy(-Values.housePrice(chosenField.getBgColor()))) {
                        player.getAccount().changeScore(-Values.housePrice(chosenField.getBgColor()));
                        chosenField.addHouse();
                    } else {
                        guiHandler.giveMsg("Du har ikke råd til at bygge dette hus/hotel");
                    }
                }
            }
        }

    }

    /**
     * A method for checking that all rules applying to a field to buy a house are upheld.
     * 
     * @param buyer The buyer of the field.
     * @param fieldNames The array to be checked.
     * @return Only the fields that live up to all rules are returned in an array.
     */
    private String[] validateFields(Player buyer, String[] fieldNames) {
        String[] validatedFields;

        int MAX_HOUSES_IN_PLAY = 32;
        int MAX_HOTELS_IN_PLAY = 12;
        if (PropertyField.getHotelsInPlay() >= MAX_HOTELS_IN_PLAY && PropertyField.getHousesInPlay() >= MAX_HOUSES_IN_PLAY) {
            throw new RuntimeException("Både hoteller og huse er udsolgt");
        }

        //checks how many fields live up to all rules
        boolean sentMessage = false;
        int count = 0;
        PropertyField currentField;
        for (String field : fieldNames) {
            currentField = stringToPropertyField(field, buyer);
            if (currentField != null) {
                //checks that a field has no more than 5 houses / a hotel
                if (currentField.getHouses() < 5 && !currentField.getHotel()) {

                    //Checks if any fields of that color has less houses than this one
                    boolean hasFewest = true;
                    for (String otherField : fieldNames) {
                        if (currentField.getHouses() > stringToPropertyField(otherField, buyer).getHouses()) {
                            hasFewest = false;
                            break;
                        }
                    }

                    if (hasFewest) {
                        //checks if there are any houses or hotels left to build
                        if (currentField.getHouses() < 5) {
                            if (PropertyField.getHousesInPlay() <= MAX_HOUSES_IN_PLAY) {
                                count++;
                            } else if (!sentMessage) {
                                guiHandler.giveMsg("Huse er udsolgt.");
                                sentMessage = true;
                            }

                        } else if (currentField.getHouses() == 5) {
                            if (PropertyField.getHotelsInPlay() <= MAX_HOTELS_IN_PLAY) {
                                count++;
                            } else if (!sentMessage) {
                                guiHandler.giveMsg("Huse er udsolgt.");
                                sentMessage = true;
                            }
                        }
                    }
                }
            }
        }

        //puts validated fields in array
        validatedFields = new String[count];
        int j = 0;
        for (String field : fieldNames) {
            currentField = stringToPropertyField(field, buyer);
            //checks if it has less than max houses and doesn't have a hotel
            if (currentField != null) {
                if (currentField.getHouses() < 5 && !currentField.getHotel()) {
                    //checks if any fields of the same color has fewer fields than this one
                    boolean hasFewest = true;
                    for (String otherField : fieldNames) {
                        if (currentField.getHouses() > stringToPropertyField(otherField, buyer).getHouses()) {
                            hasFewest = false;
                            break;
                        }
                    }

                    if (hasFewest) {
                        //checks if there are any houses or hotels left depending on what you are about to buy
                        if (currentField.getHouses() < 5 && PropertyField.getHousesInPlay() <= MAX_HOUSES_IN_PLAY) {
                            validatedFields[j++] = field;
                        } else if (currentField.getHouses() == 5 && PropertyField.getHotelsInPlay() <= MAX_HOTELS_IN_PLAY) {
                            validatedFields[j++] = field;
                        }
                    }
                }
            }
        }

        return validatedFields;
    }

    private PropertyField stringToPropertyField(String fieldString, Player owner){
        for (int n = 0; n < owner.getOwnedFields().size(); n++) {
            if (fieldString.equalsIgnoreCase(owner.getOwnedFields().get(n).getName())) {
                if (owner.getOwnedFields().get(n).getClass().equals(PropertyField.class))
                    return (PropertyField) owner.getOwnedFields().get(n);
            }
        }
        return null;
    }

    private PropertyField getChosenField(Player owner, String[] fieldNames) {
        //Select a field to trade based on user input
        if (fieldNames.length > 0) {
            String fieldString = guiHandler.makeButtons("Vælg felt du vil købe/sælge huse på", fieldNames);
            return stringToPropertyField(fieldString, owner);
        } else {
            return null;
        }

    }

    /**
     * A method for adding string arrays together
     * @param arr1 A String array
     * @param arr2 A String array
     * @return An array of all the elements of both arr1 and arr2
     */
    private String[] stringArrayAddition(String[] arr1, String[] arr2){
        String[] temp = new String[arr1.length + arr2.length];

        for (int i = 0; i < arr1.length; i++) {
            temp[i] = arr1[i];
        }

        for (int i = 0; i < arr2.length; i++) {
            temp[arr1.length + i] = arr2[i];
        }
        return temp;
    }

    /**
     * A method for adding color arrays together
     * @param arr1 A Color array
     * @param arr2 A Color array
     * @return An array of all the elements of both arr1 and arr2
     */
    private Color[] colorArrayAddition(Color[] arr1, Color[] arr2){
        Color[] temp = new Color[arr1.length + arr2.length];

        for (int i = 0; i < arr1.length; i++) {
            temp[i] = arr1[i];
        }

        for (int i = 0; i < arr2.length; i++) {
            temp[arr1.length + i] = arr2[i];
        }
        return temp;
    }
}