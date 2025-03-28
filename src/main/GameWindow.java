package main;

import utils.CustomLogger;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public final class GameWindow extends JFrame
{
    int code = 0;
    public GameWindow(GamePanel gamePanel)
    {
        setTitle("Triada Întunecată");
        setSize(1280, 600);
        try
        {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        catch(SecurityException e)
        {
            CustomLogger.logException("JFrame nu permite variabila EXIT_ON_CLOSE.", e);
        }
        catch(IllegalArgumentException e)
        {
            CustomLogger.logException("JFrame nu permite argumentul acesta.", e);
        }
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                gamePanel.getGame().getAudioPlayer().cleanup();
                gamePanel.getGame().getPlaying().getDatabaseHandler().closeConnection();
                System.exit(code);
            }
        });
        setResizable(false);
        add(gamePanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        addWindowFocusListener(new WindowFocusListener()
                               {
                                   @Override
                                   public void windowGainedFocus(WindowEvent e)
                                   {
                                       //Deocamdată nimic aici
                                   }

                                   @Override
                                   public void windowLostFocus(WindowEvent e)
                                   {
                                       gamePanel.getGame().windowFocusLost();
                                   }
                               }
        );
    }

    public void setCode(int value)
    {
        code = value;
    }
}
