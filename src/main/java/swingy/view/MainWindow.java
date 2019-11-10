/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingy.view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import swingy.controller.GameController;
import swingy.controller.MechanicsController;
import swingy.model.artifact.*;
import swingy.Swingy;

/**
 *
 * @author jordan
 */
public class MainWindow extends javax.swing.JFrame {

    private final Random random;
    private int type;
    private int direction;
    private int interaction;
    private Artifact artifact;
    /**
     * Creates new form MainWindow
     * @param pane
     */
    public MainWindow(int pane) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            setTitle("SWINGY");
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            initComponents();
            switch(pane) {
                case 1: setContentPane(menuPane);
                        break;
                case 2: setContentPane(newHeroPane);
                        break;
                case 3: setContentPane(loadHeroPane);
                        initLoadHeroBox();
                        break;
                case 4: setContentPane(gamePane);
                        initGamePane();
                        break;
                default: throw new IllegalArgumentException();
            }
            pack();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | SQLException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        this.direction = 0;
        this.interaction = 0;
        this.random = new Random();
    }
    
    public void initLoadHeroBox() throws SQLException {
        ResultSet heroList = Swingy.gameDirector.dbDirector.getResultSet();
        heroList.beforeFirst();
        while(heroList.next()) {
            heroListBox.addItem(heroList.getString("name"));
        }
    }
    
    public void refreshStats() {
        statLvlLabel.setText(Swingy.gameDirector.hero.getLvl() + "");
        statXPLabel.setText(Swingy.gameDirector.hero.getXP() + "/" + Swingy.gameDirector.hero.getLvlUp());
        statHPLabel.setText(Swingy.gameDirector.hero.getHP() + "/" + Swingy.gameDirector.hero.getMaxHP());
        statAtkLabel.setText(Swingy.gameDirector.hero.getAtk() + "");
        statDefLabel.setText(Swingy.gameDirector.hero.getDef() + "");
        
        if (Swingy.gameDirector.hero.getWeapon().getName().equals("")) {
            statWnameLabel.setText("-");
            statWlvlLabel.setText("-");
            statWboostLabel.setText("-");
        } else {
            statWnameLabel.setText(Swingy.gameDirector.hero.getWeapon().getName());
            statWlvlLabel.setText(Swingy.gameDirector.hero.getWeapon().getLvl() + "");
            statWboostLabel.setText(Swingy.gameDirector.hero.getWeapon().getBoost() + "");
        }
        
        if (Swingy.gameDirector.hero.getArmour().getName().equals("")) {
            statAnameLabel.setText("-");
            statAlvlLabel.setText("-");
            statAboostLabel.setText("-");
        } else {
            statAnameLabel.setText(Swingy.gameDirector.hero.getArmour().getName());
            statAlvlLabel.setText(Swingy.gameDirector.hero.getArmour().getLvl() + "");
            statAboostLabel.setText(Swingy.gameDirector.hero.getArmour().getBoost() + "");
        }
        
        if (Swingy.gameDirector.hero.getHelm().getName().equals("")) {
            statHnameLabel.setText("-");
            statHlvlLabel.setText("-");
            statHboostLabel.setText("-");
        } else {
            statHnameLabel.setText(Swingy.gameDirector.hero.getHelm().getName());
            statHlvlLabel.setText(Swingy.gameDirector.hero.getHelm().getLvl() + "");
            statHboostLabel.setText(Swingy.gameDirector.hero.getHelm().getBoost() + "");
        }
    }
    
    public void refreshMap() {
        mapArea.setText("");
        for (int i = 0; i < Swingy.gameDirector.mapDirector.size; i++) {
            for (int j = 0; j < Swingy.gameDirector.mapDirector.size; j++)
                if (Swingy.gameDirector.mapDirector.getMap()[i][j] > 0)
                    mapArea.append(" V ");
                else if (Swingy.gameDirector.mapDirector.getMap()[i][j] == 0)
                    mapArea.append(" 0 ");
                else
                    mapArea.append(" H ");
            mapArea.append("\n");
        }
    }
    
    public void initGamePane() {
        statNameLabel.setText(Swingy.gameDirector.hero.getName());
        statClassLabel.setText(Swingy.gameDirector.hero.getType());
        refreshStats();
        refreshMap();
    }
    
    public void initRunOrFightBox() {
        rofdStatLvlLabel.setText(MechanicsController.findVillain(this.direction, Swingy.gameDirector.mapDirector).getLvl() + "");
        rofdStatHPLabel.setText(MechanicsController.findVillain(this.direction, Swingy.gameDirector.mapDirector).getMaxHP() + "");
        rofdStatAtkLabel.setText(MechanicsController.findVillain(this.direction, Swingy.gameDirector.mapDirector).getAtk() + "");
        rofdStatDefLabel.setText(MechanicsController.findVillain(this.direction, Swingy.gameDirector.mapDirector).getDef() + "");
        runOrFightDialog.setVisible(true);
        runOrFightDialog.pack();
    }
    
    public void initInteraction(int interact) {
        this.interaction = interact;
        switch(interaction) {
            case 1: idLabel.setText("You lost the fight! GAME OVER");
                    break;
            case 2: idLabel.setText("You won the fight!");
                    break;
            case 3: idLabel.setText("You moved!");
                    break;
            case 4: idLabel.setText("You managed to escaped!");
                    break;
            case 5: idLabel.setText("Uh-oh! The villain caught up!");
                    break;
            case 6: idLabel.setText("You beat the Game. Congratulations!");
                    break;
            default: throw new IllegalArgumentException();
        }
        interactionDialog.setVisible(true);
        interactionDialog.pack();
    }
    
    public void initArtifact() {
        if (random.nextInt(100) >= 85) {
            this.type = random.nextInt(3) + 1;
            switch (this.type) {
                case 1: this.artifact = new Weapon(Swingy.gameDirector.hero.getType(), MechanicsController.findVillain(this.direction, Swingy.gameDirector.mapDirector).getLvl(), Swingy.gameDirector.hero.getWeapon().getBoost());
                        adDroppedArtLabel.setText("Dropped Weapon Artifact:");
                        break;
                case 2: this.artifact = new Armour(Swingy.gameDirector.hero.getType(), MechanicsController.findVillain(this.direction, Swingy.gameDirector.mapDirector).getLvl(), Swingy.gameDirector.hero.getArmour().getBoost());
                        adDroppedArtLabel.setText("Dropped Armour Artifact:");
                        break;
                case 3: artifact = new Helm(Swingy.gameDirector.hero.getType(), MechanicsController.findVillain(this.direction, Swingy.gameDirector.mapDirector).getLvl(), Swingy.gameDirector.hero.getHelm().getBoost());
                        adDroppedArtLabel.setText("Dropped Helm Artifact:");
                        break;
                default: throw new IllegalArgumentException();
            }
            adDStatNameLabel.setText(this.artifact.getName() + "");
            adDStatLVLLabel.setText(this.artifact.getLvl() + "");
            adDStatBoostLabel.setText(this.artifact.getBoost() + "");
            artifactDialog.setVisible(true);
            artifactDialog.pack();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        runOrFightDialog = new javax.swing.JDialog();
        rofdLabel = new javax.swing.JLabel();
        rofdLvlLabel = new javax.swing.JLabel();
        rofdAtkLabel = new javax.swing.JLabel();
        rofdDefLabel = new javax.swing.JLabel();
        rofdHPLabel = new javax.swing.JLabel();
        rofdOptionLabel = new javax.swing.JLabel();
        rofdFightBtn = new javax.swing.JButton();
        rofdRunBtn = new javax.swing.JButton();
        rofdStatLvlLabel = new javax.swing.JLabel();
        rofdStatAtkLabel = new javax.swing.JLabel();
        rofdStatDefLabel = new javax.swing.JLabel();
        rofdStatHPLabel = new javax.swing.JLabel();
        rofdIconLabel = new javax.swing.JLabel();
        interactionDialog = new javax.swing.JDialog();
        idIconLabel = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        idOKBtn = new javax.swing.JButton();
        artifactDialog = new javax.swing.JDialog();
        adIconLabel = new javax.swing.JLabel();
        adLabel = new javax.swing.JLabel();
        adDroppedArtLabel = new javax.swing.JLabel();
        adDNameLabel = new javax.swing.JLabel();
        adDLVLLabel = new javax.swing.JLabel();
        adDBoostLabel = new javax.swing.JLabel();
        adLeaveBtn = new javax.swing.JButton();
        adEquipBtn = new javax.swing.JButton();
        adDStatNameLabel = new javax.swing.JLabel();
        adDStatLVLLabel = new javax.swing.JLabel();
        adDStatBoostLabel = new javax.swing.JLabel();
        mainPane = new javax.swing.JLayeredPane();
        menuPane = new javax.swing.JPanel();
        newHeroBtn = new javax.swing.JButton();
        loadHeroBtn = new javax.swing.JButton();
        consoleBtn = new javax.swing.JButton();
        quitBtn = new javax.swing.JButton();
        menuPaneLabel = new javax.swing.JLabel();
        menuLabel = new javax.swing.JLabel();
        loadHeroPane = new javax.swing.JPanel();
        lhpCreateBtn = new javax.swing.JButton();
        lhpLabel = new javax.swing.JLabel();
        lhpLoadBtn = new javax.swing.JButton();
        lhpConsoleBtn = new javax.swing.JButton();
        lhpQuitBtn = new javax.swing.JButton();
        heroStatsScrollPane = new javax.swing.JScrollPane();
        heroStatsArea = new javax.swing.JTextArea();
        lhpHeroListLabel = new javax.swing.JLabel();
        heroListBox = new javax.swing.JComboBox<>();
        newHeroPane = new javax.swing.JPanel();
        nhpCreateBtn = new javax.swing.JButton();
        nhpLabel = new javax.swing.JLabel();
        nhpLoadBtn = new javax.swing.JButton();
        nhpConsoleBtn = new javax.swing.JButton();
        nhpQuitBtn = new javax.swing.JButton();
        classStatsScrollPane = new javax.swing.JScrollPane();
        classStatsArea = new javax.swing.JTextArea();
        nhpNameLabel = new javax.swing.JLabel();
        nhpClassBox = new javax.swing.JComboBox<>();
        nameField = new javax.swing.JTextField();
        nhpClassLabel = new javax.swing.JLabel();
        gamePane = new javax.swing.JPanel();
        gpLabel = new javax.swing.JLabel();
        mapScrollPane = new javax.swing.JScrollPane();
        mapArea = new javax.swing.JTextArea();
        gpMapLabel = new javax.swing.JLabel();
        gpStatsLabel = new javax.swing.JLabel();
        gpClassLabel = new javax.swing.JLabel();
        gpHPLabel = new javax.swing.JLabel();
        gpAtkLabel = new javax.swing.JLabel();
        gpDefLabel = new javax.swing.JLabel();
        gpWnameLabel = new javax.swing.JLabel();
        gpWlvlLabel = new javax.swing.JLabel();
        gpLvlLabel = new javax.swing.JLabel();
        gpWboostLabel = new javax.swing.JLabel();
        gpAnameLabel = new javax.swing.JLabel();
        gpAlvlLabel = new javax.swing.JLabel();
        gpAboostLabel = new javax.swing.JLabel();
        gpHnameLabel = new javax.swing.JLabel();
        gpHlvlLabel = new javax.swing.JLabel();
        gpHboostLabel = new javax.swing.JLabel();
        statClassLabel = new javax.swing.JLabel();
        statLvlLabel = new javax.swing.JLabel();
        statHPLabel = new javax.swing.JLabel();
        statAtkLabel = new javax.swing.JLabel();
        statDefLabel = new javax.swing.JLabel();
        statWnameLabel = new javax.swing.JLabel();
        statWlvlLabel = new javax.swing.JLabel();
        statWboostLabel = new javax.swing.JLabel();
        statAnameLabel = new javax.swing.JLabel();
        statAlvlLabel = new javax.swing.JLabel();
        statAboostLabel = new javax.swing.JLabel();
        statHnameLabel = new javax.swing.JLabel();
        statHlvlLabel = new javax.swing.JLabel();
        statHboostLabel = new javax.swing.JLabel();
        northBtn = new javax.swing.JButton();
        eastBtn = new javax.swing.JButton();
        westBtn = new javax.swing.JButton();
        southBtn = new javax.swing.JButton();
        gpConsoleBtn = new javax.swing.JButton();
        gpQuitBtn = new javax.swing.JButton();
        gpNameLabel = new javax.swing.JLabel();
        statNameLabel = new javax.swing.JLabel();
        gpXPLabel = new javax.swing.JLabel();
        statXPLabel = new javax.swing.JLabel();

        rofdLabel.setText("You have encounted a Villain:");

        rofdLvlLabel.setText("LVL");

        rofdAtkLabel.setText("Attack");

        rofdDefLabel.setText("Defense");

        rofdHPLabel.setText("HP");

        rofdOptionLabel.setText("What do you want to do?");

        rofdFightBtn.setText("Fight");
        rofdFightBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rofdFightBtnActionPerformed(evt);
            }
        });

        rofdRunBtn.setText("Run");
        rofdRunBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rofdRunBtnActionPerformed(evt);
            }
        });

        rofdIconLabel.setIcon(new javax.swing.ImageIcon("/home/jordan/Desktop/swingy2/src/main/resources/circle-cropped.png")); // NOI18N

        javax.swing.GroupLayout runOrFightDialogLayout = new javax.swing.GroupLayout(runOrFightDialog.getContentPane());
        runOrFightDialog.getContentPane().setLayout(runOrFightDialogLayout);
        runOrFightDialogLayout.setHorizontalGroup(
            runOrFightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(runOrFightDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rofdIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(runOrFightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(runOrFightDialogLayout.createSequentialGroup()
                        .addComponent(rofdFightBtn)
                        .addGap(51, 51, 51)
                        .addComponent(rofdRunBtn))
                    .addComponent(rofdOptionLabel)
                    .addGroup(runOrFightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(rofdLabel)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, runOrFightDialogLayout.createSequentialGroup()
                            .addGroup(runOrFightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(runOrFightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(runOrFightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(runOrFightDialogLayout.createSequentialGroup()
                                            .addComponent(rofdLvlLabel)
                                            .addGap(107, 107, 107))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, runOrFightDialogLayout.createSequentialGroup()
                                            .addComponent(rofdAtkLabel)
                                            .addGap(88, 88, 88)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, runOrFightDialogLayout.createSequentialGroup()
                                        .addComponent(rofdDefLabel)
                                        .addGap(77, 77, 77)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, runOrFightDialogLayout.createSequentialGroup()
                                    .addComponent(rofdHPLabel)
                                    .addGap(113, 113, 113)))
                            .addGroup(runOrFightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(rofdStatLvlLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rofdStatAtkLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rofdStatDefLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rofdStatHPLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        runOrFightDialogLayout.setVerticalGroup(
            runOrFightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(runOrFightDialogLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(rofdLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(runOrFightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(runOrFightDialogLayout.createSequentialGroup()
                        .addGroup(runOrFightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(rofdLvlLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rofdStatLvlLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(runOrFightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(rofdAtkLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rofdStatAtkLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(runOrFightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rofdDefLabel)
                            .addComponent(rofdStatDefLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(rofdIconLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(runOrFightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rofdStatHPLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rofdHPLabel))
                .addGap(18, 18, 18)
                .addComponent(rofdOptionLabel)
                .addGap(18, 18, 18)
                .addGroup(runOrFightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rofdFightBtn)
                    .addComponent(rofdRunBtn))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        idIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/circle-cropped.png"))); // NOI18N

        idOKBtn.setText("OK");
        idOKBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idOKBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout interactionDialogLayout = new javax.swing.GroupLayout(interactionDialog.getContentPane());
        interactionDialog.getContentPane().setLayout(interactionDialogLayout);
        interactionDialogLayout.setHorizontalGroup(
            interactionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(interactionDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(interactionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(interactionDialogLayout.createSequentialGroup()
                        .addComponent(idIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, interactionDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(idOKBtn)))
                .addContainerGap())
        );
        interactionDialogLayout.setVerticalGroup(
            interactionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(interactionDialogLayout.createSequentialGroup()
                .addGroup(interactionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(interactionDialogLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(idIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(interactionDialogLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(idLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(idOKBtn)
                .addContainerGap())
        );

        adIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/circle-cropped.png"))); // NOI18N

        adLabel.setText("Your Enemy dropped an Artifact:");

        adDroppedArtLabel.setText("Dropped Weapon Artifact:");

        adDNameLabel.setText("Name");

        adDLVLLabel.setText("LVL");

        adDBoostLabel.setText("Boost");

        adLeaveBtn.setText("Leave");
        adLeaveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adLeaveBtnActionPerformed(evt);
            }
        });

        adEquipBtn.setText("Equip");
        adEquipBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adEquipBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout artifactDialogLayout = new javax.swing.GroupLayout(artifactDialog.getContentPane());
        artifactDialog.getContentPane().setLayout(artifactDialogLayout);
        artifactDialogLayout.setHorizontalGroup(
            artifactDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(artifactDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(artifactDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(artifactDialogLayout.createSequentialGroup()
                        .addComponent(adIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(artifactDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(artifactDialogLayout.createSequentialGroup()
                                .addGroup(artifactDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(adDLVLLabel)
                                    .addComponent(adDBoostLabel)
                                    .addComponent(adDNameLabel))
                                .addGap(82, 82, 82)
                                .addGroup(artifactDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(adDStatNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(adDStatBoostLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(adDStatLVLLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(artifactDialogLayout.createSequentialGroup()
                                .addGroup(artifactDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(adDroppedArtLabel)
                                    .addComponent(adLabel))
                                .addGap(0, 31, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, artifactDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(adEquipBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(adLeaveBtn)))
                .addContainerGap())
        );
        artifactDialogLayout.setVerticalGroup(
            artifactDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(artifactDialogLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(adIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(artifactDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(adLeaveBtn)
                    .addComponent(adEquipBtn))
                .addGap(18, 18, 18))
            .addGroup(artifactDialogLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(adLabel)
                .addGap(18, 18, 18)
                .addComponent(adDroppedArtLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(artifactDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(adDNameLabel)
                    .addComponent(adDStatNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(artifactDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(adDLVLLabel)
                    .addComponent(adDStatLVLLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(artifactDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(adDBoostLabel)
                    .addComponent(adDStatBoostLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        newHeroBtn.setText("New Hero");
        newHeroBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newHeroBtnActionPerformed(evt);
            }
        });

        loadHeroBtn.setText("Load Hero");
        loadHeroBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadHeroBtnActionPerformed(evt);
            }
        });

        consoleBtn.setText("Console");
        consoleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    consoleBtnActionPerformed(evt);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        quitBtn.setText("Quit");
        quitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitBtnActionPerformed(evt);
            }
        });

        menuPaneLabel.setFont(new java.awt.Font("Purisa", 3, 65)); // NOI18N
        menuPaneLabel.setText("SWINGY");

        menuLabel.setText("Main Menu");

        javax.swing.GroupLayout menuPaneLayout = new javax.swing.GroupLayout(menuPane);
        menuPane.setLayout(menuPaneLayout);
        menuPaneLayout.setHorizontalGroup(
            menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuPaneLayout.createSequentialGroup()
                        .addComponent(consoleBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(quitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(menuPaneLayout.createSequentialGroup()
                        .addGroup(menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(menuPaneLayout.createSequentialGroup()
                                .addComponent(newHeroBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(loadHeroBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(menuPaneLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(menuPaneLayout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addComponent(menuLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        menuPaneLayout.setVerticalGroup(
            menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menuPaneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuLabel)
                .addGap(23, 23, 23)
                .addGroup(menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(newHeroBtn)
                    .addComponent(loadHeroBtn))
                .addGap(18, 18, 18)
                .addGroup(menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuPaneLayout.createSequentialGroup()
                        .addComponent(consoleBtn)
                        .addContainerGap(24, Short.MAX_VALUE))
                    .addGroup(menuPaneLayout.createSequentialGroup()
                        .addComponent(quitBtn)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        lhpCreateBtn.setText("Create");
        lhpCreateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lhpCreateBtnActionPerformed(evt);
            }
        });

        lhpLabel.setFont(new java.awt.Font("Purisa", 3, 65)); // NOI18N
        lhpLabel.setText("SWINGY");

        lhpLoadBtn.setText("Load");
        lhpLoadBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lhpLoadBtnActionPerformed(evt);
            }
        });

        lhpConsoleBtn.setText("Console");
        lhpConsoleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    lhpConsoleBtnActionPerformed(evt);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        lhpQuitBtn.setText("Quit");
        lhpQuitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lhpQuitBtnActionPerformed(evt);
            }
        });

        heroStatsArea.setEditable(false);
        heroStatsArea.setColumns(20);
        heroStatsArea.setRows(5);
        heroStatsScrollPane.setViewportView(heroStatsArea);

        lhpHeroListLabel.setText("Hero");

        heroListBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                heroListBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout loadHeroPaneLayout = new javax.swing.GroupLayout(loadHeroPane);
        loadHeroPane.setLayout(loadHeroPaneLayout);
        loadHeroPaneLayout.setHorizontalGroup(
            loadHeroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loadHeroPaneLayout.createSequentialGroup()
                .addGroup(loadHeroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(loadHeroPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lhpLabel))
                    .addGroup(loadHeroPaneLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(loadHeroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(heroListBox, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, loadHeroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(loadHeroPaneLayout.createSequentialGroup()
                                    .addComponent(lhpConsoleBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(47, 47, 47)
                                    .addComponent(lhpQuitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, loadHeroPaneLayout.createSequentialGroup()
                                    .addComponent(lhpLoadBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lhpCreateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(heroStatsScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(loadHeroPaneLayout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(lhpHeroListLabel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        loadHeroPaneLayout.setVerticalGroup(
            loadHeroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loadHeroPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lhpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lhpHeroListLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(heroListBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(heroStatsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(loadHeroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lhpLoadBtn)
                    .addComponent(lhpCreateBtn))
                .addGap(18, 18, 18)
                .addGroup(loadHeroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lhpConsoleBtn)
                    .addComponent(lhpQuitBtn))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        nhpCreateBtn.setText("Create");
        nhpCreateBtn.setEnabled(false);
        nhpCreateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nhpCreateBtnActionPerformed(evt);
            }
        });

        nhpLabel.setFont(new java.awt.Font("Purisa", 3, 65)); // NOI18N
        nhpLabel.setText("SWINGY");

        nhpLoadBtn.setText("Load");
        nhpLoadBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nhpLoadBtnActionPerformed(evt);
            }
        });

        nhpConsoleBtn.setText("Console");
        nhpConsoleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    nhpConsoleBtnActionPerformed(evt);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        nhpQuitBtn.setText("Quit");
        nhpQuitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nhpQuitBtnActionPerformed(evt);
            }
        });

        classStatsArea.setEditable(false);
        classStatsArea.setColumns(20);
        classStatsArea.setRows(5);
        classStatsScrollPane.setViewportView(classStatsArea);

        nhpNameLabel.setText("Name");

        nhpClassBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                nhpClassBoxItemStateChanged(evt);
            }
        });
        nhpClassBox.addItem("Human");
        nhpClassBox.addItem("Elf");
        nhpClassBox.addItem("Dwarf");

        nhpClassLabel.setText("Class");

        javax.swing.GroupLayout newHeroPaneLayout = new javax.swing.GroupLayout(newHeroPane);
        newHeroPane.setLayout(newHeroPaneLayout);
        newHeroPaneLayout.setHorizontalGroup(
            newHeroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newHeroPaneLayout.createSequentialGroup()
                .addGroup(newHeroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(newHeroPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(nhpLabel))
                    .addGroup(newHeroPaneLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(newHeroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(classStatsScrollPane, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nhpClassBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nameField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, newHeroPaneLayout.createSequentialGroup()
                                .addGroup(newHeroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(nhpConsoleBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nhpCreateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(newHeroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nhpLoadBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nhpQuitBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(newHeroPaneLayout.createSequentialGroup()
                        .addGap(139, 139, 139)
                        .addComponent(nhpNameLabel))
                    .addGroup(newHeroPaneLayout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(nhpClassLabel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        newHeroPaneLayout.setVerticalGroup(
            newHeroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, newHeroPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nhpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nhpNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nhpClassLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nhpClassBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(classStatsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(newHeroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhpCreateBtn)
                    .addComponent(nhpLoadBtn))
                .addGap(18, 18, 18)
                .addGroup(newHeroPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhpConsoleBtn)
                    .addComponent(nhpQuitBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gpLabel.setFont(new java.awt.Font("Purisa", 3, 65)); // NOI18N
        gpLabel.setText("SWINGY");

        mapArea.setColumns(20);
        mapArea.setRows(5);
        mapScrollPane.setViewportView(mapArea);

        gpMapLabel.setText("Map");

        gpStatsLabel.setText("Stats");

        gpClassLabel.setText("Class");

        gpHPLabel.setText("HP");

        gpAtkLabel.setText("Attack");

        gpDefLabel.setText("Defense");

        gpWnameLabel.setText("Weapon Name");

        gpWlvlLabel.setText("Weapon Lvl");

        gpLvlLabel.setText("LVL");

        gpWboostLabel.setText("Weapon Boost");

        gpAnameLabel.setText("Armour Name");

        gpAlvlLabel.setText("Armour Lvl");

        gpAboostLabel.setText("Armour Boost");

        gpHnameLabel.setText("Helm Name");

        gpHlvlLabel.setText("Helm Lvl");

        gpHboostLabel.setText("Helm Boost");

        northBtn.setText("North");
        northBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                northBtnActionPerformed(evt);
            }
        });

        eastBtn.setText("East");
        eastBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eastBtnActionPerformed(evt);
            }
        });

        westBtn.setText("West");
        westBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                westBtnActionPerformed(evt);
            }
        });

        southBtn.setText("South");
        southBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                southBtnActionPerformed(evt);
            }
        });

        gpConsoleBtn.setText("Console");
        gpConsoleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    gpConsoleBtnActionPerformed(evt);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        gpQuitBtn.setText("Quit");
        gpQuitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gpQuitBtnActionPerformed(evt);
            }
        });

        gpNameLabel.setText("Name");

        gpXPLabel.setText("XP");

        javax.swing.GroupLayout gamePaneLayout = new javax.swing.GroupLayout(gamePane);
        gamePane.setLayout(gamePaneLayout);
        gamePaneLayout.setHorizontalGroup(
            gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamePaneLayout.createSequentialGroup()
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gamePaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mapScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(gamePaneLayout.createSequentialGroup()
                                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(westBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(gpConsoleBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(eastBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(gpQuitBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gamePaneLayout.createSequentialGroup()
                                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(gpHPLabel)
                                    .addComponent(gpAtkLabel)
                                    .addComponent(gpDefLabel)
                                    .addComponent(gpWnameLabel)
                                    .addComponent(gpWlvlLabel)
                                    .addComponent(gpLvlLabel)
                                    .addComponent(gpWboostLabel)
                                    .addComponent(gpAnameLabel)
                                    .addComponent(gpAlvlLabel)
                                    .addComponent(gpAboostLabel)
                                    .addComponent(gpHnameLabel)
                                    .addComponent(gpHlvlLabel)
                                    .addComponent(gpHboostLabel)
                                    .addComponent(gpClassLabel)
                                    .addComponent(gpNameLabel)
                                    .addComponent(gpXPLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(statHlvlLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statHnameLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statAboostLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statAlvlLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statAnameLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statWboostLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statWlvlLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statWnameLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statDefLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statAtkLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statHPLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statClassLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statLvlLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statHboostLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statXPLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)))))
                    .addGroup(gamePaneLayout.createSequentialGroup()
                        .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(gamePaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(gpLabel))
                            .addGroup(gamePaneLayout.createSequentialGroup()
                                .addGap(116, 116, 116)
                                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(southBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                                    .addComponent(northBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(gamePaneLayout.createSequentialGroup()
                                .addGap(144, 144, 144)
                                .addComponent(gpMapLabel))
                            .addGroup(gamePaneLayout.createSequentialGroup()
                                .addGap(129, 129, 129)
                                .addComponent(gpStatsLabel)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        gamePaneLayout.setVerticalGroup(
            gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gpMapLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mapScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gpStatsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gpNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gpClassLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statClassLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(gamePaneLayout.createSequentialGroup()
                        .addComponent(gpLvlLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gpXPLabel))
                    .addGroup(gamePaneLayout.createSequentialGroup()
                        .addComponent(statLvlLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statXPLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gpHPLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statHPLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gpAtkLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statAtkLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gpDefLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statDefLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gpWnameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statWnameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gpWlvlLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statWlvlLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gpWboostLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statWboostLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gpAnameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statAnameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gpAlvlLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statAlvlLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gpAboostLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statAboostLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gpHnameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statHnameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gpHlvlLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statHlvlLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gpHboostLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statHboostLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(northBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eastBtn)
                    .addComponent(westBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(southBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gpConsoleBtn)
                    .addComponent(gpQuitBtn))
                .addGap(18, 18, 18))
        );

        mainPane.setLayer(menuPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainPane.setLayer(loadHeroPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainPane.setLayer(newHeroPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainPane.setLayer(gamePane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout mainPaneLayout = new javax.swing.GroupLayout(mainPane);
        mainPane.setLayout(mainPaneLayout);
        mainPaneLayout.setHorizontalGroup(
            mainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(mainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(loadHeroPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(newHeroPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(mainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainPaneLayout.createSequentialGroup()
                    .addGap(112, 112, 112)
                    .addComponent(gamePane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(113, Short.MAX_VALUE)))
        );
        mainPaneLayout.setVerticalGroup(
            mainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(mainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainPaneLayout.createSequentialGroup()
                    .addComponent(loadHeroPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(newHeroPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(mainPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainPaneLayout.createSequentialGroup()
                    .addGap(453, 453, 453)
                    .addComponent(gamePane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(454, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPane)
        );

        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            public void changed() {
                if (nameField.getText().equals(""))
                    nhpCreateBtn.setEnabled(false);
                else
                    nhpCreateBtn.setEnabled(true);
            }
        });

        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                changed();
            }
            @Override
            public void keyTyped(KeyEvent e) {
                changed();
            }
            @Override
            public void keyPressed(KeyEvent e) {
                changed();
            }

            public void changed() {
                if (nameField.getText().contains(" "))
                    nhpCreateBtn.setEnabled(false);
                else if (nameField.getText().length() > 8)
                    nhpCreateBtn.setEnabled(false);
                else
                    nhpCreateBtn.setEnabled(true);
            }
         });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadHeroBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadHeroBtnActionPerformed
        try {
            setContentPane(loadHeroPane);
            initLoadHeroBox();
            pack();
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_loadHeroBtnActionPerformed

    private void consoleBtnActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {//GEN-FIRST:event_consoleBtnActionPerformed
        setVisible(false);
        Swingy.cli(Swingy.gameDirector, 'a');
    }//GEN-LAST:event_consoleBtnActionPerformed

    private void quitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitBtnActionPerformed
        setVisible(false);
        dispose();
        System.exit(0);
    }//GEN-LAST:event_quitBtnActionPerformed

    private void newHeroBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newHeroBtnActionPerformed
        setContentPane(newHeroPane);
        pack();
    }//GEN-LAST:event_newHeroBtnActionPerformed

    private void lhpQuitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lhpQuitBtnActionPerformed
        setVisible(false);
        dispose();
        System.exit(0);
    }//GEN-LAST:event_lhpQuitBtnActionPerformed

    private void lhpConsoleBtnActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {//GEN-FIRST:event_lhpConsoleBtnActionPerformed
        setVisible(false);
        Swingy.cli(Swingy.gameDirector, 'c');
    }//GEN-LAST:event_lhpConsoleBtnActionPerformed

    private void lhpCreateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lhpCreateBtnActionPerformed
        setContentPane(newHeroPane);
        pack();
    }//GEN-LAST:event_lhpCreateBtnActionPerformed

    private void heroListBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_heroListBoxItemStateChanged
        try {
            ResultSet heroStats = Swingy.gameDirector.dbDirector.getResultSet();
            heroStats.absolute(heroListBox.getSelectedIndex() + 1);
            heroStatsArea.setText("");
            heroStatsArea.append("Class: " + heroStats.getString("class") + '\n');
            heroStatsArea.append("XP: " + heroStats.getInt("XP") + '\n');
            heroStatsArea.append("LVL: " + heroStats.getInt("lvl") + '\n');
            heroStatsArea.append("XP2LVLUP: " + heroStats.getInt("lvlUp") + '\n');
            heroStatsArea.append("HP: " + heroStats.getInt("HP") + '\n');
            heroStatsArea.append("Attack: " + heroStats.getInt("atk") + '\n');
            heroStatsArea.append("Defense: " + heroStats.getInt("def") + '\n');
            heroStatsArea.append("Weapon Name: " + heroStats.getString("wname") + '\n');
            heroStatsArea.append("Weapon LVL: " + heroStats.getInt("wlvl") + '\n');
            heroStatsArea.append("Weapon Boost: " + heroStats.getInt("wboost") + '\n');
            heroStatsArea.append("Armour Name: " + heroStats.getString("aname") + '\n');
            heroStatsArea.append("Armour LVL: " + heroStats.getInt("alvl") + '\n');
            heroStatsArea.append("Armour Boost: " + heroStats.getInt("aboost") + '\n');
            heroStatsArea.append("Helm Name: " + heroStats.getString("hname") + '\n');
            heroStatsArea.append("Helm LVL: " + heroStats.getInt("hlvl") + '\n');
            heroStatsArea.append("Helm Boost: " + heroStats.getInt("hboost") + '\n');
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_heroListBoxItemStateChanged

    private void nhpCreateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhpCreateBtnActionPerformed
        try {
            switch ((String) nhpClassBox.getSelectedItem()) {
                case "Human":   Swingy.gameDirector.newHero(1, nameField.getText());
                                break;
                case "Elf":   Swingy.gameDirector.newHero(2, nameField.getText());
                                break;
                case "Dwarf":   Swingy.gameDirector.newHero(3, nameField.getText());
                                break;
                default: throw new IllegalArgumentException();
            }
            initGamePane();
            setContentPane(gamePane);
            pack();
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_nhpCreateBtnActionPerformed

    private void nhpConsoleBtnActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {//GEN-FIRST:event_nhpConsoleBtnActionPerformed
        setVisible(false);
        Swingy.cli(Swingy.gameDirector, 'b');
    }//GEN-LAST:event_nhpConsoleBtnActionPerformed

    private void nhpQuitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhpQuitBtnActionPerformed
        setVisible(false);
        dispose();
        System.exit(0);
    }//GEN-LAST:event_nhpQuitBtnActionPerformed

    private void nhpClassBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_nhpClassBoxItemStateChanged
        classStatsArea.setText("");
        switch ((String) nhpClassBox.getSelectedItem()) {
            case "Human":   classStatsArea.append("Attack: 25\nDefense: 25\nHP: 25");
                            break;
            case "Elf":     classStatsArea.append("Attack: 35\nDefense: 15\nHP: 25");
                            break;
            case "Dwarf":   classStatsArea.append("Attack: 15\nDefense: 35\nHP: 25");
                            break;
            default: throw new IllegalArgumentException();
        }
    }//GEN-LAST:event_nhpClassBoxItemStateChanged

    private void nhpLoadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nhpLoadBtnActionPerformed
        try {
            setContentPane(loadHeroPane);
            initLoadHeroBox();
            pack();
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_nhpLoadBtnActionPerformed

    private void lhpLoadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lhpLoadBtnActionPerformed
        try {
            Swingy.gameDirector.loadHero(heroListBox.getSelectedIndex() + 1);
            initGamePane();
            setContentPane(gamePane);
            pack();
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_lhpLoadBtnActionPerformed

    private void gpQuitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gpQuitBtnActionPerformed
        setVisible(false);
        dispose();
        System.exit(0);
    }//GEN-LAST:event_gpQuitBtnActionPerformed

    private void gpConsoleBtnActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {//GEN-FIRST:event_gpConsoleBtnActionPerformed
        setVisible(false);
        Swingy.cli(Swingy.gameDirector, 'd');
    }//GEN-LAST:event_gpConsoleBtnActionPerformed

    private void northBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_northBtnActionPerformed
        this.direction = 1;
        if (MechanicsController.isNotOccupied(1, Swingy.gameDirector.mapDirector)) {
            initInteraction(3);
        } else
            initRunOrFightBox();
    }//GEN-LAST:event_northBtnActionPerformed

    private void eastBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eastBtnActionPerformed
        this.direction = 4;
        if (MechanicsController.isNotOccupied(4, Swingy.gameDirector.mapDirector)) {
            initInteraction(3);
        } else
            initRunOrFightBox();
    }//GEN-LAST:event_eastBtnActionPerformed

    private void southBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_southBtnActionPerformed
        this.direction = 2;
        if (MechanicsController.isNotOccupied(2, Swingy.gameDirector.mapDirector)) {
            initInteraction(3);
        } else
            initRunOrFightBox();
    }//GEN-LAST:event_southBtnActionPerformed

    private void westBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_westBtnActionPerformed
        this.direction = 3;
        if (MechanicsController.isNotOccupied(3, Swingy.gameDirector.mapDirector)) {
            initInteraction(3);
        } else
            initRunOrFightBox();
    }//GEN-LAST:event_westBtnActionPerformed

    private void rofdFightBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rofdFightBtnActionPerformed
        runOrFightDialog.setVisible(false);
        if (MechanicsController.fight(direction, Swingy.gameDirector.mapDirector, Swingy.gameDirector.hero))
            initInteraction(2);
        else
            initInteraction(1);
    }//GEN-LAST:event_rofdFightBtnActionPerformed

    private void idOKBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idOKBtnActionPerformed
        try {
            interactionDialog.setVisible(false);
            Swingy.gameDirector.dbDirector.updateHero(Swingy.gameDirector.hero);
            switch(this.interaction) {
                case 1: setContentPane(menuPane);
                        pack();
                        Swingy.gameDirector.dbDirector.getResultSet().updateInt("HP", Swingy.gameDirector.hero.getMaxHP());
                        Swingy.gameDirector.dbDirector.getResultSet().updateRow();
                        break;
                case 2: MechanicsController.move(this.direction, Swingy.gameDirector.mapDirector);
                        initArtifact();
                        if (MechanicsController.winGame(Swingy.gameDirector.mapDirector))
                            initInteraction(6);
                        refreshStats();
                        refreshMap();
                        break;
                case 3: MechanicsController.move(this.direction, Swingy.gameDirector.mapDirector);
                        refreshMap();
                        if (MechanicsController.winGame(Swingy.gameDirector.mapDirector))
                            initInteraction(6);
                        break;
                case 4: break;
                case 5: if (MechanicsController.fight(direction, Swingy.gameDirector.mapDirector, Swingy.gameDirector.hero))
                            initInteraction(2);
                        else
                            initInteraction(1);
                        break;
                case 6: setContentPane(menuPane);
                        pack();
                        Swingy.gameDirector.dbDirector.getResultSet().updateInt("HP", Swingy.gameDirector.hero.getMaxHP());
                        Swingy.gameDirector.dbDirector.getResultSet().updateRow();
                        break;
                default: throw new IllegalArgumentException();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_idOKBtnActionPerformed

    private void adEquipBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adEquipBtnActionPerformed
        try {
            artifactDialog.setVisible(false);
            switch (this.type) {
                case 1: MechanicsController.equipWeapon(Swingy.gameDirector.hero, (Weapon) this.artifact);
                        break;
                case 2: MechanicsController.equipArmour(Swingy.gameDirector.hero, (Armour) this.artifact);
                        break;
                case 3: MechanicsController.equipHelm(Swingy.gameDirector.hero, (Helm) this.artifact);
                        break;
            }
            Swingy.gameDirector.dbDirector.updateHero(Swingy.gameDirector.hero);
            refreshStats();
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_adEquipBtnActionPerformed

    private void adLeaveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adLeaveBtnActionPerformed
        artifactDialog.setVisible(false);
    }//GEN-LAST:event_adLeaveBtnActionPerformed

    private void rofdRunBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rofdRunBtnActionPerformed
        runOrFightDialog.setVisible(false);
        if (MechanicsController.run())
            initInteraction(4);
        else
            initInteraction(5);
    }//GEN-LAST:event_rofdRunBtnActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new MainWindow().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel adDBoostLabel;
    private javax.swing.JLabel adDLVLLabel;
    private javax.swing.JLabel adDNameLabel;
    private javax.swing.JLabel adDStatBoostLabel;
    private javax.swing.JLabel adDStatLVLLabel;
    private javax.swing.JLabel adDStatNameLabel;
    private javax.swing.JLabel adDroppedArtLabel;
    private javax.swing.JButton adEquipBtn;
    private javax.swing.JLabel adIconLabel;
    private javax.swing.JLabel adLabel;
    private javax.swing.JButton adLeaveBtn;
    private javax.swing.JDialog artifactDialog;
    private javax.swing.JTextArea classStatsArea;
    private javax.swing.JScrollPane classStatsScrollPane;
    private javax.swing.JButton consoleBtn;
    private javax.swing.JButton eastBtn;
    private javax.swing.JPanel gamePane;
    private javax.swing.JLabel gpAboostLabel;
    private javax.swing.JLabel gpAlvlLabel;
    private javax.swing.JLabel gpAnameLabel;
    private javax.swing.JLabel gpAtkLabel;
    private javax.swing.JLabel gpClassLabel;
    private javax.swing.JButton gpConsoleBtn;
    private javax.swing.JLabel gpDefLabel;
    private javax.swing.JLabel gpHPLabel;
    private javax.swing.JLabel gpHboostLabel;
    private javax.swing.JLabel gpHlvlLabel;
    private javax.swing.JLabel gpHnameLabel;
    private javax.swing.JLabel gpLabel;
    private javax.swing.JLabel gpLvlLabel;
    private javax.swing.JLabel gpMapLabel;
    private javax.swing.JLabel gpNameLabel;
    private javax.swing.JButton gpQuitBtn;
    private javax.swing.JLabel gpStatsLabel;
    private javax.swing.JLabel gpWboostLabel;
    private javax.swing.JLabel gpWlvlLabel;
    private javax.swing.JLabel gpWnameLabel;
    private javax.swing.JLabel gpXPLabel;
    private javax.swing.JComboBox<String> heroListBox;
    private javax.swing.JTextArea heroStatsArea;
    private javax.swing.JScrollPane heroStatsScrollPane;
    private javax.swing.JLabel idIconLabel;
    private javax.swing.JLabel idLabel;
    private javax.swing.JButton idOKBtn;
    private javax.swing.JDialog interactionDialog;
    private javax.swing.JButton lhpConsoleBtn;
    private javax.swing.JButton lhpCreateBtn;
    private javax.swing.JLabel lhpHeroListLabel;
    private javax.swing.JLabel lhpLabel;
    private javax.swing.JButton lhpLoadBtn;
    private javax.swing.JButton lhpQuitBtn;
    private javax.swing.JButton loadHeroBtn;
    private javax.swing.JPanel loadHeroPane;
    private javax.swing.JLayeredPane mainPane;
    private javax.swing.JTextArea mapArea;
    private javax.swing.JScrollPane mapScrollPane;
    private javax.swing.JLabel menuLabel;
    private javax.swing.JPanel menuPane;
    private javax.swing.JLabel menuPaneLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JButton newHeroBtn;
    private javax.swing.JPanel newHeroPane;
    private javax.swing.JComboBox<String> nhpClassBox;
    private javax.swing.JLabel nhpClassLabel;
    private javax.swing.JButton nhpConsoleBtn;
    private javax.swing.JButton nhpCreateBtn;
    private javax.swing.JLabel nhpLabel;
    private javax.swing.JButton nhpLoadBtn;
    private javax.swing.JLabel nhpNameLabel;
    private javax.swing.JButton nhpQuitBtn;
    private javax.swing.JButton northBtn;
    private javax.swing.JButton quitBtn;
    private javax.swing.JLabel rofdAtkLabel;
    private javax.swing.JLabel rofdDefLabel;
    private javax.swing.JButton rofdFightBtn;
    private javax.swing.JLabel rofdHPLabel;
    private javax.swing.JLabel rofdIconLabel;
    private javax.swing.JLabel rofdLabel;
    private javax.swing.JLabel rofdLvlLabel;
    private javax.swing.JLabel rofdOptionLabel;
    private javax.swing.JButton rofdRunBtn;
    private javax.swing.JLabel rofdStatAtkLabel;
    private javax.swing.JLabel rofdStatDefLabel;
    private javax.swing.JLabel rofdStatHPLabel;
    private javax.swing.JLabel rofdStatLvlLabel;
    private javax.swing.JDialog runOrFightDialog;
    private javax.swing.JButton southBtn;
    private javax.swing.JLabel statAboostLabel;
    private javax.swing.JLabel statAlvlLabel;
    private javax.swing.JLabel statAnameLabel;
    private javax.swing.JLabel statAtkLabel;
    private javax.swing.JLabel statClassLabel;
    private javax.swing.JLabel statDefLabel;
    private javax.swing.JLabel statHPLabel;
    private javax.swing.JLabel statHboostLabel;
    private javax.swing.JLabel statHlvlLabel;
    private javax.swing.JLabel statHnameLabel;
    private javax.swing.JLabel statLvlLabel;
    private javax.swing.JLabel statNameLabel;
    private javax.swing.JLabel statWboostLabel;
    private javax.swing.JLabel statWlvlLabel;
    private javax.swing.JLabel statWnameLabel;
    private javax.swing.JLabel statXPLabel;
    private javax.swing.JButton westBtn;
    // End of variables declaration//GEN-END:variables
}
