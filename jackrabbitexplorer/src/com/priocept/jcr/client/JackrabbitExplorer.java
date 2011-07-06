package com.priocept.jcr.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.priocept.jcr.client.callback.CRUDServiceCallback;
import com.priocept.jcr.client.callback.DefaultLoginDetailsServiceCallback;
import com.priocept.jcr.client.callback.GetBrowsableContentFilterRegexsServiceCallback;
import com.priocept.jcr.client.callback.GetNodeServiceCallback;
import com.priocept.jcr.client.callback.GetNodeTreeServiceCallback;
import com.priocept.jcr.client.callback.GetNodeTypeIconsServiceCallback;
import com.priocept.jcr.client.callback.LoginServiceCallback;
import com.priocept.jcr.client.callback.NewBooleanCallback;
import com.priocept.jcr.client.domain.JcrTreeNode;
import com.priocept.jcr.client.domain.LoginDetails;
import com.priocept.jcr.client.ui.AddNewNode;
import com.priocept.jcr.client.ui.Details;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.types.LayoutResizeBarPolicy;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.SubmitValuesHandler;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.SubmitItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.CellMouseDownHandler;
import com.smartgwt.client.widgets.grid.events.CellSavedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * 
 * @author James Pickup
 *
 */
public class JackrabbitExplorer implements EntryPoint {
	public JackrabbitExplorer() {
		super();
	}
	
	public final static String NT_UNSTRUCTURED = "nt:unstructured";
	public static String browsableContentFilterRegex = "";
    public static final String defaultIcon = "icons/folder_open.png";
	public JcrTreeNode jcrRoot = new JcrTreeNode("", "", NT_UNSTRUCTURED, new HashMap<String, String>(), 
			new JcrTreeNode("root", "/", NT_UNSTRUCTURED, new HashMap<String, String>(), 
					defaultIcon));
	public Tree jcrTree = new Tree();
	public TreeGrid jcrTreeGrid = new TreeGrid();
	private HLayout layout = new HLayout();
	public static JcrServiceAsync service;
	public TabSet bottomRightTabSet = new TabSet();
	private ListGrid propertiesListGrid = new ListGrid();  
	private ListGridRecord[] propertiesListGridRecords = new ListGridRecord[]{};
	public ListGrid searchResultsListGrid = new ListGrid();  
	private String sourcePath = "";
	private String destinationPath = "";
	private String copyCellPath = null;
	private String cutCellPath = null;
	private String deleteCellPath = null;
	public static TreeGrid cellMouseDownTreeGrid;
	public static Window loginWindow = new Window();
	public static String loadingImgPath = "loading/loading.gif";
    public static Img loadingImg = new Img(loadingImgPath);
    public static HLayout disabledHLayout = new HLayout();
    public LoginDetails loginDetails = null;
    protected final static String BINARY_SERVLET_PATH = "/jackrabbitexplorer/BinaryServlet?path=";
    public static List<Map<String, String>> customNodeList = null;
	public static void setCustomTreeIcon(TreeNode treeNode, String primaryNodeType) {
		if (null != customNodeList) {//rep:system nt:folder
			for (Map<String, String> property : customNodeList) {
				if (property.containsKey(primaryNodeType)) {
					treeNode.setAttribute("treeGridIcon", property.get(primaryNodeType));
				}
			}
		}

	}
	
