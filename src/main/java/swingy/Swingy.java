package swingy;

import swingy.controller.*;
import swingy.model.artifact.Armour;
import swingy.model.artifact.Artifact;
import swingy.model.artifact.Helm;
import swingy.model.artifact.Weapon;
import swingy.view.*;

import javax.swing.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Swingy {
    public static Scanner scan = new Scanner(System.in);
    public static GameController gameDirector = new GameController();

    public static void startGame(GameController gameDirector) throws SQLException {
        Random random = new Random();
        int direction = 0;

        while (true) {
            for (int i = 0; i < gameDirector.mapDirector.size; i++) {
                for (int j = 0; j < gameDirector.mapDirector.size; j++) {
                    System.out.print(gameDirector.mapDirector.getMap()[i][j] + " ");
                }
                System.out.println();
            }

            do {
                System.out.println("1. North");
                System.out.println("2. South");
                System.out.println("3. West");
                System.out.println("4. East");
                System.out.println("5. Switch to GUI");
                if (scan.hasNextInt()) {
                    direction = scan.nextInt();
                    if (direction < 1 || direction > 5) {
                        System.out.println("Not a valid response! Please try again.");
                        continue;
                    }
                } else {
                    System.out.println("Not a valid response! Please try again.");
                    scan.next();
                    continue;
                }
            } while (direction < 1 || direction > 5);
            if (direction == 5)
                break;
            if (MechanicsController.isNotOccupied(direction, gameDirector.mapDirector)) {
                MechanicsController.move(direction, gameDirector.mapDirector);
                System.out.println("Moved.");
            } else {
                int choice = 0;
                do {
                    System.out.println("1. Fight");
                    System.out.println("2. Run");
                    if (scan.hasNextInt()) {
                        choice = scan.nextInt();
                        if (choice < 1 || choice > 2) {
                            System.out.println("Not a valid response! Please try again.");
                            continue;
                        }
                    } else {
                        System.out.println("Not a valid response! Please try again.");
                        scan.next();
                        continue;
                    }
                } while (choice < 1 || choice > 2);
                if (choice == 2) {
                    if (MechanicsController.run()) {
                        System.out.println("Success.");
                    } else {
                        if (MechanicsController.fight(direction, gameDirector.mapDirector, gameDirector.hero)) {
                            System.out.println("Won.");
                            if (random.nextInt(100) >= 85) {
                                System.out.println("Your enemy dropped an artifact.");
                                Artifact artifact;
                                int type = random.nextInt(3) + 1;
                                switch (type) {
                                    case 1: artifact = new Weapon(gameDirector.hero.getType(), MechanicsController.findVillain(direction, gameDirector.mapDirector).getLvl(), gameDirector.hero.getWeapon().getBoost());
                                        if (gameDirector.hero.getWeapon().getLvl() > 0) {
                                            System.out.println("Your Weapon Artifact Stats:");
                                            System.out.println("\tName:" + gameDirector.hero.getWeapon().getName());
                                            System.out.println("\tLevel:" + gameDirector.hero.getWeapon().getLvl());
                                            System.out.println("\tBoost:" + gameDirector.hero.getWeapon().getBoost());
                                        }
                                        System.out.println("\nDropped Weapon Artifact Stats:");
                                        System.out.println("\tName:" + artifact.getName());
                                        System.out.println("\tLevel:" + artifact.getLvl());
                                        System.out.println("\tBoost:" + artifact.getBoost());
                                        break;
                                    case 2: artifact = new Armour(gameDirector.hero.getType(), MechanicsController.findVillain(direction, gameDirector.mapDirector).getLvl(), gameDirector.hero.getArmour().getBoost());
                                        if (gameDirector.hero.getArmour().getLvl() > 0) {
                                            System.out.println("Your Armour Artifact Stats:");
                                            System.out.println("\tName:" + gameDirector.hero.getArmour().getName());
                                            System.out.println("\tLevel:" + gameDirector.hero.getArmour().getLvl());
                                            System.out.println("\tBoost:" + gameDirector.hero.getArmour().getBoost());
                                        }
                                        System.out.println("\nDropped Armour Artifact Stats:");
                                        System.out.println("\tName:" + artifact.getName());
                                        System.out.println("\tLevel:" + artifact.getLvl());
                                        System.out.println("\tBoost:" + artifact.getBoost());
                                        break;
                                    case 3: artifact = new Helm(gameDirector.hero.getType(), MechanicsController.findVillain(direction, gameDirector.mapDirector).getLvl(), gameDirector.hero.getHelm().getBoost());
                                        if (gameDirector.hero.getHelm().getLvl() > 0) {
                                            System.out.println("Your Helm Artifact Stats:");
                                            System.out.println("\tName:" + gameDirector.hero.getHelm().getName());
                                            System.out.println("\tLevel:" + gameDirector.hero.getHelm().getLvl());
                                            System.out.println("\tBoost:" + gameDirector.hero.getHelm().getBoost());
                                        }
                                        System.out.println("\nDropped Helm Artifact Stats:");
                                        System.out.println("\tName:" + artifact.getName());
                                        System.out.println("\tLevel:" + artifact.getLvl());
                                        System.out.println("\tBoost:" + artifact.getBoost());
                                        break;
                                    default:
                                        throw new IllegalStateException("Unexpected value: " + type);
                                }
                                do {
                                    System.out.println("1. Equip It");
                                    System.out.println("2. Leave It");
                                    if (scan.hasNextInt()) {
                                        choice = scan.nextInt();
                                        if (choice < 1 || choice > 2) {
                                            System.out.println("Not a valid response! Please try again.");
                                            continue;
                                        }
                                    } else {
                                        System.out.println("Not a valid response! Please try again.");
                                        scan.next();
                                        continue;
                                    }
                                } while (choice < 1 || choice > 4);
                                if (choice == 1) {
                                    switch (type) {
                                        case 1: MechanicsController.equipWeapon(gameDirector.hero, (Weapon) artifact);
                                            break;
                                        case 2: MechanicsController.equipArmour(gameDirector.hero, (Armour) artifact);
                                            break;
                                        case 3: MechanicsController.equipHelm(gameDirector.hero, (Helm) artifact);
                                            break;
                                    }
                                }
                            }
                            MechanicsController.move(direction, gameDirector.mapDirector);
                        }
                        else {
                            System.out.println("You Died! GAME OVER!");
                            break;
                        }
                    }
                } else {
                    if (MechanicsController.fight(direction, gameDirector.mapDirector, gameDirector.hero)) {
                        System.out.println("Won.");
                        if (random.nextInt(100) >= 85) {
                            System.out.println("Your enemy dropped an artifact.");
                            Artifact artifact;
                            int type = random.nextInt(3) + 1;
                            switch (type) {
                                case 1: artifact = new Weapon(gameDirector.hero.getType(), MechanicsController.findVillain(direction, gameDirector.mapDirector).getLvl(), gameDirector.hero.getWeapon().getBoost());
                                    if (gameDirector.hero.getWeapon().getLvl() > 0) {
                                        System.out.println("Your Weapon Artifact Stats:");
                                        System.out.println("\tName:" + gameDirector.hero.getWeapon().getName());
                                        System.out.println("\tLevel:" + gameDirector.hero.getWeapon().getLvl());
                                        System.out.println("\tBoost:" + gameDirector.hero.getWeapon().getBoost());
                                    }
                                    System.out.println("\nDropped Weapon Artifact Stats:");
                                    System.out.println("\tName:" + artifact.getName());
                                    System.out.println("\tLevel:" + artifact.getLvl());
                                    System.out.println("\tBoost:" + artifact.getBoost());
                                    break;
                                case 2: artifact = new Armour(gameDirector.hero.getType(), MechanicsController.findVillain(direction, gameDirector.mapDirector).getLvl(), gameDirector.hero.getArmour().getBoost());
                                    if (gameDirector.hero.getArmour().getLvl() > 0) {
                                        System.out.println("Your Armour Artifact Stats:");
                                        System.out.println("\tName:" + gameDirector.hero.getArmour().getName());
                                        System.out.println("\tLevel:" + gameDirector.hero.getArmour().getLvl());
                                        System.out.println("\tBoost:" + gameDirector.hero.getArmour().getBoost());
                                    }
                                    System.out.println("\nDropped Armour Artifact Stats:");
                                    System.out.println("\tName:" + artifact.getName());
                                    System.out.println("\tLevel:" + artifact.getLvl());
                                    System.out.println("\tBoost:" + artifact.getBoost());
                                    break;
                                case 3: artifact = new Helm(gameDirector.hero.getType(), MechanicsController.findVillain(direction, gameDirector.mapDirector).getLvl(), gameDirector.hero.getHelm().getBoost());
                                    if (gameDirector.hero.getHelm().getLvl() > 0) {
                                        System.out.println("Your Helm Artifact Stats:");
                                        System.out.println("\tName:" + gameDirector.hero.getHelm().getName());
                                        System.out.println("\tLevel:" + gameDirector.hero.getHelm().getLvl());
                                        System.out.println("\tBoost:" + gameDirector.hero.getHelm().getBoost());
                                    }
                                    System.out.println("\nDropped Helm Artifact Stats:");
                                    System.out.println("\tName:" + artifact.getName());
                                    System.out.println("\tLevel:" + artifact.getLvl());
                                    System.out.println("\tBoost:" + artifact.getBoost());
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + type);
                            }
                            do {
                                System.out.println("1. Equip It");
                                System.out.println("2. Leave It");
                                if (scan.hasNextInt()) {
                                    choice = scan.nextInt();
                                    if (choice < 1 || choice > 2) {
                                        System.out.println("Not a valid response! Please try again.");
                                        continue;
                                    }
                                } else {
                                    System.out.println("Not a valid response! Please try again.");
                                    scan.next();
                                    continue;
                                }
                            } while (choice < 1 || choice > 2);
                            if (choice == 1) {
                                switch (type) {
                                    case 1: MechanicsController.equipWeapon(gameDirector.hero, (Weapon) artifact);
                                        break;
                                    case 2: MechanicsController.equipArmour(gameDirector.hero, (Armour) artifact);
                                        break;
                                    case 3: MechanicsController.equipHelm(gameDirector.hero, (Helm) artifact);
                                        break;
                                }
                            }
                        }
                        MechanicsController.move(direction, gameDirector.mapDirector);
                    }
                    else {
                        System.out.println("You Died! GAME OVER!");
                        break;
                    }
                }
            }

            gameDirector.dbDirector.updateHero(gameDirector.hero);

            System.out.format("|%-9s|%-7s|%-8s|%-4s|%-8s|%-7s|%-4s|%-4s|%-4s|%-12s|%-6s|%-8s|%-11s|%-6s|%-8s|%-15s|%-6s|%-8s|\n", "Name", "Class", "XP", "LVL", "XP2LVLUP", "MAX HP", "HP", "ATK", "DEF", "W NAME", "W LVL", "W BOOST", "A NAME", "A LVL", "A BOOST", "H NAME", "H LVL", "H BOOST");
            Object[] row = new String[] { gameDirector.dbDirector.getResultSet().getString("name"), gameDirector.dbDirector.getResultSet().getString("class"), gameDirector.dbDirector.getResultSet().getInt("XP") + "", gameDirector.dbDirector.getResultSet().getInt("lvl") + "", gameDirector.dbDirector.getResultSet().getInt("lvlUp") + "", gameDirector.dbDirector.getResultSet().getInt("maxHP") + "", gameDirector.dbDirector.getResultSet().getInt("HP") + "", gameDirector.dbDirector.getResultSet().getInt("atk") + "", gameDirector.dbDirector.getResultSet().getInt("def") + "", gameDirector.dbDirector.getResultSet().getString("wname"), gameDirector.dbDirector.getResultSet().getInt("wlvl") + "", gameDirector.dbDirector.getResultSet().getInt("wboost") + "", gameDirector.dbDirector.getResultSet().getString("aname"), gameDirector.dbDirector.getResultSet().getInt("alvl") + "", gameDirector.dbDirector.getResultSet().getInt("aboost") + "", gameDirector.dbDirector.getResultSet().getString("hname"), gameDirector.dbDirector.getResultSet().getInt("hlvl") + "", gameDirector.dbDirector.getResultSet().getInt("hboost") + ""};
            System.out.format("|%-9s|%-7s|%-8s|%-4s|%-8s|%-7s|%-4s|%-4s|%-4s|%-12s|%-6s|%-8s|%-11s|%-6s|%-8s|%-15s|%-6s|%-8s|\n", row);

            if (MechanicsController.winGame(gameDirector.mapDirector)) {
                System.out.println("You Won!");
                break;
            }
        }
        if (direction == 5) {
            gui(4);
        }
        else {
            gameDirector.dbDirector.getResultSet().updateInt("HP", gameDirector.hero.getMaxHP());
            gameDirector.dbDirector.getResultSet().updateRow();
        }
    }

    public static void cli(GameController gameDirector, char phase) throws SQLException {
        Scanner scan = new Scanner(System.in);

        @Min(1)
        @Max(4)
        int option = 0;
        if (phase == 'a') {
            System.out.println("SWINGY\n\n");
            do {
                System.out.println("1. New Hero");
                System.out.println("2. Load Hero");
                System.out.println("3. Switch to GUI");
                System.out.println("4. Quit Game");
                if (scan.hasNextInt()) {
                    option = scan.nextInt();
                    if (option < 1 || option > 4) {
                        System.out.println("Not a valid response! Please try again.");
                    }
                } else {
                    System.out.println("Not a valid response! Please try again.");
                    scan.next();
                }
            } while (option < 1 || option > 4);
        }
        if (option == 1 || phase == 'b') {
            option = 0;
            String name = "";
        
            scan.nextLine();
            do {
                System.out.println("Please enter a name for your hero(Max length: 8 & No spaces):");
                name = scan.nextLine();
            } while (name.equals("") || name.contains(" ") || name.length() > 8);
        
            do {
                System.out.println("1. Human");
                System.out.println("2. Elf");
                System.out.println("3. Dwarf");
                System.out.println("4. Switch to GUI");
                if (scan.hasNextInt()) {
                    option = scan.nextInt();
                    if (option < 1 || option > 4) {
                        System.out.println("Not a valid response! Please try again.");
                    }
                } else {
                    System.out.println("Not a valid response! Please try again.");
                    scan.next();
                }
            } while (option < 1 || option > 4);
            
            if (option == 4)
                gui(2);
            else {
                gameDirector.newHero(option, name);
                startGame(gameDirector);
            }
            
        } else if (option == 2 || phase == 'c') {
            ResultSet heroList = gameDirector.dbDirector.getResultSet();
        
            option = 0;
            do {
                System.out.format("|%-3s|%-9s|%-7s|%-8s|%-4s|%-8s|%-7s|%-4s|%-4s|%-4s|%-12s|%-6s|%-8s|%-11s|%-6s|%-8s|%-15s|%-6s|%-8s|\n", "#", "Name", "Class", "XP", "LVL", "XP2LVLUP", "MAX HP", "HP", "ATK", "DEF", "W NAME", "W LVL", "W BOOST", "A NAME", "A LVL", "A BOOST", "H NAME", "H LVL", "H BOOST");
                heroList.beforeFirst();
                while(heroList.next()) {
                    Object[] row = new String[] { heroList.getRow() + "", heroList.getString("name"), heroList.getString("class"), heroList.getInt("XP") + "", heroList.getInt("lvl") + "", heroList.getInt("lvlUp") + "", heroList.getInt("maxHP") + "", heroList.getInt("HP") + "", heroList.getInt("atk") + "", heroList.getInt("def") + "", heroList.getString("wname"), heroList.getInt("wlvl") + "", heroList.getInt("wboost") + "", heroList.getString("aname"), heroList.getInt("alvl") + "", heroList.getInt("aboost") + "", heroList.getString("hname"), heroList.getInt("hlvl") + "", heroList.getInt("hboost") + ""};
                    System.out.format("|%-3s|%-9s|%-7s|%-8s|%-4s|%-8s|%-7s|%-4s|%-4s|%-4s|%-12s|%-6s|%-8s|%-11s|%-6s|%-8s|%-15s|%-6s|%-8s|\n", row);
                }
                heroList.last();
                System.out.println(heroList.getRow() + 1 + ". Switch to GUI");
                if (scan.hasNextInt()) {
                    option = scan.nextInt();
                    if (option < 1 || option > heroList.getRow() + 1) {
                        System.out.println("Not a valid response! Please try again.");
                    }
                } else {
                    System.out.println("Not a valid response! Please try again.");
                    scan.next();
                }
            } while (option < 1 || option > heroList.getRow() + 1);
            
            if (option == heroList.getRow() + 1)
                gui(3);
            else {
                gameDirector.loadHero(option);
                startGame(gameDirector);
            }
        } else if (phase == 'd') {
            startGame(gameDirector);
        } else if (option == 3) {
            gui(1);
        } else
            System.exit(0);
    }
    
    public static void gui(int pane) throws SQLException {
        JFrame mainWindow = new MainWindow(pane);
        mainWindow.setVisible(true);
    }
    
    public static void main(String[] args) throws SQLException {
        try {
            if (args.length == 1 && args[0].equals("console")) {
                cli(gameDirector, 'a');
            } else if (args.length == 1 && args[0].equals("gui")) {
                gui(1);
            } else {
                System.out.println("\t\tSWINGY\n");
                System.out.println("Usage: java swingy.jar <argument>\n");
                System.out.println("Arguments:\n");
                System.out.println("\tconsole\t\truns swingy in cli mode");
                System.out.println("\tgui\t\truns swingy in gui mode");
                System.out.println("\t-h\t\tdisplays this help manual");
            }
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException | NullPointerException e) {
//            e.printStackTrace();
            System.exit(1);
        }
    }
}
