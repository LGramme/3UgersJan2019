/**
 * All GuiHandler related classes are kept here.
 */
package UI.GUI;


import Domain.GameElements.Entities.Player;
import Domain.GameElements.Fields.*;
import Domain.GameElements.Fields.Ownable.*;
import Domain.GameElements.Fields.ChanceField.*;
import gui_codebehind.GUI_BoardController;
import gui_fields.*;
import gui_main.GUI;

import java.awt.*;


public class GuiHandler {
    private GUI gui;
    private GUI_Field[] gui_fields = new GUI_Field[40];
    private GUI_Player[] guiPlayers;
    private int[] dicefacevalues = new int[2];
    private GUI_BoardController bc;

    private static GuiHandler guiHandlerInstance;

    /**
     * returns the instance of the GuiHandler
     * this method ensures that all classes can access the same GuiHandler instance (singleton)
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
     * @param fields the fields in the board class
     * @return The instance of the object
     */
    public void initGuiFields(Domain.GameElements.Fields.Field[] fields) throws IllegalStateException{
        for(int i = 0; i < gui_fields.length; i++){
            if(fields[i].getClass().equals(EmptyField.class) && i == 0){
                gui_fields[i] = (new GUI_Start(fields[i].getName(), fields[i].getSubtext(), "", fields[i].getBgColor(), null));

            } else if(fields[i].getClass().equals(PropertyField.class)){
                PropertyField propertyField = (PropertyField) fields[i];
                gui_fields[i] = (new GUI_Street(fields[i].getName(), fields[i].getSubtext(), "", Integer.toString(propertyField.getPrice()), fields[i].getBgColor(), null));

            } else if(fields[i].getClass().equals(EmptyField.class)){
                gui_fields[i] = (new GUI_Street(fields[i].getName(), fields[i].getSubtext(), "", "0", fields[i].getBgColor(), null));//This one be causing trouble

            } else if(fields[i].getClass().equals(ChanceField.class)){
                gui_fields[i] = (new GUI_Chance(fields[i].getName(), fields[i].getSubtext(), "", fields[i].getBgColor(), Color.white));

            } else if (fields[i].getClass().equals(TaxField.class)) {
               gui_fields[i] = new GUI_Tax(fields[i].getName(), fields[i].getSubtext(), "", fields[i].getBgColor(),null);

            } else if (fields[i].getClass().equals(ShippingField.class)) {
                gui_fields[i] = new GUI_Shipping("", fields[i].getName(), fields[i].getSubtext(), "", "",fields[i].getBgColor(),null);

            } else if (fields[i].getClass().equals(CompanyField.class)) {
                gui_fields[i] = new GUI_Brewery("", fields[i].getName(), fields[i].getSubtext(), "", "",fields[i].getBgColor(),null);

            } else if (fields[i].getClass().equals(JailorField.class)) {
                gui_fields[i] = new GUI_Street(fields[i].getName(), fields[i].getSubtext(), "", "0", fields[i].getBgColor(), null);
            }
        }

        gui = new GUI(gui_fields, Color.lightGray);
    }

    /**
     * Constructor. it is private to make sure it cannot be used externally.
     */
    private GuiHandler(){
    }

    /**
     * Methods returns an integer given by a player
     * @param min The minimum number of players
     * @param max The maximum number of players
     */
    public int getNumberOfPlayers(int min, int max){
        int output;

        do { //Made this loop due to us sometimes being able to choose any number of players despite min/max value
            output = gui.getUserInteger("Choose between 3 and 6 players", min, max);
        } while (output < min || output > max);
        return output;
    }

    /**
     * Creates players and set car types.
     * @param p
     */
    public void initGuiPlayers(Player[] p){
        GUI_Car.Type carType;
        Color primaryColor;
        //Create players
        guiPlayers = new GUI_Player[p.length];
        for(int i = 0; i < p.length; i++){
            switch (i){
                case 0:{
                    carType = GUI_Car.Type.CAR;
                    primaryColor = Color.RED;
                    break;
                }
                case 1:{
                    carType = GUI_Car.Type.RACECAR;
                    primaryColor = Color.GREEN;
                    break;
                }
                case 2:{
                    carType = GUI_Car.Type.TRACTOR;
                    primaryColor = Color.CYAN;
                    break;
                }
                case 3:{
                    carType = GUI_Car.Type.UFO;
                    primaryColor = Color.MAGENTA;
                    break;
                }
                case 4:{
                    carType = GUI_Car.Type.CAR;
                    primaryColor = Color.yellow;
                break;
                }
                case 5:{
                    carType = GUI_Car.Type.UFO;
                    primaryColor = Color.ORANGE;
                    break;
                }
                default:{
                    carType = GUI_Car.Type.CAR;
                    primaryColor = Color.BLUE;
                    break;
                }
            }
            guiPlayers[i] = new GUI_Player("Player" + (i+1), p[i].getAccount().getScore(), new GUI_Car(primaryColor, Color.WHITE, carType, GUI_Car.Pattern.HORIZONTAL_LINE));
            //Add players to GUI
            gui.addPlayer(guiPlayers[i]);
            guiPlayers[i].setBalance(p[i].getAccount().getScore());
            //Adds the players car to the board at START
            gui_fields[0].setCar(guiPlayers[i], true);
        }


    }

