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
package com.aionemu.gameserver.spawnengine;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Rolandas
 */
public class InstanceWalkerFormations {

	private static final Logger log = LoggerFactory.getLogger(InstanceWalkerFormations.class);

	private final Map<String, List<ClusteredNpc>> groupedSpawnObjects;
	private final Map<String, WalkerGroup> walkFormations;

	public InstanceWalkerFormations() {
		groupedSpawnObjects = Maps.newHashMap();
		walkFormations = Maps.newHashMap();
	}

	public WalkerGroup getSpawnWalkerGroup(String walkerId) {
		return walkFormations.get(walkerId);
	}

	protected synchronized boolean cacheWalkerCandidate(ClusteredNpc npcWalker) {
		String walkerId = npcWalker.getWalkTemplate().getRouteId();
		List<ClusteredNpc> candidateList = groupedSpawnObjects.computeIfAbsent(walkerId, k -> Lists.newArrayList());
		return candidateList.add(npcWalker);
	}

	/**
	 * Organizes spawns in all processed walker groups. Must be called only when spawning all npcs for the instance of
	 * world.
	 */
	protected void organizeAndSpawn() {
		for (List<ClusteredNpc> candidates : groupedSpawnObjects.values()) {
			Map<Integer, List<ClusteredNpc>> bySize = candidates.stream().collect(Collectors.groupingBy(ClusteredNpc::getPositionHash));
			Set<Integer> keys = bySize.keySet();
			int maxSize = 0;
			List<ClusteredNpc> npcs = null;
			for (Integer key : keys) {
				if (bySize.get(key).size() > maxSize) {
					npcs = bySize.get(key);
				}
			}
			if (maxSize == 1) {
				for (ClusteredNpc snpc : candidates)
					snpc.spawn(snpc.getNpc().getSpawn().getZ());
			}
			else {
				WalkerGroup wg = new WalkerGroup(npcs);
				if (candidates.get(0).getWalkTemplate().getPool() != candidates.size())
					log.warn("Incorrect pool for route: " + candidates.get(0).getWalkTemplate().getRouteId());
				wg.form();
				wg.spawn();
				walkFormations.put(candidates.get(0).getWalkTemplate().getRouteId(), wg);
				// spawn the rest which didn't have the same coordinates
				for (ClusteredNpc snpc : candidates) {
					if (npcs.contains(snpc))
						continue;
					snpc.spawn(snpc.getNpc().getZ());
				}
			}
		}
	}

	protected synchronized void onInstanceDestroy() {
		groupedSpawnObjects.clear();
		walkFormations.clear();
	}

}
