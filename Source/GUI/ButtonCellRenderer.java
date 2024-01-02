package Source.GUI;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class ButtonCellRenderer extends JButton implements TableCellRenderer {
    private String text;

    public ButtonCellRenderer(String s) {
        this.text = s;
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        setText(this.text);
        return this;
    }
}
