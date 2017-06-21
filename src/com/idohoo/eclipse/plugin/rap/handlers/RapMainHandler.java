package com.idohoo.eclipse.plugin.rap.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.idohoo.eclipse.plugin.rap.exception.RapException;
import com.idohoo.eclipse.plugin.rap.http.RapHttpClient;
import com.idohoo.eclipse.plugin.rap.main.Activator;
import com.idohoo.eclipse.plugin.rap.util.ClassUtil;
import com.idohoo.eclipse.plugin.rap.util.DocumentUtil;
import com.idohoo.eclipse.plugin.rap.util.PreferenceUtil;
import com.idohoo.eclipse.plugin.rap.util.StringUtil;
import com.idohoo.eclipse.plugin.rap.views.RapView;
import com.idohoo.eclipse.plugin.rap.vo.ClassVO;
import com.idohoo.eclipse.plugin.rap.vo.RapPreferenceVO;

public class RapMainHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		System.out.println(Activator.getDefault().getBundle().getLocation());
//		System.out.println(Platform.getLocation());
//		System.out.println(Platform.getInstanceLocation());
//		System.out.println(HandlerUtil.getActiveEditor(event).getEditorInput().getName());
//		System.out.println(ResourcesPlugin.getWorkspace().getRoot().getProjects()[0].getLocationURI().toString());
//		System.out.println(ResourcesPlugin.getWorkspace().getRoot().getProjects()[1].getLocationURI().toString());
//		System.out.println(ResourcesPlugin.getWorkspace().getRoot().getProjects()[2].getLocationURI().getPath());
//
//		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
//		//先检查配置
//		RapPreferenceVO rapPreferenceVO = PreferenceUtil.getVO(Activator.getDefault(), RapPreferenceVO.class);
//		if(StringUtil.isEmpty(rapPreferenceVO.getUrl()) || StringUtil.isEmpty(rapPreferenceVO.getUsername()) || StringUtil.isEmpty(rapPreferenceVO.getPassword())){
//			MessageDialog.openWarning(window.getShell(), "提示", "请先配置好rap-plugin首选项");
//			return event;
//		}
//		IDocument document = DocumentUtil.getDocument(HandlerUtil.getActiveEditor(event));
//		if(document == null) return event;
//		//System.out.println(document.get());
//		System.out.println(HandlerUtil.getActiveEditorInput(event).getClass().getInterfaces()[0]);
//		System.out.println(HandlerUtil.getActiveEditorInput(event).getClass().getInterfaces()[1]);
//		System.out.println(HandlerUtil.getActiveEditorInput(event).getClass().getInterfaces()[2]);
//		System.out.println(HandlerUtil.getActiveEditorInput(event).getClass().getInterfaces()[3]);
//		if(HandlerUtil.getActiveEditorInput(event) instanceof IFileEditorInput){
//			System.out.println(((IFileEditorInput)HandlerUtil.getActiveEditorInput(event)).getFile().getProject().getLocationURI().toString());
//			System.out.println(((IFileEditorInput)HandlerUtil.getActiveEditorInput(event)).getFile().getLocationURI().toString());
//			
//		}
//		
//		Display display = window.getWorkbench().getDisplay();
//        Shell shell = new Shell(window.getShell(),SWT.DIALOG_TRIM   
//                | SWT.APPLICATION_MODAL);  
//          
//        shell.setText("hello");   
//        shell.setSize(200, 200);
//        shell.setLayout(new FillLayout());  
//          
//        Label label = new Label(shell,SWT.CENTER);  
//        label.setText("fd");  
//          
//        Color red = new Color(display,255,0,0);  
//        label.setForeground(red);  
//          
//        shell.open();  
//          
//        while(!shell.isDisposed()){  
//            if(!display.readAndDispatch()){  
//                display.sleep();  
//            }   
//        }  
//        System.out.println("end 1");
//        red.dispose();   
//        shell.dispose();
//		return event;
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		//先检查配置
		RapPreferenceVO rapPreferenceVO = PreferenceUtil.getVO(Activator.getDefault(), RapPreferenceVO.class);
		if(StringUtil.isEmpty(rapPreferenceVO.getUrl()) || StringUtil.isEmpty(rapPreferenceVO.getUsername()) || StringUtil.isEmpty(rapPreferenceVO.getPassword())){
			MessageDialog.openWarning(window.getShell(), "提示", "请先配置好rap-plugin首选项");
			return event;
		}
		IDocument document = DocumentUtil.getDocument(HandlerUtil.getActiveEditor(event));
		if(document == null) return event;
		RapView rapView = new RapView(event);
		rapView.show();
		
		return event;
	}

}
