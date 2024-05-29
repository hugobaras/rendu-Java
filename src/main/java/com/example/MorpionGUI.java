package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MorpionGUI extends JFrame implements ActionListener {
    private JButton[][] buttons;
    private boolean joueurX;
    private JLabel label;

    public MorpionGUI() {
        setTitle("Morpion");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        joueurX = true;

        JPanel gridPanel = new JPanel(new GridLayout(3, 3));
        buttons = new JButton[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
                buttons[i][j].addActionListener(this);
                gridPanel.add(buttons[i][j]);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        label = new JLabel("Tour du joueur X");
        add(label, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource();

        if (!buttonClicked.getText().equals("")) {
            return; // Case déjà jouée
        }

        if (joueurX) {
            buttonClicked.setText("X");
            label.setText("Tour du joueur O");
        } else {
            buttonClicked.setText("O");
            label.setText("Tour du joueur X");
        }

        joueurX = !joueurX;

        if (checkVictoire()) {
            String joueurGagnant = joueurX ? "O" : "X";
            JOptionPane.showMessageDialog(this, "Le joueur " + joueurGagnant + " a gagné !");
            reset();
        } else if (estEgalite()) {
            JOptionPane.showMessageDialog(this, "Match nul !");
            reset();
        }
    }

    private boolean checkVictoire() {
        // Vérification des lignes
        for (int i = 0; i < 3; i++) {
            if (!buttons[i][0].getText().equals("") && buttons[i][0].getText().equals(buttons[i][1].getText())
                    && buttons[i][0].getText().equals(buttons[i][2].getText())) {
                return true;
            }
        }

        // Vérification des colonnes
        for (int j = 0; j < 3; j++) {
            if (!buttons[0][j].getText().equals("") && buttons[0][j].getText().equals(buttons[1][j].getText())
                    && buttons[0][j].getText().equals(buttons[2][j].getText())) {
                return true;
            }
        }

        // Vérification des diagonales
        if (!buttons[0][0].getText().equals("") && buttons[0][0].getText().equals(buttons[1][1].getText())
                && buttons[0][0].getText().equals(buttons[2][2].getText())) {
            return true;
        }

        if (!buttons[0][2].getText().equals("") && buttons[0][2].getText().equals(buttons[1][1].getText())
                && buttons[0][2].getText().equals(buttons[2][0].getText())) {
            return true;
        }

        return false;
    }

    private boolean estEgalite() {
        // Vérification s'il reste des cases vides
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void reset() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        joueurX = true;
        label.setText("Tour du joueur X");
    }

    public static void main(String[] args) {
        new MorpionGUI();
    }
}
