/**
 * Container for various JavaFX "base" classes and interfaces, suitable
 * for extension and implementation by future modules' subclasses.
 * <p>
 * Similar to the module {@code vgp.base}, this module is designed to
 * contain superclasses and interfaces, not subclasses or implementing
 * classes. As such, this module cannot {@code require} any other
 * non-library modules. In other words, while this module can rely upon
 * {@code java.base} (which it does by default) or another built-in
 * module such as {@code java.xml}, this module cannot rely upon, say,
 * {@code vgp.fx.image}. <em>The only exception to this "don't require
 * any custom modules" rule is that this module can and, in fact,
 * <strong>must</strong> require the module {@code vgp.base}.</em> The
 * reason is to use that module's {@code CloneableBase} interface.
 * <p>
 * In addition, this module should not rely on JUnit library modules.
 * Normally, a desire to include JUnit test classes results in relying
 * on JUnit library modules. However, <em>this</em> module should not
 * contain JUnit tests for its <em>own</em> "base" classes, mainly
 * because base classes are skeletons and thus do not implement much,
 * if any, of the functionality they specify, meaning that whoever writes
 * the source code for the test classes has to devise his/her own
 * implementation. In other words, because most classes in this module
 * are either abstract classes, interfaces, or skeleton concrete classes,
 * <em>designers of tests for this module's classes should first
 * implement said classes and then test the implementing classes.</em>
 * <p>
 * Generally, this module's classes either define new functionality or
 * redefine existing functionality. For example, this module's {@code
 * ApplicationBase} class
 * <ol>
 * <li>defines new functionality by adding the method {@code
 *     creatingStartingScene} and
 * <li>redefines existing functionality by overriding its
 *     superclass's {@code start} method.
 * </ol>
 * <p>
 * Theoretically, other modules, such as {@code vgp.fx.image},
 * may structure themselves like this one by imposing the above
 * specifications on <em>their</em> classes. In this case, an
 * extender of the JavaFX {@code ImageView} class (found in the module
 * {@code javafx.graphics}) may redefine existing functionality not
 * <em>only</em> by overriding superclass methods but <em>also</em>
 * by customizing the superclass's properties via its setter methods.
 * <p>
 * <strong>Change Log</strong>
 * <ol>
 * <li>Version 1.0
 *     <ul>
 *     <li>Module {@code vgp.fx.base} created
 *     <li>Package {@code vgp.fx.application} created
 *     <li>Class {@code vgp.fx.application.ApplicationBase} created
 *     </ul>
 * <li>Version 1.1
 *     <ul>
 *     <li>Package {@code vgp.fx.fxml} created
 *     <li>Interface {@code vgp.fx.fxml.Loadable} created
 *     </ul>
 * <li>Version 1.2
 *     <ul>
 *     <li>Package {@code vgp.fx.converter} created
 *     <li>Class {@code vgp.fx.converter.StringConverterBase} created
 *     </ul>
 * <li>Version 1.3
 *     <ul>
 *     <li>Class {@code vgp.fx.converter.EnumStringConverter} created
 *     </ul>
 * <li>Version 1.4
 *     <ul>
 *     <li>Class {@code vgp.fx.fxml.SystemOutLoadListener} created
 *     </ul>
 * <li>Version 1.5
 *     <ul>
 *     <li>Class {@code vgp.fx.fxml.FXMLToJavaConverter} created
 *     </ul>
 * <li>Version 1.6
 *     <ul>
 *     <li>Invisible tester class added to package {@code vgp.fx.converter}
 *     </ul>
 * <li>Version 1.7
 *     <ul>
 *     <li>Package {@code vgp.fx.collections} created
 *     </ul>
 * <li>Version 1.8
 *     <ul>
 *     <li>Class {@code vgp.fx.converter.ColorStringConverter} created
 *     </ul>
 * <li>Version 1.9
 *     <ul>
 *     <li>Class {@code vgp.fx.collections.CallbackWrapperList} created
 *     </ul>
 * <li>Version 2.0
 *     <ul>
 *     <li>Package {@code vgp.fx.controls} created
 *     <li>Class {@code vgp.fx.controls.DatePickerListCell} created
 *     <li>Class {@code vgp.fx.controls.ColorPickerListCell} created
 *     <li>Class {@code vgp.fx.application.DatePickerApplication} created
 *     <li>Class {@code vgp.fx.application.ColorPickerApplication} created
 *     </ul>
 * <li>Version 2.1
 *     <ul>
 *     <li>Class {@code vgp.fx.controls.ListEditorViewSkin} created
 *     <li>Class {@code vgp.fx.controls.ListEditorViewSkin2} created
 *     </ul>
 * <li>Version 2.2
 *     <ul>
 *     <li>Moved the package {@code vgp.fx.fxml} to its own new module:
 *         {@code vgp.fx.fxml}
 *     </ul>
 * <li>Version 2.3
 *     <ul>
 *     <li>Class {@code vgp.fx.controls.DatePickerTreeCell} created
 *     <li>Class {@code vgp.fx.controls.ColorPickerTreeCell} created
 *     <li>Class {@code vgp.fx.controls.TreeEditorViewSkin} created
 *     <li>Class {@code vgp.fx.controls.TreeEditorViewSkin2} created
 *     </ul>
 * <li>Version 2.4
 *     <ul>
 *     <li>Class {@code vgp.fx.controls.ListEditorViewSkin} and all its
 *         subclasses now implement {@code vgp.base.Disposable}
 *     <li>Class {@code vgp.fx.controls.TreeEditorViewSkin} and all its
 *         subclasses now implement {@code vgp.base.Disposable}
 *     </ul>
 * <li>Version 2.5
 *     <ul>
 *     <li>Class {@code vgp.fx.controls.DatePickerTableCell} created
 *     <li>Class {@code vgp.fx.controls.ColorPickerTableCell} created
 *     <li>Class {@code vgp.fx.application.TableEditorApplication} created
 *     </ul>
 * <li>Version 2.6
 *     <ul>
 *     <li>Class {@code vgp.fx.controls.DatePicker_ActionEventForwarder} created in order to debug
 *         class {@code vgp.fx.controls.DatePickerTableCell}
 *     <li>Class {@code vgp.fx.controls.DatePickerSkin_ActionEventForwarder} created in order to debug
 *         class {@code vgp.fx.controls.DatePickerTableCell}
 *     <li>Class {@code vgp.fx.controls.DatePickerTableCell} edited in order to debug
 *         class {@code vgp.fx.controls.DatePickerTableCell}
 *     <li>Class {@code vgp.fx.application.TableEditorApplication} edited in order to debug
 *         class {@code vgp.fx.controls.DatePickerTableCell} and to fix at least one typo in the comments
 *     </ul>
 * <li>Version 2.7
 *     <ul>
 *     <li>Class {@code vgp.fx.application.CellViewerApplication} created
 *     <li>Class {@code vgp.fx.controls.ListEditorViewSkin} edited to keep instances of it
 *         from constantly growing in height
 *     </ul>
 * <li>Version 2.8
 *     <ul>
 *     <li>Class {@code vgp.fx.application.CellViewerApplication} edited, mainly to add new
 *         nested classes and move some code in {@code CellViewerApplication.createStartingScene()}
 *         to those nested classes' constructors
 *     <li>Class {@code vgp.fx.controls.ColorPickerListCellSkin} created
 *     <li>Class {@code vgp.fx.controls.ColorPickerListCell} edited
 *     <li>Class {@code vgp.fx.controls.SubroutinePrinter} created
 *     <li>Used {@code vgp.fx.controls.SubroutinePrinter} in these classes
 *         and replaced the identifier "subroutineName" with "subroutineText" in these classes:
 *         <ul>
 *         <li>Class {@code vgp.fx.controls.ColorPickerListCell}
 *         <li>Class {@code vgp.fx.controls.ColorPickerTableCell}
 *         <li>Class {@code vgp.fx.controls.ColorPickerTreeCell}
 *         <li>Class {@code vgp.fx.controls.DatePickerListCell}
 *         <li>Class {@code vgp.fx.controls.DatePickerTableCell}
 *         <li>Class {@code vgp.fx.controls.DatePickerTreeCell}
 *         </ul>
 *     </ul>
 * <li>Version 2.9
 *     <ul>
 *     <li>Class {@code vgp.fx.controls.DatePickerListCellSkin} created
 *     <li>Class {@code vgp.fx.controls.DatePickerListCell} edited
 *     <li>Added a "set item to null" button to {@code vgp.fx.controls.ListEditorViewSkin}
 *     </ul>
 * <li>Version 2.10
 *     <ul>
 *     <li>Increased return value of {@code vgp.fx.controls.ListEditorViewSkin.computePrefWidth(double, double, double, double, double)}
 *     <li>Increased return value of {@code vgp.fx.controls.TreeEditorViewSkin.computePrefWidth(double, double, double, double, double)}
 *     <li>Edited showChildrenEditor() in {@code vgp.fx.controls.TreeEditorViewSkin}
 *     <li>Added a "set item to null" button to {@code vgp.fx.controls.TreeEditorViewSkin}
 *     </ul>
 * <li>Version 2.11
 *     <ul>
 *     <li>Class {@code vgp.fx.controls.ColorPickerTreeCellSkin} created
 *     <li>Class {@code vgp.fx.controls.ColorPickerTreeCell} edited
 *     <li>Class {@code vgp.fx.controls.DatePickerTreeCellSkin} created
 *     <li>Class {@code vgp.fx.controls.DatePickerTreeCell} edited
 *     </ul>
 * <li>Version 2.12
 *     <ul>
 *     <li>Class {@code vgp.fx.controls.TableEditorViewSkin} created
 *     <li>Class {@code vgp.fx.application.CellViewerApplication} edited
 *     <li>Class {@code vgp.fx.controls.TableColumnListCell} created
 *     <li>Class {@code vgp.fx.controls.TableColumnListCellSkin} created
 *     <li>Class {@code vgp.fx.controls.TableColumnTreeCell} created
 *     <li>Class {@code vgp.fx.controls.TableColumnTreeCellSkin} created
 *     </ul>
 * <li>Version 3.0
 *     <ul>
 *     <li>Package {@code vgp.fx.objects} created
 *     <li>Class {@code ColoredDate} edited and moved to package {@code vgp.fx.objects}
 *     <li>Class {@code vgp.fx.application.CellViewerApplication} edited to compensate
 *     <li>Class {@code vgp.fx.application.TableEditorApplication} edited to compensate
 *     </ul>
 * <li>Version 3.1
 *     <ul>
 *     <li>Package {@code vgp.fx.logging} created
 *     <li>Class {@code SubroutinePrinter} edited and moved to package {@code vgp.fx.logging}
 *     <li>Class {@code vgp.fx.controls.ColorPickerListCell} edited to compensate
 *     <li>Class {@code vgp.fx.controls.ColorPickerTableCell} edited to compensate
 *     <li>Class {@code vgp.fx.controls.ColorPickerTreeCell} edited to compensate
 *     <li>Class {@code vgp.fx.controls.DatePickerListCell} edited to compensate
 *     <li>Class {@code vgp.fx.controls.DatePickerTableCell} edited to compensate
 *     <li>Class {@code vgp.fx.controls.DatePickerTreeCell} edited to compensate
 *     </ul>
 * <li>Version 3.2
 *     <ul>
 *     <li>Package {@code vgp.fx.controls.list.cell.color} created
 *     <li>Package {@code vgp.fx.controls.list.cell.date} created
 *     <li>Package {@code vgp.fx.controls.list.view} created
 *     <li>Package {@code vgp.fx.controls.tree.cell.color} created
 *     <li>Package {@code vgp.fx.controls.tree.cell.date} created
 *     <li>Package {@code vgp.fx.controls.tree.view} created
 *     </ul>
 * <li>Version 3.3
 *     <ul>
 *     <li>Class {@code ColorPickerListCell} edited and moved to package {@code vgp.fx.controls.list.cell.color}
 *     <li>Class {@code ColorPickerListCellSkin} edited and moved to package {@code vgp.fx.controls.list.cell.color}
 *     <li>Class {@code DatePickerListCell} edited and moved to package {@code vgp.fx.controls.list.cell.date}
 *     <li>Class {@code DatePickerListCellSkin} edited and moved to package {@code vgp.fx.controls.list.cell.date}
 *     <li>Class {@code ListEditorViewSkin} edited and moved to package {@code vgp.fx.controls.list.view}
 *     <li>Class {@code ListEditorViewSkin2} edited and moved to package {@code vgp.fx.controls.list.view}
 *     <li>Class {@code vgp.fx.application.CellViewerApplication} edited to compensate
 *     <li>Class {@code vgp.fx.application.ColorPickerApplication} edited to compensate
 *     <li>Class {@code vgp.fx.application.DatePickerApplication} edited to compensate
 *     <li>Class {@code vgp.fx.controls.TableEditorViewSkin} edited to compensate
 *     <li>Class {@code vgp.fx.controls.TreeEditorViewSkin} edited to compensate
 *     </ul>
 * <li>Version 3.4
 *     <ul>
 *     <li>Class {@code ColorPickerTreeCell} edited and moved to package {@code vgp.fx.controls.tree.cell.color}
 *     <li>Class {@code ColorPickerTreeCellSkin} edited and moved to package {@code vgp.fx.controls.tree.cell.color}
 *     <li>Class {@code DatePickerTreeCell} edited and moved to package {@code vgp.fx.controls.tree.cell.date}
 *     <li>Class {@code DatePickerTreeCellSkin} edited and moved to package {@code vgp.fx.controls.tree.cell.date}
 *     <li>Class {@code TreeEditorViewSkin} edited and moved to package {@code vgp.fx.controls.tree.view}
 *     <li>Class {@code TreeEditorViewSkin2} edited and moved to package {@code vgp.fx.controls.tree.view}
 *     <li>Class {@code vgp.fx.application.CellViewerApplication} edited to compensate
 *     <li>Class {@code vgp.fx.application.ColorPickerApplication} edited to compensate
 *     <li>Class {@code vgp.fx.application.DatePickerApplication} edited to compensate
 *     <li>Class {@code vgp.fx.controls.TableEditorViewSkin} edited to compensate
 *     </ul>
 * <li>Version 3.5
 *     <ul>
 *     <li>Class {@code vgp.fx.controls.TableEditorViewSkin} edited
 *     <li>Class {@code vgp.fx.controls.TableColumnListCell} edited
 *     <li>Class {@code vgp.fx.controls.tree.view.TreeEditorViewSkin} edited
 *     <li>Class {@code vgp.fx.application.CellViewerApplication} edited
 *     <li>Class {@code vgp.fx.controls.TableColumnTreeCell} edited
 *     <li>Class {@code vgp.fx.controls.tree.view.TreeEditorViewSkin.WrapperList_ValuesOfTreeItems} edited
 *     <li>Class {@code vgp.fx.controls.tree.view.TreeEditorViewSkin_BoundItems} created
 *     <li>Class {@code vgp.fx.controls.TableEditorViewSkin.TreeEditorViewSkin_TableColumns} created
 *     <li>Class {@code vgp.fx.application.ApplicationBase} edited
 *     </ul>
 * <li>Version 3.6
 *     <ul>
 *     <li>Package {@code vgp.fx.controls.list.cell.tablecolumn} created
 *     <li>Package {@code vgp.fx.controls.tree.cell.tablecolumn} created
 *     </ul>
 * <li>Version 3.7
 *     <ul>
 *     <li>Class {@code TableColumnListCell} edited and moved to package {@code vgp.fx.controls.list.cell.tablecolumn}
 *     <li>Class {@code TableColumnListCellSkin} edited and moved to package {@code vgp.fx.controls.list.cell.tablecolumn}
 *     <li>Class {@code TableColumnTreeCell} edited and moved to package {@code vgp.fx.controls.tree.cell.tablecolumn}
 *     <li>Class {@code TableColumnTreeCellSkin} edited and moved to package {@code vgp.fx.controls.tree.cell.tablecolumn}
 *     <li>Class {@code vgp.fx.controls.TableEditorViewSkin} edited to compensate
 *     </ul>
 * <li>Version 3.8
 *     <ul>
 *     <li>Class {@code vgp.fx.application.ApplicationBase} edited
 *     <li>Class {@code vgp.fx.controls.ColorPickerTableCell} edited
 *     <li>Class {@code vgp.fx.controls.ColorPickerTableCellSkin} created
 *     <li>Class {@code vgp.fx.controls.DatePickerTableCell} edited
 *     <li>Class {@code vgp.fx.controls.DatePickerTableCellSkin} created
 *     </ul>
 * <li>Version 3.9
 *     <ul>
 *     <li>Class {@code vgp.fx.objects.ColoredDate} edited
 *     <li>Class {@code vgp.fx.controls.TableEditorViewSkin} edited
 *     <li>Class {@code vgp.fx.application.CellViewerApplication} edited
 *     </ul>
 * <li>Version 3.10
 *     <ul>
 *     <li>Package {@code vgp.fx.controls.table.cell.color} created
 *     <li>Package {@code vgp.fx.controls.table.cell.date} created
 *     <li>Package {@code vgp.fx.controls.table.view} created
 *     <li>{@code package-info.java} for {@code vgp.fx.controls} edited
 *     </ul>
 * <li>Version 3.11
 *     <ul>
 *     <li>Class {@code ColorPickerTableCell} edited and moved to package {@code vgp.fx.controls.table.cell.color}
 *     <li>Class {@code ColorPickerTableCellSkin} edited and moved to package {@code vgp.fx.controls.table.cell.color}
 *     <li>Class {@code DatePickerTableCell} edited and moved to package {@code vgp.fx.controls.table.cell.date}
 *     <li>Class {@code DatePickerTableCellSkin} edited and moved to package {@code vgp.fx.controls.table.cell.date}
 *     <li>Class {@code TableEditorViewSkin} edited and moved to package {@code vgp.fx.controls.table.view}
 *     <li>Class {@code vgp.fx.application.CellViewerApplication} edited to compensate
 *     <li>Class {@code vgp.fx.application.TableEditorApplication} edited to compensate
 *     <li>Class {@code vgp.fx.controls.DatePicker_ActionEventForwarder} edited to compensate
 *     </ul>
 * <li>Version 4.0
 *     <ul>
 *     <li>Class {@code vgp.fx.collections.PropertyMap} created
 *     <li>Class {@code vgp.fx.collections.BeanPropertyMetadata} created
 *     </ul>
 * <li>Version 4.1
 *     <ul>
 *     <li>Class {@code vgp.fx.controls.table.view.TableEditorViewSkin} edited
 *     <li>Class {@code vgp.fx.controls.table.view.TableEditorViewSkin.TreeEditorViewSkin_TableColumns} edited
 *     <li>Class {@code vgp.fx.controls.tree.cell.tablecolumn.TableColumnTreeCell} edited
 *     <li>Class {@code vgp.fx.controls.tree.view.TreeEditorViewSkin_BoundItems} edited
 *     <li>Class {@code vgp.fx.application.CellViewerApplication} edited
 *     <li>Class {@code vgp.fx.application.ApplicationBase} edited
 *     <li>Class {@code vgp.fx.application.CellViewerApplication.StartingPane} created
 *     <li>Class {@code vgp.fx.application.CellViewerApplication.StartingPane.HandlerForViewingViews} created
 *     </ul>
 * <li>Version 4.2
 *     <ul>
 *     <li>Changed {@code computePrefWidth()} to {@code computePrefWidth()}
 *     <li>Changed {@code computePrefHeight()} to {@code computePrefHeight()}
 *     <li>Class {@code ListEditorViewSkin} edited to compensate
 *     <li>Class {@code ListEditorViewSkin2} edited to compensate
 *     <li>Class {@code TableEditorViewSkin} edited to compensate
 *     <li>Class {@code TreeEditorViewSkin} edited to compensate
 *     <li>Class {@code TreeEditorViewSkin2} edited to compensate
 *     </ul>
 * <li>Version 4.3
 *     <ul>
 *     <li>Undid edits made in Version 4.2
 *     </ul>
 * <li>Version 4.4
 *     <ul>
 *     <li>Changed {@code new Pane(...)} to <code>&sol;&ast; new Pane(...) &ast;&sol;</code>
 *     <li>Class {@code TableEditorViewSkin} edited to compensate
 *     <li>Class {@code TreeEditorViewSkin} edited to compensate
 *     </ul>
 * <li>Version 4.5
 *     <ul>
 *     <li>Class {@code vgp.fx.application.CellViewerApplication.StartingPane.HandlerForViewingViews} edited
 *     <li>Class {@code vgp.fx.controls.tree.cell.tablecolumn.TableColumnTreeCell} edited
 *     <li>Class {@code vgp.fx.controls.table.view.TableEditorViewSkin} edited
 *     <li>Class {@code vgp.fx.controls.tree.view.TreeEditorViewSkin} edited
 *     <li>Class {@code vgp.fx.controls.list.view.ListEditorViewSkin} edited
 *     </ul>
 * <li>Version 4.6
 *     <ul>
 *     <li>Class {@code vgp.fx.collections.CustomObservableList} created
 *     </ul>
 * <li>Version 5.0
 *     <ul>
 *     <li>Class {@code vgp.fx.controls.table.view.TableEditorViewSkin} edited
 *     <li>Class {@code vgp.fx.controls.table.view.TableEditorViewSkin.ListEditorViewSkin_TableItems} created
 *     <li>Class {@code vgp.fx.controls.tree.view.TreeEditorViewSkin} edited
 *     <li>Class {@code vgp.fx.controls.tree.view.TreeEditorViewSkin.ListEditorViewSkin_ChildrenOfTreeItem} created
 *     <li>Class {@code vgp.fx.controls.tree.cell.tablecolumn.TableColumnTreeCell} edited
 *     <li>Class {@code vgp.fx.controls.tree.cell.tablecolumn.TableColumnTreeCell.GraphicToUseIfItemHasNoParent} created
 *     <li>Class {@code vgp.fx.controls.tree.view.TreeEditorViewSkin_BoundItems} edited
 *     <li>Class {@code vgp.fx.application.CellViewerApplication} edited
 *     <li>Class {@code ColorListView} edited and moved from class {@code vgp.fx.application.CellViewerApplication}
 *         to package {@code vgp.fx.controls.list.view}
 *     <li>Class {@code DateListView} edited and moved from class {@code vgp.fx.application.CellViewerApplication}
 *         to package {@code vgp.fx.controls.list.view}
 *     <li>Class {@code ColorTreeView} edited and moved from class {@code vgp.fx.application.CellViewerApplication}
 *         to package {@code vgp.fx.controls.tree.view}
 *     <li>Class {@code DateTreeView} edited and moved from class {@code vgp.fx.application.CellViewerApplication}
 *         to package {@code vgp.fx.controls.tree.view}
 *     <li>Class {@code ObjectTableView} edited and moved from class {@code vgp.fx.application.CellViewerApplication}
 *         to package {@code vgp.fx.controls.table.view}
 *     <li>Class {@code ColoredDateTableView} edited and moved from class {@code vgp.fx.application.CellViewerApplication}
 *         to package {@code vgp.fx.controls.table.view}
 *     </ul>
 * <li>Version 5.1
 *     <ul>
 *     <li>Class {@code vgp.fx.application.ColorPickerApplication} deleted
 *     <li>Class {@code vgp.fx.application.DatePickerApplication} deleted
 *     <li>Class {@code vgp.fx.application.TableEditorApplication} deleted
 *     <li>Class {@code vgp.fx.objects.ColoredDate} edited to compensate
 *     </ul>
 * <li>Version 5.2
 *     <ul>
 *     <li>Class {@code vgp.fx.controls.DatePickerSkin_ActionEventForwarder} deleted
 *     <li>Class {@code vgp.fx.controls.DatePicker_ActionEventForwarder} deleted
 *     <li>Class {@code vgp.fx.converter.StringConverterTester} deleted
 *     </ul>
 * <li>Version 5.3
 *     <ul>
 *     <li>Class {@code vgp.fx.controls.list.cell.tablecolumn.TableColumnListCell} edited
 *     <li>Class {@code vgp.fx.controls.list.cell.tablecolumn.TableColumnListCellSkin} edited
 *     <li>Class {@code vgp.fx.controls.tree.view.TreeEditorViewSkin.ListEditorViewSkin_ChildrenOfTreeItem} edited
 *     </ul>
 * <li>Version 5.4
 *     <ul>
 *     <li>Class {@code vgp.fx.controls.table.view.ColoredDateTableView} edited
 *     <li>Class {@code vgp.fx.controls.table.view.ColoredDateTableView.ColoredDateListCell} created
 *     <li>Class {@code vgp.fx.controls.table.view.TableEditorViewSkin} edited
 *     </ul>
 * <li>Version 5.5
 *     <ul>
 *     <li>Package {@code vgp.fx.controls.table.view.property} created
 *     <li>Class {@code vgp.fx.controls.table.view.property.PropertyTableView} created
 *     <li>Class {@code BeanPropertyMetadata} edited and moved to package {@code vgp.fx.controls.table.view.property}
 *     <li>Class {@code PropertyMap} edited and moved to package {@code vgp.fx.controls.table.view.property}
 *     <li>Class {@code vgp.fx.application.CellViewerApplication} edited
 *     </ul>
 * <li>Version 5.6
 *     <ul>
 *     <li>Package {@code vgp.fx.controls.table.view.map} created
 *     <li>Class {@code vgp.fx.controls.table.view.map.MapTableView] created
 *     <li>Class {@code vgp.fx.controls.table.view.map.MapTableViewSkin] created
 *     <li>Class {@code vgp.fx.application.CellViewerApplication} edited
 *     </ul>
 * <li>Version 5.7
 *     <ul>
 *     <li>Class {@code vgp.fx.collections.CustomObservableList} edited
 *     <li>Class {@code vgp.fx.collections.CustomObservableList.BackingArrayList} created
 *     </ul>
 * <li>Version 6.0
 *     <ul>
 *     <li>Improved and/or corrected comments in package {@code vgp.fx.collections}
 *     <li>Changed this file to compensate
 *     <li>Used method {@code java.util.Objects.requireNonNull(Object)} in package {@code vgp.fx.collections}
 *     <li>Improved method {@code vgp.fx.collections.CallbackWrapperList.ViewChange.getPermutation()}
 *     <li>Made class {@code vgp.fx.collections.CallbackWrapperList.ViewChange} {@code private}
 *     </ul>
 * <li>Version 6.1
 *     <ul>
 *     <li>Improved and/or corrected comments in package {@code vgp.fx.converter}
 *     <li>Changed this file to compensate
 *     </ul>
 * <li>Version 6.2
 *     <ul>
 *     <li>Improved and/or corrected comments in package {@code vgp.fx.objects}
 *     <li>Changed this file to compensate
 *     </ul>
 * <li>Version 6.3
 *     <ul>
 *     <li>Improved and/or corrected comments in package {@code vgp.fx.logging}
 *     </ul>
 * <li>Version 6.4
 *     <ul>
 *     <li>Improved and/or corrected comments in package {@code vgp.fx.controls}
 *     </ul>
 * <li>Version 6.5
 *     <ul>
 *     <li>Improved and/or corrected comments in package {@code vgp.fx.application}
 *     <li>Changed this file to compensate
 *     </ul>
 * <li>Version 6.6
 *     <ul>
 *     <li>Removed FXML from class {@code vgp.fx.application.App}
 *     <li>Removed FXML from class {@code vgp.fx.application.PrimaryController}
 *     <li>Removed FXML from class {@code vgp.fx.application.SecondaryController}
 *     <li>Removed FXML from this file
 *     </ul>
 * <li>Version 6.7
 *     <ul>
 *     <li>Made {@code vgp.fx.application.ApplicationBase.LISTENER_AND_HANDLER_BINDINGS}
 *         an instance of a new class: {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings}
 *     <li>Package {@code vgp.fx.listenerhandler} created
 *     <li>Class {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings} created
 *     <li>Class {@code vgp.fx.application.ApplicationBase} edited to compensate
 *     <li>Class {@code vgp.fx.application.CellViewerApplication} edited to compensate
 *     <li>Class {@code vgp.fx.collections.CustomObservableList} edited to compensate
 *     <li>Class {@code vgp.fx.controls.list.view.ListEditorViewSkin} edited to compensate
 *     <li>Class {@code vgp.fx.controls.table.view.TableEditorViewSkin} edited to compensate
 *     <li>Class {@code vgp.fx.controls.table.view.map.MapTableViewSkin} edited to compensate
 *     <li>Class {@code vgp.fx.controls.tree.view.TreeEditorViewSkin} edited to compensate
 *     <li>Class {@code vgp.fx.controls.tree.view.TreeEditorViewSkin2} edited to compensate
 *     <li>Class {@code vgp.fx.controls.tree.view.TreeEditorViewSkin_BoundItems} edited to compensate
 *     </ul>
 * <li>Version 6.8
 *     <ul>
 *     <li>Improved and/or corrected comments in class
 *         {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings}
 *     <li>Methods created in class {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings}
 *         for event filters
 *     <li>Class {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings.InvalidationListenerBinding} created
 *     <li>Class {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings.ChangeListenerBinding} created
 *     <li>Class {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings.ListChangeListenerBinding} created
 *     <li>Class {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings.SetChangeListenerBinding} created
 *     <li>Class {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings.MapChangeListenerBinding} created
 *     <li>Class {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings.ArrayChangeListenerBinding} created
 *     <li>Class {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings.EventHandlerFilterBinding} created
 *     <li>Improved the implementations of the methods of the class
 *         {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings} by getting rid of
 *         {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings.LISTENER_AND_HANDLER_BINDINGS}
 *     <li>Made sure that every class inside the class {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings}
 *         overrides to the {@code toString()}, {@code equals(Object)}, and {@code hashCode()} methods and that
 *         all fields in those classes are {@code final}
 *     <li>Interface {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings.CanCheckIfAnyFieldIsNull} created
 *     <li>Interface {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings.CanCheckIWeak} created
 *     <li>Interface {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings.ListenerBinding} created
 *     </ul>
 * <li>Version 6.9
 *     <ul>
 *     <li>Method {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings.EventHandlerFilterBinding.toString()} edited
 *     <li>Added functionality to class {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings} for keeping
 *         track of {@code setOnAction(...)}
 *     <li>Fixed a bug in {@code vgp.fx.controls.list.cell.tablecolumn.TableColumnListCellSkin.dispose()}
 *     <li>Used {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings.setOnAction(...)} in the following classes:
 *         <ul>
 *         <li>{@code vgp.fx.application.CellViewerApplication.StartingPane}
 *         <li>{@code vgp.fx.application.ApplicationBase}
 *         <li>{@code vgp.fx.controls.list.cell.color.ColorPickerListCellSkin}
 *         <li>{@code vgp.fx.controls.list.cell.date.DatePickerListCellSkin}
 *         <li>{@code vgp.fx.controls.list.cell.tablecolumn.TableColumnListCellSkin}
 *         <li>{@code vgp.fx.controls.list.view.ListEditorViewSkin}
 *         <li>{@code vgp.fx.controls.list.view.ListEditorViewSkin2}
 *         <li>{@code vgp.fx.controls.table.cell.color.ColorPickerTableCellSkin}
 *         <li>{@code vgp.fx.controls.table.cell.date.DatePickerTableCellSkin}
 *         <li>{@code vgp.fx.controls.table.view.ColoredDateTableView}
 *         <li>{@code vgp.fx.controls.table.view.TableEditorViewSkin}
 *         <li>{@code vgp.fx.controls.tree.cell.color.ColorPickerTreeCellSkin}
 *         <li>{@code vgp.fx.controls.tree.cell.date.DatePickerTreeCellSkin}
 *         <li>{@code vgp.fx.controls.tree.cell.tablecolumn.TableColumnTreeCellSkin}
 *         <li>{@code vgp.fx.controls.tree.view.TreeEditorViewSkin}
 *         <li>{@code vgp.fx.controls.tree.view.TreeEditorViewSkin2}
 *         </ul>
 *     </ul>
 * <li>Version 6.10
 *     <ul>
 *     <li>Made sure that methods in {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings}
 *         are used in the class {@code vgp.fx.collections.CallbackWrapperList}
 *     </ul>
 * <li>Version 6.11
 *     <ul>
 *     <li>Key and value columns added to {@code vgp.fx.controls.table.view.map.MapTableViewSkin}
 *     <li>Tweaked {@code vgp.fx.controls.table.view.property.PropertyTableViewSkin.dispose()}
 *     <li>Replaced uses of Map.of(...) and putAll() in the zero-argument constructor for
 *         {@code vgp.fx.application.CellViewerApplication.StartingPane}
 *     <li>Changed {@code vgp.fx.application.CellViewerApplication.start(Stage)} so that it now
 *         shows an {@code Alert} describing a bug, which is that if {@code observableMap.put(...)}
 *         is used after {@code mapTableView = new MapTableView<>(observableMap)} in the
 *         {@code StartingPane} constructor, then things in the {@code MapTableView} won't
 *         update properly.
 *     </ul>
 * <li>Version 6.12
 *     <ul>
 *     <li>Replaced {@code ListenerAndHandlerBindings.setOnAction(XXXX.onActionProperty(), this)}
 *         with {@code ListenerAndHandlerBindings.setOnAction(XXXX.onActionProperty(), this.XXXX_onAction)}
 *         in the following classes and added fields like {@code private final EventHandler<ActionEvent> XXXX_onAction})
 *         to the following classes and made it so that the following classes no longer implement {@code EventHandler}:
 *         <ul>
 *         <li>{@code vgp.fx.controls.list.cell.color.ColorPickerListCellSkin}
 *         <li>{@code vgp.fx.controls.list.cell.date.DatePickerListCellSkin}
 *         <li>{@code vgp.fx.controls.list.cell.tablecolumn.TableColumnListCellSkin}
 *         <li>{@code vgp.fx.controls.tree.cell.color.ColorPickerTreeCellSkin}
 *         <li>{@code vgp.fx.controls.tree.cell.date.DatePickerTreeCellSkin}
 *         <li>{@code vgp.fx.controls.tree.cell.tablecolumn.TableColumnTreeCellSkin}
 *         <li>{@code vgp.fx.controls.table.cell.color.ColorPickerTableCellSkin}
 *         <li>{@code vgp.fx.controls.table.cell.date.DatePickerTableCellSkin}
 *         </ul>
 *     </ul>
 * <li>Version 6.13
 *     <ul>
 *     <li>Replaced {@code ListenerAndHandlerBindings.setOnAction(XXXX.onActionProperty(), this)}
 *         with {@code ListenerAndHandlerBindings.setOnAction(XXXX.onActionProperty(), this.XXXX_onAction)}
 *         in the following classes and added fields like {@code private final EventHandler<ActionEvent> XXXX_onAction})
 *         to the following classes and made it so that the following classes no longer implement {@code EventHandler}
 *         and got rid of unneeded methods in the following classes and reduced the visibility of methods in the
 *         following classes and used {@code verifyNotDisposed()} more often in the following classes:
 *         <ul>
 *         <li>{@code vgp.fx.controls.list.view.ListEditorViewSkin}
 *         <li>{@code vgp.fx.controls.list.view.ListEditorViewSkin2}
 *         <li>{@code vgp.fx.controls.tree.view.TreeEditorViewSkin}
 *         <li>{@code vgp.fx.controls.tree.view.TreeEditorViewSkin2}
 *         <li>{@code vgp.fx.controls.tree.view.TreeEditorViewSkin_BoundItems}
 *         <li>{@code vgp.fx.controls.table.view.TableEditorViewSkin}
 *         </ul>
 *     </ul>
 * <li>Version 6.14
 *     <ul>
 *     <li>In the class {@code vgp.fx.controls.table.view.map.MapTableViewSkin}, replaced
 *         {@code ListenerAndHandlerBindings.addMapChangeListener(backingMap, this)} with
 *         {@code ListenerAndHandlerBindings.addMapChangeListener(backingMap, this.backingMap_mapChangeListener)},
 *         {@code ListenerAndHandlerBindings.removeMapChangeListener(backingMap, this)} with
 *         {@code ListenerAndHandlerBindings.removeMapChangeListener(backingMap, this.backingMap_mapChangeListener)},
 *         added the field {@code backingMap_mapChangeListener}, made it so that that class no longer implements
 *         {@code MapChangeListener}, and got rid of unneeded methods
 *     </ul>
 * <li>Version 6.15
 *     <ul>
 *     <li>Made the following classes no longer implement {@code ChangeListener} and added fields and
 *         added/edited/removed methods in the following classes to compensate and used
 *         {@code verifyNotDisposed()} more often in the following classes:
 *         <ul>
 *         <li>{@code vgp.fx.controls.list.view.ListEditorViewSkin}
 *         <li>{@code vgp.fx.controls.table.view.TableEditorViewSkin}
 *         <li>{@code vgp.fx.controls.tree.view.TreeEditorViewSkin}
 *         <li>{@code vgp.fx.controls.tree.view.TreeEditorViewSkin2}
 *         <li>{@code vgp.fx.controls.tree.view.TreeEditorViewSkin_BoundItems}
 *         </ul>
 *     </ul>
 * <li>Version 6.16
 *     <ul>
 *     <li>Made the following classes no longer implement {@code ListChangeListener} and
 *         added/edited/removed fields and methods in the following classes to compensate:
 *         <ul>
 *         <li>{@code vgp.fx.controls.list.view.ListEditorViewSkin}
 *         <li>{@code vgp.fx.controls.table.view.TableEditorViewSkin}
 *         <li>{@code vgp.fx.controls.table.view.map.MapTableViewSkin}
 *         </ul>
 *     </ul>
 * <li>Version 6.17
 *     <ul>
 *     <li>Used {@code WeakMapChangeListener} in the following classes:
 *         <ul>
 *         <li>{@code vgp.fx.controls.table.view.map.MapTableViewSkin}
 *         </ul>
 *     </ul>
 * <li>Version 6.18
 *     <ul>
 *     <li>Used {@code WeakChangeListener} in the following classes:
 *         <ul>
 *         <li>{@code vgp.fx.controls.list.view.ListEditorViewSkin}
 *         </ul>
 *     <li>Method {@code vgp.fx.listenerhandler.ListenerAndHandlerBindings.outputNumbersOfBindings()} edited
 *     </ul>
 * <li>Version 6.19
 *     <ul>
 *     <li>Removed field {@code selectedItemProperty_changeListener} and uses of it from class
 *         {@code vgp.fx.controls.tree.view.TreeEditorViewSkin}
 *     <li>Removed field {@code expandedItemCountProperty_changeListener} and uses of it from class
 *         {@code vgp.fx.controls.tree.view.TreeEditorViewSkin2}
 *     <li>Used {@code WeakChangeListener} in class {@code vgp.fx.controls.tree.view.TreeEditorViewSkin}
 *     <li>Heavily edited class {@code vgp.fx.controls.tree.view.TreeEditorViewSkin_BoundItems}
 *     <li>Rewrote {@code ListEditorViewSkin.insertButton_onAction.handle(ActionEvent)}
 *     <li>Rewrote {@code ListEditorViewSkin.removeButton_onAction.handle(ActionEvent)}
 *     </ul>
 * </ol>
 * 
 * 
 * @author [...]
 * @see "The library module javafx.base"
 * @see "The library module javafx.controls"
 * @see "The custom module vgp.base"
 * @see "The custom module vgp.fx.image"
 */
