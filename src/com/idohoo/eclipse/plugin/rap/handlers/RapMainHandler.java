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
