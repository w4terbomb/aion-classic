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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.CubeExpandService;

public class CM_CUBE_EXPAND extends AionClientPacket
{
    int type;
	
    public CM_CUBE_EXPAND(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }
	
    @Override
    protected void readImpl() {
        type = readC();
    }
	
    @Override
    protected void runImpl() {
		final Player activePlayer = getConnection().getActivePlayer();
		if (activePlayer == null || !activePlayer.isSpawned()) {
            return;
        } if (activePlayer.isProtectionActive()) {
            activePlayer.getController().stopProtectionActiveTask();
        } if (activePlayer.isCasting()) {
            activePlayer.getController().cancelCurrentSkill();
        } switch (this.type) {
            case 0:
                CubeExpandService.expansionKinah(activePlayer);
            break;
        }
    }
}