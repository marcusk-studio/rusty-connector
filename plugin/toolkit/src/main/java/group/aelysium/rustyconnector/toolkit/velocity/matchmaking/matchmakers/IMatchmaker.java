package group.aelysium.rustyconnector.toolkit.velocity.matchmaking.matchmakers;

import group.aelysium.rustyconnector.toolkit.core.serviceable.interfaces.Service;
import group.aelysium.rustyconnector.toolkit.velocity.matchmaking.gameplay.ISession;
import group.aelysium.rustyconnector.toolkit.velocity.matchmaking.gameplay.ITeam;
import group.aelysium.rustyconnector.toolkit.velocity.matchmaking.storage.IRankedGame;
import group.aelysium.rustyconnector.toolkit.velocity.matchmaking.storage.IRankedPlayer;
import group.aelysium.rustyconnector.toolkit.velocity.matchmaking.storage.IScoreCard;
import group.aelysium.rustyconnector.toolkit.velocity.matchmaking.storage.player_rank.IPlayerRank;
import group.aelysium.rustyconnector.toolkit.velocity.players.Player;
import group.aelysium.rustyconnector.toolkit.velocity.storage.IMySQLStorageService;
import group.aelysium.rustyconnector.toolkit.velocity.util.LiquidTimestamp;

import java.util.List;

public interface IMatchmaker<TPlayer extends Player, TRankedPlayer extends IRankedPlayer<? extends Player, ? extends IPlayerRank<?>>> extends Service {
    /**
     * Using the players contained in the matchmaker, attempt to make a game.
     */
    ISession make();

    /**
     * Checks if there is the bare minimum worth of players in the matchmaker necessary to create at least one game.
     * @return `true` if there are at least enough players to make a single game. `false` otherwise.
     */
    boolean minimumPlayersExist();

    /**
     * Completely sort all players in the matchmaker based on their rank in order from least to greatest.
     * The sorting algorithm is a weighted quicksort.
     */
    void completeSort();

    /**
     * Inserts a player into the matchmaker.
     * <p>
     * Specifically, this method will resolve the passed player into a {@link IRankedPlayer}.
     * This method performs a single sort and injects the player into an approximation of the best place for them to reside.
     * Thus reducing how frequently you'll need to perform a full sort on the metchmaker.
     * @param player The player to add.
     * @throws RuntimeException If there was an issue while adding the player to this matchmaker.
     */
    void add(TPlayer player);

    /**
     * Removes the player from the matchmaker.
     * This method will only remove the player if they are currently waiting.
     * <p>
     * If this player has already leaded into a session, you'll have to find their session and remove them from it.
     * @param player The player to remove.
     */
    void remove(TPlayer player);

    /**
     * Gets The number of players currently waiting in the matchmaker.
     * @return The number of players waiting.
     */
    List<TRankedPlayer> waitingPlayers();

    /**
     * Checks if a player is currently waiting in the matchmaker.
     * @param player The player to look for.
     * @return `true` if the player is waiting in the matchmaker. `false` otherwise.
     */
    boolean contains(TRankedPlayer player);

    record Settings (
            IMySQLStorageService storage,
            IScoreCard.IRankSchema.Type<?> algorithm,
            IRankedGame<? extends Player> game,
            List<ITeam.Settings> teams,
            double variance,
            LiquidTimestamp interval
    ) {}
}