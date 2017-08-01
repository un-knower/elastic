package com.manji.elastic.api.model;


public class JPushDevice {
	private String alias;
	private String mobile;
	private Tags tags;
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Tags getTags() {
		return tags;
	}
	public void setTags(Tags tags) {
		this.tags = tags;
	}

	public class Tags{
		private String[] add;
		private String[] remove;
		public String[] getAdd() {
			return add;
		}
		public void setAdd(String[] add) {
			this.add = add;
		}
		public String[] getRemove() {
			return remove;
		}
		public void setRemove(String[] remove) {
			this.remove = remove;
		}
		
	}
}
