package com.eudycontreras.weathersense.customView;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

import java.util.HashMap;

/**
 * @author  Eudy Contreras
 * This class is a simple custom autoCompleteTextView
 * which implements a custom convertSelectionToString method.
 */
public class CustomAutoCompleteTextView extends AppCompatAutoCompleteTextView {
	
	public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected CharSequence convertSelectionToString(Object selectedItem) {
		HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
		return hm.get("name");
	}
	
}
