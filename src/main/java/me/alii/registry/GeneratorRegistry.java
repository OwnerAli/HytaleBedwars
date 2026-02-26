package me.alii.registry;

import me.alii.components.GeneratorComponent;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class GeneratorRegistry {
    private final Map<String, GeneratorComponent> componentMap;

    public GeneratorRegistry() {
        this.componentMap = new ConcurrentHashMap<>();
    }

    public Optional<GeneratorComponent> getComponent(String id) {
        return Optional.ofNullable(componentMap.get(id));
    }

    public void registerComponent(GeneratorComponent generatorComponent) {
        this.componentMap.put(generatorComponent.getGeneratorConfig().getId(), generatorComponent);
    }

    private static final class InstanceHolder {
        private static final GeneratorRegistry instance = new GeneratorRegistry();
    }

    public static GeneratorRegistry getInstance() {
        return InstanceHolder.instance;
    }
}
