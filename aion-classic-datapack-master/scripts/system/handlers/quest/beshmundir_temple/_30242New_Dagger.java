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
package quest.beshmundir_temple;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Rinzler (Encom)
/****/

public class _30242New_Dagger extends QuestHandler
{
	private final static int questId = 30242;
	
	public _30242New_Dagger() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestItem(182209632, questId);
		qe.registerQuestNpc(799032).addOnQuestStart(questId);
		qe.registerQuestNpc(799032).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 799032) {
				switch (env.getDialog()) {
                    case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					} case ASK_ACCEPTION: {
						return sendQuestDialog(env, 4);
					} case ACCEPT_QUEST: {
						return sendQuestStartDialog(env, 182209632, 1);
					} case REFUSE_QUEST: {
				        return closeDialogWindow(env);
					}
                }
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			long sielEssence = player.getInventory().getItemCountByItemId(182209640);
            if (targetId == 799032) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 10002);
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					if (sielEssence == 1) {
						removeQuestItem(env, 100200839, 1);
						removeQuestItem(env, 186000099, 1);
						removeQuestItem(env, 186000106, 1);
						removeQuestItem(env, 186000107, 1);
						return sendQuestDialog(env, 5);
					} else {
						return sendQuestDialog(env, 2716);
					}
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		long vorpalEssence = player.getInventory().getItemCountByItemId(186000099);
		long blessedAuriumIngot = player.getInventory().getItemCountByItemId(186000106);
		long specialShulackRefiner = player.getInventory().getItemCountByItemId(186000107);
		if (id != 182209632) {
			return HandlerResult.UNKNOWN;
		} if (!player.isInsideZone(ZoneName.get("DEBILKARIMS_FORGE_300160000"))) {
			return HandlerResult.UNKNOWN;
		} if (vorpalEssence == 0 && blessedAuriumIngot == 0 && specialShulackRefiner == 0) {
			PacketSendUtility.sendWhiteMessage(player, "You need collect <Vorpal Essence + Blessed Aurium Ingot + Special Shulack Refiner> for use this item!!!");
			return HandlerResult.UNKNOWN;
		}
		PacketSendUtility.broadcastPacket(player, new S_USE_ITEM(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				qs.setQuestVar(0);
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				giveQuestItem(env, 182209640, 1);
				removeQuestItem(env, 182209632, 1);
				PacketSendUtility.broadcastPacket(player, new S_USE_ITEM(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
			}
		}, 3000);
		return HandlerResult.SUCCESS;
	}
}