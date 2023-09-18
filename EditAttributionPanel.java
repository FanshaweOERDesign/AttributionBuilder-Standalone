/**
 * Program Name: EditAttributionPanel.java
 * Purpose: TODO
 * Coder: Jason Benoit 0885941
 * Date: Sep 17, 2023
 */

import javax.swing.*;

/**
 * 
 */
public class EditAttributionPanel extends JPanel
{

	private static final long serialVersionUID = 1L;
	JLabel pageURLLabel, pageTitleLabel, bookTitleLabel, bookUrlLabel, authorLabel, licenseTypeLabel, licenseUrlLabel;
	JTextField pageURLField, bookTitleField, bookURLField, pageTitleField, authorField, licenseTypeField, licenseURLField;
	JPanel pageURLPanel, pageURLLblPanel, bookTitleLblPanel, bookTitlePanel, pageTitleLblPanel, pageTitlePanel, bookUrlLblPanel, bookUrlPanel, authorLblPanel,
  authorPanel, licenseTypeLblPanel, licenseTypePanel, licenseUrlLblPanel, licenseUrlPanel;
	
	public EditAttributionPanel(Attribution attribution)
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		
		pageURLLabel = new JLabel("Page URL:");
		pageURLLblPanel = new JPanel();
		pageURLLblPanel.add(pageURLLabel);
		
		pageURLField = new JTextField(attribution.pageURL);
		pageURLField.setColumns(30);
		pageURLPanel = new JPanel();
		pageURLPanel.add(pageURLField);
		
		pageTitleLabel = new JLabel("Page Title:");
		pageTitleLblPanel = new JPanel();
		pageTitleLblPanel.add(pageTitleLabel);
		
		pageTitleField = new JTextField(attribution.pageTitle);
		pageTitleField.setColumns(30);
		pageTitlePanel = new JPanel();
		pageTitlePanel.add(pageTitleField);

		bookTitleLabel = new JLabel("Book Title:");
		bookTitleLblPanel = new JPanel();
		bookTitleLblPanel.add(bookTitleLabel);
		
		bookTitleField = new JTextField(attribution.bookTitle);
		bookTitleField.setColumns(30);
		bookTitlePanel = new JPanel();
		bookTitlePanel.add(bookTitleField);
		
		bookUrlLabel = new JLabel("Book URL:");
		bookUrlLblPanel = new JPanel();
		bookUrlLblPanel.add(bookUrlLabel);
		
		bookURLField = new JTextField(attribution.bookURL);
		bookURLField.setColumns(30);
		bookUrlPanel = new JPanel();
		bookUrlPanel.add(bookURLField);
		
		authorLabel = new JLabel("Author:");
		authorLblPanel = new JPanel();
		authorLblPanel.add(authorLabel);
		
		authorField = new JTextField(attribution.author);
		authorField.setColumns(30);
		authorPanel = new JPanel();
		authorPanel.add(authorField);
		
		licenseTypeLabel = new JLabel("License Type:");
		licenseTypeLblPanel = new JPanel();
		licenseTypeLblPanel.add(licenseTypeLabel);
		
		licenseTypeField = new JTextField(attribution.licenseType);
		licenseTypeField.setColumns(30);
		licenseTypePanel = new JPanel();
		licenseTypePanel.add(licenseTypeField);
		
		licenseUrlLabel = new JLabel("License URL:");
		licenseUrlLblPanel = new JPanel();
		licenseUrlLblPanel.add(licenseUrlLabel);
		
		licenseURLField = new JTextField(attribution.licenseURL);
		licenseURLField.setColumns(30);
		licenseUrlPanel = new JPanel();
		licenseUrlPanel.add(licenseURLField);
		
		add(pageURLLblPanel);
		add(pageURLPanel);
		add(pageTitleLblPanel);
		add(pageTitlePanel);
		add(bookTitleLblPanel);
		add(bookTitlePanel);
		add(bookUrlLblPanel);
		add(bookUrlPanel);
		add(authorLblPanel);
		add(authorPanel);
		add(licenseTypeLblPanel);
		add(licenseTypePanel);
		add(licenseUrlLblPanel);
		add(licenseUrlPanel);
	}	
	
	public Attribution getAttribution()
	{
		Attribution output = new Attribution();
		output.pageURL = pageURLField.getText();
		output.pageTitle = pageTitleField.getText();
		output.bookURL = bookURLField.getText();
		output.bookTitle = bookTitleField.getText();
		output.author = authorField.getText();
		output.licenseURL = licenseURLField.getText();
		output.licenseType = licenseTypeField.getText();
		
		if (!output.isComplete())
		{
			return null;
		}
		
		return output;
	}
}
//end class