package dev.zskn.client.features;

import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class Features {
    public static Feature XRay = new XRayFeature();
    public static Feature Fly = new FlyFeature();
    public static Feature Fullbright = new FullbrightFeature();
    public static Feature NoFall = new NoFallFeature();
    public static Feature Speed = new SpeedFeature();
    public static Feature HighJump = new HighJumpFeature();
    public static Feature NoHunger = new NoHungerFeature();
    public static Feature AutoCrit = new AutoCritFeature();
    public static Feature KillAura = new KillAuraFeature();
    public static Feature BaseXray = new BaseXRayFeature();
    public static Feature ESP = new ESPFeature();
    public static Feature InstaMine = new InstaMineFeature();

    public static List<Feature> getAll() {
        Field[] declaredFields = Features.class.getDeclaredFields();
        List<Feature> features = new ArrayList<>();
        for (Field field : declaredFields) {
            if (field.getType() == Feature.class) {
                try {
                    features.add((Feature) field.get(null));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return  features;
    }

    public static Feature getFeature(String name) {
        return getAll().stream().filter(feature -> feature.displayText.equalsIgnoreCase(name)).findFirst().get();
    }

    public static void saveFeatures() {
        Yaml yaml = new Yaml();
        Map<String, Object> data = new HashMap<>();

        for (Feature feature : getAll()) {
            data.put(feature.displayText.toLowerCase(), feature.toggle);
        }

        try {
            FileWriter file = new FileWriter(FabricLoader.getInstance().getConfigDir().resolve("zskn-features.yaml").toFile());
            yaml.dump(data, file);
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadFromConfig() {
        Yaml yaml = new Yaml();
        File file = FabricLoader.getInstance().getConfigDir().resolve("zskn-features.yaml").toFile();
        if (!file.exists()) {
            return;
        }

        try {
            FileReader reader = new FileReader(file);
            Map<String, Object> data = yaml.load(reader);
            for (String name : data.keySet()) {
                getFeature(name).toggle = (boolean) data.get(name);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerKeybindings() {
        for (Feature feature: getAll()) {
            feature.registerKeybinding();
        }
    }
}