	private static HLayout mainLayout = new HLayout();
	public void onModuleLoad() {
		try {
			service = (JcrServiceAsync) GWT.create(JcrService.class);
			ServiceDefTarget endpoint = (ServiceDefTarget) service;
			endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "JcrService");
		} catch (Exception e) {
			SC.warn("There was an error: " + e.getMessage(), new NewBooleanCallback());
		}
		service.getDefaultLoginDetails(new DefaultLoginDetailsServiceCallback(this));
		service.getNodeTypeIcons(new GetNodeTypeIconsServiceCallback());
		service.getBrowsableContentFilterRegex(new GetBrowsableContentFilterRegexsServiceCallback());
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setBackgroundColor("#F0F0F0");
		mainLayout.draw();
	}
	
	public void drawMainLayout() {
		mainLayout.setLayoutMargin(15);
		Label navigationLabel = new Label();
		navigationLabel.setAlign(Alignment.CENTER);
		navigationLabel.setOverflow(Overflow.HIDDEN);
		navigationLabel.setWidth("40%");
		navigationLabel.setShowResizeBar(true);
		navigationLabel.setBorder("1px solid blue");
		mainLayout.addMember(navigationLabel);
		VLayout vLayout = new VLayout();
		vLayout.setWidth("60%");
		Label topRightLabel = new Label();
		topRightLabel.setAlign(Alignment.CENTER);
		topRightLabel.setOverflow(Overflow.HIDDEN);
		topRightLabel.setHeight("185");
		topRightLabel.setShowResizeBar(true);
		topRightLabel.setBorder("1px solid blue");
		Label bottonRightLabel = new Label();
		bottonRightLabel.setAlign(Alignment.CENTER);
		bottonRightLabel.setOverflow(Overflow.HIDDEN);
		bottonRightLabel.setHeight("100%");
		bottonRightLabel.setBorder("1px solid blue");
		navigationLabel.addChild(createNavigation());
		topRightLabel.addChild(new Details().createDetails(this));
		bottomRightTabSet.setWidth100();
		bottomRightTabSet.setHeight100();
		bottomRightTabSet.setTabs(createPropertiesTab(), createSearchResultsTab());
		bottonRightLabel.addChild(bottomRightTabSet);
		vLayout.addMember(topRightLabel);
		vLayout.addMember(bottonRightLabel);
		vLayout.setAlign(VerticalAlignment.CENTER);
		mainLayout.addMember(vLayout);
	}
	
	public static void showLoadingImg() {
		showDisableLayer();
		loadingImg.setSize("100px","100px");
		loadingImg.setTop(mainLayout.getHeight()/2 - 50); //loading image height is 50px
		loadingImg.setLeft(mainLayout.getWidth()/2 - 50); //loading image width is 50px
		loadingImg.show();
		loadingImg.bringToFront();
	}
	
	public static void showDisableLayer() {
		disabledHLayout.setSize("100%", "100%");
		disabledHLayout.setStyleName("disabledBackgroundStyle");
		disabledHLayout.show();
	}
	
	public static void hideDisableLayer() {
		disabledHLayout.hide();
	}
	
	public static void hideLoadingImg() {
		loadingImg.hide();
		hideDisableLayer();
	}

	public void treeRecordClick(TreeGrid selectedTreeGrid, boolean refresh, String parentPath) {
		showLoadingImg();
		JcrTreeNode selectedAnimateTreeNode = jcrRoot;
		if (null != selectedTreeGrid && null == parentPath) {
			selectedAnimateTreeNode = (JcrTreeNode) selectedTreeGrid.getSelectedRecord();
		}
		if (null != parentPath) {
			selectedAnimateTreeNode = (JcrTreeNode) jcrTree.find("/root" + parentPath);
			if (null == selectedAnimateTreeNode) {
				if (null != parentPath && !parentPath.equals("/")) {
					service.getNodeTree(parentPath, new GetNodeTreeServiceCallback(this));
				} else {
					service.getNode(parentPath, new GetNodeServiceCallback(this, parentPath));
				}
				Details.nameTxt.setValue("");
				Details.pathTxt.setValue("");
				return;
			}
		}
		if (selectedAnimateTreeNode.getTitle().contains("[")) {
			Details.nameTxt.setValue(selectedAnimateTreeNode.getTitle().substring(0, selectedAnimateTreeNode.getTitle().lastIndexOf("[")));
		} else {
			Details.nameTxt.setValue(selectedAnimateTreeNode.getTitle());
		}
		Details.pathTxt.setValue(selectedAnimateTreeNode.getAttribute("path"));
		if (selectedAnimateTreeNode.getAttribute("path").equals("/")) {
			Details.nameTxt.setDisabled(true);
		} else {
			Details.nameTxt.setDisabled(false);
		}
		jcrTree.openFolder(selectedAnimateTreeNode);
		if ((null != selectedAnimateTreeNode
				&& selectedAnimateTreeNode.getAttribute("children").length() < 1) || refresh) {
			if (refresh) {
				TreeNode[] selectedTreeNodeChildren = jcrTree.getChildren(selectedAnimateTreeNode);
				for (int j = 0; j < selectedTreeNodeChildren.length; j++) {
					TreeNode treeNode = selectedTreeNodeChildren[j];
					jcrTree.remove(treeNode);
				}
			}
			if (null != parentPath && !parentPath.equals("/")) {
				service.getNodeTree(parentPath, new GetNodeTreeServiceCallback(this));
			} else {
				service.getNode(selectedAnimateTreeNode.getAttribute("path"), new GetNodeServiceCallback(this, parentPath));
			}
		} else {
			hideLoadingImg();
		}
	    Iterator<Map.Entry<String, String>> it = selectedAnimateTreeNode.getProperties().entrySet().iterator();
	    ListGridRecord listGridRecord;
	    propertiesListGridRecords = new ListGridRecord[selectedAnimateTreeNode.getProperties().size()];
	    int i = 0;
	    while (it.hasNext()) {
	    	Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
	        listGridRecord = new ListGridRecord();
	        if (pairs.getKey().contains("jcr:data")) {
		        listGridRecord.setAttribute("property", "<b>" + pairs.getKey() + "</b>");
		        listGridRecord.setAttribute("value", "<b>" + pairs.getValue() + "</b>");
	        } else {
		        listGridRecord.setAttribute("property", pairs.getKey());
		        listGridRecord.setAttribute("value", pairs.getValue());
	        }
	        propertiesListGridRecords[i] = listGridRecord;
	        i++;
	    }
	    propertiesListGrid.setData(propertiesListGridRecords);
	    Details.addNodeSubmitItem.setDisabled(false);
	    Details.addPropertySubmitItem.setDisabled(false);
	}
	
	public void treeDeleteUpdate(String parentPath) {
		showLoadingImg();
		JcrTreeNode selectedAnimateTreeNode = jcrRoot;

		if (null != parentPath) {
			selectedAnimateTreeNode = (JcrTreeNode) jcrTree.find("/root" + parentPath);
		}
		Details.nameTxt.setValue("");
		Details.pathTxt.setValue(parentPath);
		if (parentPath.equals("/")) {
			Details.nameTxt.setDisabled(true);
		} else {
			Details.nameTxt.setDisabled(false);
		}
		jcrTree.openFolder(selectedAnimateTreeNode);
		if (null != selectedAnimateTreeNode) {
				TreeNode[] selectedTreeNodeChildren = jcrTree.getChildren(selectedAnimateTreeNode);
				for (int j = 0; j < selectedTreeNodeChildren.length; j++) {
					TreeNode treeNode = selectedTreeNodeChildren[j];
					jcrTree.remove(treeNode);
				}
			//service.getNode(selectedAnimateTreeNode.getAttribute("path"), new GetNodeServiceCallback(this, parentPath));
		}
	    propertiesListGridRecords = new ListGridRecord[0];
	    propertiesListGrid.setData(propertiesListGridRecords);
	}
	
	
	class JcrTreeGridCellClickHandler implements CellClickHandler {
		public void onCellClick(CellClickEvent event) {
			treeRecordClick((TreeGrid) event.getSource(), false, null);
		}
	};
	
	class JcrTreeDropHandler implements DropHandler {
    	private JackrabbitExplorer jackrabbitExplorer;
    	public JcrTreeDropHandler(JackrabbitExplorer jackrabbitExplorer) {
    		this.jackrabbitExplorer = jackrabbitExplorer;
    	}
		public void onDrop(com.smartgwt.client.widgets.events.DropEvent event) {
			TreeGrid dropToTreeGrid =(TreeGrid) event.getSource();
			TreeGrid sourceTreeGrid = cellMouseDownTreeGrid;
			sourcePath = sourceTreeGrid.getSelectedRecord().getAttribute("path");
			destinationPath = dropToTreeGrid.getRecord(dropToTreeGrid.getEventRow()).getAttribute("path");
			 SC.confirm("Proceed with moving " + sourcePath + " to " + 
					 destinationPath, new BooleanCallback() {  
                    public void execute(Boolean value) {  
                        if (value != null && !value.equals("")) {  
                        	showLoadingImg();
                        	service.moveNode(sourcePath, destinationPath, new CRUDServiceCallback(jackrabbitExplorer, destinationPath, sourcePath));
                        } else {  
                        	return;
                        }  
                    }  
                });  
			jcrTreeGrid.deselectAllRecords();
		}
	}
	
	class JcrTreeCellMouseDownHandler implements CellMouseDownHandler {
		 public void onCellMouseDown(com.smartgwt.client.widgets.grid.events.CellMouseDownEvent event) {
				TreeGrid dropToTreeGrid =(TreeGrid) event.getSource();
				dropToTreeGrid.getTreeFieldTitle();
				cellMouseDownTreeGrid = dropToTreeGrid;
		 }
	}
	
	private TreeGrid createNavigation() {
		jcrTree.setModelType(TreeModelType.PARENT);
		jcrTree.setNameProperty("title");
		jcrTree.setRoot(jcrRoot);
		jcrTreeGrid.setData(jcrTree);
		jcrTreeGrid.setWidth100();
		jcrTreeGrid.setHeight100();
		jcrTreeGrid.setCanDragResize(true);
		jcrTreeGrid.setAnimateFolders(true);
		jcrTreeGrid.setAnimateFolderSpeed(450);
		jcrTreeGrid.setCustomIconProperty("treeGridIcon");
		jcrTreeGrid.setTreeFieldTitle("Workspace: " + workspaceTxt.getValue().toString());
		jcrTreeGrid.setCanReorderRecords(true);  
		jcrTreeGrid.setCanAcceptDroppedRecords(true);  
		jcrTreeGrid.setCanDragRecordsOut(true);
		jcrTreeGrid.setShowConnectors(true);
		jcrTreeGrid.setSelectionType(SelectionStyle.SINGLE);
		jcrTreeGrid.setContextMenu(createRightClickMenu());
		jcrTreeGrid.addCellMouseDownHandler(new JcrTreeCellMouseDownHandler());
		jcrTreeGrid.addDropHandler(new JcrTreeDropHandler(this));
		layout.setCanDragResize(true);
		layout.setMembersMargin(10);
		layout.addChild(jcrTreeGrid);
		jcrTreeGrid.addCellClickHandler(new JcrTreeGridCellClickHandler());
		return jcrTreeGrid;
	}
	
	private Menu createRightClickMenu() {
		Menu rightClickMenu = new Menu();
		MenuItem newMenuItem = new MenuItem("Add New Node", "icons/icon_add_files.png", "Ctrl+N");
		class NewClickHandler implements com.smartgwt.client.widgets.menu.events.ClickHandler {
			JackrabbitExplorer jackrabbitExplorer = null;
			NewClickHandler(JackrabbitExplorer jackrabbitExplorer) {
				this.jackrabbitExplorer = jackrabbitExplorer;
			}
			public void onClick(com.smartgwt.client.widgets.menu.events.MenuItemClickEvent event) {
				new AddNewNode().addNewNodeBox(jackrabbitExplorer);
			}
		};
		newMenuItem.addClickHandler(new NewClickHandler(this));
		MenuItem cutMenuItem = new MenuItem("Cut", "icons/cut.png", "Ctrl+X");
		class cutClickHandler implements com.smartgwt.client.widgets.menu.events.ClickHandler {
			public void onClick(com.smartgwt.client.widgets.menu.events.MenuItemClickEvent event) {
				TreeGrid selectedJcrTreeGrid = (TreeGrid) event.getTarget();
				cutCellPath = selectedJcrTreeGrid.getSelectedRecord().getAttribute("path");
				selectedJcrTreeGrid.removeSelectedData();
			}
		};
		cutMenuItem.addClickHandler(new cutClickHandler());
		MenuItem copyMenuItem = new MenuItem("Copy", "icons/copy.png", "Ctrl+C");
		class copyClickHandler implements com.smartgwt.client.widgets.menu.events.ClickHandler {
			public void onClick(com.smartgwt.client.widgets.menu.events.MenuItemClickEvent event) {
				TreeGrid selectedJcrTreeGrid = (TreeGrid) event.getTarget();
				copyCellPath = selectedJcrTreeGrid.getSelectedRecord().getAttribute("path");
			}
		};
		copyMenuItem.addClickHandler(new copyClickHandler());
		MenuItem pasteMenuItem = new MenuItem("Paste", "icons/paste.png", "Ctrl+P");
		class PasteClickHandler implements com.smartgwt.client.widgets.menu.events.ClickHandler {
	    	private JackrabbitExplorer jackrabbitExplorer;
	    	public PasteClickHandler(JackrabbitExplorer jackrabbitExplorer) {
	    		this.jackrabbitExplorer = jackrabbitExplorer;
	    	}
			public void onClick(com.smartgwt.client.widgets.menu.events.MenuItemClickEvent event) {
				TreeGrid selectedJcrTreeGrid = (TreeGrid) event.getTarget();
				String copyToPath = selectedJcrTreeGrid.getSelectedRecord().getAttribute("path");
				if (null != copyCellPath) {
					showLoadingImg();
					service.copyNode(copyCellPath, copyToPath, new CRUDServiceCallback(jackrabbitExplorer, copyToPath, null));
					copyCellPath = null;
				} else if (null != cutCellPath) {
					showLoadingImg();
					service.cutAndPasteNode(cutCellPath, copyToPath, new CRUDServiceCallback(jackrabbitExplorer, copyToPath, cutCellPath));
					cutCellPath = null;
				} else {
					SC.say("Nothing to paste.");
					return;
				}
			}
		};
		pasteMenuItem.addClickHandler(new PasteClickHandler(this));
		MenuItem refreshMenuItem = new MenuItem("Refresh", "icons/refresh.png", "Ctrl+R");
		class refreshClickHandler implements com.smartgwt.client.widgets.menu.events.ClickHandler {
			public void onClick(com.smartgwt.client.widgets.menu.events.MenuItemClickEvent event) {
				TreeGrid selectedJcrTreeGrid = (TreeGrid) event.getTarget();
				treeRecordClick(selectedJcrTreeGrid, true, null);
			}
		};
		refreshMenuItem.addClickHandler(new refreshClickHandler());
		MenuItem deleteMenuItem = new MenuItem("Delete", "icons/icon_remove_files.png");
		class DeleteClickHandler implements com.smartgwt.client.widgets.menu.events.ClickHandler {
	    	private JackrabbitExplorer jackrabbitExplorer;
	    	public DeleteClickHandler(JackrabbitExplorer jackrabbitExplorer) {
	    		this.jackrabbitExplorer = jackrabbitExplorer;
	    	}
			public void onClick(com.smartgwt.client.widgets.menu.events.MenuItemClickEvent event) {
				TreeGrid selectedJcrTreeGrid = (TreeGrid) event.getTarget();
				deleteCellPath = selectedJcrTreeGrid.getSelectedRecord().getAttribute("path");
				 SC.confirm("Proceed with deleting " + deleteCellPath, new BooleanCallback() {  
	                    public void execute(Boolean value) {  
	                        if (value != null && value) {  
	                        	showLoadingImg();
	                        	service.deleteNode(deleteCellPath, new CRUDServiceCallback(jackrabbitExplorer, null, deleteCellPath));
	                        } else {  
	                        	return;
	                        }  
	                    }  
	                });  
			}
		};
		deleteMenuItem.addClickHandler(new DeleteClickHandler(this));
		rightClickMenu.setItems(newMenuItem, new MenuItemSeparator(), refreshMenuItem, new MenuItemSeparator(),
				cutMenuItem, copyMenuItem, pasteMenuItem, new MenuItemSeparator(), deleteMenuItem);
		return rightClickMenu;
	}
	
	public void navigateTo(String path) {
		showLoadingImg();
		service.getNodeTree(path, new GetNodeTreeServiceCallback(this));
	}
	
	private Tab createSearchResultsTab() {
		Tab searchResultsTab = new Tab();
		searchResultsTab.setTitle("Search Results");
		searchResultsListGrid.setWidth(500);  
		searchResultsListGrid.setHeight(224);  
		searchResultsListGrid.setAlternateRecordStyles(true);  
		searchResultsListGrid.setShowAllRecords(true);  
		searchResultsListGrid.setCanEdit(false);  
		searchResultsListGrid.setEditByCell(false); 
		searchResultsListGrid.setShowHover(true);
        ListGridField pathField = new ListGridField("path", "Path");  
        pathField.setShowHover(true);
        searchResultsListGrid.setFields(pathField);  
        searchResultsListGrid.setCanResizeFields(true);  
        searchResultsListGrid.setWidth100();
        searchResultsListGrid.setHeight100();
        ListGridRecord listGridRecord = new ListGridRecord(); 
        searchResultsListGrid.setData(new ListGridRecord[] {listGridRecord});
        searchResultsListGrid.addClickHandler(new SearchResultsClickClickHandler(this));
        searchResultsTab.setPane(searchResultsListGrid);
		return searchResultsTab;
	}
	
    class SearchResultsClickClickHandler implements com.smartgwt.client.widgets.events.ClickHandler {
    	private JackrabbitExplorer jackrabbitExplorer;
    	public SearchResultsClickClickHandler(JackrabbitExplorer jackrabbitExplorer) {
    		this.jackrabbitExplorer = jackrabbitExplorer;
    	}
		@Override
		public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
    		ListGrid selectedSearchResultTreeGrid = (ListGrid) event.getSource();
    		showLoadingImg();
    		service.getNodeTree(selectedSearchResultTreeGrid.getSelectedRecord().getAttribute("path"), new GetNodeTreeServiceCallback(jackrabbitExplorer));
    	}
    }
	
	private Tab createPropertiesTab() {
		Tab propertiesTab = new Tab();
		propertiesTab.setTitle("Node Properties");
		propertiesListGrid.setWidth(500);  
		propertiesListGrid.setHeight(224);  
		propertiesListGrid.setAlternateRecordStyles(true);  
		propertiesListGrid.setShowAllRecords(true);  
	    propertiesListGrid.setCanEdit(true);  
	    propertiesListGrid.setEditEvent(ListGridEditEvent.CLICK);  
	    propertiesListGrid.setEditByCell(true); 
        ListGridField propertyField = new ListGridField("property", "Property");  
        propertyField.setCanEdit(false);
        propertyField.setShowHover(true);
        ListGridField valueField = new ListGridField("value", "Value");  
        valueField.setShowHover(true);
        propertiesListGrid.setFields(propertyField, valueField);  
        propertiesListGrid.setCanResizeFields(true);  
        propertiesListGrid.setWidth100();
        propertiesListGrid.setHeight100();
        ListGridRecord listGridRecord = new ListGridRecord(); 
        propertiesListGrid.setData(new ListGridRecord[] {listGridRecord});
        propertiesListGrid.addCellSavedHandler(new PropertiesCellSavedHandler(this));
        propertiesListGrid.addCellClickHandler(new PropertiesCellClickHandler());
        propertiesListGrid.setContextMenu(createPropertyRightClickMenu());
        propertiesTab.setPane(propertiesListGrid);
        return propertiesTab;
	}
	
    class PropertiesCellSavedHandler implements CellSavedHandler   {  
    	private JackrabbitExplorer jackrabbitExplorer;
    	public PropertiesCellSavedHandler(JackrabbitExplorer jackrabbitExplorer) {
    		this.jackrabbitExplorer = jackrabbitExplorer;
    	}
        public void onCellSaved(com.smartgwt.client.widgets.grid.events.CellSavedEvent event) {
        	String selectedNodePath = cellMouseDownTreeGrid.getSelectedRecord().getAttribute("path");
        	String propertyName = event.getRecord().getAttribute("property");
        	if (event.getColNum() == 0) {
        		//update property name
        		//service.savePropertyStringValue(selectedNodePath, propertyName, event.getNewValue().toString(), SimpleStringServiceCallback);
        	} else if (event.getColNum() == 1) {
        		//update value
        		showLoadingImg();
        		service.savePropertyStringValue(selectedNodePath, propertyName, event.getNewValue().toString(),
        				new CRUDServiceCallback(jackrabbitExplorer, selectedNodePath, null));
        	}
      }  
    };
    
    ListGridRecord selectedPropertyListGridRecord = null;
    class PropertiesCellClickHandler implements CellClickHandler {
    	public void onCellClick(com.smartgwt.client.widgets.grid.events.CellClickEvent event) {
    		selectedPropertyListGridRecord = event.getRecord(); 
    		String propertyName = selectedPropertyListGridRecord.getAttribute("property");
        	if (propertyName.contains("jcr:data")) {
        		propertiesListGrid.setCanEdit(false);
        		
        		String selectedNodePath = cellMouseDownTreeGrid.getSelectedRecord().getAttribute("path");
            		String mimeType = "";
            		for (int i = 0; i < propertiesListGridRecords.length; i++) {
            			if ("jcr:mimeType".equalsIgnoreCase(propertiesListGridRecords[i].getAttribute("property"))) {
            				mimeType = propertiesListGridRecords[i].getAttribute("value");
            			}
    				}
            		if (mimeType.startsWith("image")) {
            			createBinaryImgWindow(selectedNodePath, selectedNodePath + "/" +  "jcr:data", mimeType);
            		} else {
            			com.google.gwt.user.client.Window.open(BINARY_SERVLET_PATH + selectedNodePath + "/" +  "jcr:data" 
                				+ "&rmiUrl=" + rmiUrlTxt.getValue().toString() + 
                				"&workSpace=" + workspaceTxt.getValue().toString() + "&mimeType=" + mimeType, 
                				"_blank", "toolbar=no,menubar=no,location=no,resizable=yes,scrollbars=yes,status=no"); 
            		}
            		return;
        	} else {
        		propertiesListGrid.setCanEdit(true);
        	}
    	}
    }
    
    private Window createRemoteWindow(String title, String url) {
    	Window remoteWindow = new Window();
    	HTMLPane htmlPane = new HTMLPane();
    	htmlPane.setContentsURL(url);
    	htmlPane.setContentsType(ContentsType.PAGE);
    	remoteWindow.addItem(htmlPane);
    	remoteWindow.setTitle(title);
    	remoteWindow.setShowMaximizeButton(true);
    	remoteWindow.setCanDragReposition(true);
    	remoteWindow.setCanDragResize(true);
    	remoteWindow.setHeight("40%");
    	remoteWindow.setWidth("40%");
    	remoteWindow.setAutoCenter(true);
    	remoteWindow.setShowResizeBar(true);
    	remoteWindow.setDefaultResizeBars(LayoutResizeBarPolicy.MARKED);
    	remoteWindow.show();
    	return remoteWindow;
    }
    
    private Window createBinaryImgWindow(String title, String path, String mimeType) {
    	Window binaryWindow = new Window();
		Img img = new Img(BINARY_SERVLET_PATH + path + "&rmiUrl=" + rmiUrlTxt.getValue().toString() + 
				"&workSpace=" + workspaceTxt.getValue().toString() + "&mimeType=" + mimeType);
		img.setImageType(ImageStyle.STRETCH);
		img.setHeight100();
		img.setWidth100();
		binaryWindow.addItem(img);
		binaryWindow.setTitle(title);
		binaryWindow.setShowMaximizeButton(true);
		binaryWindow.setCanDragReposition(true);
		binaryWindow.setCanDragResize(true);
		binaryWindow.setHeight("40%");
		binaryWindow.setWidth("40%");
		binaryWindow.setAutoCenter(true);
		binaryWindow.setShowResizeBar(true);
		//binaryWindow.setDefaultResizeBars(LayoutResizeBarPolicy.MARKED);
		binaryWindow.show();
    	return binaryWindow;
    }

    String deletePropertyString;
	private Menu createPropertyRightClickMenu() {
		Menu rightClickMenu = new Menu();
		MenuItem deleteMenuItem = new MenuItem("Delete", "icons/icon_remove_files.png");
		class DeleteClickHandler implements com.smartgwt.client.widgets.menu.events.ClickHandler {
	    	private JackrabbitExplorer jackrabbitExplorer;
	    	public DeleteClickHandler(JackrabbitExplorer jackrabbitExplorer) {
	    		this.jackrabbitExplorer = jackrabbitExplorer;
	    	}
			public void onClick(com.smartgwt.client.widgets.menu.events.MenuItemClickEvent event) {
				sourcePath = cellMouseDownTreeGrid.getSelectedRecord().getAttribute("path");
				ListGrid selectedPropListGrid = (ListGrid) event.getTarget();
				deletePropertyString = selectedPropListGrid.getSelectedRecord().getAttribute("property");
				 SC.confirm("Proceed with deleting property " + sourcePath + "?", new BooleanCallback() {  
	                    public void execute(Boolean value) {  
	                        if (value != null && value) {  
	                        	showLoadingImg();
	                        	service.deleteProperty(sourcePath, deletePropertyString,
	                        			new CRUDServiceCallback(jackrabbitExplorer, null, sourcePath));
	                        } else {  
	                        	return;
	                        }  
	                    }  
	                });  
			}
		};
		deleteMenuItem.addClickHandler(new DeleteClickHandler(this));
		rightClickMenu.setItems(deleteMenuItem);
		return rightClickMenu;
	}
	
	private TextItem rmiUrlTxt = new TextItem();
	private TextItem workspaceTxt = new TextItem();
	private TextItem usernameTxt = new TextItem();
	private PasswordItem passwordTxt = new PasswordItem();
	public void showLoginBox() {
		final DynamicForm loginForm = new DynamicForm();
		loginForm.setID("loginForm");
		loginForm.setNumCols(2);
		loginForm.setPadding(25);
		rmiUrlTxt.setName("rmiUrlTxt");
		rmiUrlTxt.setTitle("RMI URL");
		rmiUrlTxt.setDefaultValue(loginDetails.getRmiUrl());
		rmiUrlTxt.setWidth(250);
		rmiUrlTxt.setRequired(true);
		workspaceTxt.setName("workspaceTxt");
		workspaceTxt.setTitle("Workspace");
		workspaceTxt.setDefaultValue(loginDetails.getWorkSpace());
		workspaceTxt.setWidth(250);
		workspaceTxt.setRequired(true);
		usernameTxt.setName("usernameTxt");
		usernameTxt.setTitle("Username");
		usernameTxt.setDefaultValue(loginDetails.getUserName());
		usernameTxt.setWidth(250);
		usernameTxt.setRequired(true);
		passwordTxt.setName("passwordTxt");
		passwordTxt.setTitle("Password");
		passwordTxt.setDefaultValue(loginDetails.getPassword());
		passwordTxt.setWidth(250);
		SubmitItem loginSubmitItem = new SubmitItem("loginSubmitItem");
		loginSubmitItem.setTitle("Login");
		loginSubmitItem.setWidth(100);
	    VStack vStack = new VStack();
	    SpacerItem spacerItem1 = new SpacerItem();
	    SpacerItem spacerItem2 = new SpacerItem();
	    spacerItem1.setStartRow(true);
	    spacerItem1.setEndRow(true);
	    spacerItem2.setStartRow(true);
	    spacerItem2.setEndRow(false);
	    loginSubmitItem.setStartRow(false);
	    loginSubmitItem.setEndRow(true);
	    class LoginSubmitValuesHandler implements SubmitValuesHandler {  
	    	private JackrabbitExplorer jackrabbitExplorer;
	    	public LoginSubmitValuesHandler(JackrabbitExplorer jackrabbitExplorer) {
	    		this.jackrabbitExplorer = jackrabbitExplorer;
	    	}
	    	public void onSubmitValues(com.smartgwt.client.widgets.form.events.SubmitValuesEvent event) {
	        	if (loginForm.validate()) {
		    		loginWindow.hide();
		        	showLoadingImg();
		        	String rmiUrlText = rmiUrlTxt.getValue().toString(); 
		        	String workspaceText = workspaceTxt.getValue().toString();
		        	String usernameText = usernameTxt.getValue().toString();
		        	String passwordText = passwordTxt.getValue() != null ? passwordTxt.getValue().toString() : "";
		        	
		        	service.login(rmiUrlText, workspaceText,usernameText, passwordText, new LoginServiceCallback(jackrabbitExplorer));
	        	}
	      }  
	    };
	    loginForm.addSubmitValuesHandler(new LoginSubmitValuesHandler(this));
	    loginForm.setSaveOnEnter(true);
	    loginForm.setItems(rmiUrlTxt, workspaceTxt, spacerItem1, usernameTxt, passwordTxt, spacerItem1, spacerItem2, loginSubmitItem);
	    vStack.setTop(30);
	    vStack.addMember(loginForm);
	    loginWindow.addChild(vStack);
	    loginWindow.setTitle("Login");
	    loginWindow.setCanDragReposition(true);
	    loginWindow.setCanDragResize(false);
	    loginWindow.setShowMinimizeButton(false);
	    loginWindow.setShowCloseButton(false);
	    loginWindow.setHeight(260);
	    loginWindow.setWidth(380);
	    loginWindow.setAutoCenter(true);
	    loginWindow.show();
	    usernameTxt.focusInItem();
	}
	
}