/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.loginserver.network.factories;

import java.nio.ByteBuffer;

import com.aionemu.loginserver.network.aion.clientpackets.*;
import com.aionemu.loginserver.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.loginserver.network.aion.AionClientPacket;
import com.aionemu.loginserver.network.aion.LoginConnection;
import com.aionemu.loginserver.network.aion.LoginConnection.State;

/**
 * @author -Nemesiss-
 */
public class AionPacketHandlerFactory {

	/** 
	 * logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(AionPacketHandlerFactory.class);

	/**
	 * Reads one packet from given ByteBuffer
	 * 
	 * @param data
	 * @param client
	 * @return AionClientPacket object from binary data
	 */
	public static AionClientPacket handle(ByteBuffer data, LoginConnection client) {
		AionClientPacket msg = null;
		State state = client.getState();
		int id = data.get() & 0xff;

		switch (state) {
			case CONNECTED: {
				switch (id) {
					case 0x07:
						msg = new CM_AUTH_GG(data, client);
						break;
					case 0x08:
						msg = new CM_UPDATE_SESSION(data, client);
						break;
					default:
						unknownPacket(state, id);
				}
				break;
			}
			case AUTHED_GG: {
				switch (id) {
					case 0xB:
						msg = new CM_LOGIN(data, client);
						break;
					case 0x12:
						msg = new CM_NP_LOGIN(data, client);
						break;
					default:
						unknownPacket(state, id, data);
				}
				break;
			}
			case AUTHED_LOGIN: {
				switch (id) {
					case 0x05:
						msg = new CM_SERVER_LIST(data, client);
						break;
					case 0x02:
						msg = new CM_PLAY(data, client);
						break;
					default:
						unknownPacket(state, id);
				}
				break;
			}
		}
		
		return msg;
	}

	/**
	 * Logs unknown packet.
	 * 
	 * @param state
	 * @param id
	 */
	private static void unknownPacket(State state, int id) {
		log.warn(String.format("Unknown packet recived from Aion client: 0x%02X state=%s", id, state.toString()));
	}

	private static void unknownPacket(State state, int id, ByteBuffer data) {
		log.warn(String.format("Unknown packet recived from Aion client: 0x%04X, state=%s %n%s", id, state.toString(), Util.toHex(data)));
	}

}
