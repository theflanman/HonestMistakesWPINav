package main.util;

import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import main.gui.GUIFront;

public class GUIFrontUtil {

	//set up building polygons
	public static ArrayList<Polygon> initializePolygons(){
		ArrayList<Polygon> polyList = new ArrayList<Polygon>();
		
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

		//Library
		Polygon gLPolygon = new Polygon();
		gLPolygon.addPoint(1245, 512);
		gLPolygon.addPoint(1304, 525);
		gLPolygon.addPoint(1279, 640);
		gLPolygon.addPoint(1220, 628);
		gLPolygon.addPoint(1226, 568);
		polyList.add(gLPolygon);

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

		//Project Center
		Polygon pCPolygon = new Polygon();
		pCPolygon.addPoint(1019, 598);
		pCPolygon.addPoint(1030, 535);
		pCPolygon.addPoint(1068, 543);
		pCPolygon.addPoint(1056, 604);
		polyList.add(pCPolygon);

		//Stratton
		Polygon sHPolygon = new Polygon();
		sHPolygon.addPoint(1014, 613);
		sHPolygon.addPoint(1052, 618);
		sHPolygon.addPoint(1038, 701);
		sHPolygon.addPoint(1000, 695);
		polyList.add(sHPolygon);
		
		return polyList;
	}
	
