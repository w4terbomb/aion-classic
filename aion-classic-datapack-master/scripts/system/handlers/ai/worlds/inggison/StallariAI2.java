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
package ai.worlds.inggison;

import ai.AggressiveNpcAI2;

import com.aionemu.commons.network.util.ThreadPoolManager;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldMapInstance;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author Rinzler (Encom)
/****/

@AIName("stallari2")
public class StallariAI2 extends AggressiveNpcAI2
{
	private boolean canThink = true;
	private int curentPercent = 100;
	private List<Integer> percents = new ArrayList<Integer>();
	
	@Override
	public boolean canThink() {
		return canThink;
	}
	
    @Override
	protected void handleAttack(Creature creature) {
        super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
    }
	
	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[]{100, 60, 40, 20});
	}
	
	private synchronized void checkPercentage(int hpPercentage) {
		curentPercent = hpPercentage;
		for (Integer percent: percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 100:
					    guardianChiefTower();
					break;
					case 60:
					case 40:
					case 20:
					    illusionGate();
					break;
				}
				percents.remove(percent);
				break;
			}
		}
	}
	
	private void guardianChiefTower() {
		spawn(296516, getOwner().getX() + 10, getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading());
		spawn(296518, getOwner().getX() - 10, getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading());
	}
	
	private void illusionGate() {
		///Everyone gather around here! We'll protect you from the enemy swords!
		sendMsg(341525, getObjectId(), false, 0);
		SkillEngine.getInstance().getSkill(getOwner(), 18003, 60, getTarget()).useNoAnimationSkill(); //Summon Illusion Gate.
		for (Player player: getKnownList().getKnownPlayers().values()) {
			final Npc illusionGate1 = getPosition().getWorldMapInstance().getNpc(296533);
			if (illusionGate1 == null) {
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						///Enemy! Destroy them! I will give you strength! Go, and defeat the enemy!
						sendMsg(341526, getObjectId(), false, 0);
						spawn(296533, getOwner().getX() + 8, getOwner().getY(), getOwner().getZ(), (byte) getOwner().getHeading());
					}
				}, 3000);
			}
		}
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				WorldMapInstance instance = getPosition().getWorldMapInstance();
				deleteNpcs(instance.getNpcs(296533));
			}
		}, 600000);
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
		deleteNpcs(instance.getNpcs(296516));
		deleteNpcs(instance.getNpcs(296518));
		deleteNpcs(instance.getNpcs(296533));
	}
	
	@Override
	protected void handleDied() {
		super.handleDied();
		percents.clear();
		getOwner().getEffectController().removeAllEffects();
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		deleteNpcs(instance.getNpcs(296516));
		deleteNpcs(instance.getNpcs(296518));
		deleteNpcs(instance.getNpcs(296533));
	}
	
	private void sendMsg(int msg, int Obj, boolean isShout, int time) {
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
}