package cn.e3mall.itemweb.test;

public class Student {

	private Integer id;
	private String name;
	private Integer age;
	private String addr;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Student(Integer id, String name, Integer age, String addr) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.addr = addr;
	}

	public Student() {
		super();
	}

}
