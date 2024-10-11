/**!
Copyright (c) 2023 Jason Benoit, Stephany Ceron, and David Giesbrecht

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

**This text is from: http://opensource.org/licenses/MIT**
!**/
package com.oer.attributionbuilder;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.awt.datatransfer.StringSelection;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.*;
import org.jsoup.*;
import org.jsoup.select.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.jsoup.nodes.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class AttributionBuilder extends JFrame
{

	private static final long serialVersionUID = 1L;
	
	enum Resource {PRESSBOOKS, OPEN_STAX, YOUTUBE, OTHER};
	
	JTextField source;
	JLabel sourceLbl, attrLabel, copyStatusLbl, manualOrImageLbl;
	JTextArea attributionTxt;
	JButton buildBtn, copyBtn;
	JCheckBox autosaveChk, imageChk;
	
	boolean manualInputFieldsVisible = false;
	boolean autosave = true;
	boolean isImage = false;

	Project currentProject;
	String currentProjectPath;
	Attribution currentAttribution;
	ProjectWindow projectWindow;
	
	public AttributionBuilder()
	{
		super("Attribution Builder");
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);//destroy this object when close button is pressed
		this.setSize(500, 415); //width and height in pixels
		this.setLocationRelativeTo(null);//centers the JFrame on the screen.
		Image icon = new ImageIcon(getClass().getResource("/resources/oer_logo.jpg")).getImage();
		this.setIconImage(icon);
		this.setLayout(new BorderLayout());
		
		currentProject = null;
		currentProjectPath = null;
		currentAttribution = null;
		projectWindow = new ProjectWindow(null);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));	
		
		Listener listener = new Listener(this);
		
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel,BoxLayout.Y_AXIS));
		sourceLbl = new JLabel("Source URL:");
		JPanel srcLblPanel = new JPanel();
		srcLblPanel.add(sourceLbl);
		source = new JTextField();
		source.setColumns(30);
		
		// Select all the contents of the source input field when clicked
		// for easy replacement via copy-paste
		source.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e)
			{
				((JTextComponent) e.getSource()).selectAll();			
			}
			@Override
			public void focusLost(FocusEvent e){}			
		});
		JPanel sourcePanel = new JPanel();
		sourcePanel.add(source);
		imageChk = new JCheckBox("Image attribution");
		imageChk.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					isImage = true;
				}
				else
				{
					isImage = false;
				}
				
			}
			
		});
		buildBtn = new JButton("Build Attribution");
		JPanel bBtnPanel = new JPanel();
		bBtnPanel.setLayout(new BoxLayout(bBtnPanel, BoxLayout.Y_AXIS));
		bBtnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel bBtnSubPanel1 = new JPanel();
		JPanel bBtnSubPanel2 = new JPanel();
		buildBtn.setPreferredSize(new Dimension(160, 40));
		buildBtn.addActionListener(listener);
		
		autosaveChk = new JCheckBox("Automatically save in project:");
		autosaveChk.setSelected(true);
		autosaveChk.setVisible(false);
		autosaveChk.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.DESELECTED)
				{
					autosave = false;
				}
				else
				{
					autosave = true;
				}			
			}		
		});
		bBtnSubPanel1.add(imageChk);
		bBtnSubPanel2.add(buildBtn);
		bBtnPanel.add(bBtnSubPanel1);	
		bBtnPanel.add(bBtnSubPanel2);	
		inputPanel.add(srcLblPanel);
		inputPanel.add(sourcePanel);		
		inputPanel.add(autosaveChk);	
		inputPanel.add(bBtnPanel);

		JPanel outputPanel = new JPanel();
		outputPanel.setLayout(new BoxLayout(outputPanel,BoxLayout.Y_AXIS));
		
		attrLabel = new JLabel("Attribution");
		JPanel attrLblPanel = new JPanel();
		attrLblPanel.add(attrLabel);
		attributionTxt = new JTextArea(4, 30);
		attributionTxt.setLineWrap(true);
		attributionTxt.setEditable(false);
		JScrollPane scroll = new JScrollPane(attributionTxt);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		JPanel attributionTxtPanel = new JPanel();
		attributionTxtPanel.add(scroll);

		JPanel copyBtnPanel = new JPanel();
		JPanel copyPanel = new JPanel();
		copyPanel.setLayout(new GridLayout(2, 1));
		copyBtn = new JButton("Copy to clipboard");
		copyBtn.addActionListener(listener);
		copyBtn.setPreferredSize(new Dimension(160, 40));
		
		copyStatusLbl = new JLabel("");
		JPanel copyStatusPanel = new JPanel();
		copyStatusPanel.add(copyStatusLbl);
		copyBtnPanel.add(copyBtn);
		copyPanel.add(copyBtnPanel);
		copyPanel.add(copyStatusPanel);
		outputPanel.add(attrLblPanel);
		outputPanel.add(attributionTxtPanel);
		outputPanel.add(copyPanel);
		
		JMenuBar menuBar = new JMenuBar();
		//File menu
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveAttr = new JMenuItem("Save to Current Project");
		JMenuItem newProject = new JMenuItem("New Project...");
		JMenuItem loadProject = new JMenuItem("Load Project...");
		JMenuItem saveAs = new JMenuItem("Save Project As...");
		JMenuItem export = new JMenuItem("Export Project As Text File...");
		JMenuItem close = new JMenuItem("Close Project");
		JMenuItem exit = new JMenuItem("Exit");
		saveAttr.addActionListener(listener);
		saveAs.addActionListener(listener);
		newProject.addActionListener(listener);
		loadProject.addActionListener(listener);
		export.addActionListener(listener);
		close.addActionListener(listener);
		exit.addActionListener(listener);
		fileMenu.add(newProject);
		fileMenu.add(loadProject);
		fileMenu.add(saveAs);
		fileMenu.add(export);
		fileMenu.add(saveAttr);
		fileMenu.add(close);
		fileMenu.add(exit);
		
		//Help menu
		JMenu helpMenu = new JMenu("Help");
		JMenuItem onlineHelpItem = new JMenuItem("Online Help");
		onlineHelpItem.addActionListener(listener);
		helpMenu.add(onlineHelpItem);
		
		menuBar.setOpaque(false);
		menuBar.add(fileMenu);
		//menuBar.add(helpMenu); //TODO
		this.add(menuBar, BorderLayout.NORTH);
		
		mainPanel.add(inputPanel);
		mainPanel.add(outputPanel);
		
		tabbedPane.addTab("Attribution", mainPanel);
		tabbedPane.addTab("Project", projectWindow);
		this.add(tabbedPane);
		
		JPanel licencePanel = new JPanel();
		licencePanel.setLayout(new BoxLayout(licencePanel, BoxLayout.Y_AXIS));
		licencePanel.setPreferredSize(new Dimension(250, 200));
		licencePanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		JLabel licTitle = new JLabel("<html><h1>AttributionBuilder</h1>");
		licTitle.setHorizontalAlignment(JLabel.CENTER);
		JLabel licTxt1 = new JLabel("<html>Created by</html>");
		licTxt1.setHorizontalAlignment(JLabel.CENTER);
		JLabel licTxt2 = new JLabel("<html>Jason Benoit, Stephany Ceron, and David Giesbrecht</html>");
		licTxt2.setHorizontalAlignment(JLabel.CENTER);
		JLabel licTxt3 = new JLabel("<html>at the</html>");
		licTxt3.setHorizontalAlignment(JLabel.CENTER);
		JLabel studioLink = new JLabel("<html><a href='https://www.fanshawelibrary.com/oerdesignstudio/'>Fanshawe OER Design Studio</a>.</html>");
		studioLink.addMouseListener(new MouseAdapter(){
			
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				try {
          
          Desktop.getDesktop().browse(new URI("https://www.fanshawelibrary.com/oerdesignstudio/"));
           
      } catch (IOException | URISyntaxException e1) {
          JOptionPane.showMessageDialog( AttributionBuilder.this,
                  "Could not open the hyperlink. Error: " + e1.getMessage(),
                  "Error",
                  JOptionPane.ERROR_MESSAGE);
      }     
			}
		});
		studioLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		studioLink.setHorizontalAlignment(JLabel.CENTER);
		JLabel licTxt4 = new JLabel("<html>Made available under the</html>");
		licTxt4.setHorizontalAlignment(JLabel.CENTER);
		JLabel licenceLink = new JLabel("<html><a href=''>MIT Licence</a>.");
		licenceLink.setHorizontalAlignment(JLabel.CENTER);
