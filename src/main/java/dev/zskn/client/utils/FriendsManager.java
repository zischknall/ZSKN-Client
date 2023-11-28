package dev.zskn.client.utils;

import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FriendsManager {
    private List<UUID> friends = new ArrayList<>();

    public void addFriend(UUID uuid) {
        if (friends.contains(uuid)) {
            return;
        }

        friends.add(uuid);
        saveFriends();
    }

    public List<UUID> getFriends() {
        return friends;
    }

    private void setFriends(List<UUID> friends) {
        this.friends = friends;
    }

    public void removeFriend(UUID uuid) {
        if (friends.remove(uuid)) {
            saveFriends();
        }
    }

    public boolean contains(UUID uuid) {
        return friends.contains(uuid);
    }

    public void saveFriends() {
        Yaml yaml = new Yaml();
        Map<String, Object> data = new HashMap<>();
        data.put("friends", getFriends().stream().map(UUID::toString).toList());
        try {
            FileWriter file = new FileWriter(FabricLoader.getInstance().getConfigDir().resolve("zskn-friends.yaml").toFile());
            yaml.dump(data, file);
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadFriends() {
        Yaml yaml = new Yaml();
        File file = FabricLoader.getInstance().getConfigDir().resolve("zskn-friends.yaml").toFile();
        if (!file.exists()) {
            return;
        }

        try {
            FileReader reader = new FileReader(file);
            Map<String, Object> data = yaml.load(reader);
            @SuppressWarnings("unchecked")
            List<String> rawFriendUUIDs = (List<String>) data.get("friends");
            List<UUID> friends = rawFriendUUIDs.stream().map(UUID::fromString).collect(Collectors.toList());
            setFriends(friends);
            reader.close();
        } catch (IOException | ClassCastException e) {
            throw new RuntimeException(e);
        }
    }
}
