/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.team2.group.events;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.common.events.PlayerStopMentoringEvent;
import com.aionemu.gameserver.model.team2.common.legacy.GroupEvent;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.network.aion.serverpackets.S_PARTY_MEMBER_INFO;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public class PlayerGroupStopMentoringEvent extends PlayerStopMentoringEvent<PlayerGroup> {

    /**
     * @param group
     * @param player
     */
    public PlayerGroupStopMentoringEvent(PlayerGroup group, Player player) {
        super(group, player);
    }

    @Override
    protected void sendGroupPacketOnMentorEnd(Player member) {
        PacketSendUtility.sendPacket(member, new S_PARTY_MEMBER_INFO(team, player, GroupEvent.MOVEMENT));
    }
}
