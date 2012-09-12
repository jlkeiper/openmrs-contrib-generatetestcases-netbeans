/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.netbeans.generatetestcases;

import com.sun.source.tree.ClassTree;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

/**
 *
 * @author jkeiper
 */
public class TestVisitor extends SourceFileVisitor {

	private List<String> tests = null;

	@Override
	public Void visitClass(ClassTree t, Void v) {
		Element el = getInfo().getTrees().getElement(getCurrentPath());

		if (el == null) {
			System.err.println("Cannot resolve class!");
		} else {
			TypeElement te = (TypeElement) el;

			// for each method ...
			for (ExecutableElement method : ElementFilter.methodsIn(te.getEnclosedElements())) {

				// find the @Verifies annotation
				for(AnnotationMirror mirror: method.getAnnotationMirrors()) {
					if (mirror.getAnnotationType().getClass().getSimpleName().equals("Verifies")) {
						for (ExecutableElement param: mirror.getElementValues().keySet()) {
							if (param.getSimpleName().equals("value"))
								System.err.println("@Verifies: " + mirror.getElementValues().get(param).toString());
							if (param.getSimpleName().equals("method"))
								System.err.println("for: " + mirror.getElementValues().get(param).toString());
						}
					}
				}
				
				// add the method name to the tests list
				addTestMethod(method.getSimpleName().toString());
			}

		}
		return null;
	}

	private void addTestMethod(String method) {
		if (tests == null) {
			tests = new ArrayList<String>();
		}

		tests.add(method);
	}

	public List<String> getTests() {
		return tests;
	}

	public void setTests(List<String> tests) {
		this.tests = tests;
	}

}