/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.motion.Motion;
import com.aionemu.gameserver.model.gameobjects.player.motion.MotionList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * @author MrPoke
 */
public class MotionDAO
{
	private static final Logger log = LoggerFactory.getLogger(MotionDAO.class);

	public static final String INSERT_QUERY = "INSERT INTO `player_motions` (`player_id`, `motion_id`, `active`,  `time`) VALUES (?,?,?,?)";
	public static final String SELECT_QUERY = "SELECT `motion_id`, `active`, `time` FROM `player_motions` WHERE `player_id`=?";
	public static final String DELETE_QUERY = "DELETE FROM `player_motions` WHERE `player_id`=? AND `motion_id`=?";
	public static final String UPDATE_QUERY = "UPDATE `player_motions` SET `active`=? WHERE `player_id`=? AND `motion_id`=?";

	public static void loadMotionList(Player player)
	{
		Connection con = null;
		MotionList motions = new MotionList(player);
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				int motionId = rset.getInt("motion_id");
				int time = rset.getInt("time");
				boolean isActive = rset.getBoolean("active");
				motions.add(new Motion(motionId, time, isActive), false);
			}
			rset.close();
			stmt.close();
		} catch (Exception e) {
			log.error("Could not restore motions for playerObjId: " + player.getObjectId() + " from DB: " + e.getMessage(),
					e);
		} finally {
			DatabaseFactory.close(con);
		}

		player.setMotions(motions);
	}

	public static boolean storeMotion(int objectId, Motion motion)
	{
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, objectId);
			stmt.setInt(2, motion.getId());
			stmt.setBoolean(3, motion.isActive());
			stmt.setInt(4, motion.getExpireTime());
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Could not store motion for player " + objectId + " from DB: " + e.getMessage(), e);
			return false;
		} finally {
			DatabaseFactory.close(con);
		}

		return true;
	}

	public static boolean updateMotion(int objectId, Motion motion)
	{
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setBoolean(1, motion.isActive());
			stmt.setInt(2, objectId);
			stmt.setInt(3, motion.getId());
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Could not store motion for player " + objectId + " from DB: " + e.getMessage(), e);
			return false;
		} finally {
			DatabaseFactory.close(con);
		}

		return true;
	}

	public static boolean deleteMotion(int objectId, int motionId)
	{
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, objectId);
			stmt.setInt(2, motionId);
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Could not delete motion for player " + objectId + " from DB: " + e.getMessage(), e);
			return false;
		} finally {
			DatabaseFactory.close(con);
		}

		return true;
	}
}
