package Domain.Controller;

import Domain.GameElements.Board;
import Domain.GameElements.Entities.Player;
import TechnicalServices.GameLogic.GameLogic;
import UI.GUI.GuiHandler;


public class MainController {

   private GuiHandler guiHandler;
    private Player[] players;
    private int currentPlayerNum;
    private Player currentPlayer;
    private MoveController moveCon;
    private JailController jailController;
    private PawnController pawnCon;
    private TradeController tradeCon;
    private int turnsInARow;

    public MainController(Player[] players){
        this.players = players;
        currentPlayerNum = 0;

        guiHandler = GuiHandler.getInstance();

        moveCon = MoveController.getInstance();
        jailController = JailController.getInstance();
        pawnCon = PawnController.getInstance();
        tradeCon = new TradeController();
    }

    public void runCase(){
        while (!GameLogic.lastManStanding(players)){
            turnsInARow = 0;
            currentPlayer = players[currentPlayerNum];
            do{
                String choice;
                if (currentPlayer.getJailTime() >=0) {
                    choice = guiHandler.makeButtons("Vælg en handling " + currentPlayer.getName(),
                            "Slip fri", "Handel", "Pantsætning");
                    if (choice.equalsIgnoreCase("Slip fri"))
                        jailController.runCase(currentPlayer);
                    if (choice.equalsIgnoreCase("Handel")) {
                        tradeCon.runCase(currentPlayer);
                        currentPlayer.setIsActive(true);
                    }
                    if (choice.equalsIgnoreCase("Pantsætning"))
                        ;//pawnCon.runCase();
                } else {
                    choice = guiHandler.makeButtons("Vælg en handling " + currentPlayer.getName(),
                            "Slå terninger", "Handel", "Pantsætning");
                    if (choice.equalsIgnoreCase("Slå terninger")) {
                        turnsInARow++;
                        if (turnsInARow == 3) {
                            currentPlayer.setIsActive(false);
                            currentPlayer.setJailTime(0);
                            currentPlayer.setPos(10);
                            guiHandler.giveMsg("Du kører for hurtigt! Vi må tage dig med på stationen.");
                            guiHandler.updatePlayerPos(currentPlayer, Board.getInstance().getPlayers());
                            break;
                        }
                        moveCon.runCase(currentPlayer);
                    } if (choice.equalsIgnoreCase("Handel")) {
                        tradeCon.runCase(currentPlayer);
                        currentPlayer.setIsActive(true);
                    }
                    if (choice.equalsIgnoreCase("Pantsætning"))
                        pawnCon.runCase(currentPlayer);
                        currentPlayer.setIsActive(true);

                }

            }while (currentPlayer.getIsActive());

            currentPlayerNum = ++currentPlayerNum % players.length;
        }
    }

}
