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
package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Ben
 */
public class ServerVariablesDAO
{
	private static final Logger log = LoggerFactory.getLogger(ServerVariablesDAO.class);

	/**
	 * Loads the server variables stored in the database
	 *
	 * @return variable stored in database
	 */
	public static int load(String var)
	{
		PreparedStatement ps = DB.prepareStatement("SELECT `value` FROM `server_variables` WHERE `key`=?");
		try {
			ps.setString(1, var);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return Integer.parseInt(rs.getString("value"));
		} catch (SQLException e) {
			log.error("Error loading last saved server time", e);
		} finally {
			DB.close(ps);
		}

		return 0;
	}

	/**
	 * Stores the server variables
	 */
	public static boolean store(String var, int time)
	{
		boolean success = false;
		PreparedStatement ps = DB.prepareStatement("REPLACE INTO `server_variables` (`key`,`value`) VALUES (?,?)");
		try {
			ps.setString(1, var);
			ps.setString(2, String.valueOf(time));
			success = ps.executeUpdate() > 0;
		} catch (SQLException e) {
			log.error("Error storing server time", e);
		} finally {
			DB.close(ps);
		}

		return success;
	}
}
