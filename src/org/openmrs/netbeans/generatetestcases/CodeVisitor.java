/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.netbeans.generatetestcases;

import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import com.sun.source.tree.ClassTree;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

/**
 *
 * @author jkeiper
 */
public class CodeVisitor extends SourceFileVisitor {

	private Map<String, List<String>> shoulds = null;
	private String className = null;

	@Override
	public Void visitClass(ClassTree t, Void v) {
		Element el = getInfo().getTrees().getElement(getCurrentPath());

		if (el == null) {
			System.err.println("Cannot resolve class!");
		} else {
			TypeElement te = (TypeElement) el;

			// get package
			// className = getInfo().getCompilationUnit().getPackageName().toString();
			
			// get class name
			setClassName(te.getQualifiedName().toString());
			
			// get project
			getInfo().
			
			// for each method ...
			for (ExecutableElement method : ElementFilter.methodsIn(te.getEnclosedElements())) {
				// get @shoulds from javadocs
				Doc javadoc = getInfo().getElementUtilities().javaDocFor(method);
				for (Tag tag : javadoc.tags("should")) {
					this.addShouldMethod(method.getSimpleName().toString(), tag.text());
				}
			}

		}
		return null;
	}

	private void addShouldMethod(String method, String should) {
		if (shoulds == null) {
			shoulds = new LinkedHashMap<String, List<String>>();
		}

		if (!shoulds.keySet().contains(method)) {
			shoulds.put(method, new ArrayList<String>());
		}

		shoulds.get(method).add(should);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Map<String, List<String>> getShoulds() {
		return shoulds;
	}

	public void setShoulds(Map<String, List<String>> shoulds) {
		this.shoulds = shoulds;
	}
	
}