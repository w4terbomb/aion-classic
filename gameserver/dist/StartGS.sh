#!/bin/sh

java -Xms1G -Xmx2G -server -ea -cp ./lib/*:gameserver.jar com.aionemu.gameserver.GameServer
