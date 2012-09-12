/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.netbeans.generatetestcases;

import com.sun.source.util.TreePathScanner;
import org.netbeans.api.java.source.CompilationInfo;

/**
 *
 * @author jkeiper
 */
public class SourceFileVisitor extends TreePathScanner<Void, Void> {

	private CompilationInfo info;

	public void init(CompilationInfo info) {
		this.info = info;
	}

	public CompilationInfo getInfo() {
		return info;
	}

	public void setInfo(CompilationInfo info) {
		this.info = info;
	}
	
}
