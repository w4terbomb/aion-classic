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
package com.aionemu.gameserver.model.templates.panels;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="SkillPanel")
public class SkillPanel
{
    @XmlAttribute(name="panel_id")
    protected byte id;
	
    @XmlAttribute(name="panel_skills")
    protected List<Integer> skills;
	
    public int getPanelId() {
        return id;
    }
	
    public List<Integer> getSkills() {
        return null;
    }
	
    public boolean canUseSkill(int skillId, int level) {
        for (Integer skill: skills) {
            if (skill >> 8 == skillId && (skill & 0xFF) == level) {
                return true;
            }
        }
        return false;
    }
}