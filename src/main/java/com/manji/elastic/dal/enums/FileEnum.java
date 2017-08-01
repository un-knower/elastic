package com.manji.elastic.dal.enums;


public enum FileEnum {
	/**跟目录*/
	UPLOAD("elastic/upload"),
	/**apph欢迎图片*/
	WELCOME("welcome"),
	/**广告图片*/
	ADV("adv"),
	/**摇钱树图片*/
	ABOUT("about"),
	/**摇钱树应用*/
	APPDOWNLOAD("appdownload"),
	/**公共*/
	COMMON("common"),
	/**游戏KEY*/
	GAMEKEY("gamekey"),
	/**用户头像*/
	HEAD("head"),
	/**博奖图片*/
	PRIZE("prize"),
	/**banner图片*/
	BANNER("banner"),
	/**商家logo*/
	SHOP_LOGO("shop/logo"),
	/**商家图片*/
	SHOP_IMG("shop/showimg"),
	/**批量充值话费xls目录*/
	UPDATE_PHONECHARGES("phonecharges");
	String value;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	private FileEnum(String value) {
		this.value = value;
	}
	/*public static String getUploadPath(FileEnum fileEnum,String filename,long appid) {
		String imgname = "";
		int index = filename.lastIndexOf(".");
		if (index > -1) {
			String suffixType = filename.substring(index,filename.length());
			imgname = StringUtil.getUUID()+ suffixType;
			String yearAndMonth = DateUtil.dateToStr(new Date(), "yyyy/MM");
			imgname = FileEnum.UPLOAD.getValue() + "/" + appid + "/" + fileEnum.getValue() +"/" + yearAndMonth + "/" + imgname;
		}
		return imgname;
	}*/
	/*public static void main(String[] args) {
		String filename = "111111.jpg";
		String imgpath = FileEnum.getUploadPath(FileEnum.HEAD, filename, 1);
		int index = imgpath.lastIndexOf("/");
		String pathname = imgpath.substring(0, index+1);
		System.out.println(pathname);
		String name = imgpath.substring(index+1);
		System.out.println(name);
	}*/
}
