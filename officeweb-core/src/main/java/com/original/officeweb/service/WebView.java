package com.original.officeweb.service;

import com.original.officeweb.model.WebDocument;
import org.springframework.ui.Model;

import java.util.Collection;

public interface WebView {

    String handle(WebDocument document, Model model) throws Exception;

    Collection<String> support();
}
