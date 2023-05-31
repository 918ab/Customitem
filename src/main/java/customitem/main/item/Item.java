package customitem.main.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Item {

    public static ItemStack createCustomItem(String fileName, String itemName) {
        Yaml yaml = new Yaml();
        try {
            FileInputStream file = new FileInputStream("Plugins/Customitem/items/" + fileName);
            Map<String, Object> data = yaml.load(file);
            Map<String, Object> itemsMap = (Map<String, Object>) data.get("items");
            Map<String, Object> itemData = (Map<String, Object>) itemsMap.get(itemName);
            String itemname = (String) itemData.get("item");
            Material material = Material.valueOf(itemname.toUpperCase());
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            // 아이템 이름 설정
            String name = (String) itemData.get("name");
            if (name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            // 아이템 로어 설정
            List<String> lore = (List<String>) itemData.get("lore");
            if(lore != null) {
                List<String> translatedLore = new ArrayList<>();
                for (String line : lore) {
                    translatedLore.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                meta.setLore(translatedLore);
            }

            // 아이템 부셔지지않음 설정
            if (itemData.containsKey("Unbreakable")) {
                Boolean setUnbreakable = (Boolean) itemData.get("Unbreakable");
                if (setUnbreakable == true) {
                    meta.setUnbreakable(true);
                }
            }
            // 아이템 커스텀모델데이터 설정
            Integer customModelData = (Integer) itemData.get("CustomModelData");
            if (customModelData != null) {
                meta.setCustomModelData(customModelData);
            }

            //아이템 내구도데미지 설정
            Integer UnbreakingDamage = (Integer) itemData.get("UnbreakingDamage");
            if (UnbreakingDamage != null) {
                Damageable damageable = (Damageable) meta;
                damageable.setDamage(UnbreakingDamage);
            }

            //아이템 무기데미지 설정
            Integer Damage = (Integer) itemData.get("Damage");
            if (Damage != null) {
                AttributeModifier attribute = new AttributeModifier(UUID.randomUUID(), "generic.Damage", Damage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
                meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, attribute);
            }

            //아이템 공격속도 설정
            Double AttackSpeed = itemData.get("AttackSpeed") instanceof Number ? ((Number) itemData.get("AttackSpeed")).doubleValue() : 0.0;
            if (AttackSpeed != null) {
                AttributeModifier attackSpeedModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", AttackSpeed, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
                meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeedModifier);
            }

            item.setItemMeta(meta);
            return item;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static ArrayList<Map<String, String>> getCustomItems() {
        String folderPath = "Plugins/Customitem/items";
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                ArrayList<Map<String, String>> items = new ArrayList<>();
                for (File file : files) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        Map<String, Object> map = loadYaml(file);
                        Map<String, Object> itemsMap = (Map<String, Object>) map.get("items");
                        for (String key : itemsMap.keySet()) {
                            Map<String, String> itemData = new HashMap<>();
                            itemData.put("itemName", key);
                            itemData.put("fileName", fileName);
                            items.add(itemData);
                        }
                    }
                }
                return items;
            }
        }
        return null;
    }
    private static Map<String, Object> loadYaml(File file) {
        Yaml yaml = new Yaml();
        try {
            FileInputStream stream = new FileInputStream(file);
            return yaml.load(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
