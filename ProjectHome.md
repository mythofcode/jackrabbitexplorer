## Introduction ##
**Jackrabbit Explorer** is an administration and management tool for JCR repositories including [Apache Jackrabbit](http://jackrabbit.apache.org/).

JCR is the [specification](http://jcp.org/en/jsr/detail?id=170) for a generic Content Repository for Java API that is designed to provide a common interface to content repositories and overcome some common limitations associated with traditional  storage of content in relational databases and on file systems. Apache Jackrabbit is the reference implementation of the JCR standard and provides a bare bones content repository with full support for the rich features specified by the API, such as versioning, full text search, observation and support for structured or unstructured content.

The JCR API is implemented by an increasing number of applications, from web development frameworks such as [Apache Sling](http://sling.apache.org/), collaboration platforms such as [Oracle Beehive](http://www.oracle.com/technetwork/middleware/beehive/overview/index.html), through to a number of enterprise CMS platforms such as [Adobe ADEP](http://www.adobe.com/solutions/customer-experience/enterprise-platform.html) (formerly [Day CQ5](http://www.day.com/day/en/products/web_content_management.html)), [Magnolia CMS](http://www.magnolia-cms.com/), [Alfresco](http://www.alfresco.com/), [Jahia](http://www.jahia.com/cms/home/product/jahia-ccp.html) and [Hippo CMS](http://www.onehippo.com/en/products/cms). Some of these implementations are built against proprietary repositories whereas others build on the Jackrabbit implementation itself.

Jackrabbit Explorer provides an application-independent way to view and manage content in a JCR repository, akin to the SQL tools developers are used to using when utilising a relational database in their applications.

## Features ##
Written as a standalone [GWT](http://code.google.com/webtoolkit/)-based web application, Jackrabbit Explorer allows full administration of Apache Jackrabbit JCR repositories, including:

  * Browsing and editing of the content tree
  * Creation, updating and deletion of nodes and properties
  * Cut, copy and paste nodes
  * Full text search
  * Advanced search based on either XPath or SQL languages
  * Management of node types
  * Import/Export node data
  * Preview of binary content (where viewable in a browser)
  * Repository connection supported via any of the three Jackrabbit [deployment models](http://jackrabbit.apache.org/deployment-models.html) (local, JNDI, RMI)

See the [screenshots page on the wiki](http://code.google.com/p/jackrabbitexplorer/wiki/ScreenShots).

## Can I use Jackrabbit Explorer with JCR implementations other than Jackrabbit? ##
Yes - though depending on the implementation this may require some customisation. As the name suggests the tool was built around Jackrabbit, so out of the box the tool can be used for Jackrabbit itself, and also applications that are based on Jackrabbit (such as Magnolia CMS, Hippo CMS, Apache Sling etc; subject to a suitable connection configuration).<br><br>

The original focus on Jackrabbit was due to some gaps in version 1.0 of the JCR specification (specifically around connection models, user management and node type administration) which meant that it was not practical to build a tool that was implemented solely against the JCR API and these features were necessarily made Jackrabbit-specific.  Since then version 2.0 of the JCR specification has been released and largely filled these holes by adopting Jackrabbit's approach. This means that extending Jackrabbit Explorer to support any JCR 2.0 compliant repositories will be relatively straight forward. See <a href='#Road_Map.md'>Road Map</a>.<br>
<br>
<br>
<h2>Road Map</h2>
The following improvements are on the immediate roadmap for Jackrabbit Explorer:<br>
<ul><li>Support for the latest <a href='http://jcp.org/en/jsr/detail?id=283'>JCR 2.0/JSR-283</a> features<br>
</li><li>Development of a plug-in architecture to encapsulate remaining JCR-implementation-specific functionality. This will enable easy extension of Jackrabbit Explorer for seamless support for other JCR 2.0 repositories.</li></ul>


<h2>Motivation</h2>
Jackrabbit Explorer is the most user-friendly and functional administration tool for Apache Jackrabbit JCR repositories.<br>
<br>
<a href='http://priocept.com/'>Priocept</a> originally started to develop Jackrabbit Explorer in 2008 when building a <a href='http://priocept.com/2011/09/12/case-study-tui-travel-content-platform/'>large scale content platform based on Jackrabbit</a>. We found that there were very few other tools available that would allow us to visually inspect and modify a Jackrabbit repository, and those tools that we could find were limited in functionality and/or not mature enough to use reliably on our project. Jackrabbit Explorer was heavily influenced by Day’s <a href='http://dev.day.com/content/docs/en/crx/current/using_crx/content_explorer.html'>CRX Explorer</a>, a proprietary tool available with Day’s commercial implementation of JCR.