/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services.siegeservice;

import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.dao.SiegeDAO;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.network.aion.serverpackets.S_ABYSS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.S_MESSAGE_CODE;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.legion.LegionService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiegeAutoRace
{
	private static final Logger log = LoggerFactory.getLogger("SIEGE_LOG");
	private static String[] siegeIds = SiegeConfig.SIEGE_AUTO_LOCID.split(";");

	public static void AutoSiegeRace(final int locid) {
		final SiegeLocation loc = SiegeService.getInstance().getSiegeLocation(locid);
		if (!loc.getRace().equals(SiegeRace.ASMODIANS) || !loc.getRace().equals(SiegeRace.ELYOS)) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				public void run() {
					SiegeService.getInstance().startSiege(locid);
				}
			}, 300000);
			SiegeService.getInstance().deSpawnNpcs(locid);
			final int oldOwnerRaceId = loc.getRace().getRaceId();
			final int legionId = loc.getLegionId();
			final String legionName = legionId != 0 ? LegionService.getInstance().getLegion(legionId).getLegionName() : "";
			final DescriptionId nameId = new DescriptionId(loc.getTemplate().getNameId());
			if (ElyosAutoSiege(locid)) {
				loc.setRace(SiegeRace.ELYOS);
			} if (AsmoAutoSiege(locid)) {
				loc.setRace(SiegeRace.ASMODIANS);
			}
			loc.setLegionId(0);
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				public void visit(Player player) {
					if (legionId != 0 && player.getRace().getRaceId() == oldOwnerRaceId) {
						//%0 has conquered %1.
						PacketSendUtility.sendPacket(player, new S_MESSAGE_CODE(1301038, legionName, nameId));
					}
					PacketSendUtility.sendPacket(player, new S_ABYSS_INFO(loc));
				}
			});
			if (ElyosAutoSiege(locid)) {
				SiegeService.getInstance().spawnNpcs(locid, SiegeRace.ELYOS, SiegeModType.PEACE);
			} else if (AsmoAutoSiege(locid)) {
				SiegeService.getInstance().spawnNpcs(locid, SiegeRace.ASMODIANS, SiegeModType.PEACE);
			}
			SiegeDAO.updateSiegeLocation(loc);
		}
		SiegeService.getInstance().broadcastUpdate(loc);
	}

	public static boolean isAutoSiege(int locId) {
		return ElyosAutoSiege(locId) || AsmoAutoSiege(locId);
	}

	public static boolean ElyosAutoSiege(int locId) {
		for (String id: siegeIds[0].split(",")) {
			if (locId == Integer.parseInt(id)) {
				return true;
			}
		}
		return false;
	}

	public static boolean AsmoAutoSiege(int locId) {
		for (String id: siegeIds[1].split(",")) {
			if (locId == Integer.parseInt(id)) {
				return true;
			}
		}
		return false;
	}
}
