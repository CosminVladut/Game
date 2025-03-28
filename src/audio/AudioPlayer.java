package audio;

import exceptions.AudioPlayerLoadingException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.BufferUtils;
import utils.CustomLogger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.lwjgl.system.MemoryUtil.NULL;

public final class AudioPlayer
{
    public static final int MENU_1 = 0;
    // Player
    public static final int DEATH = 0;
    public static final int JUMP = 1;
    public static final int LEVEL_COMPLETED = 2;
    public static final int LAST_HIT = 6;
    public static final int TAKE_HIT = 7;
    public static final int HEAL = 8;
    public static final int DROP_ATTACK = 9;
    public static final int HARD_HIT = 13;
    public static final int BURN = 14;
    // Skeleton macer
    public static final int SKELETON_MACER_ATTACK1 = 15;
    public static final int SKELETON_MACER_ATTACK2 = 16;
    public static final int SKELETON_MACER_DEATH = 17;
    // Shardsoul slayer
    public static final int SHARDSOUL_SLAYER_ATTACK = 18;
    public static final int SHARDSOUL_SLAYER_DEATH = 19;
    // Flying demon
    public static final int FLYING_DEMON_ATTACK = 20;
    public static final int FLYING_DEMON_DEATH = 21;
    // Huntress
    public static final int HUNTRESS_ATTACK1 = 22;
    public static final int HUNTRESS_ATTACK2 = 23;
    public static final int HUNTRESS_ATTACK3 = 24;
    public static final int HUNTRESS_DEATH = 25;
    // Demon
    public static final int DEMON_ATTACK = 26;
    public static final int DEMON_DEATH = 27;
    // Ice golem
    public static final int ICE_GOLEM_ATTACK = 28;
    public static final int ICE_GOLEM_DEATH = 29;
    // Ice borne
    public static final int ICE_BORNE_ATTACK = 30;
    public static final int ICE_BORNE_DEATH = 31;
    // Snow knight
    public static final int SNOW_KNIGHT_ATTACK1 = 32;
    public static final int SNOW_KNIGHT_ATTACK2 = 33;
    public static final int SNOW_KNIGHT_ATTACK3 = 34;
    public static final int SNOW_KNIGHT_DEATH = 35;
    // Fire worm
    public static final int FIRE_WORM_ATTACK = 36;
    public static final int FIRE_WORM_DEATH = 37;
    // Fire wizard
    public static final int FIRE_WIZARD_ATTACK = 38;
    public static final int FIRE_WIZARD_DEATH = 39;
    // Minotaur
    public static final int MINOTAUR_ATTACK1 = 40;
    public static final int MINOTAUR_ATTACK2 = 41;
    public static final int MINOTAUR_DEATH = 42;
    public static final int MINOTAUR_BLOCK = 43;
    // Final Boss
    public static final int FINAL_BOSS_ATTACK1 = 44;
    public static final int FINAL_BOSS_ATTACK2 = 45;
    public static final int FINAL_BOSS_ATTACK3 = 46;
    public static final int FINAL_BOSS_DEATH = 47;
    // Fire Knight
    public static final int FIRE_KNIGHT_ATTACK1 = 48;
    public static final int FIRE_KNIGHT_ATTACK2 = 49;
    public static final int FIRE_KNIGHT_ATTACK3 = 50;
    public static final int FIRE_KNIGHT_ATTACK4 = 51;
    public static final int FIRE_KNIGHT_BLOCK = 52;
    public static final int FIRE_KNIGHT_DEATH = 53;
    // Leaf Ranger
    public static final int LEAF_RANGER_ATTACK1 = 54;
    public static final int LEAF_RANGER_ATTACK2 = 55;
    public static final int LEAF_RANGER_ATTACK3 = 56;
    public static final int LEAF_RANGER_ATTACK4 = 57;
    public static final int LEAF_RANGER_BLOCK = 58;
    public static final int LEAF_RANGER_DEATH = 59;
    // Necromancer
    public static final int NECROMANCER_ATTACK1 = 60;
    public static final int NECROMANCER_ATTACK2 = 61;
    public static final int NECROMANCER_DEATH = 62;
    // Viking
    public static final int VIKING_ATTACK1 = 63;
    public static final int VIKING_ATTACK2 = 64;
    public static final int VIKING_ATTACK3 = 65;
    public static final int VIKING_ATTACK4 = 66;
    public static final int VIKING_BLOCK = 67;
    public static final int VIKING_DEATH = 68;
    // Werewolf
    public static final int WEREWOLF_ATTACK1 = 69;
    public static final int WEREWOLF_ATTACK2 = 70;
    public static final int WEREWOLF_ATTACK3 = 71;
    public static final int WEREWOLF_ATTACK4 = 72;
    public static final int WEREWOLF_DEATH = 73;
    public static final int INSIGNIA_GOT = 74;
    public static final int PRAY = 75;
    public static final int CHEST_OPEN = 76;
    public static AudioPlayer instance = null;

