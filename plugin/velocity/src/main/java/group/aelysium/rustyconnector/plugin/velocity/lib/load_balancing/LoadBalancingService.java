package group.aelysium.rustyconnector.plugin.velocity.lib.load_balancing;

import group.aelysium.rustyconnector.core.lib.events.EventManager;
import group.aelysium.rustyconnector.plugin.velocity.event_handlers.EventDispatch;
import group.aelysium.rustyconnector.plugin.velocity.lib.family.FamilyService;
import group.aelysium.rustyconnector.toolkit.core.log_gate.GateKey;
import group.aelysium.rustyconnector.toolkit.core.serviceable.ClockService;
import group.aelysium.rustyconnector.plugin.velocity.PluginLogger;
import group.aelysium.rustyconnector.plugin.velocity.lib.family.Family;
import group.aelysium.rustyconnector.plugin.velocity.lib.lang.ProxyLang;
import group.aelysium.rustyconnector.toolkit.velocity.events.family.RebalanceEvent;
import group.aelysium.rustyconnector.toolkit.velocity.util.DependencyInjector;
import group.aelysium.rustyconnector.toolkit.velocity.util.LiquidTimestamp;

public class LoadBalancingService extends ClockService {
    protected final LiquidTimestamp heartbeat;
    public LoadBalancingService(int threads, LiquidTimestamp heartbeat) {
        super(threads);
        this.heartbeat = heartbeat;
    }

    public void init(DependencyInjector.DI3<FamilyService, PluginLogger, EventManager> deps) {
        for (Family family : deps.d1().dump()) {
            this.scheduleRecurring(() -> {
                try {
                    PluginLogger logger = deps.d2();

                    EventDispatch.UnSafe.fireAndForget(new RebalanceEvent(family));
                    family.balance();
                    if (logger.loggerGate().check(GateKey.FAMILY_BALANCING))
                        ProxyLang.FAMILY_BALANCING.send(logger, family);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, this.heartbeat);
        }
    }
}
