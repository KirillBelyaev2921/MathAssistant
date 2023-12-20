package ua.bieliaiev.kyrylo.math_assistant.view;

import ua.bieliaiev.kyrylo.math_assistant.controller.EquationsController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EquationView {

	public EquationView(EquationsController controller) {

		JFrame frame = new JFrame("Math Assistant");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		frame.add(mainPanel, BorderLayout.CENTER);

		JLabel equationLabel = new JLabel("Enter the equation:");
		JTextField equationField = new JTextField(25);
		mainPanel.add(equationLabel);
		mainPanel.add(equationField);

		JLabel rootLabel = new JLabel("Enter the root of equation:");
		JTextField rootField = new JTextField(25);
		mainPanel.add(rootLabel);
		mainPanel.add(rootField);

		JLabel equationsListLabel = new JLabel("List of equations (press to choose equation):");
		JList<String> equations = new JList<>();
		equations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		equations.addListSelectionListener(e -> equationField.setText(equations.getSelectedValue()));
		JScrollPane equationsPane = new JScrollPane(equations);
		mainPanel.add(equationsListLabel);
		mainPanel.add(equationsPane);

		JLabel rootsListLabel = new JLabel("List of equations:");
		JTextArea roots = new JTextArea(3, 25);
		JScrollPane rootsPane = new JScrollPane(roots);
		mainPanel.add(rootsListLabel);
		mainPanel.add(rootsPane);

		JPanel responsePanel = new JPanel();
		responsePanel.setLayout(new FlowLayout());
		JLabel response = new JLabel("Select action.");
		responsePanel.add(response);
		frame.add(responsePanel, BorderLayout.NORTH);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		frame.add(buttonsPanel, BorderLayout.SOUTH);

		JButton saveEquation = new JButton("Save equation");
		saveEquation.addActionListener(e ->
				response.setText(controller.saveEquation(
						equationField.getText())));
		buttonsPanel.add(saveEquation);

		JButton saveEquationWithRoot = new JButton("Save equation with root");
		saveEquationWithRoot.addActionListener(e ->
				response.setText(controller.saveEquationWithRoot(
						equationField.getText(), rootField.getText())));
		buttonsPanel.add(saveEquationWithRoot);

		JButton getAllEquations = new JButton("Get all equations");
		getAllEquations.addActionListener(e -> {
			List<String> allEquations = controller.getAllEquations();
			equations.setListData(allEquations.toArray(new String[0]));
			response.setText("Get all equations");
		});
		buttonsPanel.add(getAllEquations);

		JButton getAllEquationsByRoot = new JButton("Get all equations by root");
		getAllEquationsByRoot.addActionListener(e -> {
			List<String> allEquations = controller.getAllEquationsByRoot(rootField.getText());
			equations.setListData(allEquations.toArray(new String[0]));
			response.setText("Get all equations by root");
		});
		buttonsPanel.add(getAllEquationsByRoot);

		JButton getRootsOfEquation = new JButton("Get roots of equation");
		getRootsOfEquation.addActionListener(e -> {
			List<String> allEquations = controller.getAllRootsOfEquation(equationField.getText());
			roots.setText("");
			allEquations.forEach(s -> roots.append(s + "\n"));
			response.setText(
					allEquations.isEmpty() ? "No roots stored" : "Get roots of equation"
			);
		});
		buttonsPanel.add(getRootsOfEquation);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
