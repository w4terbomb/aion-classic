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
package ai.worlds.gelkmaros;

import ai.ActionItemNpcAI2;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/** Author Rinzler (Encom)
/****/

@AIName("mastarius_aether_concentrator")
public class Mastarius_Aether_ConcentratorAI2 extends ActionItemNpcAI2
{
	@Override
    protected void handleSpawned() {
        super.handleSpawned();
		announceMastariusIII();
		SkillEngine.getInstance().getSkill(getOwner(), 20125, 60, getOwner()).useNoAnimationSkill(); //Aether Concentrator Standby.
    }
	
	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
		    //Mastarius's Aether Concentrator I
			case 296913:
				if (player.getLevel() >= 50) {
					announceMastariusI();
				    AI2Actions.targetCreature(Mastarius_Aether_ConcentratorAI2.this, getPosition().getWorldMapInstance().getNpc(258220)); //Enraged Mastarius.
				    AI2Actions.useSkill(Mastarius_Aether_ConcentratorAI2.this, 20107); //Defense Aether.
				} else {
					//You have failed to use the Empyrean Avatar. You will need to gather power and summon it again.
				    PacketSendUtility.playerSendPacketTime(player, S_MESSAGE_CODE.STR_MSG_GODELITE_DEATHBLOW_FAIL, 0);
				}
		    break;
			//Mastarius's Aether Concentrator II
			case 296914:
			    if (player.getLevel() >= 50) {
					announceMastariusII();
				    AI2Actions.targetCreature(Mastarius_Aether_ConcentratorAI2.this, getPosition().getWorldMapInstance().getNpc(258220)); //Enraged Mastarius.
				    AI2Actions.useSkill(Mastarius_Aether_ConcentratorAI2.this, 20108); //Elemental Resistance Aether.
				} else {
					//You have failed to use the Empyrean Avatar. You will need to gather power and summon it again.
				    PacketSendUtility.playerSendPacketTime(player, S_MESSAGE_CODE.STR_MSG_GODELITE_DEATHBLOW_FAIL, 0);
				}
			break;
			//Mastarius's Aether Concentrator III
			case 296915:
			    if (player.getLevel() >= 50) {
					announceMastariusII();
				    AI2Actions.targetCreature(Mastarius_Aether_ConcentratorAI2.this, getPosition().getWorldMapInstance().getNpc(258220)); //Enraged Mastarius.
				    AI2Actions.useSkill(Mastarius_Aether_ConcentratorAI2.this, 20109); //Power Aether.
					AI2Actions.useSkill(Mastarius_Aether_ConcentratorAI2.this, 20110); //Magical Aether.
				} else {
					//You have failed to use the Empyrean Avatar. You will need to gather power and summon it again.
				    PacketSendUtility.playerSendPacketTime(player, S_MESSAGE_CODE.STR_MSG_GODELITE_DEATHBLOW_FAIL, 0);
				}
			break;
		}
	}
	
	private void announceMastariusI() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The first Sphere of Destiny has been activated.
				PacketSendUtility.playerSendPacketTime(player, S_MESSAGE_CODE.STR_MSG_GODELITE_BUFF_FIRST_OBJECT_ON_DF, 0);
			}
		});
	}
	
	private void announceMastariusII() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The second Sphere of Destiny has been activated. Marchutan's Agent Mastarius prepares to cast the Empyrean Lord's blessing.
				PacketSendUtility.playerSendPacketTime(player, S_MESSAGE_CODE.STR_MSG_GODELITE_BUFF_SECOND_OBJECT_ON_DF, 0);
			}
		});
	}
	
	private void announceMastariusIII() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//You may use the Sphere of Destiny again.
				PacketSendUtility.playerSendPacketTime(player, S_MESSAGE_CODE.STR_MSG_GODELITE_BUFF_CAN_USE_OBJECT_DF, 120000);
			}
		});
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}