/*
 * All GuiHandler related classes are kept here.
 */
package UI.GUI;


import Domain.GameElements.Board;
import Domain.GameElements.Entities.Player;
import Domain.GameElements.Fields.*;
import Domain.GameElements.Fields.Ownable.*;
import Domain.GameElements.Fields.ChanceField.*;
import Domain.GameElements.Fields.Ownable.PropertyField;
import gui_codebehind.GUI_BoardController;
import gui_fields.*;
import gui_main.GUI;
import sun.applet.Main;


import java.awt.*;



public class GuiHandler {
    private GUI gui;
    private GUI_Field[] gui_fields = new GUI_Field[40];
    private GUI_Player[] guiPlayers;
    private GUI_BoardController bc;


    private static GuiHandler guiHandlerInstance;

    /**
     * returns the instance of the GuiHandler
     * this method ensures that all classes can access the same GuiHandler instance (singleton)
     *
     * @return
     */
    public static GuiHandler getInstance() {
        if (guiHandlerInstance == null) {
            guiHandlerInstance = new GuiHandler();
            return guiHandlerInstance;
        } else
            return guiHandlerInstance;
    }

    /**
     * creates the gui_fields
     *
     * @param fields the fields in the board class
     */
    public void initGuiFields(Domain.GameElements.Fields.Field[] fields) throws IllegalStateException {
        for (int i = 0; i < gui_fields.length; i++) {
            if (fields[i].getClass().equals(EmptyField.class) && i == 0) {
                gui_fields[i] = (new GUI_Start(fields[i].getName(), fields[i].getSubtext(), "", fields[i].getBgColor(), null));

            } else if (fields[i].getClass().equals(PropertyField.class)) {
                PropertyField propertyField = (PropertyField) fields[i];
                gui_fields[i] = (new GUI_Street(fields[i].getName(), fields[i].getSubtext(), "", Integer.toString(propertyField.getPrice()), fields[i].getBgColor(), null));

            } else if (fields[i].getClass().equals(EmptyField.class)) {
                if(fields[i].getName().equals("På besøg\nI fængsel")){
                    gui_fields[i] = (new GUI_Jail("classes\\Resources\\jail.jpg", fields[i].getName(), fields[i].getSubtext(), "", fields[i].getBgColor(), Color.WHITE));
                }else if(fields[i].getName().equals("Gratis Parkering")){
                    gui_fields[i] = (new GUI_Refuge("classes\\Resources\\parking.jpg",fields[i].getName(), fields[i].getSubtext(), "De er beruset, parkér bilen til næste tur.", fields[i].getBgColor(), null));//This one be causing trouble
                }else {
                    gui_fields[i] = (new GUI_Street(fields[i].getName(), fields[i].getSubtext(), "", "0", fields[i].getBgColor(), null));//This one be causing trouble
                }

            } else if (fields[i].getClass().equals(ChanceField.class)) {
                gui_fields[i] = (new GUI_Chance(fields[i].getName(), fields[i].getSubtext(), "", fields[i].getBgColor(), Color.white));

            } else if (fields[i].getClass().equals(TaxField.class)) {
                gui_fields[i] = new GUI_Tax(fields[i].getName(), fields[i].getSubtext(), "", fields[i].getBgColor(), null);

            } else if (fields[i].getClass().equals(ShippingField.class)) {
                gui_fields[i] = new GUI_Shipping("classes\\Resources\\ferry.jpg", fields[i].getName(), fields[i].getSubtext(), "", "", fields[i].getBgColor(), Color.WHITE);

            } else if (fields[i].getClass().equals(CompanyField.class)) {
                if(fields[i].getName().equals("Tuborg")){
                    gui_fields[i] = new GUI_Brewery("classes\\Resources\\squash.jpg", fields[i].getName(), fields[i].getSubtext(), "", "", fields[i].getBgColor(), null);
                }else {
                    gui_fields[i] = new GUI_Brewery("classes\\Resources\\cola.jpg", fields[i].getName(), fields[i].getSubtext(), "", "", fields[i].getBgColor(), null);
                }

            } else if (fields[i].getClass().equals(JailorField.class)) {
                gui_fields[i] = new GUI_Jail("classes\\Resources\\politi.jpg", fields[i].getName(), fields[i].getSubtext(), "De fængsles", fields[i].getBgColor(), Color.WHITE);
            }
        }

        gui = new GUI(gui_fields, Color.lightGray);
    }

    /**
     * Constructor. it is private to make sure it cannot be used externally.
     */
    private GuiHandler() {

    }

    /**
     * Methods returns an integer given by a player within the bounds of min and max
     *
     * @param message The message given to the player before they give their input
     * @param min     The minimum number of players
     * @param max     The maximum number of players
     * @return The user chosen int
     */
    public int getUserInt(String message, int min, int max) {
        int output;

        do { //Made this loop due to us sometimes being able to choose any number of players despite min/max value
            output = gui.getUserInteger(message, min, max);
        } while (output < min || output > max);
        return output;
    }


