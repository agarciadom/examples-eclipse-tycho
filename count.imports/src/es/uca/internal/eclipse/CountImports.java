package es.uca.internal.eclipse;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.statushandlers.StatusManager;

public class CountImports extends AbstractHandler implements IHandler,
		IWorkbenchWindowActionDelegate {

	public static final String PLUGIN_ID = "count.imports";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final Shell shell = HandlerUtil.getActiveShell(event);
		final ISelection sel = HandlerUtil.getActiveWorkbenchWindow(event)
				.getSelectionService().getSelection();
		if (sel instanceof IStructuredSelection) {
			final IStructuredSelection structuredSel = (IStructuredSelection) sel;
			if (structuredSel.size() == 1
					&& structuredSel.getFirstElement() instanceof ICompilationUnit) {
				final ICompilationUnit unit = (ICompilationUnit) structuredSel
						.getFirstElement();
				countLines(shell, unit);
			}
		}
		return null;
	}

	@Override
	public void run(IAction action) {
		final Shell shell = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell();
		
		final IEditorInput openEditorInput = PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor().getEditorInput();
		final IJavaElement elem = (IJavaElement)openEditorInput.getAdapter(IJavaElement.class); 

		if (elem instanceof ICompilationUnit) {
			countLines(shell, (ICompilationUnit)elem);
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// do nothing
	}

	@Override
	public void init(IWorkbenchWindow window) {
		// do nothing
	}

	public void countLines(final Shell shell, final ICompilationUnit elem) {
		try {
			MessageDialog.openInformation(
					shell,
					"Number of imports",
					"Resource " + elem.getResource() + " has "
							+ elem.getImports().length + " imports.");
		} catch (JavaModelException e) {
			StatusManager.getManager().addLoggedStatus(
					new Status(IStatus.ERROR, PLUGIN_ID,
							"Could not count imports", e));
		}
	}

}
