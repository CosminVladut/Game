package ui;

import gamestates.Playing;
import utils.CustomLogger;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static main.Game.*;

public final class TalkingOverlay
{
    private static TalkingOverlay instance = null;

    private final Playing playing;
    private BufferedImage backgroundImage;
    private String[][] dialogues;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;
    private boolean canContinue = false;
    private int charIndex = 0;
    private int currentIndexInDialogue = 0;
    private boolean isFinal = false;
    private int opacityBackground = 0;
    private int indexInDialogues = 0;
    private boolean scoreShown = false;

    private TalkingOverlay(Playing playing)
    {
        this.playing = playing;
        loadDialogues();
        loadBackground();
    }

    public static TalkingOverlay createTalkingOverlay(Playing playing)
    {
        if(instance == null)
        {
            instance = new TalkingOverlay(playing);
        }
        return instance;
    }

    private void loadDialogues( )
    {
        dialogues = new String[][]
        {
                new String[]
                {
                    "Regele Oponent: Generalule, ești în viață doar pentru că am nevoie de tine. " +
                            "Ești un războinic fără rival, și acum vei dovedi dacă ești la fel de iscusit în îndeplinirea unei misiuni imposibile.",

                    "Regele Oponent: În acest tărâm se află trei insigne sacre, ce unite pot aduce întreaga lume în genunchi. " +
                            "Îți ordon să le aduci la mine. În schimb, îți voi elibera armata.",

                    "Regele Oponent: Dar să știi, Generalule, că drumul este presărat cu pericole, iar cei care păzesc insignele vor face totul să te oprească. " +
                            "Îi vei elimina, fără milă, pe toti.",

                    "Regele Oponent: Deoarece toate insignele sunt protejate de câte o barieră magică, care nu va dispărea dacă nu sunt eliminați toți oponenții din zona respectivă.",

                    "Generalul: Nu-mi amintesc de ce sunt aici, dar voi face orice pentru a-mi salva oamenii. Accept provocarea.",

                    "Regele Oponent: Bine, dar nu uita: timpul este scurt, iar loialitatea ta acum aparține mie. Îmi vei aduce insignele, altfel vei pierde totul. " +
                            "Misiunea ta va începe odată ce ieși pe ușă."
                },

                new String[]
                {
                    "Arcașul: Ai venit prea departe, străinule. " +
                            "Această pădure întunecată nu este locul tău, iar insigna pe care o cauți nu va ajunge niciodată în mâinile tale.",


                    "Generalul: Nu voi da înapoi. Oamenii mei depind de mine. Dacă trebuie să te înfrunt pentru a obține insigna, atunci așa va fi.",

                    "Arcașul: Te-ai gândit vreodată de ce ai ajuns aici? Poate că nu e doar dorința de a-ți salva oamenii. " +
                            "Poate că ai un scop mai mare, unul pe care îl ai în subconștient, dar pe care nu ți-l amintești.",

                    "Generalul: Nu am nevoie de lecții. Înfruntă-mă sau pleacă din calea mea. ",

                    "Arcașul: Foarte bine. Dar să știi că pădurea aceasta nu uită și nu iartă. Și nici eu."
                },

                new String[]
                {
                    "Războinicul: Așa deci, Generalule, ai venit după insignă. Dar ca să o obții, va trebui să mă învingi mai întâi.",

                    "Generalul: Nu caut să lupt cu tine. Dar dacă vei sta în calea mea, nu voi avea de ales.",

                    "Războinicul: Cuvintele tale sunt puternice, dar este sabia ta la fel de puternică? " +
                            "Se spune că un general a venit aici dintr-un regat îndepărtat, căutând să ia aceste insigne pentru propriile scopuri. " +
                            "Ești tu acel general? Sau ai uitat cine ești cu adevărat?",

                    "Generalul: Nu-mi amintesc de ce am venit, dar știu ce trebuie să fac acum. Te voi învinge dacă asta îmi cere misiunea.",

                    "Războinicul: Atunci să vedem dacă ești într-adevăr atât de puternic pe cât se spune. " +
                            "Pentru insigna pe care o cauți, trebuie să-mi dovedești că ești demn."
                },

                new String[]
                {
                    "Vikingul: Ești nebun să vii aici, Generalule. Insigna nu îți va aduce decât distrugere. Pleacă acum, sau vei sfârși ca toți ceilalți.",

                    "Generalul: Nu plec fără insignă. Am trecut prin prea multe ca să dau înapoi acum.",

                    "Vikingul: Ai curaj, dar asta nu te va salva. Se spune că un mare războinic a venit cândva aici, căutând insignele pentru a le distruge. " +
                            "Poate că tu ai fost acel războinic, înainte să-ți pierzi memoria. Ce vei face acum, când adevărul este la un pas de a fi dezvăluit?",

                    "Generalul: Indiferent de cine am fost, acum lupt pentru oamenii mei. Nu am nevoie de amintiri ca să știu ce e corect.",

                    "Vikingul: Foarte bine. Dacă vei supraviețui duelului nostru, poate vei descoperi cine ești cu adevărat. Dar nu te aștepta să fie ușor."
                },

                new String[]
                {
                    "Regele Oponent: Ai reușit, Generalule. Insignele sunt ale mele. În sfârșit, voi deține puterea absolută. Acum, dă-mi-le.",

                    "Generalul: Am obținut insignele, dar pe parcursul căutării am aflat ceva. Nu sunt un simplu pion în jocul tău. Nu sunt aici pentru tine.",

                    "Regele Oponent: Ce prostii spui? Ești un nimic fără trecut, fără scop. Îmi vei da insignele și vei vedea cum lumea se va supune voinței mele!",

                    "Generalul: Nu. Am venit aici inițial pentru a găsi aceste insigne, dar nu pentru a le folosi. " +
                            "Am fost trimis să le distrug, pentru ca nimeni să nu aibă puterea să controleze lumea. Și asta voi face.",

                    "Regele Oponent: Nu! Trădătorule, te voi distruge!",

                    "Generalul: Nu astăzi."
                },

                new String[]
                {
                    "Generalul, cu insignele în mâini, a înțeles că adevărata misiune a fost dintotdeauna să protejeze lumea de forțele care ar putea-o distruge. " +
                            "Cu o decizie fermă, a distrus insignele, asigurându-se că puterea lor nu va cădea niciodată în mâinile unui tiran. " +
                            "Și astfel, a închis un capitol din viața sa, alegând să devină nu doar un războinic, ci un apărător al lumii împotriva celor care ar căuta să o subjuge. " +
                            "Oamenii lui au fost eliberați, iar Generalul și-a recâștigat onoarea."
                },

                new String[]
                {
                    "Daune suferite : " + playing.getPlayer().getDamageTaken() + " Potiuni folosite : " + playing.getPlayer().getPotionsUsed()
                }
        };
    }

