package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
//@Access(AccessType.PROPERTY)
public class Parent {

	@Id
	private Long id;

	private String name;
	
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "parent")
	private List<Child1> child1 = new ArrayList<Child1>();
	
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "parent")
	private List<Child2> child2 = new ArrayList<Child2>();

//	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "parent")
	public List<Child1> getChild1() {
		return child1;
	}

	public void setChild1(List<Child1> child1) {
		if (this.child1 == null) {
			this.child1 = child1;
		} else {
			this.child1.clear();
			this.child1.addAll(child1);
		}
	}

	public List<Child1> getChild2() {
		return child1;
	}

	public void setChild2(List<Child2> child2) {
		if (this.child2 == null) {
			this.child2 = child2;
		} else {
			this.child2.clear();
			this.child2.addAll(child2);
		}
	}

	@Override
	public String toString() {
		return "Parent [id=" + id + ", name=" + name + ", child1=" + child1 + "]";
	}

}
