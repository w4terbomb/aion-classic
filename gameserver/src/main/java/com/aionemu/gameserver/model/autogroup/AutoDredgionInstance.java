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
package com.aionemu.gameserver.model.autogroup;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.instancereward.DredgionReward;
import com.aionemu.gameserver.model.team2.TeamType;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.services.instance.DredgionService;
import com.aionemu.gameserver.services.teleport.TeleportService2;

import java.util.List;
import java.util.stream.Collectors;

public class AutoDredgionInstance extends AutoInstance
{
	@Override
	public AGQuestion addPlayer(Player player, SearchInstance searchInstance) {
		super.writeLock();
		try {
			if (!satisfyTime(searchInstance) || (players.size() >= agt.getPlayerSize())) {
				return AGQuestion.FAILED;
			}
			EntryRequestType ert = searchInstance.getEntryRequestType();
			List<AGPlayer> playersByRace = getAGPlayersByRace(player.getRace());
			if (ert.isGroupEntry()) {
				if (searchInstance.getMembers().size() + playersByRace.size() > 6) {
					return AGQuestion.FAILED;
				} for (Player member : player.getPlayerGroup2().getOnlineMembers()) {
					if (searchInstance.getMembers().contains(member.getObjectId())) {
						players.put(member.getObjectId(), new AGPlayer(player));
					}
				}
			} else {
				if (playersByRace.size() >= 6) {
					return AGQuestion.FAILED;
				}
				players.put(player.getObjectId(), new AGPlayer(player));
			}
			return instance != null ? AGQuestion.ADDED : (players.size() == agt.getPlayerSize() ? AGQuestion.READY : AGQuestion.ADDED);
		} finally {
			super.writeUnlock();
		}
	}
	
	@Override
	public void onEnterInstance(Player player) {
		super.onEnterInstance(player);
		List<Player> playersByRace = getPlayersByRace(player.getRace());
		playersByRace.remove(player);
		if (playersByRace.size() == 1 && !playersByRace.get(0).isInGroup2()) {
			PlayerGroup newGroup = PlayerGroupService.createGroup(playersByRace.get(0), player, TeamType.GROUP);
			int groupId = newGroup.getObjectId();
			if (!instance.isRegistered(groupId)) {
				instance.register(groupId);
			}
		} else if (!playersByRace.isEmpty() && playersByRace.get(0).isInGroup2()) {
			PlayerGroupService.addPlayer(playersByRace.get(0).getPlayerGroup2(), player);
		}
		Integer object = player.getObjectId();
		if (!instance.isRegistered(object)) {
			instance.register(object);
		}
	}
	
	@Override
	public void onPressEnter(Player player) {
		super.onPressEnter(player);
		DredgionService.getInstance().addCoolDown(player);
		((DredgionReward) instance.getInstanceHandler().getInstanceReward()).portToPosition(player);
	}
	
	@Override
	public void onLeaveInstance(Player player) {
		super.unregister(player);
		PlayerGroupService.removePlayer(player);
		TeleportService2.moveToBindLocation(player, true);
	}

	private List<AGPlayer> getAGPlayersByRace(Race race) {
		return players.values().stream().filter(pl -> pl.getRace() == race).collect(Collectors.toList());
	}

	private List<Player> getPlayersByRace(Race race) {
		return instance.getPlayersInside().stream().filter(pl -> pl.getRace() == race).collect(Collectors.toList());
	}
}