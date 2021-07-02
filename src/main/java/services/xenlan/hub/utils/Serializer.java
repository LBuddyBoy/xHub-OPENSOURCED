package services.xenlan.hub.utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;

import java.util.*;

public class Serializer {

    public static String serializeItemStack(ItemStack i) {
        String[] parts = new String[9];
        parts[0] = i.getType().name();
        parts[1] = Integer.toString(i.getAmount());
        parts[2] = String.valueOf(i.getDurability());
        parts[3] = (i.getItemMeta().hasDisplayName()?i.getItemMeta().getDisplayName().replaceAll("\\xa7","&").replaceAll("§", "&"):"");
        parts[4] = String.valueOf(i.getData().getData());
        if (i.getItemMeta() instanceof LeatherArmorMeta) {
            parts[7] = String.valueOf(((LeatherArmorMeta) i).getColor());
        } else {
            parts[7] = null;
        }
        parts[8] = getEnchants(i);
        parts[5] = serializeLore(i);
        parts[6] = serializeItemFlags(i.getItemMeta().getItemFlags());
        return StringUtils.join(parts, ";");
    }

    public static ItemStack deserializeItemStack(String p) {

        String[] a = p.split(";");
        ItemStack i = new ItemStack(Material.getMaterial(a[0]), Integer.parseInt(a[1]));
        i.setDurability((short) Integer.parseInt(a[2]));
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',a[3]));
        if(a.length>5)
            meta.setLore(deserializeLore(a[5]));
        MaterialData data = i.getData();
        data.setData((byte) Integer.parseInt(a[4]));
        i.setData(data);
        if(a.length > 6) {
            for (ItemFlag flag : deserializeItemFlags(a[6]))
                meta.addItemFlags(flag);
            if (a.length > 7) {
                if (meta instanceof LeatherArmorMeta) {
                    ((LeatherArmorMeta) meta).setColor(getColor(a[7]));
                }
            }
            if (a.length > 8) {
                String[] parts = a[8].split(",");
                for (String s : parts) {
                    String label = s.split(":")[0];
                    String amplifier = s.split(":")[1];
                    Enchantment type = Enchantment.getByName(label);
                    if (type == null) {
                        continue;
                    }
                    int f;
                    try {
                        f = Integer.parseInt(amplifier);
                    } catch (Exception ex) {
                        continue;
                    }
                    meta.addEnchant(type, f, true);
                }
            }
        }

        i.setItemMeta(meta);
        return i;
    }

    private static String splitArmorStand = "/A/";

    public static String serializeArmorStand(ArmorStand stand) {
        String name = stand.getCustomName();
        Location loc = stand.getLocation();
        String visible = Boolean.toString(stand.isVisible());
        String nameVisible = Boolean.toString(stand.isCustomNameVisible());
        String gravity = Boolean.toString(stand.hasGravity());
        String pickup = Boolean.toString(stand.getCanPickupItems());
        String remove = Boolean.toString(stand.getRemoveWhenFarAway());
        return name+splitArmorStand+serializeLocation(loc)+splitArmorStand+visible
                +splitArmorStand+nameVisible+splitArmorStand+gravity+splitArmorStand+pickup+splitArmorStand+remove;
    }

    public static ArmorStand deserializeArmorStand(String s) {
        String[] split = s.split(splitArmorStand);
        String name = split[0];
        Location loc = deserializeLocation(split[1]);
        Boolean visible = Boolean.valueOf(split[2]);
        Boolean nameVisible = Boolean.valueOf(split[3]);
        Boolean gravity = Boolean.valueOf(split[4]);
        Boolean pickup = Boolean.valueOf(split[5]);
        Boolean remove = Boolean.valueOf(split[6]);
        ArmorStand stand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        stand.setCustomName(name);
        stand.setVisible(visible);
        stand.setCustomNameVisible(nameVisible);
        stand.setGravity(gravity);
        stand.setCanPickupItems(pickup);
        stand.setRemoveWhenFarAway(remove);
        return stand;
    }


    private static String splitInventory = "/I/";
    private static String splitInventorySlot = "/S/";
    private static String splitItems = "/IT/";

