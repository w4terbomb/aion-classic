/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.S_MESSAGE_CODE;

/**
 * Handler for "/loc" command
 * 
 * @author SoulKeeper
 * @author EvilSpirit
 */
public class CM_CLIENT_COMMAND_LOC extends AionClientPacket {

	/**
	 * Constructs new client packet instance.
	 * 
	 * @param opcode
	 */
	public CM_CLIENT_COMMAND_LOC(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);

	}

	/**
	 * Nothing to do
	 */
	@Override
	protected void readImpl() {
		// empty
	}

	/**
	 * Logging
	 */
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		sendPacket(S_MESSAGE_CODE.STR_CMD_LOCATION_DESC(player.getWorldId(), player.getX(), player.getY(), player.getZ()));
	}
}
