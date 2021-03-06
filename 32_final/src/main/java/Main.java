import Domain.Controller.JailController;
import Domain.Controller.MainController;
import Domain.Controller.MoveController;
import Domain.GameElements.Board;
import Domain.GameElements.Fields.Field;
import Domain.GameElements.Fields.Ownable.OwnableField;
import Domain.GameElements.Entities.ChanceCardStack;
import Domain.GameElements.Fields.Ownable.OwnableField;
import Domain.GameElements.Fields.Ownable.PropertyField;
import UI.GUI.GuiHandler;

public class Main {
    /**
     * The method forstarting up the game. this is the first method to run and will set up the most important elements of the game in the correct order.
     * @param args The standard argument for the public static void main class.
     */
    public static void main(String[] args) {

        Board board = Board.getInstance();
        GuiHandler guiHandler = GuiHandler.getInstance();
        guiHandler.initGuiFields(board.getFields());
        boolean isPresentation = true;

        String valg = guiHandler.makeButtons("Vil du spille spillet eller køre præsentationen?", "Spil spillet", "Præsentation");

        if (valg.equalsIgnoreCase("Spil spillet")) {
            isPresentation = false;
        }
        if (!isPresentation) {

            standardMode(board, guiHandler);
        } else {
            presentationMode(board, guiHandler);

        }


    }

    private static void standardMode(Board board, GuiHandler guiHandler) {
        board.initBoard(guiHandler.getUserInt("Vælg mellem 3 og 6 spillere", 3, 6), false);
        guiHandler.initGuiPlayers(board.getPlayers());
        ChanceCardStack.getStackInstance().shuffleCards();

        MainController mainController = new MainController(board.getPlayers());
        MoveController.getInstance().initiate(board);
        JailController.getInstance();

        mainController.runCase();
    }

    private static void presentationMode(Board board, GuiHandler guiHandler){
        board.initBoard(4, true);
        guiHandler.initGuiPlayers(board.getPlayers());



        //Sets accountScore for all dummy players
        for (int i = 0; i < board.getPlayers().length; i++) {
            board.getPlayers()[i].getAccount().setScore(10000);
        }
        guiHandler.updateBalance(board.getPlayers());
        ChanceCardStack.getStackInstance().arrangeCards(Board.getInstance().getPlayers(), Board.getInstance().getFields());

        setPlayerStartPosition(board, guiHandler, 0, 13);
        setPlayerStartPosition(board, guiHandler, 1, 29);
        setPlayerStartPosition(board, guiHandler, 2, 34);
        setPlayerStartPosition(board, guiHandler, 3, 39);


        setFieldOwner(board, 0, 11);
        setFieldOwner(board, 0, 13);
        setFieldOwner(board, 0, 14);
        setFieldOwner(board,0,5);
        setFieldOwner(board,0,25);
        setFieldOwner(board,0,35);
        setFieldOwner(board, 1, 26 );
        setFieldOwner(board, 1, 27);
        setFieldOwner(board, 1, 29);
        setFieldOwner(board, 2, 31);
        setFieldOwner(board, 2, 32);
        setFieldOwner(board, 2, 34);
        setFieldOwner(board, 3, 37);
        setFieldOwner(board, 3, 39);

        //setHouses(board, guiHandler, 1, 14, 5);
        setHouses(board, guiHandler, 0, 14, 1);
        setHouses(board, guiHandler, 1, 29, 1);
        setHouses(board, guiHandler, 2, 31, 1);
        setHouses(board, guiHandler, 3, 37, 5);
        setHouses(board, guiHandler, 3, 39, 5);


        MainController mainController = new MainController(board.getPlayers());
        MoveController.getInstance().initiatePresentation(board);
        JailController.getInstance();

        mainController.runCase();

    }

    //Method to set a players starting position
    private static void setPlayerStartPosition(Board board, GuiHandler guiHandler, int playerNum, int pPos){
        board.getPlayers()[playerNum].setPos(pPos);
        guiHandler.updateGui(board.getPlayers()[playerNum], board.getPlayers(), board.getFields());
    }

    //Method to assign ownable fields to a player
    private static void setFieldOwner(Board board, int playerNum, int FieldNum){
        board.getPlayers()[playerNum].getOwnedFields().add((OwnableField)board.getFields()[FieldNum]);
        ((OwnableField)board.getFields()[FieldNum]).setOwner(board.getPlayers()[playerNum]);


    }

    /**
     *Method which sets houses on the board from start
     * @param board
     * @param guiHandler
     * @param playerNum = Which player
     * @param buyHouseOnField = Which field should the house be on
     * @param houseAmount = How many houses should be there
     */
    private static void setHouses(Board board, GuiHandler guiHandler, int playerNum, int buyHouseOnField, int houseAmount){
        PropertyField pf = ((PropertyField)board.getFields()[buyHouseOnField]);

        if(pf.getOwner()==board.getPlayers()[playerNum]){

            for (int i = 0; i < houseAmount ; i++) {
                pf.addHouse();
                guiHandler.updateGui(board.getPlayers()[playerNum], board.getPlayers(), board.getFields());
            }



        }
    }

}
