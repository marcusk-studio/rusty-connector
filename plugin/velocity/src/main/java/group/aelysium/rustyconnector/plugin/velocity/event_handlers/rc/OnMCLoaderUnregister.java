package group.aelysium.rustyconnector.plugin.velocity.event_handlers.rc;

import group.aelysium.rustyconnector.plugin.velocity.PluginLogger;
import group.aelysium.rustyconnector.plugin.velocity.central.Tinder;
import group.aelysium.rustyconnector.plugin.velocity.lib.lang.ProxyLang;
import group.aelysium.rustyconnector.plugin.velocity.lib.server.RankedMCLoader;
import group.aelysium.rustyconnector.plugin.velocity.lib.webhook.DiscordWebhookMessage;
import group.aelysium.rustyconnector.plugin.velocity.lib.webhook.WebhookAlertFlag;
import group.aelysium.rustyconnector.plugin.velocity.lib.webhook.WebhookEventManager;
import group.aelysium.rustyconnector.toolkit.core.events.Listener;
import group.aelysium.rustyconnector.toolkit.core.log_gate.GateKey;
import group.aelysium.rustyconnector.toolkit.velocity.events.mc_loader.UnregisterEvent;

public class OnMCLoaderUnregister implements Listener<UnregisterEvent> {
    public void handler(UnregisterEvent event) {
        PluginLogger logger = Tinder.get().logger();

        System.out.println("MCLoader unregister event has fired!");

        try {
            RankedMCLoader mcLoader = (RankedMCLoader) event.mcLoader();
            mcLoader.implodeSession("The server that this session was on has closed!");
        } catch (Exception ignore) {}

        // Fire console message
        if (logger.loggerGate().check(GateKey.UNREGISTRATION_ATTEMPT))
            ProxyLang.UNREGISTERED.send(logger, event.mcLoader().uuidOrDisplayName(), event.family().id());

        // Fire discord webhook
        WebhookEventManager.fire(WebhookAlertFlag.SERVER_UNREGISTER, DiscordWebhookMessage.PROXY__SERVER_UNREGISTER.build(event.mcLoader()));
        WebhookEventManager.fire(WebhookAlertFlag.SERVER_UNREGISTER, event.family().id(), DiscordWebhookMessage.FAMILY__SERVER_UNREGISTER.build(event.mcLoader()));
    }
}