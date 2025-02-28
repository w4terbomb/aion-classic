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
package com.aionemu.gameserver.model.templates.abyss_bonus;

import com.aionemu.gameserver.model.Race;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbyssServiceAttr", propOrder = {"bonusAttr"})
public class AbyssServiceAttr
{
	@XmlElement(name = "bonus_attr")
	protected List<AbyssPenaltyAttr> bonusAttr;
	
	@XmlAttribute(name = "buff_id", required = true)
	protected int buffId;
	
	@XmlAttribute(name = "name", required = true)
	private String name;
	
	@XmlAttribute(name = "race", required = true)
	private Race race;
	
	public List<AbyssPenaltyAttr> getPenaltyAttr() {
		if (bonusAttr == null) {
			bonusAttr = new ArrayList<AbyssPenaltyAttr>();
		}
		return bonusAttr;
	}
	
	public int getBuffId() {
		return buffId;
	}
	
	public void setBuffId(int value) {
		buffId = value;
	}
	
	public String getName() {
		return name;
	}
	
	public Race getRace() {
		return race;
	}
}