    /**
     * Takes user input in the form of a String and returns it to the system
     *
     * @param message
     * @return user String
     */
    public String getUserString(String message) {
        return gui.getUserString(message);
    }

    /**
     * Creates players, set car types and colours.
     *
     * @param p
     */
    public void initGuiPlayers(Player[] p) {
        GUI_Car.Type carType;
        Color primaryColor;
        //Create players
        guiPlayers = new GUI_Player[p.length];
        for (int i = 0; i < p.length; i++) {
            switch (i) {
                case 0: {
                    carType = GUI_Car.Type.RACECAR;
                    primaryColor = Color.RED;
                    break;
                }
                case 1: {
                    carType = GUI_Car.Type.RACECAR;
                    primaryColor = Color.GREEN;
                    break;
                }
                case 2: {
                    carType = GUI_Car.Type.RACECAR;
                    primaryColor = Color.CYAN;
                    break;
                }
                case 3: {
                    carType = GUI_Car.Type.RACECAR;
                    primaryColor = Color.MAGENTA;
                    break;
                }
                case 4: {
                    carType = GUI_Car.Type.RACECAR;
                    primaryColor = Color.yellow;
                    break;
                }
                case 5: {
                    carType = GUI_Car.Type.RACECAR;
                    primaryColor = Color.ORANGE;
                    break;
                }
                default: {
                    carType = GUI_Car.Type.RACECAR;
                    primaryColor = Color.BLUE;
                    break;
                }
            }

            guiPlayers[i] = new GUI_Player(p[i].getName(), p[i].getAccount().getScore(), new GUI_Car(primaryColor, Color.WHITE, carType, GUI_Car.Pattern.HORIZONTAL_LINE));

            //Add players to GUI
            gui.addPlayer(guiPlayers[i]);
            guiPlayers[i].setBalance(p[i].getAccount().getScore());
            //Adds the players car to the board at START
            gui_fields[0].setCar(guiPlayers[i], true);
        }

    }

    /**
     * Updates where the player(s) are located on the field.
     *
     * @param pArr
     * @param f
     */
    public void updateGui(Player currentPlayer, Player[] pArr, Domain.GameElements.Fields.Field[] f) {

        updatePlayerPos(currentPlayer, pArr);

        updateBalance(pArr);


        /**
         * Updates the owner of the fields and telling what the rental of the field.
         */
        Player owner;
        String rent;
        for (int i = 0; i < gui_fields.length; i++) {
            if (f[i].getClass().getSuperclass().equals(OwnableField.class)) {
                owner = ((OwnableField) f[i]).getOwner();
                rent = Board.getInstance().getRentString(i);
                if (owner != null) {
                    gui_fields[i].setDescription("Ejer: " + owner.getName() + " - " + "Leje: " + rent);
                    gui_fields[i].setSubText("Ejer: " + owner.getName());
                } else {
                    gui_fields[i].setDescription("Ingen ejer");
                }
                if (f[i].getClass().equals(PropertyField.class))
                    if (((PropertyField) f[i]).getHotel()) {
                        setHouses((GUI_Street) gui_fields[i], 0);
                        setHotel((GUI_Street) gui_fields[i], true);
                    } else {
                        setHouses((GUI_Street) gui_fields[i], ((PropertyField) f[i]).getHouses());
                    }
            }
            if (f[i].getClass().getSuperclass().equals(OwnableField.class)) {
                OwnableField ownableField = (OwnableField) f[i];
                if (ownableField.getIsPawned()) {
                    gui_fields[i].setSubText("PANTSAT");
                } else if (ownableField.getOwner() != null) {
                    gui_fields[i].setSubText("Ejer: " + ownableField.getOwner().getName());
                } else {
                    gui_fields[i].setSubText("kr. " + ownableField.getPrice());
                }
            }
        }
    }

    /**
     * Moves a player on the board
     *
     * @param player The player to move
     * @param pArr The array of all players
     */
    public void updatePlayerPos(Player player, Player[] pArr) {
        /*
         * Move the players on the map, field by field
         */
        boolean carMoved = false;
        //moves the active player up until start
        for (int i = player.getPos() + 1; i < gui_fields.length; i++) {
            for (int j = 0; j < guiPlayers.length; j++) {
                if (gui_fields[i].hasCar(guiPlayers[j]) && pArr[j].getPos() != i) {
                    gui_fields[(i + 1) % gui_fields.length].setCar(guiPlayers[j], true);
                    carMoved = true;
                }
            }

            gui_fields[i].setCar(findGuiPlayer(player, pArr), false);

            carMoved = onCarMoved(carMoved);
        }
        //moves the active player after start
        for (int i = 0; i < player.getPos(); i++) {
            for (int j = 0; j < guiPlayers.length; j++) {
                if (gui_fields[i].hasCar(guiPlayers[j]) && pArr[j].getPos() != i) {
                    gui_fields[(i + 1) % gui_fields.length].setCar(guiPlayers[j], true);
                    carMoved = true;
                }
            }

            gui_fields[i].setCar(findGuiPlayer(player, pArr), false);

            carMoved = onCarMoved(carMoved);
        }
    }

