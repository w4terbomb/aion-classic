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
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.templates.abyss_bonus.AbyssServiceAttr;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;


/**
 * @Author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"abyssBonusattr"})
@XmlRootElement(name = "abyss_bonusattrs")
public class AbyssBuffData
{
	@XmlElement(name = "abyss_bonusattr")
	protected List<AbyssServiceAttr> abyssBonusattr;
	
	@XmlTransient
	private TIntObjectHashMap<AbyssServiceAttr> templates = new TIntObjectHashMap<AbyssServiceAttr>();
	
	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (AbyssServiceAttr template: abyssBonusattr) {
			templates.put(template.getBuffId(), template);
		}
		abyssBonusattr.clear();
		abyssBonusattr = null;
	}
	
	public int size() {
		return templates.size();
	}
	
	public AbyssServiceAttr getInstanceBonusattr(int buffId) {
		return templates.get(buffId);
	}
}