    private final Map<Integer, WeakReference<Integer>> soundBuffers = new HashMap<>();
    private final ConcurrentLinkedQueue<Integer> sources = new ConcurrentLinkedQueue<>();
    private final Random random = new Random();
    private int currentSongSource;
    private float volume = 0.5f;
    private long device;
    private long context;

    private AudioPlayer( )
    {
        initOpenAL();
        loadSounds();
        playSong(MENU_1);
    }

    public static AudioPlayer createAudioPlayer( )
    {
        if(instance == null)
        {
            instance = new AudioPlayer();
        }
        return instance;
    }

    private void initOpenAL( )
    {
        device = ALC10.alcOpenDevice((ByteBuffer) null);
        if(device == NULL)
        {
            CustomLogger.logException("Nu s-a putut deschide dispozitivul OpenAL", new AudioPlayerLoadingException());
        }

        context = ALC10.alcCreateContext(device, (IntBuffer) null);
        ALC10.alcMakeContextCurrent(context);
        AL.createCapabilities(ALC.createCapabilities(device));

        checkALError("Nu s-a putut crea contextul sau capabilitatile OpenAL");
    }

    private void loadSounds( )
    {
        String[] songFiles = {"menu", "1stMap", "2ndMap", "3rdMap", "4thMap", "5thMap", "2ndMapBoss", "3rdMapBoss", "4thMapBoss", "lastBoss"};
        for(int i = 0; i < songFiles.length; ++i)
        {
            try
            {
                soundBuffers.put(i, new WeakReference<>(loadWavFile("audio/" + songFiles[i])));
            }
            catch(UnsupportedOperationException e)
            {
                CustomLogger.logException("Operatie nepermisa la melodii.", e);
            }
            catch(NullPointerException e)
            {
                CustomLogger.logException("Nu se permit valori nule la melodii.", e);
            }
            catch(ClassCastException e)
            {
                CustomLogger.logException("Tipul acesta de data nu poate fi stocat la melodii.", e);
            }
            catch(IllegalArgumentException e)
            {
                CustomLogger.logException("Una sau mai multe proprietati a datei nu permite stocarea la melodii.", e);
            }
        }

        String[] effectFiles =
                {
                        "death", "jump", "levelCompleted", "sword1", "sword2", "sword3", "sword4",
                        "takehit", "heal", "dropattack", "attack1hit", "attack2hit", "attack3hit", "attack4hit", "burn",
                        "skeletonmacerattack1", "skeletonmacerattack2", "skeletonmacerdeath", "shardsoulslayerattack",
                        "shardsoulslayerdeath", "flyingdemonattack", "flyingdemondeath", "huntressattack1", "huntressattack2",
                        "huntressattack3", "huntressdeath", "demonattack", "demondeath", "icegolemattack", "icegolemdeath",
                        "iceborneattack", "icebornedeath", "snowknightattack1", "snowknightattack2", "snowknightattack3",
                        "snowknightdeath", "firewormattack", "firewormdeath", "firewizardattack", "firewizarddeath",
                        "minotaurattack1", "minotaurattack2", "minotaurdeath", "minotaurblock", "finalbossattack1",
                        "finalbossattack2", "finalbossattack3", "finalbossdeath", "fireknightattack1", "fireknightattack2",
                        "fireknightattack3", "fireknightattack4", "fireknightblock", "fireknightdeath", "leafrangerattack1",
                        "leafrangerattack2", "leafrangerattack3", "leafrangerattack4", "leafrangerblock", "leafrangerdeath",
                        "necromancerattack1", "necromancerattack2", "necromancerdeath", "vikingattack1", "vikingattack2",
                        "vikingattack3", "vikingattack4", "vikingblock", "vikingdeath", "werewolfattack1", "werewolfattack2",
                        "werewolfattack3", "werewolfattack4", "werewolfdeath", "insigniagot", "pray", "chestopen"
                };

        for(int i = 0; i < effectFiles.length; ++i)
        {
            try
            {
                soundBuffers.put(i + songFiles.length, new WeakReference<>(loadWavFile("audio/" + effectFiles[i])));
            }
            catch(UnsupportedOperationException e)
            {
                CustomLogger.logException("Operatie nepermisa pe efecte.", e);
            }
            catch(NullPointerException e)
            {
                CustomLogger.logException("Nu se permit valori nule la efecte.", e);
            }
            catch(ClassCastException e)
            {
                CustomLogger.logException("Tipul acesta de data nu poate fi stocat la efecte.", e);
            }
            catch(IllegalArgumentException e)
            {
                CustomLogger.logException("Una sau mai multe proprietati a datei nu permite stocarea la efecte.", e);
            }
        }
    }

