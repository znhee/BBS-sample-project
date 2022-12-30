package taro;

public class Taro {
	private int qNo;
	private String content;
	private String ans1;
	private String ans2;
	
	public Taro() {}
	public Taro(int qNo, String content, String ans1, String ans2) {
		this.qNo = qNo;
		this.content = content;
		this.ans1 = ans1;
		this.ans2 = ans2;
	}
	public int getqNo() {
		return qNo;
	}
	public void setqNo(int qNo) {
		this.qNo = qNo;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAns1() {
		return ans1;
	}
	public void setAns1(String ans1) {
		this.ans1 = ans1;
	}
	public String getAns2() {
		return ans2;
	}
	public void setAns2(String ans2) {
		this.ans2 = ans2;
	}
	
}
