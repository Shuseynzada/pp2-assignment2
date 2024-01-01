package GUI;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import UserManagement.UsersDatabase;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButton button;
    private JTable table;
    private MoviePage moviePage;
    private int action;

    public ButtonCellEditor(MoviePage moviePage, int action) {
        this.moviePage = moviePage;
        this.action = action;
        button = new JButton((action == 0) ? "+" : "-");
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
            if (action == 0) {
                moviePage.getUser().getWatchList().addToWatchList(value);
            } else if (action == 1) {
                moviePage.getUser().getWatchList().removeFromList(value);
            }
            UsersDatabase.updateFile();
            moviePage.refreshTables();
        }
    }
}