    /**
     * Moves a player on the board in a counter clockwise direction
     *
     * @param player The player to move
     * @param pArr The array of all players
     */
    public void movePlayerBackwards(Player player, Player[] pArr) {
        /*
         * Move the players on the map, field by field
         */
        boolean carMoved = false;

        //moves the active player up until start
        for (int i = player.getPos(); i >= 0; i--) {
            for (int j = 0; j < guiPlayers.length; j++) {
                if (gui_fields[i].hasCar(guiPlayers[j]) && pArr[j].getPos() != i) {
                    if (i > 0)
                        gui_fields[i - 1].setCar(guiPlayers[j], true);
                    else
                        gui_fields[gui_fields.length - 1].setCar(guiPlayers[j], true);
                    carMoved = true;
                }
            }

            gui_fields[i].setCar(findGuiPlayer(player, pArr), false);

            carMoved = onCarMoved(carMoved);
        }

        //moves the active player after start
        for (int i = gui_fields.length - 1; i >= player.getPos() + 1; i--) {
            for (int j = 0; j < guiPlayers.length; j++) {
                if (gui_fields[i].hasCar(guiPlayers[j]) && pArr[j].getPos() != i) {
                    gui_fields[i - 1].setCar(guiPlayers[j], true);
                    carMoved = true;
                }
            }

            gui_fields[i].setCar(findGuiPlayer(player, pArr), false);

            carMoved = onCarMoved(carMoved);
        }
    }


    /**
     * updates only the balance in the gui
     *
     * @param players all players in the game
     */
    public void updateBalance(Player[] players) {
        for (int i = 0; i < guiPlayers.length; i++) {
            guiPlayers[i].setBalance(players[i].getAccount().getScore());
        }
    }


    /**
     * A method that moves the player instantly from one field to another without showing up on any other fields on the way.
     * @param player the player being moved
     * @param players the array of players in the game
     */
    public void teleportPlayer(Player player, Player[] players) {
        for (int i = 0; i < gui_fields.length; i++) {
            gui_fields[i].setCar(findGuiPlayer(player, players), false);
        }
        gui_fields[player.getPos()].setCar(findGuiPlayer(player, players), true);
    }

    public void removePlayerCar (Player player, Player[] players) {
        for (int i = 0; i < gui_fields.length; i++) {
            gui_fields[i].setCar(findGuiPlayer(player, players), false);
        }
    }

    /**
     * A method for when a car has moved in the gui. makes a short delay in between moving.
     *
     * @param carMoved boolean, true if a car has moved since last call of this method
     * @return true if the
     */
    private boolean onCarMoved(boolean carMoved) {
        try {
            if (carMoved) {
                carMoved = false;
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return carMoved;


    }

    /**
     * Shows the roll of the  two dices.
     *
     * @param faceValue1 The value of the first dice
     * @param faceValue2 The value of the second dice
     */
    public void showDice(int faceValue1, int faceValue2) {
        gui.setDice(faceValue1, 4, 8, faceValue2, 5, 8);
    }

    /**
     * Writes a message in the midle of the Board
     *
     * @param msg
     */
    public void msgInMiddle(String msg) {
        gui.displayChanceCard(msg);
    }

    /**
     * Message when a player has to press the roll button
     *
     * @param msg
     */
    public void waitForRoll(String msg) {
        gui.getUserButtonPressed(msg, "Kast terningerne");
    }

    /**
     * Gives message on the top left corner
     *
     * @param msg
     */
    public void giveMsg(String msg) {
        gui.showMessage(msg);
    }


    /**
     * A string that builds the field.
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (GUI_Field f : gui_fields) {
            builder.append(f + "\n");
        }

        return builder.toString();
    }

    /**
     * This method lets any class create an arbitrary number of buttons and waits for the user to press one
     *
     * @param msg
     * @param buttonName
     * @return the name of the user-selected button
     */
    public String makeButtons(String msg, String... buttonName) {
        return gui.getUserButtonPressed(msg, buttonName);
    }

    /**
     * Translates a player from the domain into it's corresponding guiPlayer
     *
     * @param domainPlayer  the player from the domain package
     * @param domainPlayers the player array in board
     * @return the corresponding guiPlayer
     */
    private GUI_Player findGuiPlayer(Player domainPlayer, Player[] domainPlayers) {
        GUI_Player guiPlayer = null;

        for (int i = 0; i < domainPlayers.length; i++) {
            if (domainPlayers[i].equals(domainPlayer))
                guiPlayer = guiPlayers[i];
        }
        return guiPlayer;
    }

    private void setHouses(GUI_Street street, int houseCount){
        street.setHouses(houseCount);
    }

    private void setHotel(GUI_Street street, boolean hasHotel) {
        street.setHotel(hasHotel);
    }


}



