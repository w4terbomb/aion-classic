@ECHO off
TITLE AION - Game Server Console
color 0F
:START
PATH=C:\Program Files\Java\jdk1.8.0_202\bin
CLS
SET NUMAENABLE=false
CLS
IF "%MODE%" == "" (
CALL PanelGS.bat
)

ECHO Starting AION Game Server in %MODE% mode.
JAVA  %JAVA_OPTS% -cp ./lib/*;gameserver.jar com.aionemu.gameserver.GameServer
SET CLASSPATH=%OLDCLASSPATH%
IF ERRORLEVEL 2 GOTO START
IF ERRORLEVEL 1 GOTO ERROR
IF ERRORLEVEL 0 GOTO END
:ERROR
ECHO.
ECHO Game Server has terminated abnormaly!
ECHO.
PAUSE
EXIT
:END
ECHO.
ECHO Game Server is terminated!
ECHO.
PAUSE
EXIT