module vgp.fx.base {

	/*
	 * Required in order to use the interface vgp.clone.CloneableBase.
	 * 
	 * Transitive in order to remove the following warning from
	 * the declaration of the start method of ApplicationBase:
	 * 
	 *     The type CloneableBase from module vgp.base may
	 *     not be accessible to clients due to missing
	 *     'requires transitive'
	 */
	requires transitive vgp.base;

	/*
	 * Required in order to extend javafx.application.Application.
	 * 
	 * Transitive in order to remove the following warning from
	 * the declaration of the start method of ApplicationBase:
	 * 
	 *     The type Stage from module javafx.graphics may
	 *     not be accessible to clients due to missing
	 *     'requires transitive'
	 */
	requires transitive javafx.graphics;

	/*
	 * Specified by the packages' respective documentation.
	 */
	exports vgp.fx.application to javafx.graphics;

	/*
	 * Required in order to use the module's FXML functionality.
	 */
	// requires javafx.fxml;

	/*
	 * Required in order to use the module's control classes.
	 * 
	 * Transitive in order to remove the following warning from
	 * the declaration of the defaultTableViewSortPolicy method
	 * of ApplicationBase:
	 * 
	 *     The type TableView from module javafx.controls
	 *     may not be accessible to clients due to missing
	 *     'requires transitive'
	 */
	requires transitive javafx.controls;
	requires javafx.base;

}