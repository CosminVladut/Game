package database;

import exceptions.ExceptionSQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseHandler
{
    private static DatabaseHandler instance = null;
    private final Connection connection;

    private DatabaseHandler( )
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:game_data.db");
            createTables();
        }
        catch(SQLException e)
        {
            throw new ExceptionSQL("Eroare la crearea conexunii cu baza de date.");
        }
        catch(ClassNotFoundException e)
        {
            throw new ExceptionSQL("Clasa nu a putut fi localizata.");
        }
        catch(LinkageError e)
        {
            throw new ExceptionSQL("Nu s-a putut face legatura.");
        }

    }

    public static DatabaseHandler createDatabaseHandler( )
    {
        if(instance == null)
        {
            instance = new DatabaseHandler();
        }
        return instance;
    }

    public void closeConnection( )
    {
        if(connection != null)
        {
            try
            {
                connection.close();
            }
            catch(SQLException e)
            {
                throw new ExceptionSQL("Eroare la inchiderea conexunii cu baza de date.");
            }
        }
    }

    private void createTables( )
    {
        try(Statement stmt = connection.createStatement())
        {
            String createPlayerTable = """
                    CREATE TABLE IF NOT EXISTS Player (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        position_x REAL NOT NULL DEFAULT 0,
                        position_y REAL NOT NULL DEFAULT 0,
                        flipX INTEGER NOT NULL DEFAULT 0,
                        flipW INTEGER NOT NULL DEFAULT 1,
                        damage_taken INTEGER NOT NULL DEFAULT 0,
                        potions_used INTEGER NOT NULL DEFAULT 0
                    );
                    """;
            stmt.execute(createPlayerTable);

            String createGameStateTable = """
                    CREATE TABLE IF NOT EXISTS GameState (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        current_level INTEGER NOT NULL DEFAULT 0,
                        is_continue_game INTEGER NOT NULL DEFAULT 0,
                        can_press_continue INTEGER NOT NULL DEFAULT 0
                    );
                    """;
            stmt.execute(createGameStateTable);

            String insertDefaultPlayer = """
                INSERT INTO Player (id, position_x, position_y, flipX, flipW, damage_taken, potions_used)
                SELECT 1, 0, 0, 0, 1, 0, 0
                WHERE NOT EXISTS (SELECT 1 FROM Player WHERE id = 1);
                """;

            stmt.execute(insertDefaultPlayer);

            String insertDefaultGameState = """
                INSERT INTO GameState (id, current_level, is_continue_game, can_press_continue)
                SELECT 1, 0, 0, 0
                WHERE NOT EXISTS (SELECT 1 FROM GameState WHERE id = 1);
                """;

            stmt.execute(insertDefaultGameState);
        }
        catch(SQLException e)
        {
            throw new ExceptionSQL("Eroare la accesarea bazei de date sau conexiune inchisa.");
        }
    }

    public void updatePlayerState(Double positionX, Double positionY, Integer flipX, Integer flipW, Integer damageTaken, Integer potionsUsed)
    {
        StringBuilder queryBuilder = new StringBuilder("UPDATE Player SET ");
        List<Object> parameters = new ArrayList<>();

        try
        {
            if(positionX != null)
            {
                queryBuilder.append("position_x = ?, ");
                parameters.add(positionX);
            }
            if(positionY != null)
            {
                queryBuilder.append("position_y = ?, ");
                parameters.add(positionY);
            }
            if(flipX != null)
            {
                queryBuilder.append("flipX = ?, ");
                parameters.add(flipX);
            }
            if(flipW != null)
            {
                queryBuilder.append("flipW = ?, ");
                parameters.add(flipW);
            }
            if(damageTaken != null)
            {
                queryBuilder.append("damage_taken = ?, ");
                parameters.add(damageTaken);
            }
            if(potionsUsed != null)
            {
                queryBuilder.append("potions_used = ?, ");
                parameters.add(potionsUsed);
            }
        }
        catch(UnsupportedOperationException e)
        {
            throw new ExceptionSQL("Operatie nepermisa pe parametrii acestia.");
        }
        catch(IllegalArgumentException e)
        {
            throw new ExceptionSQL("Una sau mai multe proprietati a datei nu permite stocarea la parametri.");
        }

        if(!parameters.isEmpty())
        {
            try
            {
                queryBuilder.setLength(queryBuilder.length() - 2);
            }
            catch(IndexOutOfBoundsException e)
            {
                throw new ExceptionSQL("Lungimea este negaitva pentru o declaratie sql.");
            }
        }

        queryBuilder.append(" WHERE id = 1");

        String query = queryBuilder.toString();

        try(PreparedStatement pstmt = connection.prepareStatement(query))
        {
            for(int i = 0; i < parameters.size(); ++i)
            {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            pstmt.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new ExceptionSQL("Eroare la actualizarea stării jucătorului.");
        }
    }

    public PlayerState getPlayerState( )
    {
        String query = "SELECT position_x, position_y, flipX, flipW, damage_taken, potions_used FROM Player WHERE id = 1";
        PlayerState playerState = new PlayerState();

        try(PreparedStatement pstmt = connection.prepareStatement(query))
        {
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
            {
                playerState.positionX = rs.getDouble("position_x");
                playerState.positionY = rs.getDouble("position_y");
                playerState.flipX = rs.getInt("flipX");
                playerState.flipW = rs.getInt("flipW");
                playerState.damageTaken = rs.getInt("damage_taken");
                playerState.potionsUsed = rs.getInt("potions_used");
            }
        }
        catch(SQLException e)
        {
            throw new ExceptionSQL("Eroare la accesarea bazei de date deoarece nu exista aceasta etichetare a coloanei, sau nu functioneaza baza de date, sau deja setul de rezultate a fost inchis.");
        }

        return playerState;
    }

    public void updateGameState(Integer currentLevel, Integer isContinueGame, Integer canPressContinue)
    {
        StringBuilder queryBuilder = new StringBuilder("UPDATE GameState SET ");
        List<Object> parameters = new ArrayList<>();

        try
        {
            if(currentLevel != null)
            {
                queryBuilder.append("current_level = ?, ");
                parameters.add(currentLevel);
            }
            if(isContinueGame != null)
            {
                queryBuilder.append("is_continue_game = ?, ");
                parameters.add(isContinueGame);
            }
            if(canPressContinue != null)
            {
                queryBuilder.append("can_press_continue = ?, ");
                parameters.add(canPressContinue);
            }
        }
        catch(UnsupportedOperationException e)
        {
            throw new ExceptionSQL("Operatie nepermisa pe parametrii acestia.");
        }
        catch(IllegalArgumentException e)
        {
            throw new ExceptionSQL("Una sau mai multe proprietati a datei nu permite stocarea la parametri.");
        }

        if(!parameters.isEmpty())
        {
            try
            {
                queryBuilder.setLength(queryBuilder.length() - 2);
            }
            catch(IndexOutOfBoundsException e)
            {
                throw new ExceptionSQL("Lungimea este negaitva pentru o declaratie sql.");
            }
        }

        queryBuilder.append(" WHERE id = 1");

        String query = queryBuilder.toString();
        try(PreparedStatement pstmt = connection.prepareStatement(query))
        {
            for(int i = 0; i < parameters.size(); ++i)
            {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            pstmt.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new ExceptionSQL("Eroare la actualizarea stării jocului.");
        }
    }

    public GameState getGameState( )
    {
        String query = "SELECT current_level, is_continue_game, can_press_continue FROM GameState WHERE id = 1";
        GameState gameState = new GameState();

        try(PreparedStatement pstmt = connection.prepareStatement(query))
        {
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
            {
                gameState.currentLevel = rs.getInt("current_level");
                gameState.isContinueGame = rs.getInt("is_continue_game");
                gameState.canPressContinue = rs.getInt("can_press_continue");
            }
        }
        catch(SQLException e)
        {
            throw new ExceptionSQL("Eroare la accesarea bazei de date deoarece nu exista aceasta etichetare a coloanei, sau nu functioneaza baza de date, sau deja setul de rezultate a fost inchis.");
        }

        return gameState;
    }

    public static class PlayerState
    {
        public double positionX;
        public double positionY;
        public int flipX;
        public int flipW;
        public int damageTaken;
        public int potionsUsed;
    }

    public static class GameState
    {
        public int currentLevel;
        public int isContinueGame;
        public int canPressContinue;
    }
}