    public static String serializeInventory(Inventory inv) {
        String name = inv.getName();
        int size = inv.getSize();
        String items = "";
        for(int i = 0 ; i < size ; i++) {
            ItemStack item = inv.getItem(i);
            if(item!=null&&item.getType()!=Material.AIR)
                items+=i+splitInventorySlot+serializeItemStack(item)+(i+1==size?"":splitItems);
        }
        return name+splitInventory+size+splitInventory+items;
    }

    public static Inventory deserializeInventory(String s) {
        String[] split = s.split(splitInventory);
        Inventory inv = Bukkit.createInventory(null, Integer.parseInt(split[1]),split[0]);
        if(split.length>2) {
            String[] items = split[2].split(splitItems);
            for (int i = 0; i < items.length; i++) {
                String[] itemSplit = items[i].split(splitInventorySlot);
                int slot = Integer.parseInt(itemSplit[0]);
                ItemStack item = deserializeItemStack(itemSplit[1]);
                inv.setItem(slot, item);
            }
        }
        return inv;
    }


    private static String splitLocation = "/L/";

    public static String serializeLocation(Location loc) {
        return loc.getX() + splitLocation + loc.getY() + splitLocation + loc.getZ() + splitLocation +loc.getWorld().getName();
    }

    public static Location deserializeLocation(String s) {
        double x,y,z;
        World world;
        String[] sep = s.split(splitLocation);
        x=Double.parseDouble(sep[0]);
        y=Double.parseDouble(sep[1]);
        z=Double.parseDouble(sep[2]);
        world= Bukkit.getWorld(sep[3]);
        return new Location(world,x,y,z);
    }

    public static String serializeItemFlags(Set<ItemFlag> itemFlags) {
        String seperator = "/IFLAG/";
        String s = "";
        for(ItemFlag flag : itemFlags)
            s+=flag.toString() + seperator;
        return s;
    }

    public static Set<ItemFlag> deserializeItemFlags(String s) {
        String seperator = "/IFLAG/";
        Set<ItemFlag> itemFlags = new HashSet<>();
        String[] split = s.split(seperator);
        for(int i = 0 ; i < split.length-1 ; i++)
            itemFlags.add(ItemFlag.valueOf(split[i]));
        return itemFlags;
    }

    public static Color getColor(String color) {
        if (color.equalsIgnoreCase("black")) {
            return Color.BLACK;
        } else if (color.equalsIgnoreCase("blue")) {
            return Color.BLUE;
        } else if (color.equalsIgnoreCase("maroon")) {
            return Color.MAROON;
        } else if (color.equalsIgnoreCase("yellow")) {
            return Color.YELLOW;
        } else if (color.equalsIgnoreCase("green")) {
            return Color.GREEN;
        } else if (color.equalsIgnoreCase("gray")) {
            return Color.GRAY;
        } else if (color.equalsIgnoreCase("aqua")) {
            return Color.AQUA;
        } else if (color.equalsIgnoreCase("orange")) {
            return Color.ORANGE;
        } else if (color.equalsIgnoreCase("red")) {
            return Color.RED;
        } else if (color.equalsIgnoreCase("purple")) {
            return Color.PURPLE;
        } else if (color.equalsIgnoreCase("white")) {
            return Color.WHITE;
        } else if (color.equalsIgnoreCase("lime")) {
            return Color.LIME;
        } else {
            return null;
        }
    }

    private static String getEnchants(ItemStack i) {
        List<String> e = new ArrayList<String>();
        Map<Enchantment, Integer> en = i.getEnchantments();
        for (Enchantment t : en.keySet()) {
            e.add(t.getName() + ":" + en.get(t));
        }
        return StringUtils.join(e, ",");
    }

    public static String serializeLore(ItemStack i) {
        if(!(i.hasItemMeta()&&i.getItemMeta().hasLore()))
            return "";
        String str = "";
        List<String> lore = i.getItemMeta().getLore();
        for(int x = 0 ; x < lore.size() ; x++)
            str+=lore.get(x).replaceAll("\\xa7","&").replaceAll("§", "&")+(x+1==lore.size()?"":"/L/");
        return str;
    }

    public static List<String> deserializeLore(String str) {
        List<String> lore = new ArrayList<String>();
        for(String s : str.split("/L/"))
            lore.add(ChatColor.translateAlternateColorCodes('&',s.replaceAll("\\xa7","&").replaceAll("§", "&")));
        return lore;
    }

}
