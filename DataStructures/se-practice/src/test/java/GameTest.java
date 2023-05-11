import org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Set;
import java.util.HashSet;

import static org.junit.Assert.*;

public class GameTest {
    @Test
    public void playGameTestTieForfeit(){
        ByteArrayInputStream forfeit = new ByteArrayInputStream(new String("False").getBytes());
        System.setIn(forfeit);
        House house = House.getInstance(100000);
        Player Raanan = new Player("Raanan", 10000, house.createUserID());
        Raanan.enterGame();
        while (Raanan.getGame().tie == false) {
            Raanan.getGame().playHand(false, 0, 10);
            System.out.println(Raanan.getBalance());
            System.out.println(house.getHouseBalance());
        }
    }

    @Test
    public void playGameTestTieDoubleDown(){
        ByteArrayInputStream doubleDown = new ByteArrayInputStream(new String("True").getBytes());
        System.setIn(doubleDown);
        House house = House.getInstance(100000);
        Player Raanan = new Player("Raanan", 10000, house.createUserID());
        Raanan.enterGame();
        while (Raanan.getGame().tie == false) {
            Raanan.getGame().playHand(false, 0, 10);
            System.out.println(Raanan.getBalance());
            System.out.println(house.getHouseBalance());
        }
    }

    @Test
    public void playGameTestDoubleTie(){
        ByteArrayInputStream doubleDown = new ByteArrayInputStream(new String("True").getBytes());
        System.setIn(doubleDown);
        House house = House.getInstance(1000000);
        Player Raanan = new Player("Raanan", 10000, house.createUserID());
        Raanan.enterGame();
        while (Raanan.getGame().doubleTie == false) {
            Raanan.getGame().playHand(false, 0, 10);
            System.out.println(Raanan.getBalance());
            System.out.println(house.getHouseBalance());
        }
    }

    @Test
    public void playGameHouseWinTest(){
        House house = House.getInstance(200);
        Player Raanan = new Player("Raanan", 50, house.createUserID());
        assertEquals(Raanan.getTotalHandsPlayed(), 0, 0);
        assertEquals(Raanan.getBalance(), 50, 0);
        assertEquals(house.getHouseBalance(), 200, 0);
        assertEquals(house.getWinPercentage(), 0, 0);
        int gamesPlayed = 0;
        Raanan.enterGame();
        while(Raanan.getGame().houseWin == false){
            Raanan.getGame().playHand(false, 0, 5);
            gamesPlayed++;
        }
        assertEquals(Raanan.getTotalHandsPlayed(), gamesPlayed, 0);
//        assertEquals(Raanan.getBalance(), 45, 0);
//        assertEquals(house.getHouseBalance(), 205, 0);
//        assertEquals(house.getWinPercentage(), 1, 0);
    }

    @Test
    public void playGamePlayerWinTest(){
        House house = House.getInstance(200);
        Player Raanan = new Player("Raanan", 50, house.createUserID());
        assertEquals(Raanan.getTotalHandsPlayed(), 0, 0);
        assertEquals(Raanan.getBalance(), 50, 0);
        assertEquals(house.getHouseBalance(), 200, 0);
        assertEquals(house.getWinPercentage(), 0, 0);
        int gamesPlayed = 0;
        Raanan.enterGame();
        while(Raanan.getGame().playerWin == false){
            Raanan.getGame().playHand(false, 0, 5);
            gamesPlayed++;
        }
        assertEquals(Raanan.getTotalHandsPlayed(), gamesPlayed, 0);
//        assertEquals(Raanan.getBalance(), 55, 0);
//        assertEquals(house.getHouseBalance(), 195, 0);
//        assertEquals(Raanan.getWinPercentage(), 1, 0);
    }

    @Test
    public void playGameTieBet(){
        ByteArrayInputStream forfeit = new ByteArrayInputStream(new String("False").getBytes());
        System.setIn(forfeit);
        House house = House.getInstance(200);
        Player Raanan = new Player("Raanan", 50, house.createUserID());
        assertEquals(Raanan.getBalance(), 50, 0);
        assertEquals(house.getHouseBalance(), 200, 0);
        Raanan.enterGame();
        while(Raanan.getGame().tie == false){
            Raanan.getGame().playHand(true, 1, 10);
        }
        System.out.println(Raanan.getBalance());
        System.out.println(house.getHouseBalance());
    }
}
