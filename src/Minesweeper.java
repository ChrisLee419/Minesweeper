import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Minesweeper extends JFrame {
    private int rows;
    private int columns;
    private int mines;
    private JButton[][] buttons;
    private boolean[][] minesLocations;
    private boolean[][] revealed;
    private int[][] adjacentMines;
    private boolean[][] flagged;
    private boolean gameOver;

    public Minesweeper() {
        super("Minesweeper");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        rows = 10;
        columns = 10;
        mines = 10;
        buttons = new JButton[rows][columns];
        minesLocations = new boolean[rows][columns];
        revealed = new boolean[rows][columns];
        adjacentMines = new int[rows][columns];
        flagged = new boolean[rows][columns];
        gameOver = false;

        setLayout(new GridLayout(rows, columns));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].addActionListener(new ButtonListener());
                buttons[i][j].addMouseListener(new FlagListener());
                add(buttons[i][j]);
            }
        }

        generateMines();
        calculateAdjacentMines();
        setVisible(true);
    }

    private void generateMines() {
        int minesPlaced = 0;
        while (minesPlaced < mines) {
            int row = (int)(Math.random() * rows);
            int column = (int)(Math.random() * columns);
            if (!minesLocations[row][column]) {
                minesLocations[row][column] = true;
                minesPlaced++;
            }
        }
    }

    private void calculateAdjacentMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!minesLocations[i][j]) {
                    int mines = 0;
                    for (int x = i - 1; x <= i + 1; x++) {
                        for (int y = j - 1; y <= j + 1; y++) {
                            if (x >= 0 && x < rows && y >= 0 && y < columns && minesLocations[x][y]) {
                                mines++;
                            }
                        }
                    }
                    adjacentMines[i][j] = mines;
                }
            }
        }
    }

    private void reveal(int row, int column) {
        if (!flagged[row][column]) {
            if (!revealed[row][column]) {
                revealed[row][column] = true;
                if (minesLocations[row][column]) {
                    gameOver = true;
                    revealAll();
                    JOptionPane.showMessageDialog(this, "Game over!");
                } else {
                    buttons[row][column].setText(String.valueOf(adjacentMines[row][column]));
                    if (adjacentMines[row][column] == 0) {
                        for (int i = row - 1; i <= row + 1; i++) {
                            for (int j = column - 1; j <= column + 1; j++) {
                                if (i >= 0 && i < rows && j >= 0 && j < columns) {
                                    reveal(i, j);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void revealAll() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (minesLocations[i][j]) {
                    buttons[i][j].setText("X");
                } else {
                    buttons[i][j].setText(String.valueOf(adjacentMines[i][j]));
                }
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (e.getSource() == buttons[i][j]) {
                        reveal(i, j);
                        if (!gameOver) {
                            int revealedSquares = 0;
                            for (int x = 0; x < rows; x++) {
                                for (int y = 0; y < columns; y++) {
                                    if (revealed[x][y]) {
                                        revealedSquares++;
                                    }
                                }
                            }
                            if (revealedSquares == rows * columns - mines) {
                                gameOver = true;
                                revealAll();
                                JOptionPane.showMessageDialog(Minesweeper.this, "Congratulations, you won!");
                            }
                        }
                    }
                }
            }
        }
    }

    private class FlagListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        if (e.getSource() == buttons[i][j]) {
                            if (!revealed[i][j]) {
                                if (!flagged[i][j]) {
                                    buttons[i][j].setText("F");
                                    flagged[i][j] = true;
                                } else {
                                    buttons[i][j].setText("");
                                    flagged[i][j] = false;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Minesweeper();
    }
}

