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
package ai.worlds.heiron.exedil;

import com.aionemu.commons.network.util.ThreadPoolManager;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.AI2Actions;

/****/
/** Author Rinzler (Encom)
/****/

@AIName("Power_Of_Exedil")
public class Power_Of_ExedilAI2 extends NpcAI2
{
	@Override
	public void think() {
	}
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		despawn();
		AI2Actions.targetCreature(Power_Of_ExedilAI2.this, getPosition().getWorldMapInstance().getNpc(212317)); //Exedil.
		AI2Actions.useSkill(Power_Of_ExedilAI2.this, 17125);
	}
	
    private void despawn() {
  	    ThreadPoolManager.getInstance().schedule(new Runnable() {
  		    @Override
  		    public void run() {
  			    getOwner().getController().onDelete();
  		    }
  	    }, 15000);
    }
	
    @Override
	public boolean isMoveSupported() {
		return false;
	}
}