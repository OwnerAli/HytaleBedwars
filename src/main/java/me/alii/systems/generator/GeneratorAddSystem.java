package me.alii.systems.generator;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefChangeSystem;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import me.alii.components.GeneratorComponent;
import me.alii.domain.TypeSpeed;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class GeneratorAddSystem extends RefChangeSystem<ChunkStore, GeneratorComponent> {

    @Override
    public void onComponentAdded(@NonNullDecl Ref<ChunkStore> ref, @NonNullDecl GeneratorComponent generatorComponent,
                                 @NonNullDecl Store<ChunkStore> store, @NonNullDecl CommandBuffer<ChunkStore> commandBuffer) {
        generatorComponent.getGeneratorConfig()
                .getTypeSpeeds()
                .forEach(ts -> ts.startGenerating(generatorComponent.getSpawnPosition(),
                        commandBuffer.getExternalData().getWorld()));
    }

    @Override
    public void onComponentRemoved(@NonNullDecl Ref<ChunkStore> ref, @NonNullDecl GeneratorComponent generatorComponent,
                                   @NonNullDecl Store<ChunkStore> store, @NonNullDecl CommandBuffer<ChunkStore> commandBuffer) {
        generatorComponent.getGeneratorConfig()
                .getTypeSpeeds()
                .forEach(TypeSpeed::stopGenerating);
    }

    @Override
    public void onComponentSet(@NonNullDecl Ref<ChunkStore> ref, @NullableDecl GeneratorComponent generatorComponent,
                               @NonNullDecl GeneratorComponent t1, @NonNullDecl Store<ChunkStore> store, @NonNullDecl CommandBuffer<ChunkStore> commandBuffer) {
        // Unused
    }

    @NonNullDecl
    @Override
    public ComponentType<ChunkStore, GeneratorComponent> componentType() {
        return GeneratorComponent.getComponentType();
    }

    @NullableDecl
    @Override
    public Query<ChunkStore> getQuery() {
        return componentType();
    }
}
