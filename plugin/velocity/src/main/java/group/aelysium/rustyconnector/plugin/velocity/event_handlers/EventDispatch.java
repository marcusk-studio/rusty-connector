package group.aelysium.rustyconnector.plugin.velocity.event_handlers;

import group.aelysium.rustyconnector.plugin.velocity.central.Tinder;
import group.aelysium.rustyconnector.toolkit.core.events.Cancelable;

/**
 * Convenience class for fetching the event manager and firing an event.
 */
public class EventDispatch {
    /**
     * A safe event dispatch.
     * The methods here should not return exceptions during the firing of events.
     */
    public static class Safe {
        public static void fire(Cancelable event) {
            try {
                Tinder.get().services().events().fire(event);
            } catch (Exception ignore) {}
        }
        public static void fireAndForget(Cancelable event) {
            try {
                Tinder.get().services().events().fireAndForget(event);
            } catch (Exception ignore) {}
        }
    }

    /**
     * An unsafe event dispatch.
     * Methods defined here are not unsafe in that they can't be used in code, instead, they may produce exceptions when attempting to fire an event.
     */
    public static class UnSafe {
        public static void fire(Cancelable event) {
            Tinder.get().services().events().fire(event);
        }
        public static void fireAndForget(Cancelable event) {
            Tinder.get().services().events().fireAndForget(event);
        }
    }
}