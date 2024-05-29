package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class DevineLeNombreGUI extends JFrame implements ActionListener {
    private JTextField textField;
    private JButton button;
    private JLabel label;
    private Random random;
    private int nombreADeviner;
    private int tentative;
    private int points;
    private JLabel attemptsLabel;
    private JLabel pointsLabel;

    public DevineLeNombreGUI() {
        setTitle("Devine le nombre");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.LIGHT_GRAY);

        random = new Random();
        points = 100;
        tentative = 0;

        label = new JLabel("Je pense à un nombre entre 1 et 200. Essayez de deviner !");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.NORTH);

        textField = new JTextField(10);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    validerProposition();
                }
            }
        });
        add(textField, BorderLayout.CENTER);

        button = new JButton("Proposer");
        button.addActionListener(this);
        add(button, BorderLayout.SOUTH);

        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        attemptsLabel = new JLabel("Tentatives : " + tentative);
        infoPanel.add(attemptsLabel);
        pointsLabel = new JLabel("Points : " + points);
        infoPanel.add(pointsLabel);
        add(infoPanel, BorderLayout.SOUTH);

        setVisible(true);

        choisirNombreADeviner();
    }

    private void choisirNombreADeviner() {
        nombreADeviner = random.nextInt(200) + 1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        validerProposition();
    }

    private void validerProposition() {
        String input = textField.getText();
        int nombrePropose;
        try {
            nombrePropose = Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide.");
            return;
        }
        tentative++;

        if (nombrePropose < nombreADeviner) {
            JOptionPane.showMessageDialog(this, "Le nombre que vous avez proposé est trop petit.");
        } else if (nombrePropose > nombreADeviner) {
            JOptionPane.showMessageDialog(this, "Le nombre que vous avez proposé est trop grand.");
        } else {
            int bonusPoints = 10 * (200 / tentative);
            points += bonusPoints;
            JOptionPane.showMessageDialog(this, "Félicitations ! Vous avez deviné le nombre en " + tentative + " tentatives !\nVous avez obtenu " + points + " points !");
            reset();
            return;
        }

        int difference = Math.abs(nombreADeviner - nombrePropose);
        if (difference <= 10) {
            JOptionPane.showMessageDialog(this, "Mais tu chauffes !");
        } else if (difference <= 20) {
            JOptionPane.showMessageDialog(this, "Tu te rapproches.");
        } else if (difference <= 30) {
            JOptionPane.showMessageDialog(this, "Tu refroidis.");
        } else {
            JOptionPane.showMessageDialog(this, "Tu gèles.");
        }

        points -= 5;

        updateInfoLabels();
    }

    private void updateInfoLabels() {
        attemptsLabel.setText("Tentatives : " + tentative);
        pointsLabel.setText("Points : " + points);
    }

    private void reset() {
        choisirNombreADeviner();
        tentative = 0;
        points = 100;
        updateInfoLabels();
    }

    public static void main(String[] args) {
        new DevineLeNombreGUI();
    }
}
