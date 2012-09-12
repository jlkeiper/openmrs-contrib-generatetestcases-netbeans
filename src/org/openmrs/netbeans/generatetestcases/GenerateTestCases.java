/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.netbeans.generatetestcases;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.api.java.source.JavaSource;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Source",
id = "org.openmrs.netbeans.generatetestcases.GenerateTestCases")
@ActionRegistration(displayName = "#CTL_GenerateTestCases")
@ActionReferences({
	@ActionReference(path = "Menu/Source", position = 350),
	@ActionReference(path = "Loaders/text/x-java")
})
@Messages("CTL_GenerateTestCases=Generate Test Cases")
public final class GenerateTestCases implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// grab the current document
		JTextComponent ed = EditorRegistry.lastFocusedComponent();
		Document doc = ed.getDocument();
		JavaSource source = JavaSource.forDocument(doc);
		
		// parse the source file to find information
		CodeVisitor codeVisitor = new CodeVisitor();
		MethodFinder sourceMethods = new MethodFinder(codeVisitor);
		try {
			source.runUserActionTask(sourceMethods, true);

			// print @shoulds out
			for (Map.Entry<String, List<String>> method : codeVisitor.getShoulds().entrySet()) {
				System.err.println(method.getKey() + ":");
				for (String should : method.getValue()) {
					System.err.println("\t" + should);
				}
			}
		} catch (IOException ex) {
			Logger.getLogger("global").log(Level.SEVERE, ex.getMessage(), ex);
		}

		// get the associated test file
		JavaSource test = getTestFileFor(codeVisitor.getClassName());
		
		TestVisitor testVisitor = new TestVisitor();
		MethodFinder testMethods = new MethodFinder(testVisitor);
		try {
			source.runUserActionTask(sourceMethods, true);
			test.runUserActionTask(testMethods, true);

			// print @shoulds out
			for (Map.Entry<String, List<String>> method : codeVisitor.getShoulds().entrySet()) {
				System.err.println(method.getKey() + ":");
				for (String should : method.getValue()) {
					System.err.println("\t" + should);
				}
			}

		} catch (IOException ex) {
			Logger.getLogger("global").log(Level.SEVERE, ex.getMessage(), ex);
		}


	}

	private void omg() {
//		// get the sourceFolderpath
//		String sourceFolderPath = getSourceFolderPath(doc);
//
//		// get the sourceFolder
//		String projectName = javaProject.getElementName();
//		String sourceFolder = sourceFolderPath.substring(sourceFolderPath
//				.indexOf(projectName) + projectName.length() + 1);
//
//		// attempt to get the testFolder; die (with message) if not found
//		try {
//			String testFolder = SourcePathProperty.getTestFolderPath(
//					javaProject, sourceFolder);
//			if (testFolder == null) {
//				MessageBox messageBox = new MessageBox(activeEditor.getSite()
//						.getShell(), SWT.ICON_QUESTION | SWT.OK);
//				messageBox
//						.setMessage("No Test folder is defined for Source folder '"
//								+ sourceFolder
//								+ "'. Please define it in Project Properties.");
//				messageBox.setText("Error");
//				messageBox.open();
//				return;
//			}
//
//			// since here we we'll create the fields
//			ICompilationUnit unit = workingCopy;
//
//			// get the sourceFilePackage
//			IPackageDeclaration declaration = unit.getPackageDeclarations()[0];
//			String sourceFilePackage = declaration.getElementName();
//
//			// generate the test file name
//			String fileName = unit.getElementName();
//			String testFileName = fileName.replace(".", "Test.");
//
//			// TODO use filesystem separator instead of /
//			String testFolderPath = "/" + projectName + "/" + testFolder;
//			String testCaseFilePath = testFolderPath + "/"
//					+ sourceFilePackage.replace('.', '/') + "/" + testFileName;
//
//			// get the test file
//			IFile testFile = ResourcesPlugin.getWorkspace().getRoot()
//					.getFile(new Path(testCaseFilePath));
//
//			// generate the test package if it does not already exist
//			IPackageFragmentRoot packageFragmentRoot = javaProject
//					.getPackageFragmentRoot(ResourcesPlugin.getWorkspace()
//							.getRoot().getProject(projectName)
//							.getFolder(new Path(testFolder)));
//			packageFragmentRoot.createPackageFragment(sourceFilePackage, false,
//					null);
//			
//			// pull the test file into a compilation unit
//			ICompilationUnit testCompilationUnit = JavaCore
//					.createCompilationUnitFrom(testFile).getWorkingCopy(null);
//			
//			// get the first type of the workingCopy
//			IType[] allTypes = unit.getAllTypes();
//			IType type = allTypes[0];
//
//			// get methods from the working copy
//			IMethod[] methods = type.getMethods();
//
//			// parse the test file into a compilation unit
//			ASTParser parser = ASTParser.newParser(AST.JLS3);
//			parser.setSource(testCompilationUnit);
//			CompilationUnit sutCompilationUnit = (CompilationUnit) parser
//					.createAST(null);
//
//			// add a visitor to identify methods in the test file 
//			ListMethodVisitor listMethodVisitor = new ListMethodVisitor();
//			sutCompilationUnit.accept(listMethodVisitor);
//
//			// find existing tests in the test file
//			Map<IMethod, List<TestMethod>> methodMap = new HashMap<IMethod, List<TestMethod>>();
//			List<String> existingMethods = listMethodVisitor.getMethods();
//			
//			// loop over methods in the working copy
//			for (IMethod method : methods) {
//				
//				// if there is a javadoc ...
//				if (method.getJavadocRange() != null && method.getJavadocRange().getLength() > 0) {
//					int length = method.getJavadocRange().getLength();
//					int offset = method.getJavadocRange().getOffset();
//					
//					// find the test statements
//					List<String> testStatements = TestMethodUtil
//							.parseTestStatements(unit.getSource().substring(
//									offset, offset + length));
//					
//					// check found test against existing tests
//					List<TestMethod> testMethods = new ArrayList<TestMethod>();
//					for (String statement : testStatements) {
//						TestMethod testMethod = new TestMethod(method,
//								statement);
//						
//						// add if it does not already exist
//						if (!existingMethods.contains(testMethod
//								.getTestMethodName())) {
//							testMethods.add(testMethod);
//						}
//					}
//					
//					// if new tests exist for this method, add them to the method map 
//					if (testMethods.size() > 0) {
//						methodMap.put(method, testMethods);
//					}
//				}
//			}
//			
//			// build a window with checklist of potential methods to create
//			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
//			TestCaseMethodLabelProvider labelProvider = new TestCaseMethodLabelProvider();
//			TestcaseMethodContentProvider contentProvider = new TestcaseMethodContentProvider(
//					methodMap);
//			CheckedTreeSelectionDialog dialog = new CheckedTreeSelectionDialog(
//					shell, labelProvider, contentProvider);
//			dialog.setSorter(new JavaElementSorter());
//			dialog.setTitle("Generate Test Methods");
//			String message = "Test";
//			dialog.setMessage(message);
//			dialog.setContainerMode(true);
//			dialog.setSize(60, 18);
//			dialog.setInput(type);
//			dialog.open();
//			
//			// get the results of the dialog
//			Object[] results = dialog.getResult();
//
//			// die if nothing was checked
//			if (results == null || results.length == 0) { 
//				return;
//			}
//
//			// build the list of methods to be created
//			List<TestMethod> testMethodsToGenerateList = new ArrayList<TestMethod>();
//			for (Object result : results) {
//				if (result instanceof TestMethod) {
//					testMethodsToGenerateList.add((TestMethod) result);
//				}
//			}
//
//			// convert the list of test methods to an array
//			TestMethod[] testMethodsToGenerate = testMethodsToGenerateList
//					.toArray(new TestMethod[0]);
//
//			// generate the test methods
//			generateTestMethods(testCompilationUnit, testMethodsToGenerate);
//
//			// open the test file in the editor
//			IEditorInput iEditorInput = new FileEditorInput(testFile);
//			activePage.openEditor(iEditorInput,
//					"org.eclipse.jdt.ui.CompilationUnitEditor", true);
//
//		} catch (Exception e) {
//			
//			// display any Exception that occurred
//			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0,
//					"Error while generating test cases.", e);
//			Activator activator = Activator.getDefault();
//			ILog logger = activator.getLog();
//			logger.log(status);
//			ErrorDialog.openError(PlatformUI.getWorkbench()
//					.getActiveWorkbenchWindow().getShell(), "Error", null,
//					status);
//			return;
//		}
	}

	private String getSourceFolderPath(Document doc) {
//		IEditorInput editorInput = activeEditor.getEditorInput();
//		IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();
//		ICompilationUnit workingCopy = manager.getWorkingCopy(editorInput);
//		IJavaProject javaProject = workingCopy.getJavaProject();
//		IJavaElement parent = workingCopy.getParent().getParent();
//		IPackageFragmentRoot fragmentRoot = (IPackageFragmentRoot) parent;
//		return fragmentRoot.getPath().toString();
		return "";
	}

	private String generateTestMethodName(String method, String should) {
		return "";
	}
	
	private JavaSource getTestFileFor(String sourceClass) {
		// get the source's package
		String testClass = sourceClass + "Test";

		// find the related test package

		// find or create the test file
		return source;
	}
}
