package main.gui.frontutil;

import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import main.gui.GUIFront;
import main.util.Constants;

public class GUIFrontUtil {

	//set up building polygons
	public static ArrayList<Polygon> initializePolygons(){
		ArrayList<Polygon> polyList = new ArrayList<Polygon>();

		//Alden Hall
		Polygon aHPolygon = new Polygon();
		aHPolygon.addPoint(889, 766);
		aHPolygon.addPoint(919, 771);
		aHPolygon.addPoint(919, 777);
		aHPolygon.addPoint(927, 779);
		aHPolygon.addPoint(913, 858);
		aHPolygon.addPoint(921, 859);
		aHPolygon.addPoint(923, 848);
		aHPolygon.addPoint(934, 850);
		aHPolygon.addPoint(932, 861);
		aHPolygon.addPoint(929, 860);
		aHPolygon.addPoint(924, 884);
		aHPolygon.addPoint(903, 880);
		aHPolygon.addPoint(901, 889);
		aHPolygon.addPoint(870, 883);
		aHPolygon.addPoint(871, 875);
		aHPolygon.addPoint(854, 872);
		aHPolygon.addPoint(858, 850);
		aHPolygon.addPoint(866, 851);
		aHPolygon.addPoint(881, 772);
		aHPolygon.addPoint(888, 772);
		polyList.add(aHPolygon);

		//Atwater Kent
		Polygon aKPolygon = new Polygon();
		aKPolygon.addPoint(1109, 241);
		aKPolygon.addPoint(1067, 311);
		aKPolygon.addPoint(1089, 325);
		aKPolygon.addPoint(1082, 339);
		aKPolygon.addPoint(1159, 381);
		aKPolygon.addPoint(1168, 369);
		aKPolygon.addPoint(1189, 381);
		aKPolygon.addPoint(1229, 310);
		aKPolygon.addPoint(1195, 289);
		aKPolygon.addPoint(1176, 318);
		aKPolygon.addPoint(1128, 290);
		aKPolygon.addPoint(1144, 260);
		polyList.add(aKPolygon);

		//Boynton
		Polygon bPolygon = new Polygon();
		bPolygon.addPoint(1044, 734);
		bPolygon.addPoint(1037, 775);
		bPolygon.addPoint(1065, 780);
		bPolygon.addPoint(1066, 773);
		bPolygon.addPoint(1117, 782);
		bPolygon.addPoint(1116, 787);
		bPolygon.addPoint(1127, 789);
		bPolygon.addPoint(1127, 783);
		bPolygon.addPoint(1134, 783);
		bPolygon.addPoint(1138, 754);
		bPolygon.addPoint(1072, 743);
		bPolygon.addPoint(1073, 739);
		polyList.add(bPolygon);

		//Campus Center
		Polygon cCPolygon = new Polygon();
		cCPolygon.addPoint(938, 346);
		cCPolygon.addPoint(920, 450);
		cCPolygon.addPoint(899, 448);
		cCPolygon.addPoint(910, 457);
		cCPolygon.addPoint(911, 467);
		cCPolygon.addPoint(904, 477);
		cCPolygon.addPoint(893, 479);
		cCPolygon.addPoint(882, 473);
		cCPolygon.addPoint(881, 458);
		cCPolygon.addPoint(875, 476);
		cCPolygon.addPoint(813, 466);
		cCPolygon.addPoint(821, 418);
		cCPolygon.addPoint(834, 420);
		cCPolygon.addPoint(850, 431);
		cCPolygon.addPoint(857, 422);
		cCPolygon.addPoint(850, 417);
		cCPolygon.addPoint(860, 405);
		cCPolygon.addPoint(854, 391);
		cCPolygon.addPoint(860, 383);
		cCPolygon.addPoint(875, 383);
		cCPolygon.addPoint(884, 372);
		cCPolygon.addPoint(873, 364);
		cCPolygon.addPoint(890, 340);
		polyList.add(cCPolygon);

		// Fuller Labs
		Polygon fLPolygon = new Polygon();
		fLPolygon.addPoint(1225, 445);
		fLPolygon.addPoint(1301, 408);
		fLPolygon.addPoint(1284, 371);
		fLPolygon.addPoint(1305, 359);
		fLPolygon.addPoint(1274, 300);
		fLPolygon.addPoint(1242, 314);
		fLPolygon.addPoint(1255, 341);
		fLPolygon.addPoint(1211, 363);
		fLPolygon.addPoint(1220, 382);
		fLPolygon.addPoint(1199, 393);
		polyList.add(fLPolygon);

		//Gordon Library
		Polygon gLPolygon = new Polygon();
		gLPolygon.addPoint(1245, 512);
		gLPolygon.addPoint(1304, 525);
		gLPolygon.addPoint(1279, 640);
		gLPolygon.addPoint(1220, 628);
		gLPolygon.addPoint(1226, 568);
		polyList.add(gLPolygon);

		//Harrington Auditorium
		Polygon hAPolygon = new Polygon();
		hAPolygon.addPoint(730, 467);
		hAPolygon.addPoint(729, 516);
		hAPolygon.addPoint(717, 556);
		hAPolygon.addPoint(688, 558);
		hAPolygon.addPoint(686, 569);
		hAPolygon.addPoint(675, 567);
		hAPolygon.addPoint(676, 558);
		hAPolygon.addPoint(669, 559);
		hAPolygon.addPoint(668, 566);
		hAPolygon.addPoint(626, 559);
		hAPolygon.addPoint(627, 554);
		hAPolygon.addPoint(608, 546);
		hAPolygon.addPoint(605, 556);
		hAPolygon.addPoint(595, 555);
		hAPolygon.addPoint(596, 541);
		hAPolygon.addPoint(583, 535);
		hAPolygon.addPoint(582, 492);
		hAPolygon.addPoint(595, 447);
		polyList.add(hAPolygon);

		//Higgins House
		Polygon hHPolygon = new Polygon();
		hHPolygon.addPoint(800, 305);
		hHPolygon.addPoint(775, 288);
		hHPolygon.addPoint(787, 271);
		hHPolygon.addPoint(757, 250);
		hHPolygon.addPoint(766, 235);
		hHPolygon.addPoint(791, 250);
		hHPolygon.addPoint(808, 231);
		hHPolygon.addPoint(834, 246);
		hHPolygon.addPoint(847, 231);
		hHPolygon.addPoint(862, 241);
		hHPolygon.addPoint(849, 258);
		hHPolygon.addPoint(839, 253);
		polyList.add(hHPolygon);

		// Higgins House Garage
		Polygon hHGPolygon = new Polygon();
		hHGPolygon.addPoint(875, 167);
		hHGPolygon.addPoint(890, 178);
		hHGPolygon.addPoint(870, 206);
		hHGPolygon.addPoint(855, 196);
		polyList.add(hHGPolygon);

		//Higgins Labs
		Polygon hLPolygon = new Polygon();
		hLPolygon.addPoint(978, 526);
		hLPolygon.addPoint(973, 553);
		hLPolygon.addPoint(958, 551);
		hLPolygon.addPoint(952, 579);
		hLPolygon.addPoint(959, 580);
		hLPolygon.addPoint(957, 592);
		hLPolygon.addPoint(950, 591);
		hLPolygon.addPoint(945, 619);
		hLPolygon.addPoint(961, 624);
		hLPolygon.addPoint(955, 654);
		hLPolygon.addPoint(941, 652);
		hLPolygon.addPoint(939, 657);
		hLPolygon.addPoint(928, 655);
		hLPolygon.addPoint(927, 659);
		hLPolygon.addPoint(913, 658);
		hLPolygon.addPoint(913, 652);
		hLPolygon.addPoint(898, 650);
		hLPolygon.addPoint(899, 645);
		hLPolygon.addPoint(882, 642);
		hLPolygon.addPoint(886, 612);
		hLPolygon.addPoint(880, 615);
		hLPolygon.addPoint(873, 602);
		hLPolygon.addPoint(880, 599);
		hLPolygon.addPoint(893, 529);
		hLPolygon.addPoint(916, 531);
		hLPolygon.addPoint(922, 511);
		hLPolygon.addPoint(962, 516);
		hLPolygon.addPoint(962, 522);
		polyList.add(hLPolygon);

		//Project Center
		Polygon pCPolygon = new Polygon();
		pCPolygon.addPoint(1019, 598);
		pCPolygon.addPoint(1030, 535);
		pCPolygon.addPoint(1068, 543);
		pCPolygon.addPoint(1056, 604);
		polyList.add(pCPolygon);

		//Salisbury
		Polygon sLPolygon = new Polygon();
		sLPolygon.addPoint(1167, 538);
		sLPolygon.addPoint(1186, 425);
		sLPolygon.addPoint(1120, 414);
		sLPolygon.addPoint(1116, 441);
		sLPolygon.addPoint(1088, 436);
		sLPolygon.addPoint(1077, 501);
		sLPolygon.addPoint(1128, 509);
		sLPolygon.addPoint(1125, 531);
		polyList.add(sLPolygon);

		//Stratton
		Polygon sHPolygon = new Polygon();
		sHPolygon.addPoint(1014, 613);
		sHPolygon.addPoint(1052, 618);
		sHPolygon.addPoint(1038, 701);
		sHPolygon.addPoint(1000, 695);
		polyList.add(sHPolygon);

		//Washburn Shops
		Polygon wSPolygon = new Polygon();
		wSPolygon.addPoint(1081, 573);
		wSPolygon.addPoint(1168, 587);
		wSPolygon.addPoint(1156, 665);
		wSPolygon.addPoint(1167, 667);
		wSPolygon.addPoint(1163, 693);
		wSPolygon.addPoint(1151, 692);
		wSPolygon.addPoint(1143, 732);
		wSPolygon.addPoint(1114, 728);
		wSPolygon.addPoint(1126, 656);
		wSPolygon.addPoint(1117, 654);
		wSPolygon.addPoint(1106, 708);
		wSPolygon.addPoint(1066, 702);
		wSPolygon.addPoint(1075, 649);
		wSPolygon.addPoint(1087, 651);
		wSPolygon.addPoint(1089, 640);
		wSPolygon.addPoint(1081, 639);
		wSPolygon.addPoint(1086, 611);
		wSPolygon.addPoint(1076, 608);
		polyList.add(wSPolygon);

		return polyList;
	}

