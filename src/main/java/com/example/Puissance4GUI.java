package com.example;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.print.attribute.standard.Media;

public class Puissance4GUI extends JFrame implements ActionListener {
    private JButton[][] buttons;
    private boolean joueurRouge;
    private JLabel label;
    private int[][] grille;
    private BufferedImage jetonRougeImg;
    private BufferedImage jetonJauneImg;

    public Puissance4GUI() {
        try {
            jetonRougeImg = ImageIO.read(getClass().getResource("jeton-rouge.png"));
            jetonJauneImg = ImageIO.read(getClass().getResource("jeton-jaune.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        setTitle("Puissance 4");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        joueurRouge = true;
        grille = new int[6][7];

        JPanel gridPanel = new JPanel(new GridLayout(6, 7));
        buttons = new JButton[6][7];

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].addActionListener(this);
                buttons[i][j].setOpaque(true);
                gridPanel.add(buttons[i][j]);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        label = new JLabel("Tour du joueur Rouge");
        add(label, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource();
        int column = -1;

        for (int j = 0; j < 7; j++) {
            for (int i = 0; i < 6; i++) {
                if (buttonClicked == buttons[i][j]) {
                    column = j;
                    break;
                }
            }
            if (column != -1) {
                break;
            }
        }

        if (column == -1 || grille[0][column] != 0) {
            return;
        }

        int row = 5;
        while (row >= 0 && grille[row][column] != 0) {
            row--;
        }

        grille[row][column] = joueurRouge ? 1 : 2;

        animateDrop(row, column, joueurRouge ? jetonRougeImg : jetonJauneImg);

        if (checkVictoire(row, column)) {
            String joueurGagnant = joueurRouge ? "Rouge" : "Jaune";
            VideoPopup popup = new VideoPopup(this, "Victoire", "Le joueur " + joueurGagnant + " a gagné !",
                    "chemin/vers/video.mp4");
            popup.setVisible(true);
            reset();
        } else if (estEgalite()) {
            JOptionPane.showMessageDialog(this, "Match nul !");
            reset();
        } else {
            joueurRouge = !joueurRouge;
            label.setText("Tour du joueur " + (joueurRouge ? "Rouge" : "Jaune"));
        }
    }

    private void animateDrop(final int row, final int column, final BufferedImage image) {
        final JButton button = buttons[0][column];
        button.setIcon(new ImageIcon(image));

        Timer timer = new Timer(100, new ActionListener() {
            int currentRow = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentRow == row) {
                    ((Timer) e.getSource()).stop();
                    return;
                }

                buttons[currentRow][column].setIcon(null);
                currentRow++;
                buttons[currentRow][column].setIcon(new ImageIcon(image));
            }
        });

        timer.start();
    }

    private boolean checkVictoire(int row, int column) {
        int joueur = grille[row][column];

        int count = 0;
        for (int i = 0; i < 6; i++) {
            if (grille[i][column] == joueur) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                count = 0;
            }
        }

        count = 0;
        for (int j = 0; j < 7; j++) {
            if (grille[row][j] == joueur) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                count = 0;
            }
        }

        count = 0;
        int startRow = row - Math.min(row, column);
        int startColumn = column - Math.min(row, column);
        while (startRow < 6 && startColumn < 7) {
            if (grille[startRow][startColumn] == joueur) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                count = 0;
            }
            startRow++;
            startColumn++;
        }

        count = 0;
        startRow = row + Math.min(5 - row, column);
        startColumn = column - Math.min(5 - row, column);
        while (startRow >= 0 && startColumn < 7) {
            if (grille[startRow][startColumn] == joueur) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                count = 0;
            }
            startRow--;
            startColumn++;
        }

        return false;
    }

    private boolean estEgalite() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (grille[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void reset() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                grille[i][j] = 0;
                buttons[i][j].setBackground(null);
            }
        }
        joueurRouge = true;
        label.setText("Tour du joueur Rouge");
    }

    public static void main(String[] args) {
        new Puissance4GUI();
    }

    public class VideoPopup extends JDialog {
        private JPanel videoPanel;
        private JButton closeButton;
        private String videoPath;

        public VideoPopup(JFrame parent, String title, String message, String videoPath) {
            super(parent, title, true);
            this.videoPath = videoPath;

            videoPanel = new JPanel();
            videoPanel.setLayout(new BorderLayout());
            videoPanel.add(new JLabel(message), BorderLayout.NORTH);

            closeButton = new JButton("Fermer");
            closeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(closeButton);

            add(videoPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setSize(400, 300);
            setLocationRelativeTo(parent);
        }
    }

}