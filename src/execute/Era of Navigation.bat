@echo off

IF EXIST %userprofile%/HonestMistakesWPINav (
	cd %userprofile%/HonestMistakesWPINav
) 
IF EXIST %userprofile%/git/HonestMistakesWPINav (
 	cd %userprofile%/git/HonestMistakesWPINav
)

java -jar EraOfNavigation.jar