    private void loadBackground( )
    {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.CAN_CONTINUE_SPRITESHEET);
        backgroundWidth = backgroundImage.getWidth();
        backgroundHeight = backgroundImage.getHeight();
        backgroundX = GAME_WIDTH - backgroundImage.getWidth();
        backgroundY = GAME_HEIGHT - backgroundImage.getHeight();
    }

    public void update( )
    {
        if(!canContinue)
        {
            if(isFinal)
            {
                if(playing.getLevelManager().getLevelIndex() != playing.getLevelManager().getHowManyLevels() && opacityBackground != 255)
                {
                    indexInDialogues = playing.getLevelManager().getHowManyLevels();
                }
                if(opacityBackground < 255)
                {
                    opacityBackground += 3;
                }
                if(charIndex < dialogues[indexInDialogues][currentIndexInDialogue].length() && opacityBackground == 255)
                {
                    ++charIndex;
                }
            }
            else
            {
                indexInDialogues = playing.getLevelManager().getLevelIndex();
                if(charIndex < dialogues[indexInDialogues][currentIndexInDialogue].length())
                {
                    ++charIndex;
                }
            }
        }
        else
        {
            if(isFinal)
            {
                ++indexInDialogues;
                if(scoreShown)
                {
                    playing.getLevelManager().loadNextLevel();
                }
            }
            else
            {
                ++currentIndexInDialogue;
                if(currentIndexInDialogue == dialogues[indexInDialogues].length)
                {
                    currentIndexInDialogue = 0;
                    playing.setTalking(false);
                    playing.getPlayer().setTalking(false);
                    playing.getObjectHandler().setTalkedOnce(true);
                    if(playing.getLevelManager().getLevelIndex() != 0)
                    {
                        playing.getGame().getAudioPlayer().playSong(playing.getLevelManager().getLevelIndex() + 5);
                    }
                }
            }
            charIndex = 0;
            canContinue = false;
        }
    }

    public void resetCurrentIndexInDialogue( )
    {
        currentIndexInDialogue = 0;
        charIndex = 0;
    }

    public void setContinue(boolean value)
    {
        if(charIndex == dialogues[indexInDialogues][currentIndexInDialogue].length())
        {
            canContinue = value;
            if(indexInDialogues == playing.getLevelManager().getHowManyLevels() + 1)
            {
                scoreShown = true;
            }
        }
    }

    public void setFinal(boolean value)
    {
        isFinal = value;
    }

    public void draw(Graphics g)
    {
        int maxWidth = (int) ( GAME_WIDTH - 0.2 * GAME_WIDTH );
        int textX = GAME_WIDTH / 10;
        int textY;
        int fontSize;

        if(isFinal)
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

            if(indexInDialogues == playing.getLevelManager().getHowManyLevels() + 1)
            {
                textY = GAME_HEIGHT / 2 - (int)(37 * SCALE);
            }
            else
            {
                textY = (int) (30 * SCALE);
            }
            fontSize = 37;
        }
        else
        {
            textY = GAME_HEIGHT - (int) ( 33 * SCALE );
            fontSize = 22;
        }


        drawWrappedText(g, dialogues[indexInDialogues][currentIndexInDialogue].substring(0, charIndex), textX, textY, maxWidth, fontSize);

        if(charIndex == dialogues[indexInDialogues][currentIndexInDialogue].length())
        {
            g.drawImage(backgroundImage, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
        }
    }

    private void drawWrappedText(Graphics g, String text, int x, int y, int maxWidth, int fontSize)
    {
        g.setFont(new Font("Serif", Font.BOLD, fontSize));
        g.setColor(Color.WHITE);

        FontMetrics fm = g.getFontMetrics();

        String[] breakMarkers = {"Daune suferite", "Potiuni folosite"};

        StringBuilder patternBuilder = new StringBuilder();
        for(String marker : breakMarkers)
        {
            if(!patternBuilder.isEmpty())
            {
                patternBuilder.append("|");
            }
            patternBuilder.append(Pattern.quote(marker));
        }

        String pattern = patternBuilder.toString();

        String[] parts = null;
        try
        {
            parts = text.split("(?=" + pattern + ")");
        }
        catch(PatternSyntaxException e)
        {
            CustomLogger.logException("Regexul este invalid.", e);
        }

        ArrayList<String> lines = new ArrayList<>();

        for(String part : parts)
        {
            String[] words = null;
            try
            {
                words = part.split(" ");
            }
            catch(PatternSyntaxException e)
            {
                CustomLogger.logException("Regexul nu este valid.", e);
            }

            StringBuilder currentLine = new StringBuilder(words[0]);

            for(int i = 1; i < words.length; ++i)
            {
                String word = words[i];
                String testLine = currentLine + " " + word;
                int width = 0;
                try
                {
                    width = fm.stringWidth(testLine);
                }
                catch(NullPointerException e)
                {
                    CustomLogger.logException("Sirul de caractere este nul.", e);
                }

                if(width <= maxWidth)
                {
                    currentLine.append(" ").append(word);
                }
                else
                {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                }
            }

            lines.add(currentLine.toString());
        }

        int textY = y;
        for(String line : lines)
        {
            int lineWidth = fm.stringWidth(line);
            int textXCentered = x + (maxWidth - lineWidth) / 2;
            try
            {
                g.drawString(line, textXCentered, textY);
            }
            catch(NullPointerException e)
            {
                CustomLogger.logException("Sirul de caractere este nul.", e);
            }
            textY += fm.getHeight();
        }
    }
}