	//sets up menu bars
	public static ArrayList<JMenu> initBuildingMenuBar(){
		
		ArrayList<JMenu> mnBuildings = new ArrayList<JMenu>();
		
		mnBuildings.add(new JMenu("Atwater Kent"));
		JMenuItem mntmAK1 = new JMenuItem("Floor 1");
		mntmAK1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(0, 0, 0, 1);
			}
		});
		JMenuItem mntmAK2 = new JMenuItem("Floor 2");
		mntmAK2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(1, 0, 0, 1);
			}
		});
		JMenuItem mntmAK3 = new JMenuItem("Floor 3");
		mntmAK3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(2, 0, 0, 1);
			}
		});
		JMenuItem mntmAKB = new JMenuItem("Basement");
		mntmAKB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				GUIFront.changeMapTo(3, 0, 0, 1);
			}
		});
		mnBuildings.get(0).add(mntmAK1);
		mnBuildings.get(0).add(mntmAK2);
		mnBuildings.get(0).add(mntmAK3);
		mnBuildings.get(0).add(mntmAKB);

		// Boynton Hall
		mnBuildings.add(new JMenu("Boynton Hall"));
		JMenuItem mntmBoy1 = new JMenuItem("Floor 1");
		mntmBoy1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				GUIFront.changeMapTo(4, 0, 0, 1);
			}
		});
		JMenuItem mntmBoy2 = new JMenuItem("Floor 2");
		mntmBoy2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				GUIFront.changeMapTo(5, 0, 0, 1);
			}
		});
		JMenuItem mntmBoy3 = new JMenuItem("Floor 3");
		mntmBoy3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(6, 0, 0, 1);
			}
		});
		JMenuItem mntmBoyB = new JMenuItem("Basement");
		mntmBoyB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(7, 0, 0, 1);
			}
		});
		mnBuildings.get(1).add(mntmBoy1);
		mnBuildings.get(1).add(mntmBoy2);
		mnBuildings.get(1).add(mntmBoy3);
		mnBuildings.get(1).add(mntmBoyB);

		// Campus Center
		mnBuildings.add(new JMenu("Campus Center"));
		JMenuItem mntmCC1 = new JMenuItem("Floor 1");
		mntmCC1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(8, 0, 0, 1);
			}
		});
		JMenuItem mntmCC2 = new JMenuItem("Floor 2");
		mntmCC2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				GUIFront.changeMapTo(9, 0, 0, 1);
			}
		});
		JMenuItem mntmCC3 = new JMenuItem("Floor 3");
		mntmCC3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(10, 0, 0, 1);
			}
		});
		mnBuildings.get(2).add(mntmCC1);
		mnBuildings.get(2).add(mntmCC2);
		mnBuildings.get(2).add(mntmCC3);

		// Fuller Labs
		mnBuildings.add(new JMenu("Fuller Labs"));
		JMenuItem mntmFL1 = new JMenuItem("Floor 1");
		mntmFL1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(12, 0, 0, 1);
			}
		});
		JMenuItem mntmFL2 = new JMenuItem("Floor 2");
		mntmFL2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(13, 0, 0, 1);
			}
		});
		JMenuItem mntmFL3 = new JMenuItem("Floor 3");
		mntmFL3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(14, 0, 0, 1);
			}
		});
		JMenuItem mntmFLB = new JMenuItem("Basement");
		mntmFLB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(15, 0, 0, 1);
			}
		});
		JMenuItem mntmFLSB = new JMenuItem("Sub Basement");
		mntmFLSB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(16, 0, 0, 1);
			}
		});
		mnBuildings.get(3).add(mntmFL1);
		mnBuildings.get(3).add(mntmFL2);
		mnBuildings.get(3).add(mntmFL3);
		mnBuildings.get(3).add(mntmFLB);
		mnBuildings.get(3).add(mntmFLSB);

		// Gordon Library
		mnBuildings.add(new JMenu("Gordon Library"));
		JMenuItem mntmGL1 = new JMenuItem("Floor 1");
		mntmGL1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(17, 0, 0, 1);
			}
		});
		JMenuItem mntmGL2 = new JMenuItem("Floor 2");
		mntmGL2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(18, 0, 0, 1);
			}
		});
		JMenuItem mntmGL3 = new JMenuItem("Floor 3");
		mntmGL3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				GUIFront.changeMapTo(19, 0, 0, 1);
			}
		});
		JMenuItem mntmGLB = new JMenuItem("Basement");
		mntmGLB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(20, 0, 0, 1);
			}
		});
		JMenuItem mntmGLSB = new JMenuItem("Sub Basement");
		mntmGLSB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(21, 0, 0, 1);
			}
		});
		mnBuildings.get(4).add(mntmGL1);
		mnBuildings.get(4).add(mntmGL2);
		mnBuildings.get(4).add(mntmGL3);
		mnBuildings.get(4).add(mntmGLB);
		mnBuildings.get(4).add(mntmGLSB);

		// Higgins House
		mnBuildings.add(new JMenu("Higgins House"));
		JMenuItem mntmHH1 = new JMenuItem("Floor 1");
		mntmHH1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				GUIFront.changeMapTo(22, 0, 0, 1);
			}
		});
		JMenuItem mntmHH2 = new JMenuItem("Floor 2");
		mntmHH2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				GUIFront.changeMapTo(23, 0, 0, 1);
			}
		});
		JMenuItem mntmHH3 = new JMenuItem("Floor 3");
		mntmHH3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				GUIFront.changeMapTo(24, 0, 0, 1);
			}
		});
		mnBuildings.get(5).add(mntmHH1);
		mnBuildings.get(5).add(mntmHH2);
		mnBuildings.get(5).add(mntmHH3);

		// Higgins House Garage
		mnBuildings.add(new JMenu("Higgins House Garage"));
		JMenuItem mntmHHG1 = new JMenuItem("Floor 1");
		mntmHHG1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(25, 0, 0, 1);
			}
		});
		JMenuItem mntmHHG2 = new JMenuItem("Floor 2");
		mntmHHG2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(26, 0, 0, 1);
			}
		});
		mnBuildings.get(6).add(mntmHHG1);
		mnBuildings.get(6).add(mntmHHG2);

		// Project Center
		mnBuildings.add(new JMenu("Project Center"));
		JMenuItem mntmPC1 = new JMenuItem("Floor 1");
		mntmPC1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){			
				GUIFront.changeMapTo(27, 0, 0, 1);
			}
		});
		JMenuItem mntmPC2 = new JMenuItem("Floor 2");
		mntmPC2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(28, 0, 0, 1);
			}
		});
		mnBuildings.get(7).add(mntmPC1);
		mnBuildings.get(7).add(mntmPC2);

		// Stratton Hall
		mnBuildings.add(new JMenu("Stratton Hall"));
		JMenuItem mntmSH1 = new JMenuItem("Floor 1");
		mntmSH1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(29, 0, 0, 1);
			}
		});
		JMenuItem mntmSH2 = new JMenuItem("Floor 2");
		mntmSH2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(30, 0, 0, 1);
			}
		});
		JMenuItem mntmSH3 = new JMenuItem("Floor 3");
		mntmSH3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeMapTo(31, 0, 0, 1);
			}
		});
		JMenuItem mntmSHB = new JMenuItem("Basement");
		mntmSHB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){			
				GUIFront.changeMapTo(32, 0, 0, 1);
			}
		});
		mnBuildings.get(8).add(mntmSH1);
		mnBuildings.get(8).add(mntmSH2);
		mnBuildings.get(8).add(mntmSH3);
		mnBuildings.get(8).add(mntmSHB);
		
		return mnBuildings;
	}
	
}
