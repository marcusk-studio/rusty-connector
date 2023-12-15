package group.aelysium.rustyconnector.toolkit.velocity.players;

import group.aelysium.rustyconnector.toolkit.core.serviceable.interfaces.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * The player service provides player based services to RustyConnector.
 * To fetch players, you can use a `Player.Reference`
 */
public interface IPlayerService extends Service {
    /**
     * Finds a player based on a uuid.
     * An alternate route of getting a player, other than "tinder.services().player().fetch()", can be to use {@link Player.Reference new Family.Reference(uuid)}{@link Player.Reference#get() .get()}.
     * @param uuid The uuid to search for.
     * @return {@link Optional<Player>}
     */
    Optional<? extends Player> fetch(UUID uuid);

    /**
     * Finds a player based on a username.
     * An alternate route of getting a player, other than "tinder.services().player().fetch()", can be to use {@link Player.UsernameReference new Family.UsernameReference(username)}{@link Player.Reference#get() .get()}.
     * @param username The username to search for.
     * @return {@link Optional<Player>}
     */
    Optional<? extends Player> fetch(String username);
}
