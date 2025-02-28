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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class S_USE_SKILL extends AionServerPacket
{
	private final int attackerObjectId;
	private final int spellId;
	private final int level;
	private final int targetType;
	private final int duration;
	private int targetObjectId;
	private float x;
	private float y;
	private float z;
	
	public S_USE_SKILL(int attackerObjectId, int spellId, int level, int targetType, int targetObjectId, int duration) {
		this.attackerObjectId = attackerObjectId;
		this.spellId = spellId;
		this.level = level;
		this.targetType = targetType;
		this.targetObjectId = targetObjectId;
		this.duration = duration;
	}
	
	public S_USE_SKILL(int attackerObjectId, int spellId, int level, int targetType, float x, float y, float z, int duration) {
		this(attackerObjectId, spellId, level, targetType, 0, duration);
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		final Player player = con.getActivePlayer();
		writeD(attackerObjectId);
		writeH(spellId);
		writeC(level);
		writeC(targetType);
		switch (targetType) {
			case 0:
			case 3:
			case 4:
				writeD(targetObjectId);
			break;
			case 1:
				writeF(x);
				writeF(y);
				writeF(z);
			break;
			case 2:
				writeF(x);
				writeF(y);
				writeF(z);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
			break;
		}
		writeH(duration);
		writeC(0x00);
		writeF((float) 0.8);
		writeC(0);
	}
}