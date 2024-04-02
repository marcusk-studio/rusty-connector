package group.aelysium.rustyconnector.toolkit.velocity.matchmaking;

import group.aelysium.rustyconnector.toolkit.velocity.load_balancing.ISortable;
import group.aelysium.rustyconnector.toolkit.velocity.matchmaking.storage.IPlayerRank;
import group.aelysium.rustyconnector.toolkit.velocity.player.IPlayer;

public interface IMatchPlayer<PlayerRank extends IPlayerRank> extends ISortable {
    public IPlayer player();
    public PlayerRank rank();
    public String gameId();
}