licenceLink.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {

			        String licenceStr = """
Copyright (c) 2023 Jason Benoit, Stephany Ceron, David Giesbrecht, and Fanshawe OER Design Studio

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

**This text is from: http://opensource.org/licenses/MIT**
			        		""";
			        JOptionPane.showMessageDialog(e.getComponent(), licenceStr);		    
			}			
		});
		
		licenceLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		licencePanel.add(licTitle);
		//licencePanel.add(licIcon);
		licencePanel.add(licTxt1);
		licencePanel.add(Box.createRigidArea(new Dimension(0, 5)));
		licencePanel.add(licTxt2);
		licencePanel.add(Box.createRigidArea(new Dimension(0, 5)));
		licencePanel.add(licTxt3);
		licencePanel.add(Box.createRigidArea(new Dimension(0, 5)));
		licencePanel.add(studioLink);
		licencePanel.add(Box.createRigidArea(new Dimension(0, 5)));
		licencePanel.add(licTxt4);
		licencePanel.add(Box.createRigidArea(new Dimension(0, 5)));
		licencePanel.add(licenceLink);
		
		this.setVisible(true);
		
		JOptionPane.showMessageDialog(this.getContentPane(), licencePanel, "Welcome", JOptionPane.PLAIN_MESSAGE, null);
	}

	private class Listener implements ActionListener
	{
		AttributionBuilder frame;
		
		public Listener(AttributionBuilder frame)
		{
			this.frame = frame;
		}

		@Override
		public void actionPerformed(ActionEvent ev)
		{
			if (ev.getSource() == buildBtn)
			{
				copyStatusLbl.setText("");
				String url = source.getText();
				
				if (isImage)
				{
					currentAttribution = new ImageAttribution();
					ImageAttribution ia = (ImageAttribution)currentAttribution;
					ia.imageURL = url;
					buildManualAttribution(ia);
					
					if (currentProject != null && autosave)
					{
						saveAttributionToCurrentProject();
						
						if (projectWindow != null)
						{
							projectWindow.updateAttributionTable(currentProject);
						}
					}
					
					return;
				}
				
				// Need to use Selenium to scrape YouTube metadata
				// Its much slower than JSoup, so we'll keep JSoup for book attributions
				if (isYouTube(url))
				{
					currentAttribution = new YouTubeAttribution();
					YouTubeAttribution ya = (YouTubeAttribution)currentAttribution;
					ya.url = url;
					buildYouTubeAttribution(url, ya);
					attributionTxt.setText(currentAttribution.toString());
					return;
				}
				
				try
				{
					Connection connection = Jsoup.connect(url)
							.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
              .header("Accept-Encoding", "gzip, deflate, sdch")
              .header("Accept-Language", "en-US,en;q=0.8")
							.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
							.timeout(10000);
					Connection.Response response = null;			
					response = connection.execute();
					int statusCode = response.statusCode();
					if (statusCode == 200)
					{					
						Document doc = connection.get();
						Resource type = setType(doc, url);
						
						currentAttribution = new BookAttribution();
						
						if (type == Resource.PRESSBOOKS || isPressbooks(doc))
						{	
							BookAttribution ba = (BookAttribution)currentAttribution;
							ba.pageURL = url;
							buildPressbooksAttribution(doc, ba);
						}
						else if (type == Resource.OPEN_STAX)
						{
							BookAttribution ba = (BookAttribution)currentAttribution;
							ba.pageURL = url;
							buildOpenStaxAttribution(doc, ba);
						}
						else
						{
							BookAttribution ba = (BookAttribution)currentAttribution;
							ba.pageURL = url;
							buildManualAttribution(ba);
						}	
						
						if (currentProject != null && autosave)
						{
							saveAttributionToCurrentProject();
							
							if (projectWindow != null)
							{
								projectWindow.updateAttributionTable(currentProject);
							}
						}
						
					}
					else
					{
						BookAttribution attribution = new BookAttribution();
						attribution.pageURL = url;
						buildManualAttribution(attribution);
					}
				}
				catch (UnknownHostException | IllegalArgumentException ex)
				{					
					JOptionPane.showMessageDialog(frame, "Invalid URL");
				}
				catch(Exception ex)
				{
					currentAttribution = new BookAttribution();
					BookAttribution ba = (BookAttribution)currentAttribution;
					ba.pageURL = url;
					buildManualAttribution(ba);
					if (currentProject != null && autosave)
					{
						saveAttributionToCurrentProject();
						
						if (projectWindow != null)
						{
							projectWindow.updateAttributionTable(currentProject);
						}
					}
					
					ex.printStackTrace();				
				}
			}
			else if (ev.getSource() == copyBtn)
			{
				Toolkit.getDefaultToolkit().getSystemClipboard()
					.setContents(new StringSelection(attributionTxt.getText()), null);
				copyStatusLbl.setText("Copied!");				
			}
			else if (ev.getActionCommand().equals("Load Project..."))
			{
				try
				{
					File currentDir = new File("projects");
					JFileChooser fileChooser = new JFileChooser(currentDir);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("AttributionBuilder Project Files",
			        "abp");
					fileChooser.setFileFilter(filter);
					if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
					{
						File f = fileChooser.getSelectedFile();
						
						if (currentProject != null)
						{
							currentProject.save();
						}
											
						currentProject = new Project(f.toString());
	
						autosaveChk.setText(autosaveChk.getText().substring(0, autosaveChk.getText().indexOf(':') + 1) 
								+ " " + currentProject.getName());
						autosaveChk.setVisible(true);
						if (projectWindow != null)
						{
							projectWindow.updateAttributionTable(currentProject);
						}
					}
				}
				catch (HeadlessException ex)
				{
					ex.printStackTrace();
				}				
			}
			else if (ev.getActionCommand().equals("Save to Current Project"))
			{				
				saveAttributionToCurrentProject();
				if (projectWindow != null)
				{
					projectWindow.updateAttributionTable(currentProject);
				}
			}
			else if (ev.getActionCommand().equals("New Project..."))
			{
				createNewProject();
				if (currentProject != null)
				{
					autosaveChk.setText(autosaveChk.getText().substring(0, autosaveChk.getText().indexOf(':') + 1) +
							" " + currentProject.getName());
					autosaveChk.setVisible(true);
				}
					
				if (projectWindow != null)
				{
					projectWindow.updateAttributionTable(currentProject);
				}
			}
			else if (ev.getActionCommand().equals("Save Project As..."))
			{
				if (currentProject == null)
				{
					JOptionPane.showMessageDialog(frame, "No project loaded");
					return;
				}
				createNewProject();
				if (currentProject != null)
				{
					autosaveChk.setText(autosaveChk.getText().substring(0, autosaveChk.getText().indexOf(':') + 1) +
							" " + currentProject.getName());
					autosaveChk.setVisible(true);
				}
					
				if (projectWindow != null)
				{
					projectWindow.updateAttributionTable(currentProject);
				}
			}
			else if (ev.getActionCommand().equals("Close Project"))
			{
				if (currentProject != null)
				{
					currentProject.save();
					currentProject = null;
					autosaveChk.setVisible(false);
					projectWindow.updateAttributionTable(currentProject);
				}
			}
			else if (ev.getActionCommand().equals("Export Project As Text File..."))
			{
				exportProjectToText();
			}
			else if (ev.getActionCommand().equals("Exit"))
			{
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		}
		
		private Resource setType(Document doc, String url)
		{
			Resource type = Resource.OTHER;
			
			if (url.contains("openstax"))
			{
				type = Resource.OPEN_STAX;
			}
			else if (url.contains("pressbooks") || isPressbooks(doc))
			{
				type = Resource.PRESSBOOKS;
			}
			
			return type;
		}
		
		private boolean isPressbooks(Document doc)
		{
			Elements elements = doc.getAllElements();
			
			for (Element el : elements)
			{
				if (el.className().contains("pressbooks"))
				{
					return true;
				}
			}
			
			return false;		
		}
		
		private boolean isYouTube(String url)
		{
			Pattern pattern = Pattern.compile("^.*(youtu.be\\/|v\\/|u\\/\\w\\/|embed\\/|watch\\?v=|\\&v=|watch\\?.+&v=)([^#\\&\\?]*).*");
			Matcher m = pattern.matcher(url);
			
			return m.find();
		}
		
		private boolean saveAttributionToCurrentProject()
		{
			if (currentAttribution == null || currentAttribution.toString() == null)
			{
				JOptionPane.showMessageDialog(frame, "Please build a valid attribution");
				return false;
			}
			if (currentProject == null)
			{
				JOptionPane.showMessageDialog(frame, "No currently active project");
				return false;
			}
			
			String tag = JOptionPane.showInputDialog("Please enter a tag name to identify this attribution");
			
			int res = JOptionPane.NO_OPTION;
			
			if (currentProject.getAttribution(tag) != null)
			{
				res = JOptionPane.showConfirmDialog(frame, "The tag '" + tag +"' already exists in the current project. Replace?");
				
				if (res == JOptionPane.CANCEL_OPTION)
				{
					return false;
				}
				else if (res == JOptionPane.NO_OPTION)
				{
					saveAttributionToCurrentProject();
				}
				else
				{
					currentProject.updateAttribution(tag, currentAttribution);
					currentProject.save();
					return true;
				}
			}
			
			currentProject.addAttribution(tag, currentAttribution);
			currentProject.save();
			return true;
		}
		
		private void buildPressbooksAttribution(Document doc, BookAttribution attribution)
		{
			Elements metaTags = doc.getElementsByTag("meta");
			for (Element el : metaTags) {
				
				if (el.attr("name").equals("citation_title")) {
					attribution.pageTitle = el.attr("content");
				}
				else if (el.attr("name").equals("citation_book_title")) {
					attribution.bookTitle = el.attr("content");
				}
				else if (el.attr("name").equals("citation_author")) {
					attribution.author = el.attr("content");
				}
			}
			
			if (attribution.pageURL.contains("chapter")) {
				attribution.bookURL = attribution.pageURL.substring(0, attribution.pageURL.lastIndexOf("/chapter")) + "/";
			}
			else
			{
				attribution.bookURL = attribution.pageURL;
			}
			
			
			Elements anchorTags = doc.getElementsByTag("a");

			
			for (Element el : anchorTags)
			{
				if (el.attr("rel").equals("cc:attributionURL"))
				{
					attribution.bookURL = el.attr("href");
					attribution.bookTitle = el.html();

				}
				else if (el.attr("rel").equals("license"))
				{
					attribution.setLicense(el.html());

				}					
			}
		
			if (attribution.pageTitle == null || attribution.pageTitle.isEmpty()) {
			Elements titleEl = doc.getElementsByTag("title");
			String pageTitle = titleEl.get(0).html();
			attribution.pageTitle = pageTitle.substring(0, pageTitle.indexOf(" – "));
			}
			
			if (attribution.author == null || attribution.author.isEmpty()) {
				Elements propertyAttrs = doc.getElementsByAttribute("property");
				
				for (Element el : propertyAttrs)
				{
					if (el.attr("property").equals("cc:attributionName"))
					{
						attribution.author = el.html();
					}
						
				}
			}
			
			if (attribution.licenseKey == null || attribution.licenseKey.isEmpty()) {
				attribution.setLicense("Other");
				attribution.hasCustomLicense = true;
				
			}
			
			if (attribution.licenseKey.equals("Other"))
			{
				buildManualAttribution(attribution);
			}
			
			attributionTxt.setText(attribution.toString());
    
		}
		
		private void buildOpenStaxAttribution(Document doc, BookAttribution attribution)
		{
			Element pageTitleEl = doc.selectFirst("[class^=\"BookBanner__BookChapter\"]");
			attribution.pageTitle = pageTitleEl.text();
				
			Element bookTitleEl = doc.selectFirst("[class^=\"BookBanner__BookTitleLink\"]");
			attribution.bookTitle = bookTitleEl.text();
			attribution.bookURL = "https://openstax.org" + bookTitleEl.attr("href");
			
			attribution.author = "<a href=\'https://openstax.org/\'>OpenStax - Rice University</a>";
			
			Element licenseEl = doc.selectFirst("[data-html=\"copyright\"] > a");
			attribution.setLicense(licenseEl.text());
			
			if (attribution.licenseKey.equals("Other"))
			{
				buildManualAttribution(attribution);
			}
			
			attributionTxt.setText(attribution.toString());

		}
		
		private void buildYouTubeAttribution(String url, YouTubeAttribution attribution)
		{
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless");
			WebDriver driver = new ChromeDriver(options);
			
			driver.get(url);
			
			attribution.title = driver.findElement(By.cssSelector("meta[name='title']")).getAttribute("content");
			
			
			attribution.channelTitle = driver.findElement(By.cssSelector("link[itemprop='name']")).getAttribute("content");
			attribution.channelURL = driver.findElement(By.cssSelector("span[itemprop='author'] link[itemprop='url']")).getAttribute("href");
			String durationMeta = driver.findElement(By.cssSelector("meta[itemprop='duration']")).getAttribute("content");
			System.out.println(durationMeta);
			
			attribution.duration = formatDuration(durationMeta);
		}
		
		private String formatDuration(String duration)
		{
			Pattern pattern = Pattern.compile("PT(\\d+)M(\\d+)S");
			Matcher m = pattern.matcher(duration);
			
			if (m.find())
			{
				String minutes = (m.group(1) != null) ? m.group(1).replace("M", "") : "0";
				String seconds = (m.group(2) != null) ? m.group(2).replace("S", "") : "00";
				return minutes + ":" + seconds;
			}
			
			
			return null;
			
		}
		
		private void buildManualAttribution(Attribution attribution)
		{
				int res = JOptionPane.OK_OPTION;
				boolean isComplete = false;
				
				while (!isComplete && res == JOptionPane.OK_OPTION)		
				{
					EditAttributionPanel editPanel = attribution.buildEditPanel();
				
					res = JOptionPane.showConfirmDialog(frame, editPanel, 
							"Edit Attribution", 
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if (res == JOptionPane.OK_OPTION)
					{
						String[] incompleteFields = editPanel.grabToAttribution();
						
						if (incompleteFields.length == 0)
						{
							boolean licenseKeyIsOther = attribution.licenseKey != null && attribution.licenseKey.equals("Other");
							boolean licenseTypeIsEmpty = attribution.getLicense() == null || attribution.getLicense().length() == 0;
							if (licenseKeyIsOther && licenseTypeIsEmpty)
							{
								JPanel pnl = new JPanel();
								JLabel lbl = new JLabel("Please enter additional license details");
								pnl.add(lbl);
								JOptionPane.showMessageDialog(frame, pnl, "Additional Info Required", JOptionPane.PLAIN_MESSAGE);
								isComplete = false;
								continue;
							}
							attributionTxt.setText(attribution.toString());
							isComplete = true;
						}
						else
						{
							String msg = "Please complete the following fields:\n";
							for (String f : incompleteFields)
							{
								msg += "\n- " + f;
							}
							
							JOptionPane.showMessageDialog(frame, msg, "Info Missing", JOptionPane.PLAIN_MESSAGE);
							isComplete = false;
						}
					}
				}
		}		
	}
	
	private void exportProjectToText()
	{
		if (currentProject == null)
		{
			JOptionPane.showMessageDialog(this, "No project loaded");
			return;
		}
		
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files",
        "txt");
		chooser.setFileFilter(filter);
		int retVal = chooser.showSaveDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION)
		{
			File path = chooser.getSelectedFile();
			String pathStr = path.toString();
			int extensionIdx = pathStr.lastIndexOf('.');
			if (extensionIdx < 0 || !pathStr.substring(extensionIdx).equals(".txt"))
			{
				pathStr += ".txt";
				path = new File(pathStr);
			}
			TreeMap<String, Attribution> attributions = currentProject.getAttributions();
			try
			{
				FileWriter fw = new FileWriter(path);
				PrintWriter pw = new PrintWriter(fw);
				
				pw.println(currentProject.getName());
				for (int i = 0; i < currentProject.getName().length(); i++)
				{
					pw.print("=");
				}
				pw.println();
				pw.println();
				
				for (Entry<String, Attribution> entry : attributions.entrySet())
				{
					String keyStr = entry.getKey();
					pw.println(keyStr);
					for (int i = 0; i < keyStr.length(); i++)
					{
						pw.print("-");
					}
					pw.println();
					pw.println(entry.getValue().toString());
					pw.println();
				}
				
				pw.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}			
		}
	}
	
	private void createNewProject()
	{
		if (currentProject != null)
		{
			currentProject.save();
		}
		
		File dir = new File("./projects");
		dir.mkdir();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("AttributionBuilder Project Files",
        "abp");
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(dir);
		int retVal = chooser.showSaveDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION)
		{
			File path = chooser.getSelectedFile();
			String pathStr = path.toString();
			int extensionIdx = pathStr.lastIndexOf('.');
			if (extensionIdx < 0 || !pathStr.substring(extensionIdx).equals(".abp"))
			{
				pathStr += ".abp";
				path = new File(pathStr);
			}
			try
			{
				path.createNewFile();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
			
			currentProject = new Project(path.toString());
			currentProject.save();
		}
	}

	public static void main(String[] args)
	{
		
			new AttributionBuilder();
			
	}
}
//end class
