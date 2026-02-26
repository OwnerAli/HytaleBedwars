package me.alii.modules;

import com.hypixel.hytale.component.ComponentRegistryProxy;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import lombok.Getter;
import me.alii.BedwarsPlugin;
import me.alii.components.GeneratorComponent;
import me.alii.systems.generator.GeneratorAddSystem;

@Getter
public class GeneratorModule {

    @Getter
    private static GeneratorModule instance;

    private ComponentType<ChunkStore, GeneratorComponent> generatorComponentType;

    public GeneratorModule(BedwarsPlugin bedwarsPlugin) {
        instance = this;
        registerComponents(bedwarsPlugin);
    }

    private void registerComponents(BedwarsPlugin bedwarsPlugin) {
        ComponentRegistryProxy<ChunkStore> chunkStoreRegistry =
                bedwarsPlugin.getChunkStoreRegistry();

        this.generatorComponentType = chunkStoreRegistry.registerComponent(
                GeneratorComponent.class,
                "GeneratorComponent",
                GeneratorComponent.CODEC
        );
        chunkStoreRegistry.registerSystem(new GeneratorAddSystem());
    }
}
