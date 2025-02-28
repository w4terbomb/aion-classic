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
package com.aionemu.gameserver.model.autogroup;

import com.aionemu.gameserver.model.gameobjects.player.Player;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SearchInstance
{
	private long registrationTime = System.currentTimeMillis();
	private int instanceMaskId;
	private EntryRequestType ert;
	private List<Integer> members;
	
	public SearchInstance(int instanceMaskId, EntryRequestType ert, Collection<Player> members) {
		this.instanceMaskId = instanceMaskId;
		this.ert = ert;
		if (members != null) {
			this.members = members.stream().map(Player::getObjectId).collect(Collectors.toList());
		}
	}
	
	public List<Integer> getMembers() {
		return members;
	}
	
	public int getInstanceMaskId() {
		return instanceMaskId;
	}
	
	public int getRemainingTime() {
		return (int) (System.currentTimeMillis() - registrationTime) / 1000 * 256;
	}
	
	public EntryRequestType getEntryRequestType() {
		return ert;
	}
	
	public boolean isDredgion() {
		return instanceMaskId == 1 || instanceMaskId == 2;
	}
	public boolean isTiakBase() {
		return instanceMaskId == 33;
	}
}