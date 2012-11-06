package com.piercey.app.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;

import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Permission", uniqueConstraints = { @UniqueConstraint(columnNames = "permissionName") })
public class Permission
{
	private String id;
	private String permissionName;
	private String description;
	private Set<Role> roles = new HashSet<Role>(0);

	public Permission()
	{
	}

	public Permission(String permissionName)
	{
		this.permissionName = permissionName;
	}

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Column(name = "id", unique = true, nullable = false)
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	@Column(name = "permissionName", unique = true, nullable = false)
	public String getPermissionName()
	{
		return permissionName;
	}

	public void setPermissionName(String permissionName)
	{
		this.permissionName = permissionName;
	}

	@Column(name = "description", nullable = true)
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
	public Set<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(Set<Role> roles)
	{
		this.roles = roles;
	}
}
