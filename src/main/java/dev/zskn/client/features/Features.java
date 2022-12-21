package dev.zskn.client.features;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Features {
    public static Feature XRay = new XRayFeature();
    public static Feature Fly = new FlyFeature();
    public static Feature Fullbright = new FullbrightFeature();
    public static Feature NoFall = new NoFallFeature();

    public static Feature SpeedFeature = new SpeedFeature();

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
}
