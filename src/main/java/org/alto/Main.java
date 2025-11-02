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
        // Simulate concurrent arrivals
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

        Thread.sleep(500);
        // Simulate arrive & leaves
        ClientsGroup groupOf2CoupleVishal = new ClientsGroup(2, "Couple Vishal");
        System.out.println("--------------------------");
        System.out.println("Group arrived:" + groupOf2CoupleVishal);
        manager.onArrive(groupOf2CoupleVishal);
        manager.printTables();

        System.out.println("--------------------------");
        Thread.sleep(500);
        System.out.println("Group leaving:" + groupOf2CoupleVishal);
        manager.onLeave(groupOf2CoupleVishal);
        manager.printTables();


        System.out.println("--------------------------");
        Thread.sleep(1500);
        ClientsGroup groupOf6FriendsHollywood = new ClientsGroup(6, "Friends Hollywood");
        System.out.println("Another group of 6 arrived:" + groupOf6FriendsHollywood);
        manager.onArrive(groupOf6FriendsHollywood);
        manager. printTables();
//        manager.printGroupsWaitingInQueue();

        System.out.println("--------------------------");
        Thread.sleep(1500);
        ClientsGroup groupOf3FriendsMusketeers = new ClientsGroup(3, "Friends Musketeers");
        System.out.println("Another group of 3 arrived:" + groupOf3FriendsMusketeers);
        manager.onArrive(groupOf3FriendsMusketeers);
        manager.printTables();
//        manager.printGroupsWaitingInQueue();

        System.out.println("--------------------------");
        Thread.sleep(2500);
        System.out.println("Group pf 6 got bored of waiting:" + groupOf6FriendsHollywood);
        manager.onLeave(groupOf6FriendsHollywood);
//        manager.printGroupsWaitingInQueue();

        System.out.println("--------------------------");
        Thread.sleep(2500);
//        manager.onLeave(clientsGroup2CoupleSmith);
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
    }

//    private static void printTables() {
//        System.out.println("printing latest tables occupancy information:");
//        for (Table table : tables) {
//            System.out.println(table);
//        }
//    }
}