	//sets up menu bars
	public static ArrayList<JMenu> initBuildingMenuBar(){

		ArrayList<JMenu> mnBuildings = new ArrayList<JMenu>();

		// Alden Hall
		mnBuildings.add(new JMenu("Alden Hall"));
		JMenuItem mntmAH1 = new JMenuItem("Floor 1");
		mntmAH1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(0, 0, 0, 1);
			}
		});
		JMenuItem mntmAH2 = new JMenuItem("Floor 2");
		mntmAH2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(1, 0, 0, 1);
			}
		});
		JMenuItem mntmAHB = new JMenuItem("Basement");
		mntmAHB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(2, 0, 0, 1);
			}
		});
		JMenuItem mntmAHSB = new JMenuItem("Sub Basement");
		mntmAHSB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(3, 0, 0, 1);
			}
		});
		mnBuildings.get(0).add(mntmAH1);
		mnBuildings.get(0).add(mntmAH2);
		mnBuildings.get(0).add(mntmAHB);
		mnBuildings.get(0).add(mntmAHSB);
		
		
		// Atwater Kent
		mnBuildings.add(new JMenu("Atwater Kent"));
		JMenuItem mntmAK1 = new JMenuItem("Floor 1");
		mntmAK1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(4, 0, 0, 1);
			}
		});
		JMenuItem mntmAK2 = new JMenuItem("Floor 2");
		mntmAK2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(5, 0, 0, 1);
			}
		});
		JMenuItem mntmAK3 = new JMenuItem("Floor 3");
		mntmAK3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(6, 0, 0, 1);
			}
		});
		JMenuItem mntmAKB = new JMenuItem("Basement");
		mntmAKB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				GUIFront.changeMapTo(7, 0, 0, 1);
			}
		});
		mnBuildings.get(1).add(mntmAK1);
		mnBuildings.get(1).add(mntmAK2);
		mnBuildings.get(1).add(mntmAK3);
		mnBuildings.get(1).add(mntmAKB);

		// Boynton Hall
		mnBuildings.add(new JMenu("Boynton Hall"));
		JMenuItem mntmBoy1 = new JMenuItem("Floor 1");
		mntmBoy1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				GUIFront.changeMapTo(8, 0, 0, 1);
			}
		});
		JMenuItem mntmBoy2 = new JMenuItem("Floor 2");
		mntmBoy2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				GUIFront.changeMapTo(9, 0, 0, 1);
			}
		});
		JMenuItem mntmBoy3 = new JMenuItem("Floor 3");
		mntmBoy3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(10, 0, 0, 1);
			}
		});
		JMenuItem mntmBoyB = new JMenuItem("Basement");
		mntmBoyB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(11, 0, 0, 1);
			}
		});
		mnBuildings.get(2).add(mntmBoy1);
		mnBuildings.get(2).add(mntmBoy2);
		mnBuildings.get(2).add(mntmBoy3);
		mnBuildings.get(2).add(mntmBoyB);

		// Campus Center
		mnBuildings.add(new JMenu("Campus Center"));
		JMenuItem mntmCC1 = new JMenuItem("Floor 1");
		mntmCC1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(12, 0, 0, 1);
			}
		});
		JMenuItem mntmCC2 = new JMenuItem("Floor 2");
		mntmCC2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				GUIFront.changeMapTo(13, 0, 0, 1);
			}
		});
		JMenuItem mntmCC3 = new JMenuItem("Floor 3");
		mntmCC3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(14, 0, 0, 1);
			}
		});
		mnBuildings.get(3).add(mntmCC1);
		mnBuildings.get(3).add(mntmCC2);
		mnBuildings.get(3).add(mntmCC3);

		// Fuller Labs
		mnBuildings.add(new JMenu("Fuller Labs"));
		JMenuItem mntmFL1 = new JMenuItem("Floor 1");
		mntmFL1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(16, 0, 0, 1);
			}
		});
		JMenuItem mntmFL2 = new JMenuItem("Floor 2");
		mntmFL2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(17, 0, 0, 1);
			}
		});
		JMenuItem mntmFL3 = new JMenuItem("Floor 3");
		mntmFL3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(18, 0, 0, 1);
			}
		});
		JMenuItem mntmFLB = new JMenuItem("Basement");
		mntmFLB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(19, 0, 0, 1);
			}
		});
		JMenuItem mntmFLSB = new JMenuItem("Sub Basement");
		mntmFLSB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(20, 0, 0, 1);
			}
		});
		mnBuildings.get(4).add(mntmFL1);
		mnBuildings.get(4).add(mntmFL2);
		mnBuildings.get(4).add(mntmFL3);
		mnBuildings.get(4).add(mntmFLB);
		mnBuildings.get(4).add(mntmFLSB);

		// Gordon Library
		mnBuildings.add(new JMenu("Gordon Library"));
		JMenuItem mntmGL1 = new JMenuItem("Floor 1");
		mntmGL1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(21, 0, 0, 1);
			}
		});
		JMenuItem mntmGL2 = new JMenuItem("Floor 2");
		mntmGL2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(22, 0, 0, 1);
			}
		});
		JMenuItem mntmGL3 = new JMenuItem("Floor 3");
		mntmGL3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				GUIFront.changeMapTo(23, 0, 0, 1);
			}
		});
		JMenuItem mntmGLB = new JMenuItem("Basement");
		mntmGLB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(24, 0, 0, 1);
			}
		});
		JMenuItem mntmGLSB = new JMenuItem("Sub Basement");
		mntmGLSB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(25, 0, 0, 1);
			}
		});
		mnBuildings.get(5).add(mntmGL1);
		mnBuildings.get(5).add(mntmGL2);
		mnBuildings.get(5).add(mntmGL3);
		mnBuildings.get(5).add(mntmGLB);
		mnBuildings.get(5).add(mntmGLSB);
		
		// Harrington Auditorium
		mnBuildings.add(new JMenu("Harrington Auditorium"));
		JMenuItem mntmHA1 = new JMenuItem("Floor 1");
		mntmHA1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				GUIFront.changeMapTo(26, 0, 0, 1);
			}
		});
		JMenuItem mntmHA2 = new JMenuItem("Floor 2");
		mntmHA2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				GUIFront.changeMapTo(27, 0, 0, 1);
			}
		});
		JMenuItem mntmHA3 = new JMenuItem("Floor 3");
		mntmHA3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				GUIFront.changeMapTo(28, 0, 0, 1);
			}
		});
		JMenuItem mntmHAB = new JMenuItem("Basement");
		mntmHAB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				GUIFront.changeMapTo(29, 0, 0, 1);
			}
		});
		mnBuildings.get(6).add(mntmHA1);
		mnBuildings.get(6).add(mntmHA2);
		mnBuildings.get(6).add(mntmHA3);
		mnBuildings.get(6).add(mntmHAB);
		

		// Higgins House
		mnBuildings.add(new JMenu("Higgins House"));
		JMenuItem mntmHH1 = new JMenuItem("Floor 1");
		mntmHH1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				GUIFront.changeMapTo(30, 0, 0, 1);
			}
		});
		JMenuItem mntmHH2 = new JMenuItem("Floor 2");
		mntmHH2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				GUIFront.changeMapTo(31, 0, 0, 1);
			}
		});
		JMenuItem mntmHH3 = new JMenuItem("Floor 3");
		mntmHH3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				GUIFront.changeMapTo(32, 0, 0, 1);
			}
		});
		mnBuildings.get(7).add(mntmHH1);
		mnBuildings.get(7).add(mntmHH2);
		mnBuildings.get(7).add(mntmHH3);

		// Higgins House Garage
		mnBuildings.add(new JMenu("Higgins House Garage"));
		JMenuItem mntmHHG1 = new JMenuItem("Floor 1");
		mntmHHG1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(33, 0, 0, 1);
			}
		});
		JMenuItem mntmHHG2 = new JMenuItem("Floor 2");
		mntmHHG2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(34, 0, 0, 1);
			}
		});
		mnBuildings.get(8).add(mntmHHG1);
		mnBuildings.get(8).add(mntmHHG2);
		
		// Higgins Labs
		mnBuildings.add(new JMenu("Higgins Labs"));
		JMenuItem mntmHL0 = new JMenuItem("Floor 0");
		mntmHL0.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(35, 0, 0, 1);
			}
		});
		JMenuItem mntmHL1 = new JMenuItem("Floor 1");
		mntmHL1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(36, 0, 0, 1);
			}
		});
		JMenuItem mntmHL2 = new JMenuItem("Floor 2");
		mntmHL2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(37, 0, 0, 1);
			}
		});
		JMenuItem mntmHL3 = new JMenuItem("Floor 3");
		mntmHL3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(38, 0, 0, 1);
			}
		});
		mnBuildings.get(9).add(mntmHL0);
		mnBuildings.get(9).add(mntmHL1);
		mnBuildings.get(9).add(mntmHL2);
		mnBuildings.get(9).add(mntmHL3);

		// Project Center
		mnBuildings.add(new JMenu("Project Center"));
		JMenuItem mntmPC1 = new JMenuItem("Floor 1");
		mntmPC1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){			
				GUIFront.changeMapTo(39, 0, 0, 1);
			}
		});
		JMenuItem mntmPC2 = new JMenuItem("Floor 2");
		mntmPC2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(40, 0, 0, 1);
			}
		});
		mnBuildings.get(10).add(mntmPC1);
		mnBuildings.get(10).add(mntmPC2);

		// Stratton Hall
		mnBuildings.add(new JMenu("Stratton Hall"));
		JMenuItem mntmSH1 = new JMenuItem("Floor 1");
		mntmSH1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(41, 0, 0, 1);
			}
		});
		JMenuItem mntmSH2 = new JMenuItem("Floor 2");
		mntmSH2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(42, 0, 0, 1);
			}
		});
		JMenuItem mntmSH3 = new JMenuItem("Floor 3");
		mntmSH3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(43, 0, 0, 1);
			}
		});
		JMenuItem mntmSHB = new JMenuItem("Basement");
		mntmSHB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){			
				GUIFront.changeMapTo(44, 0, 0, 1);
			}
		});
		mnBuildings.get(11).add(mntmSH1);
		mnBuildings.get(11).add(mntmSH2);
		mnBuildings.get(11).add(mntmSH3);
		mnBuildings.get(11).add(mntmSHB);
		
		// Salisbury
		mnBuildings.add(new JMenu("Salisbury Labs"));
		JMenuItem mntmSL0 = new JMenuItem("Floor 0");
		mntmSL0.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(45, 0, 0, 1);
			}
		});
		JMenuItem mntmSL1 = new JMenuItem("Floor 1");
		mntmSL1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(46, 0, 0, 1);
			}
		});
		JMenuItem mntmSL2 = new JMenuItem("Floor 2");
		mntmSL2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(47, 0, 0, 1);
			}
		});
		JMenuItem mntmSL3 = new JMenuItem("Floor 3");
		mntmSL3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){			
				GUIFront.changeMapTo(48, 0, 0, 1);
			}
		});
		JMenuItem mntmSL4 = new JMenuItem("Floor 4");
		mntmSL4.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){			
				GUIFront.changeMapTo(49, 0, 0, 1);
			}
		});
		JMenuItem mntmSL5 = new JMenuItem("Floor 5");
		mntmSL5.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){			
				GUIFront.changeMapTo(50, 0, 0, 1);
			}
		});
		mnBuildings.get(12).add(mntmSL0);
		mnBuildings.get(12).add(mntmSL1);
		mnBuildings.get(12).add(mntmSL2);
		mnBuildings.get(12).add(mntmSL3);
		mnBuildings.get(12).add(mntmSL4);
		mnBuildings.get(12).add(mntmSL5);
		
		// Washburn Shops
		mnBuildings.add(new JMenu("Washburn Shops"));
		JMenuItem mntmWB0 = new JMenuItem("Floor 0");
		mntmWB0.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(51, 0, 0, 1);
			}
		});
		JMenuItem mntmWB1 = new JMenuItem("Floor 1");
		mntmWB1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(52, 0, 0, 1);
			}
		});
		JMenuItem mntmWB2 = new JMenuItem("Floor 2");
		mntmWB2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(53, 0, 0, 1);
			}
		});
		JMenuItem mntmWB3 = new JMenuItem("Floor 3");
		mntmWB3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){			
				GUIFront.changeMapTo(54, 0, 0, 1);
			}
		});
		mnBuildings.get(13).add(mntmWB0);
		mnBuildings.get(13).add(mntmWB1);
		mnBuildings.get(13).add(mntmWB2);
		mnBuildings.get(13).add(mntmWB3);

		return mnBuildings;
	}
	
	//switch for floor chooser
	public static void setFloorMenu(int index){
		switch(index){
		case 0:
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(1, 0, 0, 1); // atwater kent 2
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(2, 0, 0, 1); // atwater kent 3
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(3, 0, 0, 1); // atwater kent basement
				}
			});
			break;
		case 1:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(0, 0, 0, 1); // atwater kent 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(2, 0, 0, 1); // atwater kent 3
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(3, 0, 0, 1); // atwater kent basement
				}
			});
			break;
		case 2:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(0, 0, 0, 1); // atwater kent 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(1, 0, 0, 1); // atwater kent 2
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(3, 0, 0, 1); // atwater kent basement
				}
			});
			break;
		case 3:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(0, 0, 0, 1); // atwater kent 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(1, 0, 0, 1); // atwater kent 2
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(2, 0, 0, 1); // atwater kent 3
				}
			});
			break;
		case 4:
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(5, 0, 0, 1); // boynton hall 2
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(6, 0, 0, 1); // boynton hall 3
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(7, 0, 0, 1); // boynton hall basement
				}
			});
			break;
		case 5:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(4, 0, 0, 1); // boynton hall 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(6, 0, 0, 1); // boynton hall 3
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(7, 0, 0, 1); // boynton hall basement
				}
			});
			break;
		case 6:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(4, 0, 0, 1); // boynton hall 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(5, 0, 0, 1); // boynton hall 2
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(7, 0, 0, 1); // boynton hall basement
				}
			});
			break;
		case 7:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(4, 0, 0, 1); // boynton hall 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(5, 0, 0, 1); // boynton hall 2
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(6, 0, 0, 1); // boynton hall 3
				}
			});
			break;
		case 8:
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(9, 0, 0, 1); // campus center 2
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(10, 0, 0, 1); // campus center 3
				}
			});
			break;
		case 9:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(8, 0, 0, 1); // campus center 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(10, 0, 0, 1); // campus center 3
				}
			});
			break;
		case 10:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(8, 0, 0, 1); // campus center 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(9, 0, 0, 1); // campus center 2
				}
			});
			break;
		case 12:
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(13, 0, 0, 1); // fuller labs 2
				}
			});	
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(14, 0, 0, 1); // fuller labs 3
				}
			});	
			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(15, 0, 0, 1); // fuller labs basement
				}
			});	
			GUIFront.floorChooser.add(new JMenuItem("Sub Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(16, 0, 0, 1); // fuller labs sub basement
				}
			});
			break;
		case 13:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(12, 0, 0, 1); // fuller labs 1
				}
			});	
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(14, 0, 0, 1); // fuller labs 3
				}
			});	
			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(15, 0, 0, 1); // fuller labs basement
				}
			});	
			GUIFront.floorChooser.add(new JMenuItem("Sub Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(16, 0, 0, 1); // fuller labs sub basement
				}
			});	
			break;
		case 14:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(12, 0, 0, 1); // fuller labs 1
				}
			});	
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(13, 0, 0, 1); // fuller labs 2
				}
			});	
			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(15, 0, 0, 1); // fuller labs basement
				}
			});	
			GUIFront.floorChooser.add(new JMenuItem("Sub Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(16, 0, 0, 1); // fuller labs sub basement
				}
			});	
			break;
		case 15:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(12, 0, 0, 1); // fuller labs 1
				}
			});	
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(13, 0, 0, 1); // fuller labs 2
				}
			});	
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(14, 0, 0, 1); // fuller labs 3
				}
			});	
			GUIFront.floorChooser.add(new JMenuItem("Sub Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(16, 0, 0, 1); // fuller labs sub basement
				}
			});	
			break;
		case 16:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(12, 0, 0, 1); // fuller labs 1
				}
			});	
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(13, 0, 0, 1); // fuller labs 2
				}
			});	
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(14, 0, 0, 1); // fuller labs 3
				}
			});	
			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(15, 0, 0, 1); // fuller labs basement
				}
			});	
			break;
		case 17:
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(18, 0, 0, 1); // library 2
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(19, 0, 0, 1); // library 3
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(20, 0, 0, 1); // library basement
				}
			});

			GUIFront.floorChooser.add(new JMenuItem("Sub Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(21, 0, 0, 1); // library sub basement
				}
			});
			break;
		case 18:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(17, 0, 0, 1); // library 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(19, 0, 0, 1); // library 3
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(20, 0, 0, 1); // library basement
				}
			});

			GUIFront.floorChooser.add(new JMenuItem("Sub Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(21, 0, 0, 1); // library sub basement
				}
			});
			break;
		case 19:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(17, 0, 0, 1); // library 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(18, 0, 0, 1); // library 2
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(20, 0, 0, 1); // library basement
				}
			});

			GUIFront.floorChooser.add(new JMenuItem("Sub Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(21, 0, 0, 1); // library sub basement
				}
			});
			break;
		case 20:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(17, 0, 0, 1); // library 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(18, 0, 0, 1); // library 2
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(19, 0, 0, 1); // library 3
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Sub Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(21, 0, 0, 1); // library sub basement
				}
			});
			break;
		case 21:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(17, 0, 0, 1); // library 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(18, 0, 0, 1); // library 2
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(19, 0, 0, 1); // library 3
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(20, 0, 0, 1); // library basement
				}
			});
			break;
		case 22:
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(23, 0, 0, 1); // higgins house 2
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(24, 0, 0, 1); // higgins house 3
				}
			});
			break;
		case 23:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(22, 0, 0, 1); // higgins house 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(24, 0, 0, 1); // higgins house 3
				}
			});
			break;
		case 24:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(22, 0, 0, 1); // higgins house 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(23, 0, 0, 1); // higgins house 2
				}
			});
			break;
		case 25:
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(26, 0, 0, 1); // higgins garage 2
				}
			});
			break;
		case 26:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(25, 0, 0, 1); // higgins garage 1
				}
			});
			break;
		case 27:
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(28, 0, 0, 1); // project center 2
				}
			});
			break;
		case 28:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(27, 0, 0, 1); // project center 1
				}
			});
			break;
		case 29:
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(30, 0, 0, 1); // stratton hall 2
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(31, 0, 0, 1); // stratton hall 3
				}
			});

			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(32, 0, 0, 1); // stratton hall basement 3
				}
			});
			break;
		case 30:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(29, 0, 0, 1); // stratton hall 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(31, 0, 0, 1); // stratton hall 3
				}
			});

			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(32, 0, 0, 1); // stratton hall basement 3
				}
			});
			break;
		case 31:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(29, 0, 0, 1); // stratton hall 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(30, 0, 0, 1); // stratton hall 2
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Basement"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(32, 0, 0, 1); // stratton hall basement 3
				}
			});
			break;
		case 32:
			GUIFront.floorChooser.add(new JMenuItem("Floor 1"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(29, 0, 0, 1); // stratton hall 1
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 2"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(30, 0, 0, 1); // stratton hall 2
				}
			});
			GUIFront.floorChooser.add(new JMenuItem("Floor 3"))
			.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					GUIFront.changeMapTo(31, 0, 0, 1); // stratton hall 3
				}
			});
			break;
		default:
			break;
		}
	}

	// intializes pan and zoom
	public static HashMap<String, double[]> initPanZoom(){
		HashMap<String, double[]> defaultVals = new HashMap<String, double[]>();
		
		defaultVals.put("AH1.png", new double[]{50.0, -30.0, 0.9}); // 0  -- done 
		defaultVals.put("AH2.png", new double[]{50.0, -40.0, 0.9}); // 1
		defaultVals.put("AHB.png", new double[]{50.0, -10.0, 0.9}); // 2 
		defaultVals.put("AHSB.png", new double[]{50.0, -20.0, 0.9}); // 3

		defaultVals.put("AK1.png", new double[]{50.0, -65.0, 1.0}); // 4  -- done
		defaultVals.put("AK2.png", new double[]{50.0, -70.0, 1.0}); // 5
		defaultVals.put("AK3.png", new double[]{50.0, -60.0, 1.0}); // 6
		defaultVals.put("AKB.png", new double[]{50.0, -120.0, 1.0}); // 7

		defaultVals.put("Boy1.png", new double[]{90.0, -90.0, 1.0}); // 8
		defaultVals.put("Boy2.png", new double[]{90.0, -90.0, 1.0}); // 9
		defaultVals.put("Boy3.png", new double[]{90.0, -90.0, 1.0}); // 10
		defaultVals.put("BoyB.png", new double[]{90.0, -90.0, 1.0}); // 11

		defaultVals.put("CC1.png", new double[]{-80.0, -110.0, 0.68}); // 12
		defaultVals.put("CC2.png", new double[]{-80.0, -110.0, 0.66}); // 13
		defaultVals.put("CC3.png", new double[]{-80.0, -120.0, 0.80}); // 14

		defaultVals.put("CCM.png", new double[]{-225.0, -250.0, 0.7}); // 15

		defaultVals.put("FL1.png", new double[]{-80.0, -110.0, 0.7}); // 16
		defaultVals.put("FL2.png", new double[]{-80.0, -110.0, 0.7}); // 17
		defaultVals.put("FL3.png", new double[]{-80.0, -110.0, 0.7}); // 18
		defaultVals.put("FLB.png", new double[]{-80.0, -110.0, 0.7}); // 19
		defaultVals.put("FLSB.png", new double[]{-80.0, -110.0, 0.7}); // 20

		defaultVals.put("GL1.png", new double[]{-80.0, -110.0, 0.74}); // 21
		defaultVals.put("GL2.png", new double[]{-80.0, -110.0, 0.74}); // 22
		defaultVals.put("GL3.png", new double[]{-80.0, -110.0, 0.74}); // 23
		defaultVals.put("GLB.png", new double[]{-80.0, -110.0, 0.74}); // 24
		defaultVals.put("GLSB.png", new double[]{-80.0, -110.0, 0.74}); //25
		
		defaultVals.put("HA1.png", new double[]{-80.0, -120.0, 0.8}); // 26
		defaultVals.put("HA2.png", new double[]{-80.0, -120.0, 0.8}); // 27
		defaultVals.put("HA3.png", new double[]{-80.0, -120.0, 0.8}); // 28
		defaultVals.put("HAB.png", new double[]{-80.0, -120.0, 0.8}); // 29

		defaultVals.put("HH1.png", new double[]{-80.0, -110.0, 0.65}); // 30
		defaultVals.put("HH2.png", new double[]{-80.0, -110.0, 0.65}); // 31
		defaultVals.put("HH3.png", new double[]{-80.0, -110.0, 0.65}); // 32

		defaultVals.put("HHG1.png", new double[]{-80.0, -95.0, 0.86}); // 33
		defaultVals.put("HHG2.png", new double[]{-80.0, -95.0, 0.86}); // 34
		
		defaultVals.put("HL0.png", new double[]{-80.0, -120.0, 0.8}); // 35
		defaultVals.put("HL1.png", new double[]{-80.0, -120.0, 0.8}); // 36
		defaultVals.put("HL2.png", new double[]{-80.0, -120.0, 0.8}); // 37
		defaultVals.put("HL3.png", new double[]{-80.0, -120.0, 0.8}); // 38

		defaultVals.put("PC1.png", new double[]{-80.0, -140.0, 0.80}); // 39
		defaultVals.put("PC2.png", new double[]{-80.0, -110.0, 0.82}); // 40

		defaultVals.put("SH1.png", new double[]{-80.0, -110.0, 0.82}); // 41
		defaultVals.put("SH2.png", new double[]{-80.0, -140.0, 0.90}); // 42
		defaultVals.put("SH3.png", new double[]{-80.0, -110.0, 0.90}); // 43
		defaultVals.put("SHB.png", new double[]{-80.0, -110.0, 0.90}); // 44
		
		defaultVals.put("SL0.png", new double[]{-80.0, -120.0, 0.8}); // 45
		defaultVals.put("SL1.png", new double[]{-80.0, -120.0, 0.8}); // 46
		defaultVals.put("SL2.png", new double[]{-80.0, -120.0, 0.8}); // 47
		defaultVals.put("SL3.png", new double[]{-80.0, -120.0, 0.8}); // 48
		defaultVals.put("SL4.png", new double[]{-80.0, -120.0, 0.8}); // 49
		defaultVals.put("SL5.png", new double[]{-80.0, -120.0, 0.8}); // 50
		
		defaultVals.put("WB0.png", new double[]{-80.0, -120.0, 0.8}); // 51
		defaultVals.put("WB1.png", new double[]{-80.0, -120.0, 0.8}); // 52 
		defaultVals.put("WB2.png", new double[]{-80.0, -120.0, 0.8}); // 53
		defaultVals.put("WB3.png", new double[]{-80.0, -120.0, 0.8}); // 54

		return defaultVals;
	}

	public static String[] createLanguageText(){
		String[] languageText = new String[41];

		languageText[0] = "English";
		languageText[1] = "Arabic";
		languageText[2] = "Bulgarian";
		languageText[3] = "Catalan";
		languageText[4] = "Chinese Simplified";
		languageText[5] = "Chinese Traditional";
		languageText[6] = "Czech";
		languageText[7] = "Danish";
		languageText[8] = "Dutch";
		languageText[9] = "Estonian";
		languageText[10] = "Finnish";
		languageText[11] = "French";
		languageText[12] = "German";
		languageText[13] = "Greek";
		languageText[14] = "Haitian Creole";
		languageText[15] = "Hebrew";
		languageText[16] = "Hindi";
		languageText[17] = "Hmong Daw";
		languageText[18] = "Hungarian";
		languageText[19] = "Indonesian";
		languageText[20] = "Italian";
		languageText[21] = "Japanese";
		languageText[22] = "Korean";
		languageText[23] = "Latvian";
		languageText[24] = "Lithuanian";
		languageText[25] = "Malay";
		languageText[26] = "Norwegian";
		languageText[27] = "Persian";
		languageText[28] = "Polish";
		languageText[29] = "Portuguese";
		languageText[30] = "Romanian";
		languageText[31] = "Russian";
		languageText[32] = "Slovak";
		languageText[33] = "Slovenian";
		languageText[34] = "Spanish";
		languageText[35] = "Swedish";
		languageText[36] = "Thai";
		languageText[37] = "Turkish";
		languageText[38] = "Ukrainian";
		languageText[39] = "Urdu";
		languageText[40] = "Vietnamese";
		
		return languageText;
	}
		
	// adds languages
	public static void addLanguageListeners(){
		GUIFront.getMntmLanguages().get(0).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.ENGLISH);
				GUIFront.changeScreenText(Language.ENGLISH);
			}
		});
		GUIFront.getMntmLanguages().get(1).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.ARABIC);
				GUIFront.changeScreenText(Language.ARABIC);
			}
		});	
		GUIFront.getMntmLanguages().get(2).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.BULGARIAN);
				GUIFront.changeScreenText(Language.BULGARIAN);
			}
		});	
		GUIFront.getMntmLanguages().get(3).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.CATALAN);
				GUIFront.changeScreenText(Language.CATALAN);
			}
		});	
		GUIFront.getMntmLanguages().get(4).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.CHINESE_SIMPLIFIED);
				GUIFront.changeScreenText(Language.CHINESE_SIMPLIFIED);
			}
		});	
		GUIFront.getMntmLanguages().get(5).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.CHINESE_TRADITIONAL);
				GUIFront.changeScreenText(Language.CHINESE_TRADITIONAL);
			}
		});	
		GUIFront.getMntmLanguages().get(6).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.CZECH);
				GUIFront.changeScreenText(Language.CZECH);
			}
		});	
		GUIFront.getMntmLanguages().get(7).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.DANISH);
				GUIFront.changeScreenText(Language.DANISH);
			}
		});	
		GUIFront.getMntmLanguages().get(8).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.DUTCH);
				GUIFront.changeScreenText(Language.DUTCH);
			}
		});	
		GUIFront.getMntmLanguages().get(9).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.ESTONIAN);
			}
		});	
		GUIFront.getMntmLanguages().get(10).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.FINNISH);
				GUIFront.changeScreenText(Language.FINNISH);
			}
		});	
		GUIFront.getMntmLanguages().get(11).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.FRENCH);
				GUIFront.changeScreenText(Language.FRENCH);
			}
		});	
		GUIFront.getMntmLanguages().get(12).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.GERMAN);
				GUIFront.changeScreenText(Language.GERMAN);
			}
		});	
		GUIFront.getMntmLanguages().get(13).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.GREEK);
				GUIFront.changeScreenText(Language.GREEK);
			}
		});	
		GUIFront.getMntmLanguages().get(14).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.HAITIAN_CREOLE);
				GUIFront.changeScreenText(Language.HAITIAN_CREOLE);
			}
		});	
		GUIFront.getMntmLanguages().get(15).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.HEBREW);
				GUIFront.changeScreenText(Language.HEBREW);
			}
		});	
		GUIFront.getMntmLanguages().get(16).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.HINDI);
				GUIFront.changeScreenText(Language.HINDI);
			}
		});	
		GUIFront.getMntmLanguages().get(17).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.HMONG_DAW);
				GUIFront.changeScreenText(Language.HMONG_DAW);
			}
		});	
		GUIFront.getMntmLanguages().get(18).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.HUNGARIAN);
				GUIFront.changeScreenText(Language.HUNGARIAN);
			}
		});	
		GUIFront.getMntmLanguages().get(19).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.INDONESIAN);
				GUIFront.changeScreenText(Language.INDONESIAN);
			}
		});	
		GUIFront.getMntmLanguages().get(20).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.ITALIAN);
				GUIFront.changeScreenText(Language.ITALIAN);
			}
		});	
		GUIFront.getMntmLanguages().get(21).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.JAPANESE);
				GUIFront.changeScreenText(Language.JAPANESE);
			}
		});	
		GUIFront.getMntmLanguages().get(22).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.KOREAN);
				GUIFront.changeScreenText(Language.KOREAN);
			}
		});	
		GUIFront.getMntmLanguages().get(23).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.LATVIAN);
				GUIFront.changeScreenText(Language.LATVIAN);
			}
		});	
		GUIFront.getMntmLanguages().get(24).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.LITHUANIAN);
				GUIFront.changeScreenText(Language.LITHUANIAN);
			}
		});	
		GUIFront.getMntmLanguages().get(25).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.MALAY);
				GUIFront.changeScreenText(Language.MALAY);
			}
		});	
		GUIFront.getMntmLanguages().get(26).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.NORWEGIAN);
				GUIFront.changeScreenText(Language.NORWEGIAN);
			}
		});
		GUIFront.getMntmLanguages().get(27).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.PERSIAN);
				GUIFront.changeScreenText(Language.PERSIAN);
			}
		});	
		GUIFront.getMntmLanguages().get(28).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.POLISH);
				GUIFront.changeScreenText(Language.POLISH);
			}
		});	
		GUIFront.getMntmLanguages().get(29).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.PORTUGUESE);
				GUIFront.changeScreenText(Language.PORTUGUESE);
			}
		});	
		GUIFront.getMntmLanguages().get(30).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.ROMANIAN);
				GUIFront.changeScreenText(Language.ROMANIAN);
			}
		});	
		GUIFront.getMntmLanguages().get(31).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.RUSSIAN);
				GUIFront.changeScreenText(Language.RUSSIAN);
			}
		});	
		GUIFront.getMntmLanguages().get(32).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.SLOVAK);
				GUIFront.changeScreenText(Language.SLOVAK);
			}
		});
		GUIFront.getMntmLanguages().get(33).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.SLOVENIAN);
				GUIFront.changeScreenText(Language.SLOVENIAN);
			}
		});	
		GUIFront.getMntmLanguages().get(34).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.SPANISH);
				GUIFront.changeScreenText(Language.SPANISH);
			}
		});	
		GUIFront.getMntmLanguages().get(35).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.SWEDISH);
				GUIFront.changeScreenText(Language.SWEDISH);
			}
		});	
		GUIFront.getMntmLanguages().get(36).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.THAI);
				GUIFront.changeScreenText(Language.THAI);
			}
		});	
		GUIFront.getMntmLanguages().get(37).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.TURKISH);
				GUIFront.changeScreenText(Language.TURKISH);
			}
		});	
		GUIFront.getMntmLanguages().get(38).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.UKRAINIAN);
				GUIFront.changeScreenText(Language.UKRAINIAN);
			}
		});	
		GUIFront.getMntmLanguages().get(39).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.URDU);
				GUIFront.changeScreenText(Language.URDU);
			}
		});	
		GUIFront.getMntmLanguages().get(40).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GUIFront.setLanguage(Language.VIETNAMESE);
				GUIFront.changeScreenText(Language.VIETNAMESE);
			}
		});
	}
}
