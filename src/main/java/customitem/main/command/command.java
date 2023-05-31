package customitem.main.command;

import customitem.main.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class command implements CommandExecutor, TabCompleter {
    String pr = "§f[§aCustomItem§f] ";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.isOp()) {
            Player player = (Player) sender;
            if (args.length == 0) {
                sender.sendMessage(pr + "/item list");
                sender.sendMessage(pr + "/item save (Name)");
                sender.sendMessage(pr + "/item give (Name)");
                player.sendMessage(pr + "/item check (Name)");
            }else if (args[0].equals("give")) {
                if (args.length > 1) {
                    ArrayList<Map<String, String>> customItems = Item.getCustomItems();
                    if (customItems != null) {
                        boolean tf = false;
                        for (Map<String, String> itemData : customItems) {
                            String itemName = itemData.get("itemName");
                            String fileName = itemData.get("fileName");
                            if (itemName.equalsIgnoreCase(args[1])) {
                                tf = true;
                                ItemStack item = Item.createCustomItem(fileName, itemName);
                                if (args.length > 2) {
                                    Player target = Bukkit.getPlayer(args[2]);
                                    if (target != null) {
                                        target.getInventory().addItem(item);
                                        player.sendMessage(pr + "§l" + itemName + "§8(" + fileName + ")§f 지급완료");
                                    } else {
                                        player.sendMessage(pr + "해당 플레이어는 존재하지 않습니다");
                                    }
                                }else{
                                    player.getInventory().addItem(item);
                                    player.sendMessage(pr + "§l" + itemName + "§8(" + fileName + ")§f 지급완료");
                                }

                            }
                        }
                        if (!tf) {
                            player.sendMessage(pr + args[1] + "은(는) 존재하지 않습니다");
                        }
                    }
                }else{
                    player.sendMessage(pr + "이름을 입력해주세요");
                }
            } else if (args[0].equals("list")) {
                ArrayList<Map<String, String>> customItems = Item.getCustomItems();
                if (customItems != null) {
                    for (Map<String, String> itemData : customItems) {
                        String itemName = itemData.get("itemName");
                        String fileName = itemData.get("fileName");
                        player.sendMessage(pr + "§l" + itemName + "§8(" + fileName + ")");
                    }
                }
            } else if (args[0].equals("check")) {
                ArrayList<Map<String, String>> customItems = Item.getCustomItems();
                if (customItems != null) {
                    boolean tf = false;
                    for (Map<String, String> itemData : customItems) {
                        String itemName = itemData.get("itemName");
                        if (itemName.equalsIgnoreCase(args[1])) {
                            tf = true;

                        }
                    }
                    if (!tf) {
                        player.sendMessage(pr + args[1] + "은(는) 존재하지 않습니다");
                    }
                }
            } else if (args[0].equals("save")) {
                //
            } else{
                player.sendMessage(pr + "/item list");
                player.sendMessage(pr + "/item save (Name)");
                player.sendMessage(pr + "/item give (Name)");
                player.sendMessage(pr + "/item check (Name)");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("list");
            completions.add("save");
            completions.add("give");
            completions.add("check");
        } else if ((args[0].equals("give") || args[0].equals("check")) && sender instanceof Player && args.length == 2) {
            ArrayList<Map<String, String>> customItems = Item.getCustomItems();
            for (Map<String, String> itemData : customItems) {
                String itemName = itemData.get("itemName");
                completions.add(itemName);
            }
        }else if (args[0].equals("give") && args.length == 3) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                completions.add(onlinePlayer.getName());
            }
        }

        return completions;
    }

}
