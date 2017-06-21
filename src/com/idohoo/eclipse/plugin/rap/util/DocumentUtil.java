package com.idohoo.eclipse.plugin.rap.util;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

public class DocumentUtil {

	public static IDocument getDocument(IEditorPart editor){
		if(editor instanceof ITextEditor){
			//.java
			ITextEditor iEditor = (ITextEditor)editor;
			return iEditor.getDocumentProvider().getDocument(editor.getEditorInput());
		}
		return null;
	}
}