    /**
     * Updates where the player(s) are located on the field.
     * @param pArr
     * @param f
     */
    public void updateGui(Player curentPlayer, Player[] pArr, Domain.GameElements.Fields.Field[] f){

        /*
         * Move the players on the map, field by field
         */
        boolean carMoved = false;
        //moves players step by step
        for (int i = curentPlayer.getPos() + 1; i < gui_fields.length; i++) {
            for (int j = 0; j < guiPlayers.length; j++) {
                if (gui_fields[i].hasCar(guiPlayers[j]) && pArr[j].getPos() != i){
                    gui_fields[(i+1)% gui_fields.length].setCar(guiPlayers[j], true);
                    carMoved = true;
                }
            }

            gui_fields[i].removeAllCars();
            for (int j = 0; j < guiPlayers.length; j++) {
                if (pArr[j].getPos() == i){
                    gui_fields[i].setCar(guiPlayers[j], true);
                }
            }


            carMoved = onCarMoved(carMoved);
        }

        for (int i = 0; i < curentPlayer.getPos(); i++) {
            for (int j = 0; j < guiPlayers.length; j++) {
                if (gui_fields[i].hasCar(guiPlayers[j]) && pArr[j].getPos() != i){
                    gui_fields[(i+1)% gui_fields.length].setCar(guiPlayers[j], true);
                    carMoved = true;
                }
            }

            gui_fields[i].removeAllCars();
            for (int j = 0; j < guiPlayers.length; j++) {
                if (pArr[j].getPos() == i){
                    gui_fields[i].setCar(guiPlayers[j], true);
                }
            }


            carMoved = onCarMoved(carMoved);
        }

        /*
         * Updates the player balance.
         */
        for(int i = 0; i < pArr.length; i++){
            guiPlayers[i].setBalance(pArr[i].getAccount().getScore());
        }

        /*
         * updates ownership of tile
         */
        Player owner;
        for(int i = 0; i < gui_fields.length; i++){
            if (f[i].getClass().equals(PropertyField.class)) {
                owner = ((PropertyField) f[i]).getOwner();
                if (owner != null) {
                    gui_fields[i].setDescription("Owner: " + owner.getName());
                } else {
                    gui_fields[i].setDescription("No owner");
                }
            }
        }

    }

    /**
     * A method for when a car has moved in the gui
     * @param carMoved boolean, true if a car has moved since last call of this method
     * @return
     */
    private boolean onCarMoved(boolean carMoved) {
        try {
            if (carMoved){
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
     * @param faceValue1 The value of the first dice
     * @param faceValue2 The value of the second dice
     */
    public void showDice(int faceValue1, int faceValue2){
        gui.setDice(faceValue1, faceValue2);
    }

    /**
     * Writes a message in the midle of the Board
     * @param msg
     */
    public void msgInMiddle(String msg){
        gui.displayChanceCard(msg);
    }

    /**
     * Message when a player has to press the roll button
     * @param msg
     */
    public void waitForRoll(String msg){
        gui.getUserButtonPressed(msg, "Roll");
    }

    /**
     * Gives message on the top left corner
     * @param msg
     */
    public void giveMsg(String msg){
        gui.showMessage(msg);
    }


    /**
     * A string that builds the field.
     * @return
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (GUI_Field f : gui_fields){
            builder.append(f + "\n");
        }

        return builder.toString();
    }

    /**
     * This method lets any class create an arbitrary number of buttons and waits for the user to press one
     * @param msg
     * @param buttonName
     * @return the name of the user-selected button
     */
    public String makeButtons(String msg, String... buttonName){
        return gui.getUserButtonPressed(msg, buttonName);
    }


    //todo make a teleportGui_player mehod to move player car instantly
    //todo make method that changes balance more slowly
}




