package org.alto;

import org.alto.coordinator.RestManager;
import org.alto.models.ClientsGroup;
import org.alto.models.Table;

import java.util.Arrays;
import java.util.List;


public class Main {
    static List<Table> tables = Arrays.asList(
            new Table(2), new Table(3), new Table(4), new Table(6)
    );

    public static void main(String[] args) throws InterruptedException {

        RestManager manager = new RestManager(tables);

        ClientsGroup clientsGroup2CoupleSmith = new ClientsGroup(2, "Couple Smith");
        ClientsGroup clientsGroup4FamilyJohnSnow = new ClientsGroup(4, "Family John Snow");
        ClientsGroup clientsGroup6FriendsRachel = new ClientsGroup(6, "Friends Rachel");
        // Simulate concurrent arrivals of 3 different groups
        Thread t1 = new Thread(() -> manager.onArrive(clientsGroup2CoupleSmith));
        Thread t2 = new Thread(() -> manager.onArrive(clientsGroup4FamilyJohnSnow));
        Thread t3 = new Thread(() -> manager.onArrive(clientsGroup6FriendsRachel));

        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();

        System.out.println("Seated groups after arrival:");
        manager.printTables();

        Thread.sleep(2500);
        // Simulate arrive & leaves
        ClientsGroup groupOf2CoupleVishal = new ClientsGroup(2, "Couple Vishal");
        System.out.println("--------------------------");
        System.out.println("Group arrived:" + groupOf2CoupleVishal);
        manager.onArrive(groupOf2CoupleVishal);
        manager.printTables();

        System.out.println("--------------------------");
        Thread.sleep(2500);
        System.out.println("Group leaving:" + groupOf2CoupleVishal);
        manager.onLeave(groupOf2CoupleVishal);
        manager.printTables();


        System.out.println("--------------------------");
        Thread.sleep(2500);
        ClientsGroup groupOf6FriendsHollywood = new ClientsGroup(6, "Friends Hollywood");
        System.out.println("Another group of 6 arrived:" + groupOf6FriendsHollywood);
        manager.onArrive(groupOf6FriendsHollywood);
        manager.printTables();

        System.out.println("--------------------------");
        Thread.sleep(2500);
        ClientsGroup groupOf3FriendsMusketeers = new ClientsGroup(3, "Friends Musketeers");
        System.out.println("Another group of 3 arrived:" + groupOf3FriendsMusketeers);
        manager.onArrive(groupOf3FriendsMusketeers);
        manager.printTables();

        System.out.println("--------------------------");
        Thread.sleep(2500);
        ClientsGroup groupOf6FriendsHolland = new ClientsGroup(6, "Friends Holland");
        System.out.println("Another group of 6 arrived:" + groupOf6FriendsHolland);
        manager.onArrive(groupOf6FriendsHolland);
        manager.printTables();

        System.out.println("--------------------------");
        Thread.sleep(2500);
        System.out.println("Group pf 6 got bored of waiting:" + groupOf6FriendsHollywood);
        manager.onLeave(groupOf6FriendsHollywood);

        System.out.println("--------------------------");
        Thread.sleep(2500);
        System.out.println("Group leaving:" + clientsGroup4FamilyJohnSnow);
        manager.onLeave(clientsGroup4FamilyJohnSnow);
        manager.printTables();

        System.out.println("--------------------------");

        ClientsGroup clientsGroup2CoupleBeckham = new ClientsGroup(2, "Couple Beckham");
        System.out.println("Group arrived:" + clientsGroup2CoupleBeckham);
        Thread.sleep(2500);
        manager.onArrive(clientsGroup2CoupleBeckham);
        manager.printTables();
        System.out.println("--------------------------");
        ClientsGroup clientsGroup2CoupleTom = new ClientsGroup(2, "Couple Tom");
        System.out.println("Group arrived:" + clientsGroup2CoupleTom);
        Thread.sleep(2500);
        manager.onArrive(clientsGroup2CoupleTom);
        manager.printTables();

        System.out.println("--------------------------");
        Thread.sleep(2500);
        System.out.println("Group leaving:" + clientsGroup2CoupleTom);

        manager.onLeave(clientsGroup2CoupleTom);
        manager.printTables();

        System.out.println("--------------------------");
        Thread.sleep(2500);
        System.out.println("Group leaving:" + clientsGroup2CoupleBeckham);
        manager.onLeave(clientsGroup2CoupleBeckham);
        manager.printTables();

        // Try to leave a group that had already left long back
        manager.onLeave(groupOf2CoupleVishal);


        System.out.println("--------------------------");
        Thread.sleep(2500);
        System.out.println("Group leaving:" + clientsGroup2CoupleSmith);
        manager.onLeave(clientsGroup2CoupleSmith);
        manager.printTables();

        System.out.println("--------------------------");
        Thread.sleep(2500);
        ClientsGroup groupOfTrinity = new ClientsGroup(3, "Trinity");
        System.out.println("Another group of 3 arrived:" + groupOfTrinity);
        manager.onArrive(groupOfTrinity);
        manager.printTables();

    }
}