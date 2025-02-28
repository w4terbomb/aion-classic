package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author WeRn
 */
public class S_2ND_PASSWORD extends AionServerPacket {

	private int type; // 0: new passkey input window, 1: passkey input window, 2: message window
	private int messageType; // 0: newpasskey complete, 2: passkey edit complete, 3: passkey input
	private int wrongCount;

	public S_2ND_PASSWORD(int type) {
		this.type = type;
	}

	public S_2ND_PASSWORD(int type, int messageType, int wrongCount) {
		this.type = type;
		this.messageType = messageType;
		this.wrongCount = wrongCount;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeC(type);

		switch (type) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				writeH(messageType); // 0: newpasskey complete, 2: passkey edit complete, 3: passkey input
				//writeC(unk);
				writeC(wrongCount > 0 ? 1 : 0); // 0: right passkey, 1: wrong passkey
				writeD(wrongCount); // wrong passkey input count
				writeD(SecurityConfig.PASSKEY_WRONG_MAXCOUNT);
				// server default value: 5)
				break;
		}
	}
}
