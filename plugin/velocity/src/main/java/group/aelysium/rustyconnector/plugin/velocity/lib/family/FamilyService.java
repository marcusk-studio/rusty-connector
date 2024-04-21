package group.aelysium.rustyconnector.plugin.velocity.lib.family;

import group.aelysium.rustyconnector.toolkit.core.absolute_redundancy.Particle;
import group.aelysium.rustyconnector.toolkit.core.serviceable.interfaces.Service;
import group.aelysium.rustyconnector.toolkit.velocity.config.FamiliesConfig;
import group.aelysium.rustyconnector.toolkit.velocity.family.IFamily;
import group.aelysium.rustyconnector.toolkit.velocity.family.IFamilyService;
import group.aelysium.rustyconnector.toolkit.velocity.family.scalar_family.IRootFamily;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FamilyService extends IFamilyService {
    private final Map<String, IFamily> families = new HashMap<>();
    private WeakReference<IRootFamily> rootFamily;

    protected FamilyService() {}

    public void setRootFamily(IRootFamily family) {
        this.families.put(family.id(), family);
        this.rootFamily = new WeakReference<>(family);
    }

    public IRootFamily rootFamily() {
        return this.rootFamily.get();
    }

    public Optional<IFamily> find(String id) {
        IFamily family = this.families.get(id);
        if(family == null) return Optional.empty();
        return Optional.of(family);
    }

    public void add(IFamily family) {
        this.families.put(family.id(), (Family) family);
    }

    public void remove(IFamily family) {
        this.families.remove(family.id());
    }

    public List<IFamily> dump() {
        return this.families.values().stream().toList();
    }

    public void clear() {
        this.families.clear();
    }

    public int size() {
        return this.families.size();
    }

    public void close() throws Exception {
        // Teardown logic for any families that need it
        for (IFamily family : this.families.values()) {
            if(family instanceof Service)
                ((Service) family).kill();
        }

        this.families.clear();
        this.rootFamily.clear();
    }

    public static class Tinder extends Particle.Tinder<FamilyService> {
        public Tinder() {}

        @Override
        public @NotNull FamilyService ignite() throws Exception {
            return new FamilyService();
        }
    }
}
