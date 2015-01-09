package gui;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import startup.Controller;

public class GuiModlist extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Controller runProgramm;
	private final Action action = new SwingAction();
	private final Action action_1 = new SwingAction_1();
	private JList<File> list;
	
	/**
	 * Create the frame.
	 * 
	 * @param runProgramm
	 */
	public GuiModlist(Controller runProgramm) {
		this.runProgramm = runProgramm;
		setBounds(100, 100, 600, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JButton btnNewButton = new JButton("Aktuallisire Liste");
		btnNewButton.setAction(action);

		JButton btnNewButton_1 = new JButton("Open Mod");
		btnNewButton_1.setAction(action_1);
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton)
						.addComponent(btnNewButton_1))
					.addContainerGap())
		);
		
		list = new JList<File>();
		scrollPane.setViewportView(list);
		contentPane.setLayout(gl_contentPane);
		
		
	}

	private class SwingAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction() {
			putValue(NAME, "Aktuallisire Liste");
			putValue(SHORT_DESCRIPTION, "Aktuallisire Liste");
		}
		public void actionPerformed(ActionEvent e) {
			runProgramm.actuallisire_modordner();
		}
	}
	
	private class SwingAction_1 extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction_1() {
			putValue(NAME, "Open Mod");
			putValue(SHORT_DESCRIPTION, "Open Mod");
		}
		public void actionPerformed(ActionEvent e) {
			openMod();
		}
	}
	
	private void openMod() {
		runProgramm.oeffneGuiModInfo(list.getSelectedValuesList());
	}

	public void aktuallisireListe() {
		list.removeAll();
		list.setListData((runProgramm.getModArray()));
	}
}
