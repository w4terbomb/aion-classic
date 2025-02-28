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

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AISummon;
import com.aionemu.gameserver.model.*;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.summons.SummonMode;
import com.aionemu.gameserver.controllers.SiegeWeaponController;
import com.aionemu.gameserver.services.summons.SummonsService;

/****/
/** Author Rinzler (Encom)
/****/

@AIName("siege_weapon")
public class Siege_WeaponAI2 extends AISummon
{
	@Override
	protected void handleSpawned() {
		this.setStateIfNot(AIState.IDLE);
		SummonsService.doMode(SummonMode.GUARD, getOwner());
	}
	
	@Override
	protected void handleFollowMe(Creature creature) {
		this.setStateIfNot(AIState.FOLLOWING);
	}
	
	@Override
	protected void handleCreatureMoved(Creature creature) {
	}
	
	@Override
	protected void handleStopFollowMe(Creature creature) {
		this.setStateIfNot(AIState.IDLE);
		this.getOwner().getMoveController().abortMove();
	}
	
	@Override
	protected void handleTargetTooFar() {
		getOwner().getMoveController().moveToDestination();
	}
	
	@Override
	protected void handleMoveArrived() {
		this.getOwner().getController().onMove();
		this.getOwner().getMoveController().abortMove();
	}
	
	@Override
	protected void handleMoveValidate() {
		this.getOwner().getController().onMove();
		getMoveController().moveToTargetObject();
	}
	
	@Override
	protected SiegeWeaponController getController() {
		return (SiegeWeaponController) super.getController();
	}
	
	@Override
	protected void handleAttack(Creature creature) {
		if (creature == null) {
			return;
		}
		Race race = creature.getRace();
		Player master = getOwner().getMaster();
		if (master == null) {
			return;
		}
		Race masterRace = master.getRace();
		if (masterRace.equals(Race.ASMODIANS) && !race.equals(Race.PC_LIGHT_CASTLE_DOOR) && !race.equals(Race.DRAGON_CASTLE_DOOR)) {
			return;
		} else if (masterRace.equals(Race.ELYOS) && !race.equals(Race.PC_DARK_CASTLE_DOOR) && !race.equals(Race.DRAGON_CASTLE_DOOR)) {
			return;
		} if (!getOwner().getMode().equals(SummonMode.ATTACK)) {
			return;
		}
	}
}