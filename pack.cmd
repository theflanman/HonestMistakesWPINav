@ECHO OFF

if exist %CD%\EraOfNavigation (
	del %CD%\EraOFNavigation
)

mkdir EraOfNavigation\src

xcopy /e %CD%\src\data %CD%\EraOfNavigation\src\data
copy %CD%\EraOfNavigation.jar %CD%\EraOfNavigation
