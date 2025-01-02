package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Bank;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EconomyHandler;
import org.achymake.essentials.handlers.ScheduleHandler;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BankCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Bank getBank() {
        return getInstance().getBank();
    }
    private EconomyHandler getEconomy() {
        return getInstance().getEconomyHandler();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public BankCommand() {
        getInstance().getCommand("bank").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getUserdata().hasBank(player)) {
                    player.sendMessage(getMessage().get("commands.bank.self", getUserdata().getBank(player), getEconomy().currency() + getEconomy().format(getBank().get(getUserdata().getBank(player)))));
                } else player.sendMessage(getMessage().get("error.bank.empty"));
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("info")) {
                    if (player.hasPermission("essentials.command.bank.info")) {
                        if (getUserdata().hasBank(player)) {
                            var bank = getUserdata().getBank(player);
                            var owner = getBank().getOwner(bank);
                            var members = getBank().getMembers(bank);
                            player.sendMessage(getMessage().get("commands.bank.info.title"));
                            player.sendMessage(getMessage().get("commands.bank.info.name", bank));
                            player.sendMessage(getMessage().get("commands.bank.info.owner", owner.getName()));
                            player.sendMessage(getMessage().get("commands.bank.info.account", getEconomy().currency() + getEconomy().format(getBank().get(bank))));
                            if (!members.isEmpty()) {
                                player.sendMessage(getMessage().get("commands.bank.info.member.title"));
                                for (var member : members) {
                                    player.sendMessage(getMessage().get("commands.bank.info.member.listed", member.getName()));
                                }
                            }
                        } else player.sendMessage(getMessage().get("error.bank.empty"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("delete")) {
                    if (getUserdata().hasBank(player)) {
                        var bank = getUserdata().getBank(player);
                        var rank = getUserdata().getBankRank(player);
                        if (rank.equalsIgnoreCase("owner")) {
                            if (!getBank().has(bank, 0.1)) {
                                getBank().delete(bank);
                                player.sendMessage(getMessage().get("commands.bank.delete.success"));
                            } else player.sendMessage(getMessage().get("commands.bank.delete.sufficient-funds", getEconomy().currency() + getEconomy().format(getBank().get(bank))));
                            return true;
                        }
                    }
                } else if (args[0].equalsIgnoreCase("leave")) {
                    if (getUserdata().hasBank(player)) {
                        var bank = getUserdata().getBank(player);
                        if (!getBank().isOwner(bank, player)) {
                            if (getBank().isMember(bank, player)) {
                                getBank().removeMember(bank, player);
                            }
                            getUserdata().setString(player, "bank", "");
                            getUserdata().setString(player, "bank-rank", "default");
                            return true;
                        }
                    }
                } else if (args[0].equalsIgnoreCase("accept")) {
                    if (getUserdata().hasTaskID(player, "bank-invite")) {
                        if (getUserdata().getBankFrom(player) != null) {
                            var target = getUserdata().getBankFrom(player).getPlayer();
                            if (target != null) {
                                var bankTask = getUserdata().getTaskID(target, "bank-invite");
                                if (getScheduler().isQueued(bankTask)) {
                                    getUserdata().setString(target, "bank-invite.sent", null);
                                    getUserdata().setString(player, "bank-invite.from", null);
                                    getUserdata().removeTask(target, "bank-invite");
                                    getUserdata().removeTask(player, "bank-invite");
                                    getBank().addMember(getUserdata().getBank(target), player);
                                    target.sendMessage(getMessage().get("commands.bank.accept.target", player.getName()));
                                    player.sendMessage(getMessage().get("commands.bank.accept.sender", target.getName(), getUserdata().getBank(target)));
                                }
                            }
                        }
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("deny")) {
                    if (getUserdata().hasTaskID(player, "bank-invite")) {
                        if (getUserdata().getBankFrom(player) != null) {
                            var target = getUserdata().getBankFrom(player).getPlayer();
                            if (target != null) {
                                var bankTask = getUserdata().getTaskID(target, "bank-invite");
                                if (getScheduler().isQueued(bankTask)) {
                                    getUserdata().setString(target, "bank-invite.sent", null);
                                    getUserdata().setString(player, "bank-invite.from", null);
                                    getUserdata().removeTask(target, "bank-invite");
                                    getUserdata().removeTask(player, "bank-invite");
                                    target.sendMessage(getMessage().get("commands.bank.deny.target", player.getName()));
                                    player.sendMessage(getMessage().get("commands.bank.deny.sender", target.getName(), getUserdata().getBank(target)));
                                }
                            }
                        }
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("cancel")) {
                    if (getUserdata().hasTaskID(player, "bank-invite")) {
                        var target = getUserdata().getBankSent(player).getPlayer();
                        if (target != null) {
                            var bankTask = getUserdata().getTaskID(player, "bank-invite");
                            if (getScheduler().isQueued(bankTask)) {
                                getUserdata().setString(target, "bank-invite.from", null);
                                getUserdata().setString(player, "bank-invite.sent", null);
                                getUserdata().removeTask(target, "bank-invite");
                                getUserdata().removeTask(player, "bank-invite");
                                target.sendMessage(getMessage().get("commands.bank.cancel.target", player.getName()));
                                player.sendMessage(getMessage().get("commands.bank.cancel.sender", target.getName()));
                            }
                        }
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("top")) {
                    if (player.hasPermission("essentials.command.bank.top")) {
                        player.sendMessage(getMessage().get("commands.bank.top.title"));
                        var list = new ArrayList<>(getEconomy().getTopBanks());
                        for (int i = 0; i < list.size(); i++) {
                            player.sendMessage(getMessage().get("commands.bank.top.listed", String.valueOf(i + 1), list.get(i).getKey(), getEconomy().currency() + getEconomy().format(list.get(i).getValue())));
                        }
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("withdraw")) {
                    if (player.hasPermission("essentials.command.bank.withdraw")) {
                        if (getUserdata().hasBank(player)) {
                            var rank = getUserdata().getBankRank(player);
                            if (rank.equalsIgnoreCase("member") || rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                                getBank().openBankWithdraw(player);
                            }
                        } else player.sendMessage(getMessage().get("error.bank.empty"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("deposit")) {
                    if (player.hasPermission("essentials.command.bank.deposit")) {
                        if (getUserdata().hasBank(player)) {
                            getBank().openBankDeposit(player);
                        } else player.sendMessage(getMessage().get("error.bank.empty"));
                        return true;
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("info")) {
                    if (player.hasPermission("essentials.command.bank.info.other")) {
                        if (getBank().exists(args[1])) {
                            var owner = getBank().getOwner(args[1]);
                            var members = getBank().getMembers(args[1]);
                            player.sendMessage(getMessage().get("commands.bank.info.title"));
                            player.sendMessage(getMessage().get("commands.bank.info.name", args[1]));
                            player.sendMessage(getMessage().get("commands.bank.info.owner", owner.getName()));
                            player.sendMessage(getMessage().get("commands.bank.info.account", getEconomy().currency() + getEconomy().format(getBank().get(args[1]))));
                            if (!members.isEmpty()) {
                                player.sendMessage(getMessage().get("commands.bank.info.member.title"));
                                for (var member : members) {
                                    player.sendMessage(getMessage().get("commands.bank.info.member.listed", member.getName()));
                                }
                            }
                        } else player.sendMessage(getMessage().get("error.bank.invalid", args[1]));
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    if (getUserdata().hasBank(player)) {
                        var bank = getUserdata().getBank(player);
                        var rank = getUserdata().getBankRank(player);
                        if (rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                            var target = getInstance().getOfflinePlayer(args[1]);
                            if (getBank().isMember(bank, target)) {
                                getBank().removeMember(bank, target);
                                player.sendMessage(getMessage().get("commands.bank.remove", target.getName(), bank));
                                return true;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("invite")) {
                    if (getUserdata().hasBank(player)) {
                        var rank = getUserdata().getBankRank(player);
                        if (rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                            var target = getInstance().getPlayer(args[1]);
                            if (target != null) {
                                if (target != player) {
                                    if (!getUserdata().hasTaskID(player, "bank-invite")) {
                                        if (!getUserdata().hasBank(target)) {
                                            var taskID = getScheduler().runLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    getUserdata().setString(target, "bank-invite.from", null);
                                                    getUserdata().setString(player, "bank-invite.sent", null);
                                                    getUserdata().removeTask(target, "bank-invite");
                                                    getUserdata().removeTask(player, "bank-invite");
                                                    target.sendMessage(getMessage().get("commands.bank.invite.expired"));
                                                    player.sendMessage(getMessage().get("commands.bank.invite.expired"));
                                                }
                                            }, 300).getTaskId();
                                            getUserdata().setString(player, "bank-invite.sent", target.getUniqueId().toString());
                                            getUserdata().setString(target, "bank-invite.from", player.getUniqueId().toString());
                                            getUserdata().addTaskID(target, "bank-invite", taskID);
                                            getUserdata().addTaskID(player, "bank-invite", taskID);
                                            target.sendMessage(getMessage().get("commands.bank.invite.target.notify", player.getName()));
                                            target.sendMessage(getMessage().get("commands.bank.invite.target.decide"));
                                            player.sendMessage(getMessage().get("commands.bank.invite.sender.notify", target.getName()));
                                            player.sendMessage(getMessage().get("commands.bank.invite.sender.decide"));
                                        } else player.sendMessage(getMessage().get("commands.bank.invite.already-has", target.getName()));
                                    } else {
                                        player.sendMessage(getMessage().get("commands.bank.invite.occupied"));
                                        player.sendMessage(getMessage().get("commands.bank.invite.sender.decide"));
                                    }
                                }
                            }
                            return true;
                        }
                    }
                } else if (args[0].equalsIgnoreCase("rename")) {
                    if (player.hasPermission("essentials.command.bank.rename")) {
                        if (getUserdata().hasBank(player)) {
                            var bank = getUserdata().getBank(player);
                            if (getBank().isOwner(bank, player)) {
                                if (getBank().rename(bank, args[1])) {
                                    getUserdata().setString(getBank().getOwner(args[1]), "bank", args[1]);
                                    var members = getBank().getMembers(args[1]);
                                    if (!members.isEmpty()) {
                                        for (var member : members) {
                                            getUserdata().setString(member, "bank", args[1]);
                                        }
                                    }
                                    player.sendMessage(getMessage().get("commands.bank.rename", args[1]));
                                } else player.sendMessage(getMessage().get("error.bank.exists", args[1]));
                                return true;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("create")) {
                    if (!getUserdata().hasBank(player)) {
                        if (getBank().create(args[1], player)) {
                            player.sendMessage(getMessage().get("commands.bank.create", args[1]));
                        } else player.sendMessage(getMessage().get("error.bank.exists", args[1]));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("withdraw")) {
                    if (player.hasPermission("essentials.command.bank.withdraw")) {
                        if (getUserdata().hasBank(player)) {
                            var rank = getUserdata().getBankRank(player);
                            if (rank.equalsIgnoreCase("member") || rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                                var amount = Double.parseDouble(args[1]);
                                if (amount >= getEconomy().getMinimumBankWithdraw()) {
                                    if (getBank().has(getUserdata().getBank(player), amount)) {
                                        getBank().remove(getUserdata().getBank(player), amount);
                                        getEconomy().add(player, amount);
                                        player.sendMessage(getMessage().get("commands.bank.withdraw.success", getEconomy().currency() + getEconomy().format(amount)));
                                        player.sendMessage(getMessage().get("commands.bank.withdraw.left", getEconomy().currency() + getEconomy().format(getBank().get(getUserdata().getBank(player)))));
                                    } else player.sendMessage(getMessage().get("commands.bank.withdraw.insufficient-funds", getEconomy().currency() + getEconomy().format(amount)));
                                } else player.sendMessage(getMessage().get("commands.bank.withdraw.minimum", getEconomy().currency() + getEconomy().format(getEconomy().getMinimumBankWithdraw())));
                                return true;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("deposit")) {
                    if (player.hasPermission("essentials.command.bank.deposit")) {
                        if (getUserdata().hasBank(player)) {
                            var amount = Double.parseDouble(args[1]);
                            if (amount >= getEconomy().getMinimumBankDeposit()) {
                                if (getEconomy().has(player, amount)) {
                                    getEconomy().remove(player, amount);
                                    getBank().add(getUserdata().getBank(player), amount);
                                    player.sendMessage(getMessage().get("commands.bank.deposit.success", getEconomy().currency() + getEconomy().format(amount)));
                                    player.sendMessage(getMessage().get("commands.bank.deposit.left", getEconomy().currency() + getEconomy().format(getBank().get(getUserdata().getBank(player)))));
                                } else player.sendMessage(getMessage().get("commands.bank.deposit.insufficient-funds", getEconomy().currency() + getEconomy().format(amount)));
                            } else player.sendMessage(getMessage().get("commands.bank.deposit.minimum", getEconomy().currency() + getEconomy().format(getEconomy().getMinimumBankDeposit())));
                            return true;
                        }
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("rank")) {
                    if (getUserdata().hasBank(player)) {
                        var rank = getUserdata().getBankRank(player);
                        if (rank.equalsIgnoreCase("owner")) {
                            var target = getInstance().getOfflinePlayer(args[1]);
                            if (getBank().isMember(getUserdata().getBank(player), target)) {
                                if (args[2].equalsIgnoreCase("default")) {
                                    getUserdata().setString(target, "bank-rank", args[2]);
                                    player.sendMessage(getMessage().get("commands.bank.rank.set", target.getName(), args[2]));
                                    return true;
                                } else if (args[2].equalsIgnoreCase("member")) {
                                    getUserdata().setString(target, "bank-rank", args[2]);
                                    player.sendMessage(getMessage().get("commands.bank.rank.set", target.getName(), args[2]));
                                    return true;
                                } else if (args[2].equalsIgnoreCase("co-owner")) {
                                    getUserdata().setString(target, "bank-rank", args[2]);
                                    player.sendMessage(getMessage().get("commands.bank.rank.set", target.getName(), args[2]));
                                    return true;
                                }
                            }
                        } else if (rank.equalsIgnoreCase("co-owner")) {
                            var target = getInstance().getOfflinePlayer(args[1]);
                            if (getBank().isMember(getUserdata().getBank(player), target)) {
                                if (args[2].equalsIgnoreCase("default")) {
                                    getUserdata().setString(target, "bank-rank", args[2]);
                                    player.sendMessage(getMessage().get("commands.bank.rank.set", target.getName(), args[2]));
                                    return true;
                                } else if (args[2].equalsIgnoreCase("member")) {
                                    getUserdata().setString(target, "bank-rank", args[2]);
                                    player.sendMessage(getMessage().get("commands.bank.rank.set", target.getName(), args[2]));
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("top")) {
                    consoleCommandSender.sendMessage(getMessage().get("commands.bank.top.title"));
                    var list = new ArrayList<>(getEconomy().getTopBanks());
                    for (int i = 0; i < list.size(); i++) {
                        consoleCommandSender.sendMessage(getMessage().get("commands.bank.top.listed", String.valueOf(i + 1), list.get(i).getKey(), getEconomy().currency() + getEconomy().format(list.get(i).getValue())));
                    }
                    return true;
                }
            } else if (args.length == 2) {
                var target = getInstance().getPlayer(args[1]);
                if (target != null) {
                    if (args[0].equalsIgnoreCase("withdraw")) {
                        if (getUserdata().hasBank(target)) {
                            var rank = getUserdata().getBankRank(target);
                            if (rank.equalsIgnoreCase("member") || rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                                getBank().openBankWithdraw(target);
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("deposit")) {
                        if (getUserdata().hasBank(target)) {
                            getBank().openBankDeposit(target);
                        }
                    }
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[1]));
                return true;
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (getUserdata().hasTaskID(player, "bank-invite")) {
                    commands.add("accept");
                    commands.add("deny");
                }
                if (getUserdata().hasBank(player)) {
                    if (!getBank().isOwner(getUserdata().getBank(player), player)) {
                        commands.add("leave");
                    }
                }
                if (getUserdata().hasBank(player)) {
                    var rank = getUserdata().getBankRank(player);
                    if (rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                        commands.add("rank");
                    }
                }
                if (getUserdata().hasBank(player)) {
                    var rank = getUserdata().getBankRank(player);
                    if (rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                        commands.add("remove");
                    }
                }
                if (getUserdata().hasBank(player)) {
                    var rank = getUserdata().getBankRank(player);
                    if (rank.equalsIgnoreCase("owner")) {
                        commands.add("delete");
                    }
                }
                if (player.hasPermission("essentials.command.bank.info")) {
                    commands.add("info");
                }
                if (player.hasPermission("essentials.command.bank.create")) {
                    if (!getUserdata().hasBank(player)) {
                        commands.add("create");
                    } else {
                        var rank = getUserdata().getBankRank(player);
                        if (rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                            commands.add("invite");
                        }
                    }
                }
                if (player.hasPermission("essentials.command.bank.rename")) {
                    if (getUserdata().hasBank(player)) {
                        if (getBank().isOwner(getUserdata().getBank(player), player)) {
                            commands.add("rename");
                        }
                    }
                }
                if (player.hasPermission("essentials.command.bank.top")) {
                    commands.add("top");
                }
                if (player.hasPermission("essentials.command.bank.withdraw")) {
                    if (getUserdata().hasBank(player)) {
                        var rank = getUserdata().getBankRank(player);
                        if (rank.equalsIgnoreCase("member") || rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                            commands.add("withdraw");
                        }
                    }
                }
                if (player.hasPermission("essentials.command.bank.deposit")) {
                    if (getUserdata().hasBank(player)) {
                        commands.add("deposit");
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("info")) {
                    if (player.hasPermission("essentials.command.bank.info.other")) {
                        for (var bank : getBank().getListed()) {
                            if (bank.startsWith(args[1])) {
                                commands.add(bank);
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("rank")) {
                    if (getUserdata().hasBank(player)) {
                        var bank = getUserdata().getBank(player);
                        var rank = getUserdata().getBankRank(player);
                        if (rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                            if (!getBank().getMembers(bank).isEmpty()) {
                                getBank().getMembers(bank).forEach(target -> {
                                    if (!getUserdata().isVanished(target)) {
                                        if (target.getName().startsWith(args[1])) {
                                            commands.add(target.getName());
                                        }
                                    }
                                });
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("invite")) {
                    if (getUserdata().hasBank(player)) {
                        var rank = getUserdata().getBankRank(player);
                        if (rank.equalsIgnoreCase("owner") || rank.equalsIgnoreCase("co-owner")) {
                            getInstance().getOnlinePlayers().forEach(target -> {
                                if (!getUserdata().isVanished(target)) {
                                    if (target.getName().startsWith(args[1])) {
                                        commands.add(target.getName());
                                    }
                                }
                            });
                        }
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (getUserdata().hasBank(player)) {
                        var bank = getUserdata().getBank(player);
                        var rank = getUserdata().getBankRank(player);
                        if (rank.equalsIgnoreCase("owner") || rank.equalsIgnoreCase("co-owner")) {
                            if (!getBank().getMembers(bank).isEmpty()) {
                                getBank().getMembers(bank).forEach(target -> {
                                    if (!getUserdata().isVanished(target)) {
                                        if (target.getName().startsWith(args[1])) {
                                            commands.add(target.getName());
                                        }
                                    }
                                });
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("rename")) {
                    if (player.hasPermission("essentials.command.bank.rename")) {
                        if (getUserdata().hasBank(player)) {
                            if (getBank().isOwner(getUserdata().getBank(player), player)) {
                                commands.add(getUserdata().getBank(player));
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("create")) {
                    if (player.hasPermission("essentials.command.create")) {
                        if (!getUserdata().hasBank(player)) {
                            commands.add(player.getName());
                        }
                    }
                } else if (args[0].equalsIgnoreCase("deposit")) {
                    if (getUserdata().hasBank(player)) {
                        if (player.hasPermission("essentials.command.bank.deposit")) {
                            commands.add("8");
                            commands.add("16");
                            commands.add("32");
                            commands.add("64");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("withdraw")) {
                    if (getUserdata().hasBank(player)) {
                        if (player.hasPermission("essentials.command.bank.withdraw")) {
                            var rank = getUserdata().getBankRank(player);
                            if (rank.equalsIgnoreCase("member") || rank.equalsIgnoreCase("co-owner") || rank.equalsIgnoreCase("owner")) {
                                commands.add("8");
                                commands.add("16");
                                commands.add("32");
                                commands.add("64");
                            }
                        }
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("rank")) {
                    if (getUserdata().hasBank(player)) {
                        var rank = getUserdata().getBankRank(player);
                        if (rank.equalsIgnoreCase("co-owner")) {
                            commands.add("default");
                            commands.add("member");
                        }
                        if (rank.equalsIgnoreCase("owner")) {
                            commands.add("default");
                            commands.add("member");
                            commands.add("co-owner");
                        }
                    }
                }
            }
        }
        return commands;
    }
}