package me.alii.domain;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.set.SetCodec;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class GeneratorConfig {
    public static final BuilderCodec<GeneratorConfig> CODEC;

    private String id = "";
    private Set<TypeSpeed> typeSpeeds = new HashSet<>();

    public GeneratorConfig() {

    }

    public GeneratorConfig(String id, Set<TypeSpeed> typeSpeeds) {
        this.id = id;
        this.typeSpeeds = typeSpeeds;
    }

    static {
        var typeSpeedSetCodec =
                new SetCodec<>(TypeSpeed.CODEC, HashSet::new, false);

        CODEC = BuilderCodec.builder(GeneratorConfig.class, GeneratorConfig::new)
                .append(new KeyedCodec<>("Id", Codec.STRING),
                        (type, value) -> type.id = value,
                        (type) -> type.id).add()
                .append(new KeyedCodec<>("TypeSpeeds", typeSpeedSetCodec),
                        (type, value) -> type.typeSpeeds = value,
                        (type) -> type.typeSpeeds).add()
                .build();
    }
}
