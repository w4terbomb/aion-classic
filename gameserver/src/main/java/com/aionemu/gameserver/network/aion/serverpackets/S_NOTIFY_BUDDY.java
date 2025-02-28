/*
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
package com.aionemu.gameserver.network.aion.serverpackets;


import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * Notifies players when their friends log in, out, or delete them
 * 
 * @author Ben
 */
public class S_NOTIFY_BUDDY extends AionServerPacket {

	/**
	 * Buddy has logged in (Or become visible)
	 */
	public static final int LOGIN = 0;
	/**
	 * Buddy has logged out (Or become invisible)
	 */
	public static final int LOGOUT = 1;
	/**
	 * Buddy has deleted you
	 */
	public static final int DELETED = 2;

	private final int code;
	private final String name;

	/**
	 * Constructs a new notify packet
	 * 
	 * @param code
	 *          Message code
	 * @param name
	 *          Name of friend
	 */
	public S_NOTIFY_BUDDY(int code, String name) {
		this.code = code;
		this.name = name;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeS(name);
		writeC(code);
	}
}
