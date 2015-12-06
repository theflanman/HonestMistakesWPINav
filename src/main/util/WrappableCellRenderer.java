package main.util;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * Custom JList Cell Rendering component which will format the elements in the list based on the given width of the JList component
 * to have HTML-Style line breaks achieved by rendering the cells onto a JLabel
 * @author Trevor
 */
@SuppressWarnings("serial")
public class WrappableCellRenderer extends DefaultListCellRenderer {
	DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	int width; // the width of the component it is set in (in pixels)

	public WrappableCellRenderer(int width){
		this.width = width;
	}

	/**
	 * @param list The JList whose cells are being rendered
	 * @param value The current element in the list
	 * @param index The index of the cell
	 * @param isSelected True if the specified cell is selected 
	 * @param cellHasFocus True if the specified call has current application focus
	 * @return A component customly formatted with line breaks in each cell, in this case a JLabel
	 */
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){
		String element = value.toString(); // gets the current element at index and converts it from Object to String
		String fmt = "<html>"; // formatted string in HTML style

		
		String[] words = element.split("\\s+"); // break the string into words without spaces
		int currentLineLength = 0;
		
		// Iterate through the list of words and simulate word wrapping by ensuring you don't break within a word
		for(String s : words){
			if(currentLineLength + s.length() > width){ // check if we're about to exceed the character limit
				fmt += "<br>" + s;
				currentLineLength = 0;
			} 
			else {
				fmt += " " + s;
			}
				
			// update current number of characters on the given line
			currentLineLength += s.length();
		}
		fmt += "</html>";

		JLabel renderer = (JLabel)defaultRenderer.getListCellRendererComponent(list, fmt, index, isSelected, cellHasFocus);
		return renderer;
	}
}