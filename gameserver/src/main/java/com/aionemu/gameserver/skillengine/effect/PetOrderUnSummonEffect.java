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
package com.aionemu.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.summons.UnsummonType;
import com.aionemu.gameserver.skillengine.model.Effect;

/****/
/** Author Rinzler (Encom)
/****/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PetOrderUnSummonEffect")
public class PetOrderUnSummonEffect extends EffectTemplate
{
	@XmlAttribute
	protected boolean release;
	
	@Override
	public void calculate(Effect effect) {
		super.calculate(effect, null, null);
	}
	
	@Override
	public void applyEffect(Effect effect) {
		Player effector = (Player) effect.getEffector();
		if (release && effector.getSummon() != null) {
			effector.getSummon().getController().release(UnsummonType.UNSPECIFIED);
		}
	}
}