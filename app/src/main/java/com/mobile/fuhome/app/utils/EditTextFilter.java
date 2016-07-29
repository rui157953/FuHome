package com.mobile.fuhome.app.utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

public class EditTextFilter implements InputFilter{
	
	private String defReg="[^<>\",&']*";  
	private String userReg="";
	
	public EditTextFilter(String reg){
		if(reg!=null && !"".equals(reg.trim())){
			this.userReg=reg;
		}
	}
	public EditTextFilter(){
	}
	
	/**   CharSequence source,  //输入的文字    
	      int start,  //开始位置    
	      int end,  //结束位置    
	      Spanned dest, //当前显示的内容    
	      int dstart,  //当前开始位置    
	      int dend //当前结束位置 
	*/
	@Override
	public CharSequence filter(CharSequence source, int start, int end,
			Spanned dest, int dstart, int dend) {
		if(source==null || source.length()<=0){
			return null;
		}
		if(Pattern.matches(defReg, source.toString())){
			if(userReg==null || "".equals(userReg) || Pattern.matches(userReg, source.toString())){
				return source;
			}
		}
		return dest.subSequence(dstart, dend);
	}

}
