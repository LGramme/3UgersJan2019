package TechnicalServices.GameLogic;

import java.awt.Color;

public class Values {

    /**
     * This method returns the price of a property, so if you want the price for field number 9, you type propertyPrice(9)
     *
     * @param i The position of the property on the board
     * @return The price of the property, given by its position on the board
     */
    public static int propertyPrice(int i) {
        int price = 0;
        switch (i) {
            case 2:
                price = 1200;
                break;
            case 4:
                price = 1200;
                break;
            case 6:
                price = 4000;
                break;
            case 7:
                price = 2000;
                break;
            case 9:
                price = 2000;
                break;
            case 10:
                price = 2400;
                break;
            case 12:
                price = 2800;
                break;
            case 13:
                price = 3000;
                break;
            case 14:
                price = 2800;
                break;
            case 15:
                price = 3200;
                break;
            case 16:
                price = 4000;
                break;
            case 17:
                price = 3600;
                break;
            case 19:
                price = 3600;
                break;
            case 20:
                price = 4000;
                break;
            case 22:
                price = 4400;
                break;
            case 24:
                price = 4400;
                break;
            case 25:
                price = 4800;
                break;
            case 26:
                price = 4000;
                break;
            case 27:
                price = 5200;
                break;
            case 28:
                price = 5200;
                break;
            case 29:
                price = 3000;
                break;
            case 30:
                price = 5600;
                break;
            case 32:
                price = 6000;
                break;
            case 33:
                price = 6000;
                break;
            case 35:
                price = 6400;
                break;
            case 36:
                price = 4000;
                break;
            case 38:
                price = 7000;
                break;
            case 40:
                price = 8000;
                break;

        }
        return price;
    }

    /**
     * [0]: Betal 3000 kr for reperation af vogn
     * [1]: De modtager en tandlægeregning, betal 2000 kr
     * [2]: De har kørt frem for fuldt stop. Betal 1000 kr i bøde
     * [3]: De har købt fire nye dæk, til deres vogn betal 1000 kr
     * [4]: Betal deres bilforsikring - 1000 kr
     * [5]: Betal for vognvask og smøring - 300 kr
     * [6]: Betal kr. 200 for levering af 2 kasser øl
     * [7]: Betal told for cigaretter - 200 kr
     * [8]: Betal parkeringsbøde 200 kr
     * [9]: Værdien af egen avl udgør 200, modtages af banken
     * [10]: Hip hurra; fødselsdag modtag fra hver spiller 200 kr                     *
     * [11]: Sammenskudsgilde, alle betaler 500 kr                                    *
     * [12]: Klasselotteriet, modtag 500 kr
     * [13]: Familiefest, få tilskud fra hver spiller på 500 kr                       *
     * [14]: Solgt gamle møbler, modtag 1000 kr
     * [15]: Aktieudbytte, modtag 1000 kr
     * [16]: Gageforhøjelse, modtag 1000 kr
     * [17]: Præmieobligation, modtag 1000 kr
     * [18]: Aktie afkast, modtag 1000 kr
     * [19]: Række med 11 rigtige, modtag 1000 kr
     * [20]: Eftergivet kvartals skal, modtag 3000 kr
     * [21]: Matador legatet, if (getWorth <= 15000 ) modtag 40000 kr
     * [22]&&[23] Oliepriserne stiger betal 500 kr. pr hus og 2000 kr pr. hotel      *
     * [24]&&[25] Ejendomsskatterne stiger betal 800 kr pr. hus og 2300 kr pr. hotel *
     *
     * @param i
     * @return Returns the value effect of the chancecard
     */
    public static int chanceCardValue(int i) {
        int value;
        int[] chanceCardValues = {
                -3000, -2000, -1000, -1000, -1000 - 300, -200, -200, -200, 200, 200, 500, 500, 500,
                1000, 1000, 1000, 1000, 1000, 1000, 3000, 40000, -500, -2000, -800, -2300
        };
        value = chanceCardValues[i];
        return value;
    }

