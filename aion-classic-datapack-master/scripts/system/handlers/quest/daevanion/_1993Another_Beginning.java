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
package quest.daevanion;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/****/
/** Author Rinzler (Encom)
/****/

public class _1993Another_Beginning extends QuestHandler
{
	private int item;
	private int choice = 0;
	private final static int questId = 1993;
	
	private final static int dialogs[] = {1013, 1034, 1055, 1076, 5103, 1098, 1119, 1140, 1161, 5104, 1183, 1204,
	1225, 1246, 5105, 1268, 1289, 1310, 1331, 5106, 2376, 2461, 2546, 2631, 2632};
	
	private final static int daevanionArmor[] = {110100931, 113100843, 112100790, 111100831, 114100866, 110300881,
	113300860, 112300784, 111300834, 114300893, 110500849, 113500827, 112500774, 111500821, 114500837, 110600834, 113600800, 112600785, 111600813, 114600794};
	
	public _1993Another_Beginning() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(203753).addOnQuestStart(questId);
		qe.registerQuestNpc(203753).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		int dialogId = env.getDialogId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 203753) {
				if (dialogId == 54) {
					QuestService.startQuest(env);
					return sendQuestDialog(env, 1011);
				} else {
					return sendQuestStartDialog(env);
				}
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.START) {
			long daevanionLight = player.getInventory().getItemCountByItemId(186000041);
			if (targetId == 203753) {
				if (dialogId == 54) {
					return sendQuestDialog(env, 1011);
				} switch (dialogId) {
					case 1012:
					case 1097:
					case 1182:
					case 1267:
						return sendQuestDialog(env, dialogId);
					case 1013:
					case 1034:
					case 1055:
					case 1076:
					case 5103:
					case 1098:
					case 1119:
					case 1140:
					case 1161:
					case 5104:
					case 1183:
					case 1204:
					case 1225:
					case 1246:
					case 5105:
					case 1268:
					case 1289:
					case 1310:
					case 1331:
					case 5106:
					case 2376:
					case 2461:
					case 2546:
					case 2631:
					case 2632: {
						item = getItem(dialogId);
						if (player.getInventory().getItemCountByItemId(item) > 0) {
							return sendQuestDialog(env, 1013);
						} else {
							return sendQuestDialog(env, 1352);
						}
					}
					case 10000:
					case 10001:
					case 10002:
					case 10003: {
						if (daevanionLight == 0) {
							return sendQuestDialog(env, 1009);
						}
						changeQuestStep(env, 0, 0, true);
						choice = dialogId - 10000;
						return sendQuestDialog(env, choice + 5);
					}
				}
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203753) {
				removeQuestItem(env, item, 1);
				removeQuestItem(env, 186000041, 1);
				return sendQuestEndDialog(env, choice);
			}
		}
		return false;
	}
	
	private int getItem(int dialogId) {
		int x = 0;
		for (int id : dialogs) {
			if (id == dialogId) {
				break;
			}
			x++;
		}
		return (daevanionArmor[x]);
	}
}