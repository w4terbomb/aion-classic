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
package quest.theobomos;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/** Author Rinzler (Encom)
/****/

public class _3096Examine_The_Extraction_Devices extends QuestHandler
{
	private final static int questId = 3096;
	
	public _3096Examine_The_Extraction_Devices() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.registerQuestNpc(798225).addOnQuestStart(questId);
		qe.registerQuestNpc(798225).addOnTalkEvent(questId);
		qe.registerQuestNpc(700423).addOnTalkEvent(questId);
		qe.registerQuestNpc(700424).addOnTalkEvent(questId);
		qe.registerQuestNpc(700425).addOnTalkEvent(questId);
		qe.registerQuestNpc(700426).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 798225) {
				switch (env.getDialog()) {
                    case START_DIALOG: {
						return sendQuestDialog(env, 1011);
					} case ASK_ACCEPTION: {
						return sendQuestDialog(env, 4);
					} case ACCEPT_QUEST: {
						return sendQuestStartDialog(env);
					} case REFUSE_QUEST: {
				        return closeDialogWindow(env);
					}
                }
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 700423) {
                switch (env.getDialog()) {
                    case USE_OBJECT: {
						giveQuestItem(env, 182208067, 1);
                        return closeDialogWindow(env);
					}
                }
            } if (targetId == 700424) {
                switch (env.getDialog()) {
                    case USE_OBJECT: {
						giveQuestItem(env, 182208068, 1);
                        return closeDialogWindow(env);
					}
                }
            } if (targetId == 700425) {
                switch (env.getDialog()) {
                    case USE_OBJECT: {
						giveQuestItem(env, 182208069, 1);
                        return closeDialogWindow(env);
					}
                }
            } if (targetId == 700426) {
                switch (env.getDialog()) {
                    case USE_OBJECT: {
						giveQuestItem(env, 182208070, 1);
                        return closeDialogWindow(env);
					}
                }
            } if (targetId == 798225) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						if (var == 0) {
							return sendQuestDialog(env, 2375);
						}
					} case CHECK_COLLECTED_ITEMS: {
						return checkQuestItems(env, 0, 1, true, 5, 2716);
					} case FINISH_DIALOG: {
						return sendQuestSelectionDialog(env);
					}
				}
			}
		} else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798225) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}