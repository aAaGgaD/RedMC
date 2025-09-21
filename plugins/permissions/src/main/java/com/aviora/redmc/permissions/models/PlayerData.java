package com.aviora.redmc.permissions.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private String name;

    private final List<String> groupIds = new ArrayList<>();
    private final List<PermissionEntry> permissions = new ArrayList<>();

    public PlayerData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void addGroup(String groupId) {
        if (!groupIds.contains(groupId.toLowerCase())) {
            groupIds.add(groupId.toLowerCase());
        }
    }

    public void removeGroup(String groupId) {
        groupIds.remove(groupId.toLowerCase());
    }

    public List<PermissionEntry> getPermissions() {
        return permissions;
    }

    public void addPermission(PermissionEntry permission) {
        permissions.add(permission);
    }

    public void removePermission(String permission) {
	    permissions.removeIf(entry -> entry.getName().equalsIgnoreCase(permission));
    }
}
