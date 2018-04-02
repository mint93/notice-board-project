package com.noticeboardproject.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	private Set<User> users;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="role_privilege", joinColumns=@JoinColumn(name="role_id", referencedColumnName="id"), inverseJoinColumns=@JoinColumn(name="privilege_id", referencedColumnName="id"))
	private Set<Privilege> privileges;
	
	private String role;
	
	public Role() {
		users = new HashSet<>();
		privileges = new HashSet<>();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Role [id=%s, users=%s, privileges=%s, role=%s]", id, users, privileges, role);
	}
	
}
