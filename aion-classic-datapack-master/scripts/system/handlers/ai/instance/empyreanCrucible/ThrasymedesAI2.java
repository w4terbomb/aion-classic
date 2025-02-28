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
package ai.instance.empyreanCrucible;

import ai.AggressiveNpcAI2;

import com.aionemu.commons.utils.Rnd;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.WorldMapInstance;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author Rinzler (Encom)
/****/

@AIName("Thrasymedes")
public class ThrasymedesAI2 extends AggressiveNpcAI2
{
	private boolean canThink = true;
	private int curentPercent = 100;
	private List<Integer> percents = new ArrayList<Integer>();
	
	@Override
	public boolean canThink() {
		return canThink;
	}
	
	@Override
	public void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[]{50});
	}
	
	private synchronized void checkPercentage(int hpPercentage) {
		curentPercent = hpPercentage;
		for (Integer percent: percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 50:
						///I never thought you'd make it as far as me.
						sendMsg(1500221, getObjectId(), false, 0);
						///You're every bit as good as they said.
						sendMsg(1500222, getObjectId(), false, 4000);
						///Once the enemy feels fear, they will start to make mistakes.
						sendMsg(1500211, getObjectId(), false, 8000);
						switch (Rnd.get(1, 3)) {
							case 1:
								applySoulSickness((Npc) spawn(282366, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading())); //Boreas.
							break;
							case 2:
								applySoulSickness((Npc) spawn(282367, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading())); //Jumentis.
							break;
							case 3:
								applySoulSickness((Npc) spawn(282368, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading())); //Charna.
							break;
						}
					break;
				}
				percents.remove(percent);
				break;
			}
		}
	}
	
	private void applySoulSickness(final Npc npc) {
		SkillEngine.getInstance().getSkill(npc, 19594, 60, npc).useNoAnimationSkill();
	}
	
	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc: npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
	
	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		percents.clear();
	}
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
	}
	
	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		addPercent();
		canThink = true;
		curentPercent = 100;
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		deleteNpcs(instance.getNpcs(282366));
		deleteNpcs(instance.getNpcs(282367));
		deleteNpcs(instance.getNpcs(282368));
	}
	
	@Override
	protected void handleDied() {
		super.handleDied();
		percents.clear();
		///You have nerves of steel.
		sendMsg(1500212, getObjectId(), false, 0);
		getOwner().getEffectController().removeAllEffects();
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		deleteNpcs(instance.getNpcs(282366));
		deleteNpcs(instance.getNpcs(282367));
		deleteNpcs(instance.getNpcs(282368));
	}
	
	private void sendMsg(int msg, int Obj, boolean isShout, int time) {
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
}