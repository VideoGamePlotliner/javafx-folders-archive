/**
 * Container for various "base" classes and interfaces, suitable for
 * extension and implementation by future modules' subclasses.
 * <p>
 * Similar to the module {@code vgp.fx.base}, this module is designed to
 * contain superclasses and interfaces, not subclasses or implementing
 * classes. As such, this module cannot {@code require} any other
 * non-library modules. In other words, while this module can rely upon
 * {@code java.base} (which it does by default) or another built-in
 * module such as {@code java.xml}, this module cannot rely upon, say,
 * {@code vgp.fx.base}.
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
 * ObjectBase} class defines new functionality by including fields and
 * methods (excluding the getters and setters) that its superclass
 * {@code Object} does <em>not</em> have. However, {@code ObjectBase}
 * also redefines existing functionality by overriding methods that its
 * superclass {@code Object} does <em>indeed</em> have.
 * <p>
 * Theoretically, other modules, such as {@code vgp.fx.base},
 * may structure themselves like this one by imposing the above
 * specifications on <em>their</em> classes. In this case, an
 * extender of the JavaFX {@code Pane} class (found in the module
 * {@code javafx.graphics}) may redefine existing functionality not
 * <em>only</em> by overriding superclass methods but <em>also</em>
 * by customizing the superclass's properties via its setter methods.
 * Please refer to the documentation for {@code vgp.fx.base} for
 * examples and clarification.
 * <p>
 * <strong>Change Log</strong>
 * <ol>
 * <li>Version 1.0
 *     <ul>
 *     <li>Module {@code vgp.base} created
 *     <li>Package {@code vgp.sample} created
 *     <li>Class {@code vgp.sample.ObjectBase} created
 *     </ul>
 * <li>Version 1.1
 *     <ul>
 *     <li>Package {@code vgp.clone} created
 *     <li>Interface {@code vgp.clone.CloneableBase} created
 *     </ul>
 * <li>Version 1.2
 *     <ul>
 *     <li>Class {@code vgp.sample.CloneableObjectBase} created
 *     <li>Hence, package {@code vgp.sample} added to
 *     </ul>
 * <li>Version 1.3
 *     <ul>
 *     <li>Class {@code vgp.sample.ComparableObjectBase} created
 *     <li>Hence, package {@code vgp.sample} added to
 *     </ul>
 * <li>Version 1.4
 *     <ul>
 *     <li>Package {@code vgp.file} created
 *     <li>Interface {@code vgp.file.FileSearcher} created
 *     <li>Class {@code vgp.file.SimpleFileSearcher} created
 *     </ul>
 * <li>Version 1.9
 *     <ul>
 *     <li>Package {@code vgp.dispose} 
 *     <li>Interface {@code vgp.dispose.Disposable} created
 *     <li>Forced every class in this module with a {@code dispose()} method
 *         to implement {@code Disposable}
 *     </ul>
 * <li>Version 2.0
 *     <ul>
 *     <li>Package {@code vgp.list}
 *     <li>Class {@code vgp.list.LazyRAMList} created
 *     </ul>
* <li>Version 2.1
 *     <ul>
 *     <li>Package {@code vgp.file} deleted
 *     <li>Package {@code vgp.sample} deleted
 *     </ul>
* <li>Version 2.2
 *     <ul>
 *     <li>Package {@code vgp.misc} created
 *     <li>Class {@code vgp.misc.USHouseIdealSize} created
 *     <li>Class {@code vgp.misc.PriorityValueCacheKey} created
 *     <li>Class {@code vgp.misc.PriorityValueFunction} created
 *     </ul>
* <li>Version 2.3
 *     <ul>
 *     <li>Class {@code vgp.misc.USHouseIdealSize} edited
 *     <li>Class {@code vgp.misc.SizeStateRepsFunction} created
 *     <li>Class {@code vgp.misc.PriorityValueCacheKey} edited
 *     </ul>
* <li>Version 2.4
 *     <ul>
 *     <li>Package {@code vgp.math} created
 *     <li>Class {@code vgp.math.Fibonacci} created
 *     <li>Class {@code vgp.math.Factorial} created
 *     </ul>
 * </ol>
 * 
 * @author [...]
 * @see "The library module java.base"
 * @see "The custom module vgp.fx.base"
 */
module vgp.base {
	exports vgp.clone;
	exports vgp.list;
	exports vgp.dispose;
}