    private int loadWavFile(String fileName)
    {
        int buffer = AL10.alGenBuffers();

        try(AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName + ".wav"))))
        {
            AudioFormat format = audioInputStream.getFormat();
            byte[] audioBytes = audioInputStream.readAllBytes();
            ByteBuffer byteBuffer = BufferUtils.createByteBuffer(audioBytes.length);
            byteBuffer.put(audioBytes);
            byteBuffer.flip();

            int formatType;
            if(format.getChannels() == 1)
            {
                formatType = AL10.AL_FORMAT_MONO16;
            }
            else
            {
                formatType = AL10.AL_FORMAT_STEREO16;
            }

            AL10.alBufferData(buffer, formatType, byteBuffer, (int) format.getSampleRate());
            checkALError("Nu s-au putut seta datele in buffer");
        }
        catch(UnsupportedAudioFileException | IOException | IllegalArgumentException | ReadOnlyBufferException |
              BufferOverflowException e)
        {
            AL10.alDeleteBuffers(buffer);
            CustomLogger.logException("Nu se poate incarca fila WAV : " + fileName, e);
        }

        return buffer;
    }

    private void checkALError(String message)
    {
        int error = AL10.alGetError();
        if (error != AL10.AL_NO_ERROR)
        {
            CustomLogger.logException(message + " : " + errorToString(error), new AudioPlayerLoadingException());
        }
    }

    private static String errorToString(int error)
    {
        return switch (error)
        {
            case AL10.AL_INVALID_NAME -> "AL_INVALID_NAME";
            case AL10.AL_INVALID_ENUM -> "AL_INVALID_ENUM";
            case AL10.AL_INVALID_VALUE -> "AL_INVALID_VALUE";
            case AL10.AL_INVALID_OPERATION -> "AL_INVALID_OPERATION";
            case AL10.AL_OUT_OF_MEMORY -> "AL_OUT_OF_MEMORY";
            default -> "Cod de eroare necunoscut: " + error;
        };
    }

    public void setVolume(float volume)
    {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
        AL10.alSourcef(currentSongSource, AL10.AL_GAIN, this.volume);
        checkALError("Nu se poate seta volumul melodiei");
        Integer source;
        while ((source = sources.poll()) != null)
        {
            AL10.alSourcef(source, AL10.AL_GAIN, this.volume);
            checkALError("Nu s-a putut seta volumu efectului");
        }
    }

    public void stopSong( )
    {
        if(currentSongSource != 0)
        {
            AL10.alSourceStop(currentSongSource);
            AL10.alDeleteSources(currentSongSource);
            checkALError("Nu s-a putut sterge sursa de melodii");
            currentSongSource = 0;
        }
    }

    public void setLevelSong(int levelIndex)
    {
        playSong(levelIndex);
    }

    public void levelCompleted( )
    {
        stopSong();
        playEffect(LEVEL_COMPLETED);
    }

    public void playAttackSound( )
    {
        int start = 3 + random.nextInt(2);
        playEffect(start);
    }

    public void playHitSound( )
    {
        int start = 10 + random.nextInt(2);
        playEffect(start);
    }

    public void update()
    {
        checkAndCleanupSources();
    }

    private void checkAndCleanupSources()
    {
        Integer source;
        while ((source = sources.poll()) != null)
        {
            if (AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) != AL10.AL_PLAYING)
            {
                AL10.alSourceStop(source);
                AL10.alDeleteSources(source);
                checkALError("Nu s-a putut sterge sursa dupa finalizarea redarii");
            }
        }
    }

    public void toggleMute( )
    {
        float gain = AL10.alGetSourcef(currentSongSource, AL10.AL_GAIN) > 0 ? 0 : volume;
        checkALError("Nu s-a putut prelua nivelul volumului");
        AL10.alSourcef(currentSongSource, AL10.AL_GAIN, gain);
        checkALError("Nu s-a putut pune pe mut melodia");
        Integer source;
        while ((source = sources.poll()) != null)
        {
            AL10.alSourcef(source, AL10.AL_GAIN, gain);
            checkALError("Nu s-a putut pune pe mut efectul");
        }

        if(gain > 0)
        {
            playEffect(JUMP);
        }
    }

    public void playEffect(int effect)
    {
        int buffer = 0;
        try
        {
            buffer = Objects.requireNonNull(soundBuffers.get(effect + 10).get());
        }
        catch(NullPointerException e)
        {
            CustomLogger.logException("Bufferul de sunet pentru efect este nul.", e);
        }

        int source = AL10.alGenSources();
        checkALError("Nu s-a putut genera sursa pentru efect");

        AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
        checkALError("Nu s-a putut transforma buffer-ul in intreg pentru efect");
        AL10.alSourcef(source, AL10.AL_GAIN, volume);
        checkALError("Nu s-a putut seta volumul pentru efect");
        AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_FALSE);
        checkALError("Nu s-a putut seta sa nu se repete efectul");
        AL10.alSourcePlay(source);
        checkALError("Nu s-a porni efectul de la sursa");

        try
        {
            sources.add(source);
        }
        catch(UnsupportedOperationException e)
        {
            CustomLogger.logException("Operatie nepermisa pe surse.", e);
        }
        catch(NullPointerException e)
        {
            CustomLogger.logException("Nu se permit valori nule la surse.", e);
        }
        catch(ClassCastException e)
        {
            CustomLogger.logException("Tipul acesta de data nu poate fi stocat la surse.", e);
        }
        catch(IllegalArgumentException e)
        {
            CustomLogger.logException("Una sau mai multe proprietati a datei nu permite stocarea la surse.", e);
        }
    }

    public void playSong(int song)
    {
        stopSong();
        try
        {
            int buffer = 0;
            try
            {
                buffer = Objects.requireNonNull(soundBuffers.get(song).get());
            }
            catch(NullPointerException e)
            {
                CustomLogger.logException("Bufferul de sunet pentru melodii este nul.", e);
            }
            currentSongSource = AL10.alGenSources();
            checkALError("Nu s-a putut genera sursa pentru melodii");
            AL10.alSourcei(currentSongSource, AL10.AL_BUFFER, buffer);
            checkALError("Nu s-a putut transforma buffer-ul in intreg pentru melodii");
            AL10.alSourcef(currentSongSource, AL10.AL_GAIN, volume);
            checkALError("Nu s-a putut seta volumul pentru melodie");
            AL10.alSourcei(currentSongSource, AL10.AL_LOOPING, AL10.AL_TRUE);
            checkALError("Nu s-a putut seta sa se repete melodia");
            AL10.alSourcePlay(currentSongSource);
            checkALError("Nu s-a putut porni melodia de la sursa");
        }
        catch(NullPointerException e)
        {
            CustomLogger.logException("Nu se permit valori nule la buffer-ul melodiilor.", e);
        }
        catch(ClassCastException e)
        {
            CustomLogger.logException("Tipul acesta de data nu este stocat la buffer-ul melodiilor.", e);
        }
    }

    public void cleanup()
    {
        stopSong();

        Integer source;
        while ((source = sources.poll()) != null)
        {
            AL10.alSourceStop(source);
            AL10.alDeleteSources(source);
        }

        for (WeakReference<Integer> bufferRef : soundBuffers.values())
        {
            Integer buffer = bufferRef.get();
            if (buffer != null)
            {
                AL10.alDeleteBuffers(buffer);
            }
        }

        soundBuffers.clear();

        ALC10.alcDestroyContext(context);
        ALC10.alcCloseDevice(device);
    }
}