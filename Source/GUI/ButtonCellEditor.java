package Source.GUI;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import Source.UserManagement.UsersDatabase;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButton button;
    private JTable table;
    private MoviePage moviePage;
    private char action;

    public ButtonCellEditor(MoviePage moviePage, char action) {
        this.moviePage = moviePage;
        this.action = action;
        button = new JButton(action+"");
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedRow = table.getEditingRow();
        if (selectedRow != -1) { // Check if a row is actually selected
            TableModel model = table.getModel();
            int value = (Integer) model.getValueAt(selectedRow, 0);
            if (action == '+') {
                moviePage.getUser().getWatchList().addToWatchList(value);
            } else if (action == '-') {
                moviePage.getUser().getWatchList().removeFromList(value);
            }
            UsersDatabase.updateFile();
            moviePage.refreshTables();
        }
    }
}
