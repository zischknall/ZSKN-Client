package dev.zskn.client.gui;

import dev.zskn.client.features.Feature;
import dev.zskn.client.features.Features;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.HorizontalFlowLayout;
import io.wispforest.owo.ui.container.VerticalFlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class ZSKNScreen extends BaseOwoScreen<FlowLayout> {
    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.horizontalAlignment(HorizontalAlignment.LEFT)
                .verticalAlignment(VerticalAlignment.CENTER)
                .surface(Surface.VANILLA_TRANSLUCENT);

        VerticalFlowLayout featuresContainer = Containers.verticalFlow(Sizing.content(), Sizing.content());
        featuresContainer.padding(Insets.of(10))
                .verticalAlignment(VerticalAlignment.TOP)
                .horizontalAlignment(HorizontalAlignment.LEFT);
        Text featuresText = Text.of("Features");
        featuresText.getStyle().withBold(true);
        featuresContainer.child(Components.label(featuresText));

        List<Feature> featureList = Features.getAll().stream().sorted(Comparator.comparing(feature -> feature.displayText)).toList();
        for (Feature feature : featureList) {
            HorizontalFlowLayout indiviualFeatureContainer = Containers.horizontalFlow(Sizing.fill(30), Sizing.content());
            indiviualFeatureContainer.verticalAlignment(VerticalAlignment.CENTER)
                    .padding(Insets.of(1));

            HorizontalFlowLayout textContainer = Containers.horizontalFlow(Sizing.fill(50), Sizing.content());
            textContainer.horizontalAlignment(HorizontalAlignment.LEFT);
            textContainer.child(Components.label(Text.of(feature.displayText)));

            Text buttonName;
            if (feature.toggle) {
                buttonName = Text.of("Enabled");
                buttonName.getStyle().withColor(0x00FF00);
            } else {
                buttonName = Text.of("Disabled");
                buttonName.getStyle().withColor(0xFF0000);
            }

            HorizontalFlowLayout buttonContainer = Containers.horizontalFlow(Sizing.fill(50), Sizing.content());
            buttonContainer.horizontalAlignment(HorizontalAlignment.RIGHT);
            buttonContainer.child(Components.button(buttonName, buttonComponent -> feature.toggle())).horizontalAlignment(HorizontalAlignment.RIGHT);

            indiviualFeatureContainer.child(textContainer);
            indiviualFeatureContainer.child(buttonContainer);
            featuresContainer.child(indiviualFeatureContainer);
        }

        rootComponent.child(Containers.verticalScroll(Sizing.content(), Sizing.fill(80), featuresContainer).padding(Insets.of(2)));
    }
}
