package services.xenlan.hub.tablist.ziggurat.utils.impl;






import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import services.xenlan.hub.tablist.utils.PlayerUtil;
import services.xenlan.hub.tablist.utils.playerversion.PlayerVersion;
import services.xenlan.hub.tablist.ziggurat.ZigguratCommons;
import services.xenlan.hub.tablist.ziggurat.ZigguratTablist;
import services.xenlan.hub.tablist.ziggurat.utils.*;

import java.util.Map;
import java.util.UUID;

public class v1_7TabImpl implements IZigguratHelper {

    private static MinecraftServer server = MinecraftServer.getServer();
    private static WorldServer world = server.getWorldServer(0);
    private static PlayerInteractManager manager = new PlayerInteractManager(world);

    public v1_7TabImpl() {
    }

    @Override
    public TabEntry createFakePlayer(ZigguratTablist zigguratTablist, String string, TabColumn column, Integer slot, Integer rawSlot) {
        OfflinePlayer offlinePlayer = new OfflinePlayer() {
            private UUID uuid = UUID.randomUUID();

            @Override
            public boolean isOnline() {
                return true;
            }

            @Override
            public String getName() {
                return string;
            }

            @Override
            public UUID getUniqueId() {
                return uuid;
            }

            @Override
            public boolean isBanned() {
                return false;
            }

            @Override
            public void setBanned(boolean b) {

            }

            @Override
            public boolean isWhitelisted() {
                return false;
            }

            @Override
            public void setWhitelisted(boolean b) {

            }

            @Override
            public Player getPlayer() {
                return null;
            }

            @Override
            public long getFirstPlayed() {
                return 0;
            }

            @Override
            public long getLastPlayed() {
                return 0;
            }

            @Override
            public boolean hasPlayedBefore() {
                return false;
            }

            @Override
            public Location getBedSpawnLocation() {
                return null;
            }

            @Override
            public Map<String, Object> serialize() {
                return null;
            }

            @Override
            public boolean isOp() {
                return false;
            }

            @Override
            public void setOp(boolean b) {

            }
        };

        Player player = zigguratTablist.getPlayer();

        PlayerVersion playerVersion = PlayerUtil.getPlayerVersion(player);

        GameProfile profile = new GameProfile(offlinePlayer.getUniqueId(), LegacyClientUtils.tabEntrys.get(rawSlot - 1) + "");

        EntityPlayer entity = new EntityPlayer(server, world, profile, manager);

        if (playerVersion != PlayerVersion.v1_7) {
            profile.getProperties().put("textures", new Property("textures", ZigguratCommons.defaultTexture.SKIN_VALUE, ZigguratCommons.defaultTexture.SKIN_SIGNATURE));
        }

        entity.ping = 1;

        Packet packet = PacketPlayOutPlayerInfo.addPlayer(entity);

        this.sendPacket(zigguratTablist.getPlayer(), packet);

        return new TabEntry(string, offlinePlayer, "", zigguratTablist, ZigguratCommons.defaultTexture, column, slot, rawSlot, 0);
    }

    @Override
    public void updateFakeName(ZigguratTablist zigguratTablist, TabEntry tabEntry, String text) {

        if (tabEntry.getText().equals(text)) {
            return;
        }

        Player player = zigguratTablist.getPlayer();

        String[] newStrings = ZigguratTablist.splitStrings(text, tabEntry.getRawSlot());

        Team team = player.getScoreboard().getTeam(LegacyClientUtils.teamNames.get(tabEntry.getRawSlot()-1));
        if (team == null) {
            team = player.getScoreboard().registerNewTeam(LegacyClientUtils.teamNames.get(tabEntry.getRawSlot()-1));
        }
        team.setPrefix(ChatColor.translateAlternateColorCodes('&', newStrings[0]));
        if (newStrings.length > 1) {
            team.setSuffix(ChatColor.translateAlternateColorCodes('&', newStrings[1]));
        } else {
            team.setSuffix("");
        }

        tabEntry.setText(text);
    }

    @Override
    public void updateFakeLatency(ZigguratTablist zigguratTablist, TabEntry tabEntry, Integer latency) {
        if (tabEntry.getLatency() == latency) {
            return;
        }

        GameProfile profile = new GameProfile(tabEntry.getOfflinePlayer().getUniqueId(), LegacyClientUtils.tabEntrys.get(tabEntry.getRawSlot() - 1) + "");
        EntityPlayer entity = new EntityPlayer(server, world, profile, manager);

        entity.ping = latency;

        Packet packet = PacketPlayOutPlayerInfo.updatePing(entity);

        sendPacket(zigguratTablist.getPlayer(), packet);

        tabEntry.setLatency(latency);
    }

    @Override
    public void updateFakeSkin(ZigguratTablist zigguratTablist, TabEntry tabEntry, SkinTexture skinTexture) {

        if (tabEntry.getTexture() == skinTexture){
            return;
        }

        GameProfile profile = new GameProfile(tabEntry.getOfflinePlayer().getUniqueId(), LegacyClientUtils.tabEntrys.get(tabEntry.getRawSlot() - 1) + "");

        EntityPlayer entity = new EntityPlayer(server, world, profile, manager);

        profile.getProperties().put("textures", new Property("textures", skinTexture.SKIN_VALUE, skinTexture.SKIN_SIGNATURE));

        Packet removePlayer = PacketPlayOutPlayerInfo.removePlayer(entity);

        this.sendPacket(zigguratTablist.getPlayer(), removePlayer);

        Packet addPlayer = PacketPlayOutPlayerInfo.addPlayer(entity);

        this.sendPacket(zigguratTablist.getPlayer(), addPlayer);

        tabEntry.setTexture(skinTexture);
    }

    @Override
    public void updateHeaderAndFooter(ZigguratTablist zigguratTablist, String header, String footer) {

    }

    private void sendPacket(Player player, Packet packet) {
        getEntity(player).playerConnection.sendPacket(packet);
    }

    private EntityPlayer getEntity(Player player) {
        return ((CraftPlayer)player).getHandle();
    }

}
