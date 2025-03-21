package com.bergerkiller.bukkit.tc.controller;

import java.util.List;

public sealed class MinecartGroupMembers extends AtomicList<MinecartMember<?>> permits MinecartGroup {
    public List<MinecartMember<?>> getMembers() {
        return getItems();
    }

    public List<MinecartMember<?>> clone() {
        return copyList();
    }
}
