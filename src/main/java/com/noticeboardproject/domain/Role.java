package com.noticeboardproject.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Role {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Long id;
	
	@ManyToMany(mappedBy="roles")
	private List<User> users;
	
	@ManyToMany
	@JoinTable(name="role_privilege", joinColumns=@JoinColumn(name="role_id", referencedColumnName="id"), inverseJoinColumns=@JoinColumn(name="privilege_id", referencedColumnName="id"))
	private List<Privilege> privileges;
	
	private String role;
}
