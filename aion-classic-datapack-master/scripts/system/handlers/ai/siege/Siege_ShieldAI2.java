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
package ai.siege;

import com.aionemu.gameserver.ai2.*;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.siegespawns.SiegeSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/** Author Rinzler (Encom)
/****/

@AIName("siege_shield")
public class Siege_ShieldAI2 extends NpcAI2
{
	@Override
	protected void handleDespawned() {
		sendShieldPacket(false);
		super.handleDespawned();
	}
	
	@Override
	protected void handleDied() {
		sendShieldPacket(false);
		AI2Actions.deleteOwner(Siege_ShieldAI2.this);
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				AionObject winner = getAggroList().getMostDamage();
				if (winner instanceof Creature) {
					final Creature kill = (Creature) winner;
					//"Player Name" of the "Race" killed the Aetheric Field Generator.
					PacketSendUtility.sendPacket(player, new S_MESSAGE_CODE(1301048, kill.getRace().getRaceDescriptionId(), kill.getName()));
				}
			}
		});
		super.handleDied();
	}
	
	@Override
	protected void handleSpawned() {
		sendShieldPacket(true);
		super.handleSpawned();
	}
	
	private void sendShieldPacket(boolean shieldStatus) {
		int id = getSpawnTemplate().getSiegeId();
		SiegeService.getInstance().getFortress(id).setUnderShield(shieldStatus);
		final S_ABYSS_SHIELD_INFO packet = new S_ABYSS_SHIELD_INFO(id);
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, packet);
			}
		});
	}
	
	@Override
	protected SiegeSpawnTemplate getSpawnTemplate() {
		return (SiegeSpawnTemplate) super.getSpawnTemplate();
	}
}