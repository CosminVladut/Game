package ui;

import exceptions.UnexpectedLevelNumberException;
import gamestates.Playing;
import utils.CustomLogger;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.Game.*;

public final class InsigniaOverlay
{
    private static InsigniaOverlay instance = null;

    private final Playing playing;
    private BufferedImage backgroundImage, canContinueImage;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight, canContinueX, canContinueY, canContinueWidth, canContinueHeight;
    private boolean canContinue = false;
    private int opacityBackground = 0;
    private double opacityImage = 0;
    private int currentIndex = 0;

    private InsigniaOverlay(Playing playing)
    {
        this.playing = playing;
        loadBackground();
    }

    public static InsigniaOverlay createInsigniaOverlay(Playing playing)
    {
        if(instance == null)
        {
            instance = new InsigniaOverlay(playing);
        }
        return instance;
    }

    private void loadBackground( )
    {
        loadBackgroundImage();
        assert backgroundImage != null : "Nu exista imaginea pentru insigna!";
        backgroundWidth = backgroundImage.getWidth();
        backgroundHeight = (int) ( backgroundImage.getHeight() / 1.1 );
        backgroundX = GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = (int) ( 3 * SCALE );

        canContinueImage = LoadSave.GetSpriteAtlas(LoadSave.CAN_CONTINUE_SPRITESHEET);
        canContinueWidth = canContinueImage.getWidth();
        canContinueHeight = canContinueImage.getHeight();
        canContinueX = GAME_WIDTH / 2 - canContinueWidth / 2;
        canContinueY = GAME_HEIGHT - (int) ( 1.1 * canContinueHeight );
    }

    public void update( )
    {
        if(!canContinue)
        {
            if(opacityBackground < 204)
            {
                opacityBackground += 2;
            }

            if(opacityImage < 1)
            {
                opacityImage += 0.005;
                if(opacityImage > 1)
                {
                    opacityImage = 1;
                }
            }

            if(playing.getLevelManager().getLevelIndex() != currentIndex)
            {
                currentIndex = playing.getLevelManager().getLevelIndex();
                loadBackgroundImage();
            }
        }
        else
        {
            if(opacityBackground > 0)
            {
                opacityBackground -= 2;
            }
            if(opacityImage > 0.005)
            {
                opacityImage -= 0.005;
            }
            else
            {
                opacityBackground = 0;
                opacityImage = 0;
                canContinue = false;
                playing.setInsigniaGot(false);
                playing.setLevelCompleted(true);
            }
        }
    }

    private void loadBackgroundImage( )
    {
        backgroundImage =
                switch(playing.getLevelManager().getLevelIndex())
                {
                    case 0, 1 -> LoadSave.GetSpriteAtlas(LoadSave.DARK_FOREST_INSIGNIA_SPRITESHEET);
                    case 2 -> LoadSave.GetSpriteAtlas(LoadSave.FIERY_ABYSS_INSIGNIA_SPRITESHEET);
                    case 3, 4 -> LoadSave.GetSpriteAtlas(LoadSave.WASTELAND_OF_ABANDONED_ICE_INSIGNIA_SPRITESHEET);
                    default ->
                    {
                        UnexpectedLevelNumberException e =  new UnexpectedLevelNumberException();
                        CustomLogger.logException("Nu exista nivelul : " + playing.getLevelManager().getLevelIndex(), e);
                        yield null;
                    }
                };
    }

    public void setContinue(boolean value)
    {
        if(opacityBackground == 204 && opacityImage == 1)
        {
            canContinue = value;
        }
    }

    public void draw(Graphics g)
    {
        try
        {
            g.setColor(new Color(0, 0, 0, opacityBackground));
        }
        catch(IllegalArgumentException e)
        {
            CustomLogger.logException("Nu exista culoarea descrisa.", e);
        }

        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        Graphics2D g2d = (Graphics2D) g;
        try
        {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacityImage));
        }
        catch(IllegalArgumentException e)
        {
            CustomLogger.logException("Nu se poate desena imaginea deoarece componenta alpha nu este pusa corect.", e);
        }

        g2d.drawImage(backgroundImage, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);

        if(opacityBackground == 204 && opacityImage == 1)
        {
            g.drawImage(canContinueImage, canContinueX, canContinueY, canContinueWidth, canContinueHeight, null);
        }
    }
}
