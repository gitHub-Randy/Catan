package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import controller.Catan;
import model.ChatModel;

public class ChatView extends JPanel implements Observer {

	private ChatModel chatModel;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JTextField textField;
	private JScrollBar verticalScrollBar;
	private JOptionPane popup;

	public ChatView() {
		this.setPreferredSize(new Dimension(600, 100));

		chatModel = new ChatModel();
		textArea = new JTextArea();
		chatModel.addObserver(this);
		
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		scrollPane = new JScrollPane(textArea);
		textField = new JTextField();
		verticalScrollBar = scrollPane.getVerticalScrollBar();
		popup = new JOptionPane();

		scrollPane.setPreferredSize(new Dimension(350, 60));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		textField.setPreferredSize(new Dimension(375, 20));
		textField.addActionListener(e -> {
			// checking so you can't sent empty messages.
			if (!textField.getText().trim().isEmpty() && !textField.getText().startsWith("\"") && !textField.getText().endsWith("\"") ) {
				chatModel.addNewMessages(textField.getText());
				verticalScrollBar.setValue(verticalScrollBar.getMaximum());
				textField.setText("");
			} else {
				// showing error pop-up
				JOptionPane.showMessageDialog(popup, "Ongeldige Invoer", "Error", JOptionPane.ERROR_MESSAGE);
				textField.setText("");
			}
		});

		add(scrollPane);
		add(textField);
	}

	private void printMessages(String tempMessage) {
		textArea.append(tempMessage);
		verticalScrollBar.setValue(verticalScrollBar.getMaximum());
	}

	public ChatModel getChatModel() {
		return chatModel;
	}
	
	public void update(Observable arg0, Object arg1) {
		for (int i = 0; i < chatModel.getFullMessages().size(); i++) {
			String tempMessage = chatModel.getFullMessages().get(i);
			printMessages(tempMessage);
		}
	}
}
