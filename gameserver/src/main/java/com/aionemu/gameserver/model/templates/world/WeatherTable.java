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
package com.aionemu.gameserver.model.templates.world;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WeatherTable", propOrder = {"zoneData"})
public class WeatherTable
{
	@XmlElement(name = "table", required = true)
	protected List<WeatherEntry> zoneData;
	
	@XmlAttribute(name = "weather_count", required = true)
	protected int weatherCount;
	
	@XmlAttribute(name = "zone_count", required = true)
	protected int zoneCount;
	
	@XmlAttribute(name = "id", required = true)
	protected int mapId;
	
	public List<WeatherEntry> getZoneData() {
		return zoneData;
	}
	
	public int getMapId() {
		return mapId;
	}
	
	public int getZoneCount() {
		return zoneCount;
	}
	
	public int getWeatherCount() {
		return weatherCount;
	}
	
	public WeatherEntry getWeatherAfter(WeatherEntry entry) {
		if (entry.getWeatherName() == null || entry.isAfter())
			return null;
		for (WeatherEntry we: getZoneData()) {
			if (we.getZoneId() != entry.getZoneId())
				continue;
			if (entry.getWeatherName().equals(we.getWeatherName())) {
				if (entry.isBefore() && !we.isBefore() && !we.isAfter())
					return we;
				else if (!entry.isBefore() && !entry.isAfter() && we.isAfter())
					return we;
			}
		}
		return null;
	}
	
	public List<WeatherEntry> getWeathersForZone(int zoneId) {
		List<WeatherEntry> result = new ArrayList<WeatherEntry>();
		for (WeatherEntry entry : getZoneData()) {
			if (entry.getZoneId() == zoneId)
				result.add(entry);
		}
		return result;
	}
}