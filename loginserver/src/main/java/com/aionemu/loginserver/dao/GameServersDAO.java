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
package com.aionemu.loginserver.dao;

import java.util.HashMap;
import java.util.Map;

import com.aionemu.commons.database.DB;
import com.aionemu.loginserver.GameServerInfo;

/**
 * DAO that manages GameServers
 *
 * @author -Nemesiss-
 * @author Skunk
 */
public class GameServersDAO
{
	/**
	 * Returns all gameservers from database.
	 *
	 * @return all gameservers from database.
	 */
	public static Map<Byte, GameServerInfo> getAllGameServers()
	{
		final Map<Byte, GameServerInfo> result = new HashMap<Byte, GameServerInfo>();
		DB.select("SELECT * FROM gameservers", resultSet -> {
			while (resultSet.next()) {
				byte id = resultSet.getByte("id");
				String ipMask = resultSet.getString("mask");
				String password = resultSet.getString("password");
				GameServerInfo gsi = new GameServerInfo(id, ipMask, password);
				result.put(id, gsi);
			}
		});

		return result;
	}
}
