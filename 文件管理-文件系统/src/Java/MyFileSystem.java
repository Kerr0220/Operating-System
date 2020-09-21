package Java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class MyFileSystem extends JFrame {
	private JTree tree;
	private JScrollPane treePane;
	private JScrollPane tablePane;
	private tableModel model = new tableModel();
	private JTable fileTable;

	private File rootFile;
	private File readMe;

	// 系统自带的文件夹
	private Folder folder1;
	private Folder folder2;
	private Folder folder3;
	private Folder folder4;
	private Folder folder5;
	private Folder folder6;
	private Folder folder7;
	private Folder folder8;
	private Folder folder9;
	private ArrayList<Folder> folders = new ArrayList<Folder>();

	// 信息条
	private JLabel blockName = new JLabel("文件夹名称:");
	private JLabel nameField = new JLabel();
	private JLabel haveUsed = new JLabel("已使用空间:");
	private JLabel usedField = new JLabel();
	private JLabel freeYet = new JLabel("剩余空间:");
	private JLabel freeField = new JLabel();
	private JLabel fileNum = new JLabel("文件数:");
	private JLabel fileNumField = new JLabel();

	private JTextField searchLine = new JTextField();

	// 删除文件夹
	public static void deleteDirectory(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			file.delete();
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File myfile : files) {
				deleteDirectory(filePath + File.separator + myfile.getName());
			}
			file.delete();
		}
	}

	// 获取空闲空间
	public double getSpace(File file) {
		double space = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			reader.readLine();
			space = Double.parseDouble(reader.readLine());
			if (space > 1024) {
				space = 0.0;
			}
			reader.close();
		} catch (Exception e) {
		}
		;
		return space;
	}

	// 更新文件夹信息
	public void upDateBlock(Folder currentBlock) {
		fileNumField.setText(String.valueOf(currentBlock.getFileNum()));
		usedField.setText(String.valueOf(currentBlock.getSpace()) + " KB");
		freeField.setText(String.valueOf(1024 - currentBlock.getSpace()) + "KB");
	}

	// 查找文件
	public boolean searchFile(String fileName, File parent) {
		File[] files = parent.listFiles();
		for (File myFile : files) {
			if (myFile.getName().equals(fileName)) {
				try {
					if (Desktop.isDesktopSupported()) {
						Desktop desktop = Desktop.getDesktop();
						desktop.open(myFile);
						return true;
					}
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, myFile.getPath() + " 抱歉，出现了一些错误！", "Fail to open",
							JOptionPane.ERROR_MESSAGE);
					return true;
				}
			}
			if (myFile.isDirectory() && myFile.canRead()) {
				if (searchFile(fileName, myFile)) {
					return true;
				}
			}
		}
		return false;
	}

	// 用户交互界面
	public MyFileSystem() throws IOException {
		setTitle("OS-myFileSystem");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon(this.getClass().getResource("/image/folder.png")).getImage());
		getContentPane().setBackground(Color.WHITE);

		ImageIcon folderImg = new ImageIcon(this.getClass().getResource("/image/folder.png"));
		folderImg.setImage(folderImg.getImage().getScaledInstance(30, 23, Image.SCALE_DEFAULT));

		ImageIcon docImg = new ImageIcon(this.getClass().getResource("/image/document.png"));
		docImg.setImage(docImg.getImage().getScaledInstance(24, 30, Image.SCALE_DEFAULT));

		Icon icon1 = folderImg;
		Icon icon2 = docImg;
		UIManager.put("Tree.openIcon", icon1);
		UIManager.put("Tree.closedIcon", icon1);
		UIManager.put("Tree.leafIcon", icon2);

		// 创建工作区——如果已有的话就直接用了
		rootFile = new File("myFileSystem");
		readMe = new File("myFileSystem" + File.separator + "ReadMe.txt");

		boolean flag = true;

		// 文件树初始化
		final DefaultMutableTreeNode root = new DefaultMutableTreeNode(new myFiles(rootFile, 0, 10240));
		if (!rootFile.exists()) {
			flag = false;
			try {
				rootFile.mkdir();
				readMe.createNewFile();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "空闲空间不足!", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			FileWriter writer = new FileWriter(readMe.getPath());
			writer.write("Hello, this my file system!!!\n");
			writer.write("Space: 10 * 1024K = 10M\n");
			writer.write("Free-Space Management:bitmap\n");
			writer.write("Store-Space Management:FAT\n");
			writer.flush();
			writer.close();
		}

		folder1 = new Folder(1, new File(rootFile.getPath() + File.separator + "1"), flag);
		folders.add(folder1);
		folder2 = new Folder(2, new File(rootFile.getPath() + File.separator + "2"), flag);
		folders.add(folder2);
		folder3 = new Folder(3, new File(rootFile.getPath() + File.separator + "3"), flag);
		folders.add(folder3);
		folder4 = new Folder(4, new File(rootFile.getPath() + File.separator + "4"), flag);
		folders.add(folder4);
		folder5 = new Folder(5, new File(rootFile.getPath() + File.separator + "5"), flag);
		folders.add(folder5);
		folder6 = new Folder(6, new File(rootFile.getPath() + File.separator + "6"), flag);
		folders.add(folder6);
		folder7 = new Folder(7, new File(rootFile.getPath() + File.separator + "7"), flag);
		folders.add(folder7);
		folder8 = new Folder(8, new File(rootFile.getPath() + File.separator + "8"), flag);
		folders.add(folder8);
		folder9 = new Folder(9, new File(rootFile.getPath() + File.separator + "9"), flag);
		folders.add(folder9);

		root.add(new DefaultMutableTreeNode(new myFiles(folder1.getBlockFile(), 1, 1024.0)));
		model.addRow(new myFiles(folder1.getBlockFile(), 1, 1024.0));
		((DefaultMutableTreeNode) root.getChildAt(0)).add(new DefaultMutableTreeNode("temp"));

		root.add(new DefaultMutableTreeNode(new myFiles(folder2.getBlockFile(), 2, 1024.0)));
		model.addRow(new myFiles(folder2.getBlockFile(), 2, 1024.0));
		((DefaultMutableTreeNode) root.getChildAt(1)).add(new DefaultMutableTreeNode("temp"));

		root.add(new DefaultMutableTreeNode(new myFiles(folder3.getBlockFile(), 3, 1024.0)));
		model.addRow(new myFiles(folder3.getBlockFile(), 3, 1024.0));
		((DefaultMutableTreeNode) root.getChildAt(2)).add(new DefaultMutableTreeNode("temp"));

		root.add(new DefaultMutableTreeNode(new myFiles(folder4.getBlockFile(), 4, 1024.0)));
		model.addRow(new myFiles(folder4.getBlockFile(), 4, 1024.0));
		((DefaultMutableTreeNode) root.getChildAt(3)).add(new DefaultMutableTreeNode("temp"));

		root.add(new DefaultMutableTreeNode(new myFiles(folder5.getBlockFile(), 5, 1024.0)));
		model.addRow(new myFiles(folder5.getBlockFile(), 5, 1024.0));
		((DefaultMutableTreeNode) root.getChildAt(4)).add(new DefaultMutableTreeNode("temp"));

		root.add(new DefaultMutableTreeNode(new myFiles(folder6.getBlockFile(), 6, 1024.0)));
		model.addRow(new myFiles(folder6.getBlockFile(), 6, 1024.0));
		((DefaultMutableTreeNode) root.getChildAt(5)).add(new DefaultMutableTreeNode("temp"));

		root.add(new DefaultMutableTreeNode(new myFiles(folder7.getBlockFile(), 7, 1024.0)));
		model.addRow(new myFiles(folder7.getBlockFile(), 7, 1024.0));
		((DefaultMutableTreeNode) root.getChildAt(6)).add(new DefaultMutableTreeNode("temp"));

		root.add(new DefaultMutableTreeNode(new myFiles(folder8.getBlockFile(), 8, 1024.0)));
		model.addRow(new myFiles(folder8.getBlockFile(), 8, 1024.0));
		((DefaultMutableTreeNode) root.getChildAt(7)).add(new DefaultMutableTreeNode("temp"));

		root.add(new DefaultMutableTreeNode(new myFiles(folder9.getBlockFile(), 9, 1024.0)));
		model.addRow(new myFiles(folder9.getBlockFile(), 9, 1024.0));
		((DefaultMutableTreeNode) root.getChildAt(8)).add(new DefaultMutableTreeNode("temp"));

		root.add(new DefaultMutableTreeNode(new myFiles(readMe, 0, 0)));
		model.addRow(new myFiles(readMe, 0, 0));

		// 文件表初始化
		fileTable = new JTable(model);
		fileTable.getTableHeader().setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE, 24));
		fileTable.setSelectionBackground(Color.ORANGE);
		fileTable.setShowHorizontalLines(true);
		fileTable.setShowVerticalLines(false);
		fileTable.getTableHeader().setFont(new Font("宋体", Font.CENTER_BASELINE, 16));
		fileTable.getTableHeader().setForeground(Color.WHITE);
		fileTable.getTableHeader().setBackground(new Color(79, 155, 250));
		fileTable.setRowHeight(30);
		fileTable.setBackground(new Color(165, 202, 241));
		fileTable.setForeground(new Color(69, 115, 160));
		fileTable.setSelectionBackground(new Color(101, 163, 240));
		fileTable.setSelectionForeground(Color.WHITE);
		fileTable.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE, 14));
		fileTable.updateUI();

		fileTable.updateUI();

		final DefaultTreeModel treeModel = new DefaultTreeModel(root);
		tree = new JTree(treeModel);
		tree.setEditable(false);
		tree.putClientProperty("Jtree.lineStyle", "Horizontal");
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode parent = null;
				TreePath parentPath = e.getPath();
				if (parentPath == null) {
					parent = root;
				} else {
					parent = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
				}
				int blokName = ((myFiles) parent.getUserObject()).getBlockName();
				Folder currentBlock = folders.get(blokName - 1);
				if (parentPath == null) {
					parent = root;
				} else {
					parent = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
				}

				nameField.setText(String.valueOf(blokName));
				upDateBlock(currentBlock);

				model.removeRows(0, model.getRowCount());
				File rootFile = new File(((myFiles) parent.getUserObject()).getFilePath());
				if (parent.getChildCount() > 0) {
					File[] childFiles = rootFile.listFiles();

					for (File file : childFiles) {
						model.addRow(new myFiles(file, blokName, getSpace(file)));
					}
				} else {
					model.addRow(new myFiles(rootFile, blokName, getSpace(rootFile)));
				}
				fileTable.updateUI();

			}
		});
		tree.addTreeWillExpandListener(new TreeWillExpandListener() {
			@Override
			public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
				DefaultMutableTreeNode parent = null;
				TreePath parentPath = event.getPath();
				if (parentPath == null) {
					parent = root;
				} else {
					parent = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
				}

				int blokName = ((myFiles) parent.getUserObject()).getBlockName();

				File rootFile = new File(((myFiles) parent.getUserObject()).getFilePath());
				File[] childFiles = rootFile.listFiles();

				model.removeRows(0, model.getRowCount());
				for (File myFile : childFiles) {
					DefaultMutableTreeNode node = null;
					node = new DefaultMutableTreeNode(new myFiles(myFile, blokName, getSpace(myFile)));
					if (myFile.isDirectory() && myFile.canRead()) {
						node.add(new DefaultMutableTreeNode("temp"));
					}

					treeModel.insertNodeInto(node, parent, parent.getChildCount());
					model.addRow(new myFiles(myFile, blokName, getSpace(myFile)));
				}
				if (parent.getChildAt(0).toString().equals("temp") && parent.getChildCount() != 1)
					treeModel.removeNodeFromParent((MutableTreeNode) parent.getChildAt(0));
				fileTable.updateUI();
			}

			@Override
			public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
				DefaultMutableTreeNode parent = null;
				TreePath parentPath = event.getPath();
				if (parentPath == null) {
					parent = root;
				} else {
					parent = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
				}
				if (parent.getChildCount() > 0) {
					int count = parent.getChildCount();
					for (int i = count - 1; i >= 0; i--) {
						treeModel.removeNodeFromParent((MutableTreeNode) parent.getChildAt(i));
					}
					treeModel.insertNodeInto(new DefaultMutableTreeNode("temp"), parent, parent.getChildCount());
				}
				model.removeRows(0, model.getRowCount());
				fileTable.updateUI();
			}
		});
		treePane = new JScrollPane(tree);
		treePane.setPreferredSize(new Dimension(150, 400));
		add(treePane, BorderLayout.WEST);

		tablePane = new JScrollPane(fileTable);
		add(tablePane, BorderLayout.CENTER);

		// 双击打开文件
		fileTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
					String fileName = ((String) model.getValueAt(fileTable.getSelectedRow(), 0));
					String filePath = ((String) model.getValueAt(fileTable.getSelectedRow(), 1));
					try {
						if (Desktop.isDesktopSupported()) {
							Desktop desktop = Desktop.getDesktop();
							desktop.open(new File(filePath));
						}
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "抱歉，出了一些错误！", "Fail to open", JOptionPane.ERROR_MESSAGE);
					}
					JOptionPane.showMessageDialog(null, "File Name: " + fileName + "\n File Path: " + filePath,
							"content", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		// 菜单初始化
		final JPopupMenu myMenu = new JPopupMenu();
		myMenu.setPreferredSize(new Dimension(300, 200));

		// 新建文件
		JMenuItem createFileItem = new JMenuItem("新建文件");
		createFileItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				myFiles temp = (myFiles) node.getUserObject();
				int blokName = temp.getBlockName();
				Folder currentBlock = folders.get(blokName - 1);

				String inputValue;
				double capacity;

				JOptionPane inputPane = new JOptionPane();
				inputPane.setPreferredSize(new Dimension(600, 600));
				inputPane.setInputValue(JOptionPane.showInputDialog("文件名："));
				if (inputPane.getInputValue() == null) {
					return;
				}
				inputValue = inputPane.getInputValue().toString();
				inputPane.setInputValue(JOptionPane.showInputDialog("文件大小(KB):"));
				if (inputPane.getInputValue() == null) {
					return;
				}
				capacity = Double.parseDouble(inputPane.getInputValue().toString());

				File newFile = new File(temp.getFilePath() + File.separator + inputValue + ".txt");
				if (!newFile.exists() && !inputValue.equals(null)) {
					try {
						if (currentBlock.createFile(newFile, capacity)) {
							DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
									new myFiles(newFile, blokName, capacity));
							model.removeRows(0, model.getRowCount());
							model.addRow(new myFiles(newFile, blokName, capacity));
							fileTable.updateUI();
							upDateBlock(currentBlock);
							JOptionPane.showMessageDialog(null, "创建成功！请刷新文件夹！", "Success", JOptionPane.DEFAULT_OPTION);
						}
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "创建失败!!!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		myMenu.add(createFileItem);

		// 新建文件夹
		JMenuItem createDirItem = new JMenuItem("新建文件夹");
		createDirItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				myFiles temp = (myFiles) node.getUserObject();
				int blokName = temp.getBlockName();
				Folder currentBlock = folders.get(blokName - 1);
				String inputValue = JOptionPane.showInputDialog("文件夹名称:");
				if (inputValue == null) {
					return;
				}
				File newDir = new File(temp.getFilePath() + File.separator + inputValue);
				if (newDir.exists())
					deleteDirectory(newDir.getPath());
				try {
					newDir.mkdir();
					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new myFiles(newDir, blokName, 0));
					newNode.add(new DefaultMutableTreeNode("temp"));
					model.removeRows(0, model.getRowCount());
					model.addRow(new myFiles(newDir, blokName, 0));
					fileTable.updateUI();
					upDateBlock(currentBlock);
					JOptionPane.showMessageDialog(null, "创建成功，请刷新文件夹！", "Success", JOptionPane.DEFAULT_OPTION);
				} catch (Exception E) {
					JOptionPane.showMessageDialog(null, "创建失败!!!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		myMenu.add(createDirItem);

		// 删除文件/文件夹
		JMenuItem deleteItem = new JMenuItem("删除");
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				myFiles temp = (myFiles) node.getUserObject();
				int blokName = temp.getBlockName();
				Folder currentBlock = folders.get(blokName - 1);
				int choose = JOptionPane.showConfirmDialog(null, "确定删除？", "confirm", JOptionPane.YES_NO_OPTION);
				if (choose == 0) {
					if (currentBlock.deleteFile(temp.getMyFile(), temp.getSpace())) {
						try {
							currentBlock.rewriteBitMap();
							currentBlock.rewriteRecoverWriter();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						upDateBlock(currentBlock);
						JOptionPane.showMessageDialog(null, "删除成功，请刷新文件夹！", "Success", JOptionPane.DEFAULT_OPTION);
					} else {
						JOptionPane.showMessageDialog(null, "删除失败!!!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		myMenu.add(deleteItem);

		// 格式化
		JMenuItem formatItem = new JMenuItem("格式化");
		formatItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				myFiles temp = (myFiles) node.getUserObject();
				int blokName = temp.getBlockName();
				Folder currentBlock = folders.get(blokName - 1);
				int choose = JOptionPane.showConfirmDialog(null, "确定格式化文件夹吗？", "confirm", JOptionPane.YES_NO_OPTION);
				if (choose == 0) {
					try {
						if (temp.getMyFile().isDirectory()) {
							for (File myfile : temp.getMyFile().listFiles()) {
								currentBlock.deleteFile(myfile, getSpace(myfile));
							}
							upDateBlock(currentBlock);
							JOptionPane.showMessageDialog(null, "格式化成功，请刷新文件夹！", "Success", JOptionPane.DEFAULT_OPTION);
							currentBlock.rewriteBitMap();
						}
					} catch (Exception E1) {
						JOptionPane.showMessageDialog(null, "格式化失败!!!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		myMenu.add(formatItem);

		// 重命名
		JMenuItem renameItem = new JMenuItem("重命名");
		renameItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				myFiles temp = (myFiles) node.getUserObject();
				int blokName = temp.getBlockName();
				Folder currentBlock = folders.get(blokName - 1);

				String inputValue = null;
				JOptionPane inputPane = new JOptionPane();
				inputPane.setInputValue(JOptionPane.showInputDialog("新的文件名:"));
				if (inputPane.getInputValue() == null) {
					return;
				}
				inputValue = inputPane.getInputValue().toString();
				try {
					currentBlock.renameFile(temp.getMyFile(), inputValue, temp.getSpace());
					JOptionPane.showMessageDialog(null, "重命名成功，请刷新文件夹", "Success", JOptionPane.DEFAULT_OPTION);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "重命名失败!!!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		myMenu.add(renameItem);

		// 文件夹信息
		JPanel panel = new JPanel();
		panel.setBackground(new Color(79, 155, 250));
		panel.setForeground(Color.WHITE);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.add(blockName);
		nameField.setForeground(Color.WHITE);
		panel.add(nameField);
		panel.add(new JLabel("  "));
		panel.add(haveUsed);
		usedField.setForeground(Color.WHITE);
		panel.add(usedField);
		panel.add(new JLabel("  "));
		panel.add(freeYet);
		freeField.setForeground(Color.WHITE);
		panel.add(freeField);
		panel.add(new JLabel("  "));
		panel.add(fileNum);
		fileNumField.setForeground(Color.WHITE);
		panel.add(fileNumField);
		add(panel, BorderLayout.SOUTH);

		// 搜索条初始化
		JPanel searchPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		final JLabel searchLabel = new JLabel("搜索(格式： 文件：1.txt 文件夹:1): ");
		searchPane.add(searchLabel);
		searchLine.setPreferredSize(new Dimension(500, 30));
		searchPane.add(searchLine);
		JButton searchButton = new JButton("开始");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String fileName = searchLine.getText();
				if (!searchFile(fileName, rootFile)) {
					JOptionPane.showMessageDialog(null, "找不到此文件!", "Fail!", JOptionPane.WARNING_MESSAGE);
				}
				searchLine.setText("");
			}
		});
		searchPane.add(searchButton);
		add(searchPane, BorderLayout.NORTH);

		// 给文件树加监听
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getButton() == MouseEvent.BUTTON3) {
					myMenu.show(e.getComponent(), e.getX(), e.getY());

				}
			}
		});

		this.setBounds(150, 50, 900, 600);
		setVisible(true);
	}

	public static void main(String args[]) throws IOException {
		new MyFileSystem();
	}
}
