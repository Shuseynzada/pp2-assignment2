package Source.GUI;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

/**
 * Custom TableCellRenderer for rendering buttons inside a JTable cell.
 * This class extends JButton and implements the TableCellRenderer interface
 * to customize the appearance of cells in a JTable.
 *
 * @author Farhad Aliyev
 * @date 02/01/2024
 */
public class ButtonCellRenderer extends JButton implements TableCellRenderer {
    private String text;

    /**
     * Constructs a ButtonCellRenderer with the specified text for the button.
     *
     * @param text The text to display on the button.
     */
    public ButtonCellRenderer(String text) {
        this.text = text;
        setOpaque(true);
    }

    /**
     * Gets the component used for rendering a table cell.
     *
     * @param table      The JTable in which the cell exists.
     * @param value      The value to be displayed in the cell (ignored here).
     * @param isSelected True if the cell is selected.
     * @param hasFocus   True if the cell has focus.
     * @param row        The row index of the cell.
     * @param column     The column index of the cell.
     * @return The customized JButton component for rendering in the cell.
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        // Set the text of the button to the specified value
        setText(this.text);
        return this; // Return the JButton as the rendered component for the cell.
    }
}
