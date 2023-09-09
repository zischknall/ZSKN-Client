package dev.zskn.client.gui;

import dev.zskn.client.ZSKNClient;
import dev.zskn.client.features.Feature;
import dev.zskn.client.features.Features;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
        FlowLayout tabsContainer = Containers.horizontalFlow(Sizing.content(), Sizing.content());
        tabsContainer.horizontalAlignment(HorizontalAlignment.LEFT);
        tabsContainer.verticalAlignment(VerticalAlignment.CENTER);

        FlowLayout featuresContainer = Containers.verticalFlow(Sizing.fill(30), Sizing.content());
        featuresContainer.padding(Insets.of(10))
                .verticalAlignment(VerticalAlignment.TOP)
                .horizontalAlignment(HorizontalAlignment.LEFT)
                .surface(Surface.PANEL);
        Text featuresText = Text.of("Features");
        featuresText.getStyle().withBold(true);
        featuresContainer.child(Components.label(featuresText));

        List<Feature> featureList = Features.getAll().stream().sorted(Comparator.comparing(feature -> feature.displayText)).toList();
        for (Feature feature : featureList) {
            FlowLayout indiviualFeatureContainer = Containers.horizontalFlow(Sizing.content(), Sizing.content());
            indiviualFeatureContainer.verticalAlignment(VerticalAlignment.CENTER)
                    .padding(Insets.of(3))
                    .surface(Surface.DARK_PANEL);

            FlowLayout textContainer = Containers.horizontalFlow(Sizing.fill(50), Sizing.content());
            textContainer.horizontalAlignment(HorizontalAlignment.LEFT);
            textContainer.child(Components.label(Text.of(feature.displayText)));

            FlowLayout buttonContainer = Containers.horizontalFlow(Sizing.fill(50), Sizing.content());
            buttonContainer.horizontalAlignment(HorizontalAlignment.RIGHT);
            buttonContainer.child(Components.button(Text.of("✔"), buttonComponent -> feature.enable())).horizontalAlignment(HorizontalAlignment.RIGHT);
            buttonContainer.child(Components.button(Text.of("❌"), buttonComponent -> feature.disable())).horizontalAlignment(HorizontalAlignment.RIGHT);

            indiviualFeatureContainer.child(textContainer);
            indiviualFeatureContainer.child(buttonContainer);
            featuresContainer.child(indiviualFeatureContainer);
        }

        FlowLayout friendsContainer = Containers.verticalFlow(Sizing.fill(30), Sizing.content());
        friendsContainer.padding(Insets.of(10))
                .verticalAlignment(VerticalAlignment.TOP)
                .horizontalAlignment(HorizontalAlignment.LEFT)
                .surface(Surface.PANEL);
        Text friendsText = Text.of("Friends");
        friendsText.getStyle().withBold(true);
        friendsContainer.child(Components.label(friendsText));

        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            Collection<UUID> players = MinecraftClient.getInstance().getNetworkHandler().getPlayerUuids();
            if (!players.isEmpty()) {
                for (UUID playerUUID : players) {
                    PlayerListEntry player = MinecraftClient.getInstance().getNetworkHandler().getPlayerListEntry(playerUUID);
                    if (Objects.equals(MinecraftClient.getInstance().player.getGameProfile().getName(), player.getProfile().getName())) {
                        continue;
                    }

                    FlowLayout indiviualFriendsContainer = Containers.horizontalFlow(Sizing.content(), Sizing.content());
                    indiviualFriendsContainer.verticalAlignment(VerticalAlignment.CENTER)
                            .padding(Insets.of(3))
                            .surface(Surface.DARK_PANEL);

                    FlowLayout textContainer = Containers.horizontalFlow(Sizing.fill(50), Sizing.content());
                    textContainer.horizontalAlignment(HorizontalAlignment.LEFT);
                    textContainer.child(Components.label(Text.of(player.getProfile().getName()))).tooltip(Text.of(playerUUID.toString()));

                    FlowLayout buttonContainer = Containers.horizontalFlow(Sizing.fill(50), Sizing.content());
                    buttonContainer.horizontalAlignment(HorizontalAlignment.RIGHT);
                    if (ZSKNClient.FRIENDS.contains(playerUUID)) {
                        buttonContainer.child(Components.button(Text.of("❌"), buttonComponent -> ZSKNClient.FRIENDS.removeFriend(playerUUID))).horizontalAlignment(HorizontalAlignment.RIGHT);
                    } else {
                        buttonContainer.child(Components.button(Text.of("✔"), buttonComponent -> ZSKNClient.FRIENDS.addFriend(playerUUID))).horizontalAlignment(HorizontalAlignment.RIGHT);
                    }

                    indiviualFriendsContainer.child(textContainer);
                    indiviualFriendsContainer.child(buttonContainer);
                    friendsContainer.child(indiviualFriendsContainer);
                }
            }
        }

        tabsContainer.child(Containers.verticalScroll(Sizing.content(), Sizing.fill(70), featuresContainer).padding(Insets.of(2)));
        tabsContainer.child(Containers.verticalScroll(Sizing.content(), Sizing.fill(70), friendsContainer).padding(Insets.of(2)));
        rootComponent.child(tabsContainer);
    }
}
