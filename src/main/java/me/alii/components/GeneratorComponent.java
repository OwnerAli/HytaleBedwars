package me.alii.components;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import lombok.Getter;
import lombok.Setter;
import me.alii.domain.GeneratorConfig;
import me.alii.modules.GeneratorModule;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

@Setter
@Getter
public class GeneratorComponent implements Component<ChunkStore> {
    public static final BuilderCodec<GeneratorComponent> CODEC;

    private Vector3d spawnPosition = new Vector3d();
    private GeneratorConfig generatorConfig = new GeneratorConfig();

    public static ComponentType<ChunkStore, GeneratorComponent> getComponentType() {
        return GeneratorModule.getInstance().getGeneratorComponentType();
    }

    @NullableDecl
    @Override
    public Component<ChunkStore> clone() {
        GeneratorComponent generatorComponent = new GeneratorComponent();
        generatorComponent.spawnPosition = this.spawnPosition;
        generatorComponent.generatorConfig = this.generatorConfig;
        return generatorComponent;
    }

    static {
        CODEC = BuilderCodec.builder(GeneratorComponent.class, GeneratorComponent::new)
                .append(new KeyedCodec<>("SpawnPosition", Vector3d.CODEC),
                        ((type, value) -> type.spawnPosition = value),
                        (type -> type.spawnPosition))
                .add()
                .append(new KeyedCodec<>("GeneratorConfig", GeneratorConfig.CODEC),
                        ((type, value) -> type.generatorConfig = value),
                        (type -> type.generatorConfig))
                .add()
                .build();
    }
}