package Domain.GameElements.Fields.Ownable;

import Domain.GameElements.Entities.Player;
import Domain.Controller.AuctionController;
import Domain.GameElements.Board;
import Domain.GameElements.Fields.Field;
import TechnicalServices.GameLogic.GameLogic;

import java.awt.*;

public abstract class OwnableField extends Field {
    private Player owner;
    private boolean isPawned;
    private int price;


    /**
     * Constructor for all ownable fields
     *
     * @param name     The name of the field
     * @param subtext  The fields subtext
     * @param bgColour The background colour of the field
     * @param price    The price of the field
     */
    public OwnableField(String name, String subtext, Color bgColour, int price) {
        super(name, subtext, bgColour);
        this.price = price;
        this.isPawned = false;
    }

    /**
     * Returns the value of a field
     *
     * @return The total value of a field
     */
    public int getWorth() {
        return price;
    }

    /**
     * Method to get the price of a field
     *
     * @return Integer value of the price of a field
     */
    public int getPrice() {
        return price;
    }

    /**
     * Method set the owner of a field.
     *
     * @param player Player to own the field
     */
    public void setOwner(Player player) {
        owner = player;
    }

    /**
     * Method to get the owner
     *
     * @return Player who is the owner
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Method to buy the fields.
     *
     * @param p The player buying the field
     */
    private void buyField(Player p) {
        if (p.getAccount().getScore() >= getPrice()) {
            setOwner(p);
            p.getAccount().changeScore(-getPrice());
            p.getOwnedFields().add(this);

        } else {
            guiHandler.giveMsg("Du har ikke råd til at købe denne grund. Derfor sættes den på auktion.");
            AuctionController.getInstance().runCase(p);
        }
    }


    /**
     * Method to determine what happens when a player lands on a field.
     *
     * @param current The current player
     */
    @Override
    public void landOnAction(Player current) {
        if (getOwner() == null) {
            String choice = guiHandler.makeButtons("Vil du købe " + getName() + "? Den koster " + price, "Ja", "Nej, sæt grunden på aktion");
            if (choice.equalsIgnoreCase("Ja")) {
                buyField(current);
            } else {
                guiHandler.giveMsg("Grunden sættes op for auktion");
                AuctionController.getInstance().runCase(current);
            }
        } else if (getOwner() == current) {
            guiHandler.giveMsg("Du ejer dette felt");
            return;
        } else if (isPawned) {
            guiHandler.giveMsg("Denne grund er blevet pantet");
            return;

        } else {
            try {
                if (owner.getJailTime() < 0) {


                    int payedRent;
                    if (ownsAll()) {
                        if ((this.getClass().equals(PropertyField.class)) && ((PropertyField) this).getHouses() == 0) {
                            payedRent = getRent(current) * 2;
                        } else if (this.getClass().equals(CompanyField.class)) {
                            payedRent = getRent(current) * 2;
                        } else {
                            payedRent = getRent(current);
                        }
                    } else {
                        payedRent = getRent(current);
                    }

                    guiHandler.giveMsg("Du er landet på " + getName() + "\n" + "Du skal betale " + payedRent + " kr. i leje til  " + getOwner().getName());

                    if (ownsAll()) {
                        if ((this.getClass().equals(PropertyField.class)) && ((PropertyField) this).getHouses() == 0) {
                            current.getAccount().changeScore(-getRent(current) * 2);
                            getOwner().getAccount().changeScore(getRent(current) * 2);
                        } else if (this.getClass().equals(CompanyField.class)) {
                            current.getAccount().changeScore(-getRent(current) * 2);
                            getOwner().getAccount().changeScore(getRent(current) * 2);
                        } else {
                            current.getAccount().changeScore(-getRent(current));
                            getOwner().getAccount().changeScore(getRent(current));
                        }
                    } else {
                        current.getAccount().changeScore(-getRent(current));
                        getOwner().getAccount().changeScore(getRent(current));
                    }

                } else {
                    guiHandler.giveMsg(getOwner().getName() + " er i fængsel og kan derfor ikke kræve leje.");
                }
            } catch (RuntimeException e) {
                GameLogic.cantPay(current, getRent(current));
            }
        }
    }

    public boolean ownsAll() {
        boolean ownsAll = false;
        for (OwnableField field : this.getFieldsOfColor()) {
            if (field.getOwner() != null && field.getOwner().equals(owner))
                ownsAll = true;
            else {
                ownsAll = false;
                break;
            }
        }
        return ownsAll;
    }

    /**
     * Goes through all fields on the board and returns all the fields of the same
     * type and color as the one the method was called on.
     *
     * @return All fields of same color and class as the object
     */
    public OwnableField[] getFieldsOfColor() {
        Field[] fields = Board.getInstance().getFields();
        int colorFieldNum = 0;
        OwnableField[] fieldsOfColor;

        //counts the number fields of the same color and class as the object
        for (Field field : fields) {
            if (field.getClass().equals(this.getClass())) {
                if (((OwnableField) field).getBgColor() == this.getBgColor()) {
                    colorFieldNum++;
                }
            }
        }

        //Fills the array with the colored fields
        fieldsOfColor = new OwnableField[colorFieldNum];
        int colorIndex = 0;
        for (Field field : fields) {
            if (field.getClass().equals(this.getClass())) {
                if (((OwnableField) field).getBgColor() == this.getBgColor()) {
                    fieldsOfColor[colorIndex++] = (OwnableField) field;
                }
            }
        }
        return fieldsOfColor;
    }

    /**
     * Returns the background color
     *
     * @return The color of the field
     */
    @Override
    public Color getBgColor() {
        return super.getBgColor();
    }

    /**
     * @return true if the field has been pawned
     */
    public boolean getIsPawned() {
        return isPawned;
    }

    /**
     * changes the pawn status of the field
     * @param changeTo
     */
    public void setIsPawned(boolean changeTo) {
        isPawned = changeTo;
    }

    /**
     * Returns the rent for landing on the field
     * @param player The player landing on the field
     * @return The rent of the field
     */
    public abstract int getRent(Player player);

    /**
     * overrides the toString method native to the Object class
     * @return the name of the field.
     */
    @Override
    public String toString() {
        return getName();
    }

}


