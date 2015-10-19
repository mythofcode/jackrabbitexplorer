# FAQ #

1. What can Jackrabbit Explorer do?

  * Connects to JCR repositories via RMI
  * Uses the Jackrabbit authentication. Has login window
  * Displays content repository in a lazily loaded tree structure
  * Nodes in tree structure have configurable icons for each node type
  * Supports drag and drop, drag and move, right click context menu
  * Searching, Full text search, SQL and Xpath queries
  * Quick path goto navigation, type path or click on search result row
  * Adding, editing, deleting nodes of different types including mixin
  * Supporting AJAX binary file uploads
  * Supports binary image file viewing in resizable non-modal windows
  * Node binary content downloads
  * Adding, editing, deleting node properties


2. What cannot Jackrabbit Explorer do?

Cannot register custom node types over RMI. This feature has not been implemented in JCR 2.0 as yet.
To register custom node types, for now you can use the The Jcr Import Export tool
http://svn.apache.org/repos/asf/jackrabbit/sandbox/jackrabbit-jcr-import-export-tool/
The Jcr Import Export Tool is included in Jackrabbit Explorer
_com.priocept.jcr.server.JcrImportExportTool_