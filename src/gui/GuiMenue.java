package gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import startup.Controller;

public class GuiMenue extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final Action action = new SwingAction();
	private final Action action_1 = new SwingAction_1();
	private Controller runprogramm;
	
	/**
	 * Create the frame.
	 */
	public GuiMenue(Controller runprogramm) {
		this.runprogramm = runprogramm;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.runprogramm = runprogramm;
		
		JButton btnOpenModlist = new JButton("Open Modlist");
		btnOpenModlist.setAction(action);
		btnOpenModlist.setBounds(10, 11, 120, 23);
		contentPane.add(btnOpenModlist);
		
		JButton btnCloseAll = new JButton("Close All");
		btnCloseAll.setAction(action_1);
		btnCloseAll.setBounds(335, 228, 89, 23);
		contentPane.add(btnCloseAll);
	}

	private class SwingAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction() {
			putValue(NAME, "OpenModlist");
			putValue(SHORT_DESCRIPTION, "OpenModlist");
		}
		public void actionPerformed(ActionEvent e) {
			runprogramm.oeffneGuiModlist();
		}
	}
	private class SwingAction_1 extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction_1() {
			putValue(NAME, "Close All");
			putValue(SHORT_DESCRIPTION, "Save And End");
		}
		public void actionPerformed(ActionEvent e) {
			runprogramm.closeAll();
		}
	}
}
