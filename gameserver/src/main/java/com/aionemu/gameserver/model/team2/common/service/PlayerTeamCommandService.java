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
package com.aionemu.gameserver.model.team2.common.service;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.TeamMember;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.alliance.events.AssignViceCaptainEvent.AssignType;
import com.aionemu.gameserver.model.team2.common.events.TeamCommand;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.model.team2.league.LeagueMember;
import com.aionemu.gameserver.model.team2.league.LeagueService;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.utils.PacketSendUtility;

import com.google.common.base.Preconditions;

public class PlayerTeamCommandService
{
    public static final void executeCommand(Player player, TeamCommand command, int playerObjId) {
        Player teamSubjective = getTeamSubjective(player, playerObjId);
        Preconditions.checkArgument(playerObjId == 0 || teamSubjective.getObjectId().equals(playerObjId) || command == TeamCommand.LEAGUE_EXPEL, "Wrong command detected " + command);
        execute(player, command, teamSubjective);
    }

    private static final void execute(Player player, TeamCommand eventCode, Player teamSubjective) {
        switch (eventCode) {
            case GROUP_BAN_MEMBER:
			    ///This code prevents some players kick other players in instance, for steal loot!!!
				if (teamSubjective.isInInstance()) {
					//You cannot use invite, leave or kick commands related to your group or alliance in this region.
					PacketSendUtility.sendPacket(player, S_MESSAGE_CODE.STR_MSG_INSTANCE_CANT_OPERATE_PARTY_COMMAND);
					return;
				} else {
				   PlayerGroupService.banPlayer(teamSubjective, player);
				}
            break;
            case GROUP_SET_LEADER:
                PlayerGroupService.changeLeader(teamSubjective);
            break;
            case GROUP_REMOVE_MEMBER:
				PlayerGroupService.removePlayer(teamSubjective);
            break;
            case GROUP_START_MENTORING:
                PlayerGroupService.startMentoring(player);
            break;
            case GROUP_END_MENTORING:
                PlayerGroupService.stopMentoring(player);
            break;
            case ALLIANCE_LEAVE:
				PlayerAllianceService.removePlayer(player);
            break;
            case ALLIANCE_BAN_MEMBER:
			    ///This code prevents some players kick other players in instance, for steal loot!!!
                if (teamSubjective.isInInstance()) {
					//You cannot use invite, leave or kick commands related to your group or alliance in this region.
					PacketSendUtility.sendPacket(player, S_MESSAGE_CODE.STR_MSG_INSTANCE_CANT_OPERATE_PARTY_COMMAND);
					return;
				} else {
					PlayerAllianceService.banPlayer(teamSubjective, player);
				}
            break;
            case ALLIANCE_SET_CAPTAIN:
                PlayerAllianceService.changeLeader(teamSubjective);
            break;
            case ALLIANCE_CHECKREADY_CANCEL:
            case ALLIANCE_CHECKREADY_START:
            case ALLIANCE_CHECKREADY_AUTOCANCEL:
            case ALLIANCE_CHECKREADY_NOTREADY:
            case ALLIANCE_CHECKREADY_READY:
                PlayerAllianceService.checkReady(player, eventCode);
            break;
            case ALLIANCE_SET_VICECAPTAIN:
                PlayerAllianceService.changeViceCaptain(teamSubjective, AssignType.PROMOTE);
            break;
            case ALLIANCE_UNSET_VICECAPTAIN:
                PlayerAllianceService.changeViceCaptain(teamSubjective, AssignType.DEMOTE);
            break;
            case LEAGUE_LEAVE:
                LeagueService.removeAlliance(player.getPlayerAlliance2());
            break;
            case LEAGUE_EXPEL:
			    ///This code prevents some players kick other players in instance, for steal loot!!!
                if (teamSubjective.isInInstance()) {
					//You cannot use invite, leave or kick commands related to your group or alliance in this region.
					PacketSendUtility.sendPacket(player, S_MESSAGE_CODE.STR_MSG_INSTANCE_CANT_OPERATE_PARTY_COMMAND);
					return;
				} else {
					LeagueService.expelAlliance(teamSubjective, player);
				}
            break;
        }
    }
	
    private static final Player getTeamSubjective(Player player, int playerObjId) {
        if (playerObjId == 0) {
            return player;
        } if (player.isInTeam()) {
            TeamMember<Player> member = player.getCurrentTeam().getMember(playerObjId);
            if (member != null) {
                return member.getObject();
            } if (player.isInLeague()) {
                LeagueMember subjective = player.getPlayerAlliance2().getLeague().getMember(playerObjId);
                if (subjective != null) {
                    return subjective.getObject().getLeaderObject();
                }
            }
        }
        return player;
    }
}