    /**
     * Returns the price for a house, relating to the tier of the property.
     * Tier 1 = 1000
     * Tier 2 = 2000
     * Tier 3 = 3000
     * Tier 4 = 4000
     *
     * @param bgColor The bgColor of the field.
     * @return price for a house
     */
    public static int housePrice(Color bgColor) {
        int price;

        if (bgColor.equals(Color.CYAN) || bgColor.equals(Color.ORANGE)) {
            price = 1000;
        } else if (bgColor.equals(Color.GREEN) || bgColor.equals(Color.lightGray)) {
            price = 2000;
        } else if (bgColor.equals(Color.RED) || bgColor.equals(Color.WHITE)) {
            price = 3000;
        } else if (bgColor.equals(Color.YELLOW) || bgColor.equals(Color.MAGENTA)) {
            price = 4000;
        } else {
            throw new IllegalArgumentException("This color doesn't correspond to any price group");
        }

        return price;
    }

    /**
     * @param fieldPlacement The location of the field on the board (x-axis of array)
     * @param numberOfHouses How many houses are owned (y-axis of array)
     * @return A ragged array, that tells the price of the rent for the properties with and without houses/hotels.
     * [0]: START
     * [1]: Rent for Rødovrevej
     * [2]: Chance
     * [3]: Rent for Hvidovrevej
     * [4]: Skat
     * [5]: Rent for Scandlines "Helsingør-Helsingborg"
     * [6]: Rent for Roskildevej
     * [7]: Chance
     * [8]: Rent for Valby Langgade
     * [9]: Rent for Allégade
     * [10]: Visit
     * [11]: Rent for Fredriksberg Allé
     * [12]: Rent for Turborg Squash
     * [13]: Rent for Bülowsvej
     * [14]: Rent for Gl. Kongevej
     * [15]: Rent for Mols-Linien
     * [16]: Rent for Bernstorffsvej
     * [17]: Chance
     * [18]: Rent for Hellerupvej
     * [19]: Rent for Strandvejen
     * [20]: Gratis
     * [21]: Rent for Trianglen
     * [22]: Chance
     * [23]: Rent for Østerbrogade
     * [24]: Rent for Grønningen
     * [25]: Rent for Scandlines "Gedser-Rostock"
     * [26]: Rent for Bredgade
     * [27]: Rent for Kgs.Nytorv
     * [28]: Rent for CocaCola
     * [29]: Rent for Østergade
     * [30]: Jail
     * [31]: Rent for Amagertorv
     * [32]: Rent for Vimmelskaffet
     * [33]: Chance
     * [34]: Rent for Nygade
     * [35]: Rent for Scandlines "Rødby-Puttgarden"
     * [36]: Chance
     * [37]: Rent for Frederiksbergade
     * [38]: Skat
     * [39]: Rent for Rådhuspladsen
     */
    public static int rentPrice(int fieldPlacement, int numberOfHouses) {
        int rent;
        int rentPrice[][] =
                {{0},
                        {50, 250, 750, 2250, 4000, 6000},
                        {0},
                        {50, 250, 750, 2250, 4000, 6000},
                        {0},
                        {500, 1000, 2000, 4000},
                        {100, 600, 1800, 5400, 8000, 11000},
                        {0},
                        {100, 600, 1800, 5400, 8000, 11000},
                        {150, 800, 2000, 6000, 9000, 12000},
                        {0},
                        {200, 1000, 3000, 9000, 12500, 15000},
                        {100},
                        {200, 1000, 3000, 9000, 12500, 15000},
                        {250, 1250, 3750, 10000, 14000, 18000},
                        {500, 1000, 2000, 4000},
                        {300, 1400, 4000, 11000, 15000, 19000},
                        {0},
                        {300, 1400, 4000, 11000, 15000, 19000},
                        {350, 1600, 4400, 12000, 16000, 20000},
                        {0},
                        {350, 1800, 5000, 14000, 17500, 21000},
                        {0},
                        {350, 1800, 5000, 14000, 17500, 21000},
                        {400, 2000, 6000, 15000, 18500, 22000},
                        {500, 1000, 2000, 4000},
                        {450, 2200, 6600, 16000, 19500, 23000},
                        {450, 2200, 6600, 16000, 19500, 23000},
                        {100},
                        {500, 2400, 7200, 17000, 20500, 24000},
                        {0},
                        {550, 2600, 7800, 18000, 22000, 25000},
                        {550, 2600, 7800, 18000, 22000, 25000},
                        {0},
                        {600, 3000, 9000, 20000, 24000, 28000},
                        {500, 1000, 2000, 4000},
                        {0},
                        {700, 3500, 10000, 22000, 26000, 30000},
                        {0},
                        {1000, 4000, 12000, 28000, 34000, 40000}};

        rent = rentPrice[fieldPlacement][numberOfHouses];
        return rent;
    }


}