package com.aviora.redmc.permissions.utils;

import com.aviora.redmc.permissions.models.Group;
import com.aviora.redmc.permissions.models.PermissionEntry;
import com.aviora.redmc.permissions.models.PlayerData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionResolver {

    public static Map<String, Boolean> resolve(PlayerData player, Map<String, Group> groups) {
        Map<String, PermissionEntry> resolved = new HashMap<>();

        for (String groupId : player.getGroupIds()) {
            Group group = groups.get(groupId.toLowerCase());
            if (group == null) continue;

            applyPermissions(group.getPermissions(), resolved);
        }

        applyPermissions(player.getPermissions(), resolved);

        Map<String, Boolean> result = new HashMap<>();
        for (Map.Entry<String, PermissionEntry> entry : resolved.entrySet()) {
            result.put(entry.getKey(), entry.getValue().isAllowed());
        }

        return result;
    }

    private static void applyPermissions(List<PermissionEntry> entries, Map<String, PermissionEntry> target) {
        for (PermissionEntry entry : entries) {
            String name = entry.getName().toLowerCase();

            if (!target.containsKey(name)) {
                target.put(name, entry);
            } else {
                PermissionEntry existing = target.get(name);
                if (entry.getWeight() > existing.getWeight()) {
                    target.put(name, entry);
                }
            }
        }
    }
}
