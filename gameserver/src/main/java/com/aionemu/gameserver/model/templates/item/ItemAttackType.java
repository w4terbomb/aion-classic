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
package com.aionemu.gameserver.model.templates.item;

import com.aionemu.gameserver.model.SkillElement;

import javax.xml.bind.annotation.XmlEnum;

/**
 * @author ATracer
 */
@XmlEnum
public enum ItemAttackType {
	PHYSICAL(false, SkillElement.NONE),
	MAGICAL_EARTH(true, SkillElement.EARTH),
	MAGICAL_WATER(true, SkillElement.WATER),
	MAGICAL_WIND(true, SkillElement.WIND),
	MAGICAL_FIRE(true, SkillElement.FIRE);

	private boolean magic;
	private SkillElement elem;

	private ItemAttackType(boolean magic, SkillElement elem) {
		this.magic = magic;
		this.elem = elem;
	}

	public boolean isMagical() {
		return magic;
	}
	
	public SkillElement getMagicalElement() {
		return elem;
	}
}
