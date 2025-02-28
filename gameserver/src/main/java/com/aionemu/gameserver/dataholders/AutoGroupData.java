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

package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.autogroup.AutoGroup;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "autoGroup"
})
@XmlRootElement(name = "auto_groups")
public class AutoGroupData {

    @XmlElement(name = "auto_group")
    protected List<AutoGroup> autoGroup;
    @XmlTransient
    private TIntObjectHashMap<AutoGroup> autoGroupByInstanceId = new TIntObjectHashMap<AutoGroup>();
    @XmlTransient
    private TIntObjectHashMap<AutoGroup> autoGroupByNpcId = new TIntObjectHashMap<AutoGroup>();
    
    void afterUnmarshal(Unmarshaller unmarshaller, Object parent){
    	for (AutoGroup ag : autoGroup){
    		autoGroupByInstanceId.put(ag.getId(), ag);
    		
    		if (!ag.getNpcIds().isEmpty()){
    			for (int npcId : ag.getNpcIds()){
    				autoGroupByNpcId.put(npcId, ag);
    			}
    		}
    	}
    	autoGroup.clear();
    	autoGroup = null;
    }

    public AutoGroup getTemplateByInstaceMaskId(int maskId) {
		return autoGroupByInstanceId.get(maskId);
	}

    public int size(){
    	return autoGroupByInstanceId.size();
    }
}
