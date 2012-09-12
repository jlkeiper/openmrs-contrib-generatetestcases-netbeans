/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.netbeans.generatetestcases;

import java.io.IOException;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.Task;

/**
 *
 * @author jkeiper
 */
public class MethodFinder implements Task<CompilationController> {

	private SourceFileVisitor visitor;
	
	public MethodFinder(SourceFileVisitor s) {
		this.visitor = s;
	}
	
	@Override
	public void run(CompilationController parameter) throws IOException {
		visitor.init(parameter);
		parameter.toPhase(JavaSource.Phase.ELEMENTS_RESOLVED);
		visitor.scan(parameter.getCompilationUnit(), null);
	}

}
