package com.idohoo.eclipse.plugin.rap.views;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class RapView {

	private ExecutionEvent executionEvent;
	private Shell shell;
	private Display display;
	private static RapView activeView;
	
	public RapView(ExecutionEvent event){
		this.executionEvent = event;
		IWorkbenchWindow window;
		try {
			window = HandlerUtil.getActiveWorkbenchWindowChecked(executionEvent);
			display = window.getWorkbench().getDisplay();
		} catch (ExecutionException e) {
		}
		activeView = this;
	}
	
	public void toScreenCenter(){
		int x = (shell.getMonitor().getClientArea().width - shell.getSize().x) / 2 + shell.getMonitor().getClientArea().x;
		int y = (shell.getMonitor().getClientArea().height - shell.getSize().y) / 2 + shell.getMonitor().getClientArea().y;
		shell.setLocation(x, y);
	}
	
	public void toScreenCenter(int width,int height){
		shell.setSize(width, height);
		toScreenCenter();
	}
	
	public void show() throws ExecutionException {
		if(shell != null) return;
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(executionEvent);
		shell = new Shell(window.getShell(),SWT.DIALOG_TRIM   
              | SWT.APPLICATION_MODAL);  
		shell.setText("RAP插件");     
		toScreenCenter();
        shell.open();  
        ProjectsView.getInstance().show();
        //EditView.getInstance().show();
        while(!shell.isDisposed()){  
          if(!display.readAndDispatch()){  
              display.sleep();  
          }   
        } 
        shell.dispose();
        shell = null;
        BaseView.disposeALl();
	}

	public Display getDisplay() {
		return display;
	}
	
	public Shell getShell(){
		return shell;
	}
	
	public IEditorPart getEditor(){
		return HandlerUtil.getActiveEditor(executionEvent);
	}
	
	public static RapView getActiveView(){
		return activeView;
	}
}
