package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public final class GamePanel extends JPanel
{
    private final Game game;

    public GamePanel(Game game)
    {
        this.game = game;
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(new MouseInputs(this));
        addMouseMotionListener(new MouseInputs(this));
    }

    public void updateGame()
    {

    }

    private void setPanelSize()
    {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    public Game getGame()
    {
        return game;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        game.render(g);
    }
}
