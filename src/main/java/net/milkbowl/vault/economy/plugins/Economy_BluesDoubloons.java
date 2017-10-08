/*
 * This file is part of Vault.
 *
 * Copyright (C) 2017 Lukas Nehrke
 * Copyright (C) 2011 Morgan Humes <morgan@lanaddict.com>
 *
 * Vault is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vault is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Vault.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.milkbowl.vault.economy.plugins;

import com.gmail.bluestreehouse.bluesdoubloons.Api;
import com.gmail.bluestreehouse.bluesdoubloons.BluesDoubloons;
import com.gmail.bluestreehouse.bluesdoubloons.datatypes.BluesEconomyResponse;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.logging.Logger;

public class Economy_BluesDoubloons extends AbstractEconomy
{
    private static final Logger log = Logger.getLogger("Minecraft");

    private final String name = "BluesDoubloons";
    private Plugin plugin;
    private Api api;

    public Economy_BluesDoubloons(Plugin plugin)
    {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(new Economy_BluesDoubloons.EconomyServerListener(this), plugin);
        if (api == null)
        {
            Plugin bluesDoubloons = plugin.getServer().getPluginManager().getPlugin(name);
            if (bluesDoubloons != null && bluesDoubloons.isEnabled())
            {
                api = ((BluesDoubloons) bluesDoubloons).getApi();
                log.info(String.format("[%s][Economy] %s hooked.", plugin.getDescription().getName(), name));
            }
        }
    }

    @Override
    public boolean isEnabled()
    {
        Plugin bluesDoubloons = plugin.getServer().getPluginManager().getPlugin(name);
        return api != null && bluesDoubloons.isEnabled();
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean hasBankSupport()
    {
        return api.hasBankSupport();
    }

    @Override
    public int fractionalDigits()
    {
        return api.fractionalDigits();
    }

    @Override
    public String format(double v)
    {
        return api.format(v);
    }

    @Override
    public String currencyNamePlural()
    {
        return api.currencyNamePlural();
    }

    @Override
    public String currencyNameSingular()
    {
        return api.currencyNameSingular();
    }

    @Override
    public boolean hasAccount(String s)
    {
        return api.hasAccount(s);
    }

    @Override
    public boolean hasAccount(String s,
                              String s1)
    {
        return api.hasAccount(s, s1);
    }

    @Override
    public double getBalance(String s)
    {
        return api.getBalance(s);
    }

    @Override
    public double getBalance(String s,
                             String s1)
    {
        return api.getBalance(s, s1);
    }

    @Override
    public boolean has(String s,
                       double v)
    {
        return api.has(s, v);
    }

    @Override
    public boolean has(String s,
                       String s1,
                       double v)
    {
        return api.has(s, s1, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s,
                                          double v)
    {
        return convertBetweenResponses(api.withdrawPlayer(s, v));
    }

    @Override
    public EconomyResponse withdrawPlayer(String s,
                                          String s1,
                                          double v)
    {
        return convertBetweenResponses(api.withdrawPlayer(s, s1, v));
    }

    @Override
    public EconomyResponse depositPlayer(String s,
                                         double v)
    {
        return convertBetweenResponses(api.depositPlayer(s, v));
    }

    @Override
    public EconomyResponse depositPlayer(String s,
                                         String s1,
                                         double v)
    {
        return convertBetweenResponses(api.depositPlayer(s, s1, v));
    }

    @Override
    public EconomyResponse createBank(String s,
                                      String s1)
    {
        return convertBetweenResponses(api.createBank(s, s1));
    }

    @Override
    public EconomyResponse deleteBank(String s)
    {
        return convertBetweenResponses(api.deleteBank(s));
    }

    @Override
    public EconomyResponse bankBalance(String s)
    {
        return convertBetweenResponses(api.bankBalance(s));
    }

    @Override
    public EconomyResponse bankHas(String s,
                                   double v)
    {
        return convertBetweenResponses(api.bankHas(s, v));
    }

    @Override
    public EconomyResponse bankWithdraw(String s,
                                        double v)
    {
        return convertBetweenResponses(api.bankWithdraw(s, v));
    }

    @Override
    public EconomyResponse bankDeposit(String s,
                                       double v)
    {
        return convertBetweenResponses(api.bankDeposit(s, v));
    }

    @Override
    public EconomyResponse isBankOwner(String s,
                                       String s1)
    {
        return convertBetweenResponses(api.isBankOwner(s, s1));
    }

    @Override
    public EconomyResponse isBankMember(String s,
                                        String s1)
    {
        return convertBetweenResponses(api.isBankMember(s, s1));
    }

    @Override
    public List<String> getBanks()
    {
        return api.getBanks();
    }

    @Override
    public boolean createPlayerAccount(String s)
    {
        return api.createPlayerAccount(s);
    }

    @Override
    public boolean createPlayerAccount(String s,
                                       String s1)
    {
        return api.createPlayerAccount(s, s1);
    }

    /**
     * Converts between the economy response received by blue's api and the economy response used by vault
     * BluesEconomyResponse is meant to be nearly identical so this exists because we can't cast it directly
     *
     * @param response The instance of blue's economy response we want to change into a usable economy response
     * @return The economy response usable in vault
     */
    private EconomyResponse convertBetweenResponses(BluesEconomyResponse response)
    {
        return new EconomyResponse(response.amount, response.balance, EconomyResponse.ResponseType.values()[response.type.ordinal()], response.errorMessage);
    }

    /**
     * Handles some basic setup and teardown of this part of the economy api
     */
    public class EconomyServerListener implements Listener
    {
        Economy_BluesDoubloons economy = null;

        public EconomyServerListener(Economy_BluesDoubloons economy_BluesDoubloons)
        {
            this.economy = economy_BluesDoubloons;
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginEnable(PluginEnableEvent event)
        {
            if (economy.api == null)
            {
                Plugin bluesDoubloons = event.getPlugin();

                if (bluesDoubloons.getDescription().getName().equals(name))
                {
                    economy.api = ((BluesDoubloons) bluesDoubloons).getApi();
                    log.info(String.format("[%s][Economy] %s hooked.", plugin.getDescription().getName(), economy.name));
                }
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginDisable(PluginDisableEvent event)
        {
            if (economy.api != null)
            {
                if (event.getPlugin().getDescription().getName().equals(name))
                {
                    economy.api = null;
                    log.info(String.format("[%s][Economy] %s unhooked.", plugin.getDescription().getName(), economy.name));
                }
            }
        }
    }
}
