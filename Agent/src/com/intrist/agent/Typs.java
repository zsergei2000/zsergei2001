package com.intrist.agent;

public class Typs {

	public static final int TRT_SELLOUT = 1, TRT_SELLOUT_UP = 0, TRT_SELLOUT_DOWN = 1;
	
	public String[] Types;
	public String Type;
	public TrtSalloutTyps trtSalloutTyps = new TrtSalloutTyps();
	
	/*Typs(int type) {
		
		setTypes(type);
		
	}
	
	private void setTypes(int Type) {
		
		switch (Type) {
		case TRT_SELLOUT:
			Types = new String[] {"��� ������", "��� ������"};
			break;
		default:
			break;
		}
	}
	
	public String[] getTypes() {
		return Types;
	}
	
	public String getType() {
		return Type;
	}
	
	public void setType(int index) {
		Type = Types[index];
	}*/
	
	public static final class TrtSalloutTyps {

		public final String aboveShell = "��� ������";
		public final String underShell = "��� ������";
		
		TrtSalloutTyps() {
			
		}
	}
	
	public static final class TRTAddrType {

		public static final String house = "���";
		public static final String market = "�����";
		
		/*TrtSalloutTyps() {
			
		}*/
	}
	
}
