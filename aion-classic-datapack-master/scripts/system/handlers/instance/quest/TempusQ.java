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
package instance.quest;

import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.*;
import com.aionemu.gameserver.model.gameobjects.*;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.TempusReward;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.*;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.*;

import java.util.*;
import java.util.concurrent.Future;

/****/
/** Author Rinzler (Encom)
/****/

@InstanceID(300510000)
public class TempusQ extends GeneralInstanceHandler
{
	private int rank;
	private long startTime;
	private Race skillRace;
	private Race spawnRace;
	private Future<?> timerPrepare;
	private Future<?> timerInstance;
	private TempusReward instanceReward;
	private boolean isInstanceDestroyed;
	private Map<Integer, StaticDoor> doors;
	//Preparation Time.
	private int prepareTimerSeconds = 180000; //...3Min
	//Duration Instance Time.
	private int instanceTimerSeconds = 3600000; //...1Hr
	private final FastList<Future<?>> tempusTask = FastList.newInstance();
	
	protected final int IDF4RE_SOLO_Q_POLYMORPH_1 = 20502;
	protected final int IDF4RE_SOLO_POLYMORPH_3 = 20503;
	
	@Override
	public void onDie(Npc npc) {
		int score = 0;
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 219039: //idf4re_solo_drakanfi_55_ae.
			    score += 1000;
				doors.get(500).setOpen(true);
			break;
			case 219040: //idf4re_solo_drakanwi_55_ae.
			    score += 1000;
				doors.get(197).setOpen(true);
			break;
			case 800525: //idf4re_solo_prisoner_m_02_l.
			case 800526: //idf4re_solo_prisoner_f_01_l.
			case 800527: //idf4re_solo_prisoner_f_02_l.
			case 800528: //idf4re_solo_prisoner_m_01_d.
			case 800529: //idf4re_solo_prisoner_m_02_d.
			case 800530: //idf4re_solo_prisoner_f_01_d.
			    score += 500;
				despawnNpc(npc);
			break;
			case 219067: //idf4re_solo_q_drakanprnmd_55_ae2.
			    score += 2000;
				spawn(800603, 193.80135f, 601.9940f, 136.3627f, (byte) 0); //idf4re_solo_q_exit_02.
				if (player.getRace() == Race.ELYOS) {
				    final QuestState qs10006 = player.getQuestStateList().getQuestState(10006);
				    if (qs10006 != null && qs10006.getStatus() == QuestStatus.START && qs10006.getQuestVarById(0) == 10) {
						ClassChangeService.onUpdateMission10006B(player);
					}
				} else if (player.getRace() == Race.ASMODIANS) {
					final QuestState qs20006 = player.getQuestStateList().getQuestState(20006);
					if (qs20006 != null && qs20006.getStatus() == QuestStatus.START && qs20006.getQuestVarById(0) == 10) {
						ClassChangeService.onUpdateMission20006B(player);
					}
				} if (checkRank(instanceReward.getPoints()) == 1) {
					spawn(800517, 199.33841f, 600.4536f, 136.3627f, (byte) 3); //idf4re_solo_rank_box_s.
					spawn(800522, 199.39780f, 603.3599f, 136.3627f, (byte) 117); //idf4re_solo_bonus_box_boss.
				} if (checkRank(instanceReward.getPoints()) == 2) {
					spawn(800518, 199.33841f, 600.4536f, 136.3627f, (byte) 3); //idf4re_solo_rank_box_a.
				} if (checkRank(instanceReward.getPoints()) == 3) {
					spawn(800519, 199.33841f, 600.4536f, 136.3627f, (byte) 3); //idf4re_solo_rank_box_b.
				} if (checkRank(instanceReward.getPoints()) == 4) {
				    spawn(800520, 199.33841f, 600.4536f, 136.3627f, (byte) 3); //idf4re_solo_rank_box_c.
				} if (checkRank(instanceReward.getPoints()) == 5) {
				    spawn(800521, 199.33841f, 600.4536f, 136.3627f, (byte) 3); //idf4re_solo_rank_box_d.
				}
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
					    instance.doOnAllPlayers(new Visitor<Player>() {
						    @Override
						    public void visit(Player player) {
							    stopInstance(player);
						    }
					    });
					}
				}, 5000);
			break;
		} if (instanceReward.getInstanceScoreType().isStartProgress()) {
			instanceReward.addNpcKill();
			instanceReward.addPoints(score);
			sendPacket(npc.getObjectTemplate().getNameId(), score);
		}
	}
	
	@Override
	public void onDialog(final Player player, final Npc npc, int dialogId) {
    	switch (npc.getNpcId()) {
			case 800577: //idf4re_solo_q_undercover_npc_l.
			case 800578: //idf4re_solo_q_undercover_npc_d.
				if (dialogId == 10000) {
					//to do...
				}
				PacketSendUtility.sendPacket(player, new S_NPC_HTML_MESSAGE(npc.getObjectId(), 0));
			break;
			case 800581: //idf4re_solo_evileye_switch_01.
				if (dialogId == 10000) {
					//to do...
				}
				PacketSendUtility.sendPacket(player, new S_NPC_HTML_MESSAGE(npc.getObjectId(), 0));
			break;
			case 800582: //idf4re_solo_evileye_switch_02.
				if (dialogId == 10001) {
					//to do...
				}
				PacketSendUtility.sendPacket(player, new S_NPC_HTML_MESSAGE(npc.getObjectId(), 0));
			break;
			case 800583: //idf4re_solo_evileye_switch_03.
				if (dialogId == 10002) {
					//to do...
				}
				PacketSendUtility.sendPacket(player, new S_NPC_HTML_MESSAGE(npc.getObjectId(), 0));
			break;
		}
	}
	
	@Override
	public void handleUseItemFinish(final Player player, final Npc npc) {
		int score = 0;
		switch (npc.getNpcId()) {
			case 800505: //idf4re_solo_1st_journaloflab.
			case 800506: //idf4re_solo_2nd_journaloflab.
			    score += 500;
				despawnNpc(npc);
			break;
			case 800507: //idf4re_solo_teleport_01.
			    tempus_stagestart(player, 455.0000f, 525.0000f, 135.0000f, (byte) 30);
			break;
			case 800524: //idf4re_solo_key_box.
			    score -= 500;
				despawnNpc(npc);
				ItemService.addItem(player, 185000123, 1);
			break;
			case 800525: //idf4re_solo_prisoner_m_02_l.
			case 800526: //idf4re_solo_prisoner_f_01_l.
			case 800527: //idf4re_solo_prisoner_f_02_l.
			case 800528: //idf4re_solo_prisoner_m_01_d.
			case 800529: //idf4re_solo_prisoner_m_02_d.
			case 800530: //idf4re_solo_prisoner_f_01_d.
			    score += 500;
				despawnNpc(npc);
			break;
			case 800580: //obj_idf4re_solo_q_2nd_journaloflab.
				switch (player.getRace()) {
				    case ELYOS:
						final QuestState qs10006 = player.getQuestStateList().getQuestState(10006);
						if (qs10006 != null && qs10006.getStatus() == QuestStatus.START && qs10006.getQuestVarById(0) == 8) {
							ClassChangeService.onUpdateMission10006A(player);
							instance.doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									if (player.isOnline()) {
										ItemService.addItem(player, 182215149, 1);
									}
								}
							});
						} else {
							///You have not acquired this quest.
							PacketSendUtility.sendPacket(player, new S_MESSAGE_CODE(1390254));
						}
					break;
					case ASMODIANS:
						final QuestState qs20006 = player.getQuestStateList().getQuestState(20006);
						if (qs20006 != null && qs20006.getStatus() == QuestStatus.START && qs20006.getQuestVarById(0) == 8) {
							ClassChangeService.onUpdateMission20006A(player);
							instance.doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									if (player.isOnline()) {
										ItemService.addItem(player, 182215151, 1);
									}
								}
							});
						} else {
							///You have not acquired this quest.
							PacketSendUtility.sendPacket(player, new S_MESSAGE_CODE(1390254));
						}
					break;
				}
            break;
			case 800584: //idf4re_solo_fobj_drakanbox.
				despawnNpc(npc);
				SkillEngine.getInstance().applyEffectDirectly(IDF4RE_SOLO_POLYMORPH_3, player, player, 7200000 * 1);
			break;
			case 800590: //idf4re_solo_fobj_siel_bomb.
				despawnNpc(npc);
				//ItemService.addItem(player, 185000123, 1);
			break;
			case 800603: //idf4re_solo_q_exit_02.
				if (player.getRace() == Race.ELYOS) {
					ClassChangeService.onUpdateMission10006C(player);
					tempus_q_exit_e(player, 2182.0000f, 1157.0000f, 282.0000f, (byte) 24);
				} else {
					ClassChangeService.onUpdateMission20006C(player);
					tempus_q_exit_a(player, 316.0000f, 1647.0000f, 358.0000f, (byte) 61);
				}
			break;
			case 800637: //idf4re_solo_teleport_08.
			    tempus_returnstart(player, 465.0000f, 441.0000f, 132.0000f, (byte) 0);
			break;
		} if (instanceReward.getInstanceScoreType().isStartProgress()) {
			instanceReward.addPoints(score);
			sendPacket(npc.getObjectTemplate().getNameId(), score);
		}
	}
	
	protected void tempus_stagestart(Player player, float x, float y, float z, byte h) {
		TeleportService2.teleportTo(player, mapId, instanceId, x, y, z, h);
	}
	protected void tempus_returnstart(Player player, float x, float y, float z, byte h) {
		TeleportService2.teleportTo(player, mapId, instanceId, x, y, z, h);
	}
	protected void tempus_q_exit_e(Player player, float x, float y, float z, byte h) {
		TeleportService2.teleportTo(player, 210050000, 1, x, y, z, h);
	}
	protected void tempus_q_exit_a(Player player, float x, float y, float z, byte h) {
		TeleportService2.teleportTo(player, 220070000, 1, x, y, z, h);
	}
	
	private int getTime() {
		long result = (int) (System.currentTimeMillis() - startTime);
		return instanceTimerSeconds - (int) result;
	}
	
	private void sendPacket(final int nameId, final int point) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (nameId != 0) {
					PacketSendUtility.sendPacket(player, new S_MESSAGE_CODE(1400237, new DescriptionId(nameId * 2 + 1), point));
				}
				PacketSendUtility.sendPacket(player, new S_INSTANT_DUNGEON_INFO(getTime(), instanceReward, null));
			}
		});
	}
	
	private int checkRank(int totalPoints) {
		if (totalPoints >= 12500) { //Rank S.
			rank = 1;
		} else if (totalPoints >= 11000) { //Rank A.
			rank = 2;
		} else if (totalPoints >= 9500) { //Rank B.
			rank = 3;
		} else if (totalPoints >= 8000) { //Rank C.
			rank = 4;
		} else if (totalPoints >= 2500) { //Rank D.
			rank = 5;
		} else if (totalPoints >= 0) { //Rank F.
			rank = 6;
		} else {
			rank = 8;
		}
		return rank;
	}
	
	protected void startInstanceTask() {
		tempusTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {
				    @Override
				    public void visit(Player player) {
					    stopInstance(player);
				    }
			    });
            }
        }, 3600000)); //...1Hr
    }
	
	@Override
	public void onOpenDoor(Player player, int doorId) {
		if (doorId == 499) {
			startInstanceTask();
			doors.get(499).setOpen(true);
			//The player has 3 min to prepare !!! [Timer Red]
			if ((timerPrepare != null) && (!timerPrepare.isDone() || !timerPrepare.isCancelled())) {
				//Start the instance time !!! [Timer White]
				startMainInstanceTimer();
			}
		}
	}
	
	@Override
	public void onEnterInstance(final Player player) {
		startPrepareTimer();
		instance.doOnAllPlayers(new Visitor<Player>() {
		    @Override
			public void visit(Player player) {
				final int idf4reSoloQPolymorph = skillRace == Race.ASMODIANS ? IDF4RE_SOLO_Q_POLYMORPH_1 : IDF4RE_SOLO_Q_POLYMORPH_1;
				SkillEngine.getInstance().applyEffectDirectly(idf4reSoloQPolymorph, player, player, 0 * 1);
			}
		});
		final int idf4reSoloUndercoverNpc = spawnRace == Race.ASMODIANS ? 800578 : 800577;
		spawn(idf4reSoloUndercoverNpc, 788.0000f, 383.0000f, 162.0000f, (byte) 0);
	}
	
	private void startPrepareTimer() {
		if (timerPrepare == null) {
			timerPrepare = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					startMainInstanceTimer();
				}
			}, prepareTimerSeconds);
		}
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new S_INSTANT_DUNGEON_INFO(prepareTimerSeconds, instanceReward, null));
			}
		});
	}
	
	private void startMainInstanceTimer() {
		if (!timerPrepare.isDone()) {
			timerPrepare.cancel(false);
		}
		startTime = System.currentTimeMillis();
		instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
		sendPacket(0, 0);
	}
	
	protected void stopInstance(Player player) {
        stopInstanceTask();
        instanceReward.setRank(6);
		instanceReward.setRank(checkRank(instanceReward.getPoints()));
		instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		sendPacket(0, 0);
	}
	
	private void stopInstanceTask() {
        for (FastList.Node<Future<?>> n = tempusTask.head(), end = tempusTask.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
        sp(npcId, x, y, z, h, 0, time, 0, null);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final int msg, final Race race) {
        sp(npcId, x, y, z, h, 0, time, msg, race);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int entityId, final int time, final int msg, final Race race) {
        tempusTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!isInstanceDestroyed) {
                    spawn(npcId, x, y, z, h, entityId);
                    if (msg > 0) {
                        sendMsgByRace(msg, race, 0);
                    }
                }
            }
        }, time));
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
        tempusTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!isInstanceDestroyed) {
                    Npc npc = (Npc) spawn(npcId, x, y, z, h);
                    npc.getSpawn().setWalkerId(walkerId);
                    WalkManager.startWalking((NpcAI2) npc.getAi2());
                }
            }
        }, time));
    }
	
	@Override
	public void onInstanceDestroy() {
		if (timerInstance != null) {
			timerInstance.cancel(false);
		} if (timerPrepare != null) {
			timerPrepare.cancel(false);
		}
		stopInstanceTask();
		isInstanceDestroyed = true;
		doors.clear();
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		instanceReward = new TempusReward(mapId, instanceId);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		doors = instance.getDoors();
		instanceReward.addPoints(3000);
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				player.getController().updateZone();
				player.getController().updateNearbyQuests();
			}
		});
	}
	
	protected void despawnNpc(Npc npc) {
        if (npc != null) {
            npc.getController().onDelete();
        }
    }
	
	protected void despawnNpcs(List<Npc> npcs) {
		for (Npc npc: npcs) {
			npc.getController().onDelete();
		}
	}
	
	@Override
	public void onExitInstance(Player player) {
		InstanceService.destroyInstance(player.getPosition().getWorldMapInstance());
		if (instanceReward.getInstanceScoreType().isEndProgress()) {
			TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
		}
	}
	
	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(IDF4RE_SOLO_POLYMORPH_3);
		effectController.removeEffect(IDF4RE_SOLO_Q_POLYMORPH_1);
	}
	
	@Override
	public void onLeaveInstance(Player player) {
		removeEffects(player);
	}
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeEffects(player);
	}
	
	protected void sendMsgByRace(final int msg, final Race race, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.getRace().equals(race) || race.equals(Race.PC_ALL)) {
							PacketSendUtility.sendPacket(player, new S_MESSAGE_CODE(msg));
						}
					}
				});
			}
		}, time);
	}
}