package com.aviora.redmc.permissions.models;

public record PermissionEntry(String name, int weight, boolean allowed) {

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isAllowed() {
        return allowed;
    }
}
