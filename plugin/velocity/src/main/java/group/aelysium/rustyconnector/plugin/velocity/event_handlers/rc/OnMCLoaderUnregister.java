package group.aelysium.rustyconnector.plugin.velocity.event_handlers.rc;

import group.aelysium.rustyconnector.plugin.velocity.PluginLogger;
import group.aelysium.rustyconnector.plugin.velocity.central.Tinder;
import group.aelysium.rustyconnector.plugin.velocity.lib.lang.ProxyLang;
import group.aelysium.rustyconnector.plugin.velocity.lib.webhook.DiscordWebhookMessage;
import group.aelysium.rustyconnector.plugin.velocity.lib.webhook.WebhookAlertFlag;
import group.aelysium.rustyconnector.plugin.velocity.lib.webhook.WebhookEventManager;
import group.aelysium.rustyconnector.toolkit.core.events.Listener;
import group.aelysium.rustyconnector.toolkit.core.log_gate.GateKey;
import group.aelysium.rustyconnector.toolkit.velocity.events.mc_loader.RegisterEvent;
import group.aelysium.rustyconnector.toolkit.velocity.events.mc_loader.UnregisterEvent;
import net.engio.mbassy.listener.Handler;

public class OnMCLoaderUnregister extends Listener<UnregisterEvent> {
    @Override
    @Handler() // Changes priority to {@link Priority.NATIVE}
    public void handler(UnregisterEvent event) {
        PluginLogger logger = Tinder.get().logger();

        // Fire console message
        if (logger.loggerGate().check(GateKey.UNREGISTRATION_ATTEMPT))
            ProxyLang.UNREGISTERED.send(logger, event.mcLoader().serverInfo(), event.family().id());

        // Fire discord webhook
        WebhookEventManager.fire(WebhookAlertFlag.SERVER_UNREGISTER, DiscordWebhookMessage.PROXY__SERVER_UNREGISTER.build(event.mcLoader()));
        WebhookEventManager.fire(WebhookAlertFlag.SERVER_UNREGISTER, event.family().id(), DiscordWebhookMessage.FAMILY__SERVER_UNREGISTER.build(event.mcLoader()));
    }
}