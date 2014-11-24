package gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import startup.RunProgramm;

public class GuiPathChoice extends JFrame {

	/**
	 * Panel Variables
	 */
	private JPanel contentPane;
	private GuiPathChoice pathchoice;
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private final Action ok = new SwingAction();
	private final Action cancel = new SwingAction_1();
	private RunProgramm runprogramm;
	private JButton okButton;
	private JButton cancelButton;

	/**
	 * Create the dialog.
	 * @param runprogramm
	 */
	public GuiPathChoice(RunProgramm runprogramm) {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 100);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.pathchoice = this;
		this.runprogramm = runprogramm;
		
		textField = new JTextField();
		textField.setColumns(35);
		textField.setBounds(10, 10, 414, 25);
		getContentPane().add(textField);
		
		okButton = new JButton("OK");
		okButton.setBounds(335, 37, 89, 23);
		getContentPane().add(okButton);
		okButton.setAction(ok);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(236, 37, 89, 23);
		cancelButton.setAction(cancel);
		contentPane.add(cancelButton);
	}

	private class SwingAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public SwingAction() {
			putValue(NAME, "OK");
			putValue(SHORT_DESCRIPTION, "");// TODO
		}

		public void actionPerformed(ActionEvent e) {
			runprogramm.setPath(textField.getText());
			runprogramm.run();//start();
			pathchoice.dispose();
		}
	}

	private class SwingAction_1 extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public SwingAction_1() {
			putValue(NAME, "CANCEL");
			putValue(SHORT_DESCRIPTION, "");// TODO
		}

		public void actionPerformed(ActionEvent e) {
			runprogramm.run();//start();
			pathchoice.dispose();
		}
	}
}
