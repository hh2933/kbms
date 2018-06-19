package com.shuxin.commons.enums;

public enum RuleInterfaceImplEnums {
	LIMITINDICATION_ILLEGAL_RULE("t_xsyzyywg","ruleXSYZYYServiceImpl"),
	LIMITINDICATION_SUSPICION_RULE("t_xsyzyyky","ruleXSYZYYServiceImpl"),
	PIECESEXCESS_RULE("t_zyypcl","ruleZYYPCLServiceImpl"),
	PIECESEXAMINE_RULE("t_zyypsh","ruleZYYPSHServiceImpl"),
	REPEATDRUG_RULE("t_zfclyy","ruleZFCLYYServiceImpl"),
	TQQYKY_RULE("t_tqqyky","ruleTQQYServiceImpl"),
	TQQYWG_RULE("t_tqqywg","ruleTQQYServiceImpl"),
	CLQYKY_RULE("t_clqyky","ruleCLQYServiceImpl"),
	CLQYWG_RULE("t_clqywg","ruleCLQYServiceImpl"),
	XDSSJG_RULE("t_xdssjgsh","ruleXDSSJGServiceImpl"),
	XDJYFS_RULE("t_xdjyfs","ruleXDJYFSServiceImpl"),
	YQYCLQY_RULE("t_yqyclqy","ruleYQYCLQYServiceImpl"),
	JTYY_RULE("t_jtyy","ruleJTYYServiceImpl"),
	XDYYLXJB_RULE("t_xdyylxjb","ruleXDYYLXJBServiceImpl"),
	XECXMBXDJ_RULE("t_xecxmbxdj","ruleXECXMBXDJServiceImpl"),
	DCCFYPZLYC_RULE("t_dccfypzlyc","ruleDCCFYPZLYCServiceImpl"),
	PFQY_RULE("t_pfqy","rulePFQYServiceImpl"),
	XXBKY_RULE("t_xxbky","ruleXDXBServiceImpl"),
	XDXBWG_RULE("t_xdxbwg","ruleXDXBServiceImpl"),
	XET_RULE("t_xet","ruleXETServiceImpl"),
	DCCFFYCX_RULE("t_dccffycx","ruleDCCFFYCXServiceImpl"),
	XDPCWG_RULE("t_xdpcwg","ruleXDPCServiceImpl"),
	XDPCKY_RULE("t_xdpcky","ruleXDPCServiceImpl"),
	XMYXMPP_RULE("t_xmyxmpp","ruleXMYXMPPServiceImpl"),
	ZFSF_RULE("t_zfsf_fzb","ruleZFSFServiceImpl"),
	ZDHLXSH_RULE("t_zdhlxsh","ruleZDHLXSHServiceImpl"),
	YPJJZSH_RULE("t_ypjjzsh","ruleYPJJZSHServiceImpl"),
	TSRQYPJJZSH_RULE("t_tsrqypjjzsh","ruleTSRQYPJJZSHServiceImpl"),
	FJBYLBXML_RULE("t_fjbylbxml","ruleFJBYLBXMLServiceImpl"),
	YPPWJJSH_RULE("t_yppwjjsh","ruleYPPWJJSHServiceImpl"),
	JZXXSJYC_RULE("t_jzxxsjyc","ruleJZXXSJYCServiceImpl"),
	FYMXSJYC_RULE("t_fymxsjyc","ruleFYMXSJYCServiceImpl"),
	MGBZFYZESH_RULE("t_mgbzfyzesh","ruleMGBZFYZESHServiceImpl"),
	XDYPJG_RULE("t_xdypjg","ruleXDYPJGServiceImpl"),
	CYDYCLSH_RULE("t_cydyclsh","ruleCYDYCLSHServiceImpl"),
	CYDYZLSH_RULE("t_cydyzlsh","ruleCYDYZLSHServiceImpl"),
	XSLFZB_RULE("t_xsl_fzb","ruleXSLFZBServiceImpl"),
	XDSYZYXM_RULE("t_xdsyzyxm","ruleXDSYZYXMServiceImpl"),
	CYDJCZLSH_RULE("t_cydjczlsh","ruleCYDJCZLSHServiceImpl"),
	FJZY_RULE("t_fjzy","ruleFJZYServiceImpl"),
	BHLRY_RULE("t_bhlry","ruleBHLRYServiceImpl"),
	MGBZYDFYZESH_RULE("t_mgbzydfyzesh","ruleMGBZYDFYZESHServiceImpl"),
	XDDCXMSL_RULE("t_xddcxmsl","ruleXDDCXMSLServiceImpl"),
	SYBDCCFFYCX_RULE("t_sybdccffycx","ruleSYBDCCFFYCXServiceImpl"),
	SYBDCCFZLYC_RULE("t_sybdccfzlyc","ruleSYBDCCFZLYCServiceImpl"),
	MGBXDYYSH_RULE("t_mgbxdyysh","ruleMGBXDYYSHServiceImpl"),
	;
	
	private String  key;

	private String value;
	
	RuleInterfaceImplEnums(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public static String getEnumValue(String key) {
		 for (RuleInterfaceImplEnums r : RuleInterfaceImplEnums.values()) 
		 {
			 if (r.getKey() .equals(key) ) 
			 {
				   return r.value;
			}
		 }
		
		return null;
	}
	

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
